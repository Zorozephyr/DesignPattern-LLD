# BookMyShow Low Level Design (LLD)

## System Flow
```
User -> location(city) -> movies -> theatre -> showtime -> availableSeats -> booking -> Payment
```

## Core Objects

### 1. Domain Objects
- **User**: Name, userId, email
- **Location/City**: List<Theatres>, getMoviesInTheCity()
- **Movie**: movieId, name, duration, genre
- **Theatre**: List<Movies>, List<Shows>, getShowTimesForMovieX(Movie X)
- **Show**: time, Movie, Screen, BookedSeats, **version** (for optimistic locking)
- **Screen**: Seats[], screenId, capacity
- **Seat**: Type, Price, seatNo, status (AVAILABLE/RESERVED/BOOKED)
- **Booking**: bookingId, Show, List<Seats>, Payment, timestamp, **version**
- **Payment**: paymentId, amount, status, timestamp

### 2. Supporting Objects
- **SeatReservation**: Temporary hold on seats with expiration
- **BookingStatus**: INITIATED, RESERVED, CONFIRMED, FAILED, CANCELLED
- **SeatStatus**: AVAILABLE, TEMPORARILY_RESERVED, BOOKED

---

## Concurrency Control Strategies

### **Optimistic Locking Implementation**

Optimistic locking assumes conflicts are rare and checks for conflicts only at commit time.

#### **Core Concept**
- Add version field to critical entities (Show, Booking)
- Allow concurrent reads without blocking
- Detect conflicts during write operations
- Retry on version mismatch

#### **Implementation Details**

##### 1. Enhanced Show Entity with Versioning
```java
public class Show {
    private int showId;
    private Movie movie;
    private Screen screen;
    private int showStartTime;
    private List<Integer> bookedSeatIds = new ArrayList<>();
    private volatile long version = 0L;  // Optimistic lock version
    private final Object versionLock = new Object();
    
    // Version management methods
    public long getVersion() {
        return version;
    }
    
    public boolean compareAndUpdateVersion(long expectedVersion) {
        synchronized(versionLock) {
            if (this.version == expectedVersion) {
                this.version++;
                return true;
            }
            return false;
        }
    }
}
```

##### 2. Optimistic Booking Service
```java
public class OptimisticBookingService {
    private static final int MAX_RETRY_ATTEMPTS = 3;
    private static final int BASE_RETRY_DELAY_MS = 100;
    
    public BookingResult bookSeats(int showId, List<Integer> seatNumbers, String userId) {
        int attempts = 0;
        
        while (attempts < MAX_RETRY_ATTEMPTS) {
            try {
                return attemptBooking(showId, seatNumbers, userId);
            } catch (OptimisticLockException e) {
                attempts++;
                if (attempts >= MAX_RETRY_ATTEMPTS) {
                    return BookingResult.failure("Booking failed after " + MAX_RETRY_ATTEMPTS + " attempts");
                }
                
                // Exponential backoff
                try {
                    Thread.sleep(BASE_RETRY_DELAY_MS * (1L << attempts));
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    return BookingResult.failure("Booking interrupted");
                }
            }
        }
        
        return BookingResult.failure("Max retry attempts exceeded");
    }
    
    private BookingResult attemptBooking(int showId, List<Integer> seatNumbers, String userId) 
            throws OptimisticLockException {
        
        // Step 1: Read current state with version
        Show show = showRepository.findById(showId);
        long currentVersion = show.getVersion();
        
        // Step 2: Business logic validation (can take time)
        validateBookingRequest(show, seatNumbers);
        
        // Step 3: Check seat availability (non-blocking read)
        for (Integer seatNumber : seatNumbers) {
            if (show.getBookedSeatIds().contains(seatNumber)) {
                return BookingResult.failure("Seat " + seatNumber + " already booked");
            }
        }
        
        // Step 4: Prepare booking data
        Booking booking = prepareBooking(show, seatNumbers, userId);
        
        // Step 5: Atomic commit with version check
        return commitBookingAtomically(show, booking, currentVersion, seatNumbers);
    }
    
    private BookingResult commitBookingAtomically(Show show, Booking booking, 
                                                long expectedVersion, List<Integer> seatNumbers) 
            throws OptimisticLockException {
        
        synchronized(show) {  // Critical section - keep minimal
            // Double-check version hasn't changed
            if (!show.compareAndUpdateVersion(expectedVersion)) {
                throw new OptimisticLockException("Show version mismatch");
            }
            
            // Double-check seat availability (race condition protection)
            for (Integer seatNumber : seatNumbers) {
                if (show.getBookedSeatIds().contains(seatNumber)) {
                    // Rollback version
                    show.compareAndUpdateVersion(show.getVersion() - 1);
                    return BookingResult.failure("Seat became unavailable during booking");
                }
            }
            
            // Commit the booking
            show.getBookedSeatIds().addAll(seatNumbers);
            bookingRepository.save(booking);
            showRepository.save(show);
            
            return BookingResult.success(booking);
        }
    }
}
```

##### 3. Database-Level Optimistic Locking
```sql
-- Example with JPA/Hibernate annotations
@Entity
@Table(name = "shows")
public class Show {
    @Id
    private Long showId;
    
    @Version  // JPA optimistic locking
    private Long version;
    
    @Column(name = "booked_seats")
    private String bookedSeatsJson;  // JSON array of seat IDs
    
    // Update query with version check
    @Query("UPDATE Show s SET s.bookedSeatsJson = :seats, s.version = s.version + 1 " +
           "WHERE s.showId = :showId AND s.version = :version")
    int updateBookedSeats(@Param("showId") Long showId, 
                         @Param("seats") String seats, 
                         @Param("version") Long version);
}
```

#### **Advantages of Optimistic Locking**
1. **High Concurrency**: Multiple users can browse seats simultaneously
2. **Better Performance**: No blocking on reads
3. **Deadlock Free**: No lock acquisition order issues
4. **Scalable**: Works well with high read-to-write ratios

#### **Disadvantages of Optimistic Locking**
1. **Retry Complexity**: Need robust retry mechanisms
2. **Potential Starvation**: High-contention scenarios may cause repeated failures
3. **User Experience**: Users might see "try again" messages
4. **Complex Error Handling**: Need to handle version conflicts gracefully

---

### **Pessimistic Locking Explanation**

Pessimistic locking assumes conflicts are likely and prevents them by acquiring locks upfront.

#### **Core Concept**
- Acquire exclusive locks before reading/modifying data
- Block other transactions until lock is released
- Guaranteed consistency but potentially lower concurrency

#### **Implementation Approaches**

##### 1. Synchronized Methods/Blocks
```java
public class PessimisticBookingService {
    private final Object showLocks = new ConcurrentHashMap<Integer, Object>();
    
    public synchronized BookingResult bookSeats(int showId, List<Integer> seatNumbers) {
        // Entire method is synchronized - very conservative
        Show show = showRepository.findById(showId);
        
        // Check availability
        for (Integer seatNumber : seatNumbers) {
            if (show.getBookedSeatIds().contains(seatNumber)) {
                return BookingResult.failure("Seat already booked");
            }
        }
        
        // Book seats
        show.getBookedSeatIds().addAll(seatNumbers);
        showRepository.save(show);
        
        return BookingResult.success(createBooking(show, seatNumbers));
    }
    
    // Better approach - per-show locking
    public BookingResult bookSeatsWithFineLocking(int showId, List<Integer> seatNumbers) {
        Object showLock = showLocks.computeIfAbsent(showId, k -> new Object());
        
        synchronized(showLock) {
            // Only this show is locked, others can proceed
            return performBooking(showId, seatNumbers);
        }
    }
}
```

##### 2. Database-Level Pessimistic Locking
```sql
-- SELECT FOR UPDATE - locks the row until transaction commits
BEGIN TRANSACTION;

SELECT * FROM shows 
WHERE show_id = ? 
FOR UPDATE;  -- This locks the row

-- Now safe to check and update
UPDATE shows 
SET booked_seats = ? 
WHERE show_id = ?;

COMMIT;
```

```java
@Repository
public class ShowRepository {
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Show s WHERE s.showId = :showId")
    Show findByIdWithLock(@Param("showId") Long showId);
    
    public BookingResult bookWithPessimisticLock(Long showId, List<Integer> seats) {
        Show show = findByIdWithLock(showId);  // Locks the row
        
        // Safe to modify - no other transaction can modify this show
        for (Integer seat : seats) {
            if (show.getBookedSeatIds().contains(seat)) {
                return BookingResult.failure("Seat unavailable");
            }
        }
        
        show.getBookedSeatIds().addAll(seats);
        save(show);  // Lock released on transaction commit
        
        return BookingResult.success();
    }
}
```

##### 3. Distributed Locking (Redis/Zookeeper)
```java
public class DistributedPessimisticBooking {
    private RedisTemplate<String, String> redisTemplate;
    private static final int LOCK_TIMEOUT_SECONDS = 30;
    
    public BookingResult bookSeats(int showId, List<Integer> seatNumbers) {
        String lockKey = "show_lock:" + showId;
        String lockValue = UUID.randomUUID().toString();
        
        try {
            // Acquire distributed lock
            Boolean acquired = redisTemplate.opsForValue()
                .setIfAbsent(lockKey, lockValue, Duration.ofSeconds(LOCK_TIMEOUT_SECONDS));
                
            if (!acquired) {
                return BookingResult.failure("Show is being modified by another user");
            }
            
            // Perform booking logic
            return performBooking(showId, seatNumbers);
            
        } finally {
            // Release lock (with value check to avoid releasing someone else's lock)
            releaseLock(lockKey, lockValue);
        }
    }
}
```

#### **Advantages of Pessimistic Locking**
1. **Guaranteed Consistency**: No race conditions
2. **Simpler Logic**: No retry mechanisms needed
3. **Predictable Behavior**: Either succeeds or fails cleanly
4. **Suitable for High Contention**: Works well when conflicts are frequent

#### **Disadvantages of Pessimistic Locking**
1. **Reduced Concurrency**: Blocking operations limit throughput
2. **Deadlock Risk**: Multiple locks can cause deadlocks
3. **Performance Impact**: Waiting threads consume resources
4. **Timeout Complexity**: Need to handle lock timeout scenarios

---

## Hybrid Approach: Two-Phase Booking

Combine both approaches for optimal user experience:

### Phase 1: Optimistic Reservation (Fast)
```java
public ReservationResult reserveSeats(int showId, List<Integer> seatNumbers) {
    // Quick optimistic check and temporary reservation
    Show show = showRepository.findById(showId);
    
    // Create temporary reservation (expires in 10 minutes)
    SeatReservation reservation = new SeatReservation(
        showId, seatNumbers, userId, 
        Instant.now().plus(10, ChronoUnit.MINUTES)
    );
    
    return reservationService.createReservation(reservation);
}
```

### Phase 2: Pessimistic Confirmation (Reliable)
```java
public BookingResult confirmBooking(String reservationId, PaymentDetails payment) {
    // Use pessimistic locking for final confirmation
    synchronized(getShowLock(reservationId)) {
        SeatReservation reservation = getValidReservation(reservationId);
        
        if (reservation.isExpired()) {
            return BookingResult.failure("Reservation expired");
        }
        
        // Process payment and confirm booking
        return finalizeBooking(reservation, payment);
    }
}
```

---

## Best Practices & Recommendations

### 1. **Choose Based on Usage Patterns**
- **High Read, Low Write**: Optimistic locking
- **High Contention**: Pessimistic locking
- **Mixed Workload**: Hybrid approach

### 2. **Implementation Guidelines**
- Keep critical sections minimal
- Implement proper retry mechanisms
- Add circuit breakers for system protection
- Use database transactions appropriately
- Monitor lock contention and performance

### 3. **Error Handling**
- Graceful degradation on lock failures
- Clear user messaging for booking conflicts
- Proper logging for debugging concurrency issues
- Fallback mechanisms for system reliability

### 4. **Testing Strategy**
- Simulate concurrent users with threading
- Test edge cases (timeouts, failures)
- Performance testing under load
- Verify no double bookings under any scenario

---

## Database Schema Considerations

```sql
-- Shows table with versioning
CREATE TABLE shows (
    show_id BIGINT PRIMARY KEY,
    movie_id BIGINT,
    screen_id BIGINT,
    show_time TIMESTAMP,
    booked_seats JSON,
    version BIGINT DEFAULT 0,  -- For optimistic locking
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_movie_time (movie_id, show_time),
    INDEX idx_screen_time (screen_id, show_time)
);

-- Seat reservations for temporary holds
CREATE TABLE seat_reservations (
    reservation_id VARCHAR(36) PRIMARY KEY,
    show_id BIGINT,
    seat_numbers JSON,
    user_id VARCHAR(36),
    expires_at TIMESTAMP,
    status ENUM('ACTIVE', 'CONFIRMED', 'EXPIRED'),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_show_expires (show_id, expires_at),
    INDEX idx_user_status (user_id, status)
);
```

This comprehensive approach ensures your BookMyShow system can handle concurrent bookings reliably while maintaining good performance and user experience.
