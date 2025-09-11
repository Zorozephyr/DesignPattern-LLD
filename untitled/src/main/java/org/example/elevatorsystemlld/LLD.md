# 🏢 Elevator System - Low Level Design (LLD)

## 📋 Table of Contents
1. [Problem Statement](#problem-statement)
2. [Requirements Analysis](#requirements-analysis)
3. [System Architecture](#system-architecture)
4. [Class Design](#class-design)
5. [Design Patterns Used](#design-patterns-used)
6. [Detailed Component Analysis](#detailed-component-analysis)
7. [Algorithms & Strategies](#algorithms--strategies)
8. [Concurrency & Thread Safety](#concurrency--thread-safety)
9. [Performance Considerations](#performance-considerations)
10. [How to Think About Similar Problems](#how-to-think-about-similar-problems)

---

## 🎯 Problem Statement

Design a **multi-elevator control system** that can efficiently handle multiple user requests simultaneously, with load balancing between elevators and pluggable scheduling algorithms.

### Core Challenges:
- **Concurrent Request Handling**: Multiple users making requests simultaneously
- **Load Balancing**: Distribute requests optimally across available elevators
- **Real-time Decision Making**: Select best elevator for each request
- **State Management**: Track elevator positions, directions, and queues
- **Extensibility**: Support different scheduling algorithms

---

## 📝 Requirements Analysis

### Functional Requirements:
1. **Multiple Elevator Support**: System should manage N elevators
2. **Request Types**:
   - **Outside Requests**: Floor panel buttons (UP/DOWN)
   - **Inside Requests**: Elevator panel buttons (specific floors)
3. **Real-time Updates**: Display current elevator status on each floor
4. **Optimal Scheduling**: Assign requests to most suitable elevator
5. **Direction Awareness**: Elevators should follow efficient routing

### Non-Functional Requirements:
1. **Performance**: Fast request processing (<100ms)
2. **Scalability**: Support 1-50 elevators, 1-100 floors
3. **Reliability**: System should handle failures gracefully
4. **Maintainability**: Easy to modify scheduling algorithms
5. **Thread Safety**: Handle concurrent requests safely

### Request Types Deep Dive:

#### 🔵 Outside Request (Floor Panel)
```
- User presses UP/DOWN button on floor
- Information Available: Floor Number, Direction
- Information Missing: Which elevator to use
- Requires: Load balancing algorithm
- Example: User on Floor 5 wants to go UP
```

#### 🔴 Inside Request (Elevator Panel)
```
- User presses floor button inside elevator
- Information Available: Floor Number, Elevator ID
- No Load Balancing Required: Request goes to same elevator
- Example: User inside Elevator #2 wants to go to Floor 8
```

---

## 🏗️ System Architecture

### High-Level Architecture Diagram

The system follows a **layered architecture** with clear separation of concerns:

```
┌─────────────────────────────────────────────────────────────┐
│                    PRESENTATION LAYER                       │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐        │
│  │ FloorButton │  │ InsidePanel │  │ FloorDisplay│        │
│  └─────────────┘  └─────────────┘  └─────────────┘        │
└─────────────────────────────────────────────────────────────┘
                              │
┌─────────────────────────────────────────────────────────────┐
│                    CONTROL LAYER                           │
│  ┌─────────────────────────────────────────────────────┐   │
│  │           ElevatorController                        │   │
│  │  - Request routing & load balancing                 │   │
│  └─────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
                              │
┌─────────────────────────────────────────────────────────────┐
│                    STRATEGY LAYER                          │
│  ┌─────────────────┐  ┌──────────────────────────────┐    │
│  │ ElevatorSelection│  │ GroupCollectiveBatching     │    │
│  │ Strategy         │  │ Strategy                     │    │
│  └─────────────────┘  └──────────────────────────────┘    │
└─────────────────────────────────────────────────────────────┘
                              │
┌─────────────────────────────────────────────────────────────┐
│                    EXECUTION LAYER                         │
│  ┌───────────┐  ┌───────────┐  ┌───────────┐  ┌────────┐  │
│  │Elevator#1 │  │Elevator#2 │  │Elevator#N │  │ Event  │  │
│  │(Thread)   │  │(Thread)   │  │(Thread)   │  │System  │  │
│  └───────────┘  └───────────┘  └───────────┘  └────────┘  │
└─────────────────────────────────────────────────────────────┘
```

### Key Architectural Principles:

1. **Single Responsibility**: Each class has one clear purpose
2. **Open/Closed**: Easy to add new scheduling strategies
3. **Dependency Inversion**: Controller depends on strategy abstraction
4. **Observer Pattern**: Event-driven updates for displays
5. **Strategy Pattern**: Pluggable elevator selection algorithms

---

## 🏛️ Class Design

### Core Classes Overview:

| Class | Responsibility | Key Methods |
|-------|---------------|-------------|
| `ElevatorController` | System orchestrator, request routing | `submitRequest()`, `pressUpButton()` |
| `Elevator` | Individual elevator logic, movement | `addRequest()`, `run()`, `moveToFloor()` |
| `ElevatorSelectionStrategy` | Algorithm interface | `selectElevator()` |
| `GroupCollectiveBatchingStrategy` | Advanced scheduling logic | `getPenalty()`, `directionAwarePenalty()` |
| `ElevatorRequest` | Request data structure | Getters for floor, direction |
| `ElevatorEvent` | Event notification system | Event type, floor, direction |

### Detailed Class Breakdown:

#### 🎮 ElevatorController (Central Orchestrator)
```java
class ElevatorController {
    // Core Components
    - List<Elevator> elevators              // Managed elevators
    - Map<Floor, List<FloorButton>> buttons // Floor button mapping
    - ElevatorSelectionStrategy strategy    // Pluggable algorithm
    
    // Key Operations
    + submitRequest(ElevatorRequest)        // Route external requests
    + pressUpButtonAtFloor(floor, elevatorId) // Handle UP button
    + pressDownButtonAtFloor(floor, elevatorId) // Handle DOWN button
    + pressInsideButton(floor, elevatorId)  // Handle internal button
}
```

**Key Design Decisions**:
- **Composition over Inheritance**: Contains elevators rather than extending
- **Strategy Pattern**: Delegates elevator selection to pluggable strategies
- **Thread Coordination**: Manages multiple elevator threads safely

#### 🛗 Elevator (Core Business Logic)
```java
class Elevator implements Runnable {
    // State Management
    - int id, currentFloor                  // Identity and position
    - Direction direction                   // UP, DOWN, IDLE
    - PriorityQueue<Integer> upQueue       // Ascending order
    - PriorityQueue<Integer> downQueue     // Descending order
    - List<ElevatorObserver> observers     // Event subscribers
    
    // Core Operations
    + synchronized addRequest(floor)        // Thread-safe request addition
    + run()                                // Main execution loop
    - synchronized getNextFloor()          // Get next destination
    - moveToFloor(floor)                   // Physical movement simulation
}
```

**Key Design Decisions**:
- **Thread Safety**: Synchronized methods for concurrent access
- **Priority Queues**: Efficient floor ordering (min-heap UP, max-heap DOWN)
- **Observer Pattern**: Notifies displays of state changes
- **State Machine**: Clear direction state transitions

#### 🧠 GroupCollectiveBatchingStrategy (Advanced Algorithm)
```java
class GroupCollectiveBatchingStrategy implements ElevatorSelectionStrategy {
    // Algorithm Logic
    + selectElevator(elevators, request)    // Main selection logic
    - getPenalty(elevator, request)         // Calculate elevator cost
    - directionAwarePenalty(elevator, floor, direction) // Smart penalties
}
```

**Penalty Calculation Logic**:
1. **Base Distance Penalty**: `distance × DISTANCE_PER_FLOOR`
2. **Direction Matching**: Lower penalty for same direction
3. **Queue Analysis**: Check if elevator already passed requested floor
4. **Opposite Direction**: High penalty for direction changes

---

## 🎨 Design Patterns Used

### 1. 🎯 Strategy Pattern
**Purpose**: Make elevator selection algorithms pluggable and interchangeable

```java
// Strategy Interface
interface ElevatorSelectionStrategy {
    Elevator selectElevator(List<Elevator> elevators, ElevatorRequest request);
}

// Concrete Strategies
class GroupCollectiveBatchingStrategy implements ElevatorSelectionStrategy
class NearestElevatorStrategy implements ElevatorSelectionStrategy

// Context (Controller uses strategy)
ElevatorSelectionStrategy strategy = ElevatorStrategyFactory.getStrategy("group-collective");
Elevator best = strategy.selectElevator(elevators, request);
```

**Benefits**:
- Easy to add new scheduling algorithms
- Runtime strategy switching possible
- Separation of algorithm logic from controller logic

### 2. 👁️ Observer Pattern
**Purpose**: Notify multiple displays when elevator state changes

```java
// Subject (Elevator)
class Elevator {
    private List<ElevatorObserver> observers;
    
    public void notify(ElevatorEvent.Type eventType) {
        ElevatorEvent event = new ElevatorEvent(currentFloor, direction, id, eventType);
        for(ElevatorObserver observer : observers) {
            observer.update(event);
        }
    }
}

// Observers
class FloorDisplay implements ElevatorObserver
class InsidePanel implements ElevatorObserver
```

**Benefits**:
- Loose coupling between elevator and displays
- Easy to add new display types
- Real-time updates without polling

### 3. 🏭 Factory Pattern
**Purpose**: Create appropriate strategy instances

```java
class ElevatorStrategyFactory {
    public static ElevatorSelectionStrategy getStrategy(String type) {
        return switch(type) {
            case "group-collective" -> new GroupCollectiveBatchingStrategy();
            case "nearest" -> new NearestElevatorStrategy();
            default -> new NearestElevatorStrategy();
        };
    }
}
```

### 4. 🏃‍♂️ Runnable Pattern
**Purpose**: Enable concurrent elevator operation

```java
class Elevator implements Runnable {
    public void run() {
        while(running) {
            Integer nextFloor = getNextFloor();
            if(nextFloor != null) {
                moveToFloor(nextFloor);
            }
        }
    }
}

// Usage
Thread elevatorThread = new Thread(elevator);
elevatorThread.start();
```

---

## 🔍 Detailed Component Analysis

### Elevator State Management

#### State Transitions:
```
IDLE ──→ UP ──→ IDLE
 ↑        ↓      ↑
 └── DOWN ←──────┘
```

#### Queue Management:
```java
// Efficient floor ordering
PriorityQueue<Integer> upQueue = new PriorityQueue<>();           // [2, 5, 8, 12]
PriorityQueue<Integer> downQueue = new PriorityQueue<>(reverseOrder()); // [15, 10, 7, 3]
```

**Why Priority Queues?**
- **Automatic Sorting**: Floors served in optimal order
- **Efficient Operations**: O(log n) insertion, O(log n) removal
- **Natural Direction Flow**: UP goes ascending, DOWN goes descending

### Request Processing Flow:

```
1. User Action
   ↓
2. Button Press (FloorButton/InsidePanel)
   ↓
3. Create ElevatorRequest
   ↓
4. ElevatorController.submitRequest()
   ↓
5. Strategy.selectElevator() → Apply penalty calculation
   ↓
6. Selected Elevator.addRequest()
   ↓
7. Add to appropriate queue (upQueue/downQueue)
   ↓
8. Elevator.run() processes queue
   ↓
9. moveToFloor() → Notify observers
   ↓
10. Display updates (FloorDisplay, InsidePanel)
```

---

## 🧮 Algorithms & Strategies

### GroupCollectiveBatchingStrategy (Production Algorithm)

This is the **most sophisticated** algorithm, mimicking real-world elevator systems.

#### Penalty Calculation Matrix:

| Scenario | Penalty | Reasoning |
|----------|---------|-----------|
| **Idle Elevator** | 1 | Best case - immediate response |
| **Same Direction, Not Passed** | 12 + distance×3 | Can accommodate with minor delay |
| **Same Direction, Already Passed** | 200 + distance×3 | Must complete journey first |
| **Opposite Direction** | 200 + queue_size×15 | Must finish current direction |
| **Pass-by Required** | 8 + distance×3 | Minor inefficiency |

#### Algorithm Steps:
```java
1. For each elevator:
   a. Calculate base distance penalty
   b. Check direction compatibility
   c. Analyze queue state (passed floor check)
   d. Apply appropriate penalty multipliers
   
2. Select elevator with minimum penalty
3. Add request to selected elevator's queue
```

#### Advanced Features:
- **Already Passed Detection**: If elevator going UP is at floor 8, and request is for floor 5 UP, apply opposite direction penalty
- **Queue Analysis**: Check if any pending stops would cause passing the requested floor
- **Load Balancing**: Consider queue sizes in penalty calculation

### NearestElevatorStrategy (Simple Algorithm)

```java
// Simple distance-based selection
int minDistance = Integer.MAX_VALUE;
Elevator bestElevator = null;

for(Elevator elevator : elevators) {
    int distance = Math.abs(elevator.getCurrentFloor() - request.getFloor());
    if(distance < minDistance) {
        minDistance = distance;
        bestElevator = elevator;
    }
}
```

**When to Use**: Simple buildings, low traffic, quick implementation

---

## 🔒 Concurrency & Thread Safety

### Thread Safety Mechanisms:

#### 1. Synchronized Methods
```java
public synchronized void addRequest(int floor) {
    // Thread-safe request addition
    if(floor < currentFloor) {
        downQueue.add(floor);
    } else if(floor > currentFloor) {
        upQueue.add(floor);
    }
}
```

#### 2. Synchronized Blocks
```java
synchronized(this) {
    if(upQueue.isEmpty() && downQueue.isEmpty()) {
        this.direction = Direction.IDLE;
        notify(ElevatorEvent.Type.IDLE);
    }
}
```

#### 3. Volatile Variables
```java
private volatile boolean running = true; // Ensures visibility across threads
```

### Potential Race Conditions & Solutions:

| Race Condition | Problem | Solution |
|----------------|---------|----------|
| **Queue Modification** | Multiple threads adding requests | `synchronized addRequest()` |
| **Direction Changes** | State inconsistency during transitions | `synchronized` blocks in `moveToFloor()` |
| **Observer Notifications** | Concurrent event firing | Synchronized notification method |

### ReadWriteLock Optimization (Advanced):
```java
// For high-read scenarios (strategy evaluation)
private final ReadWriteLock upQueueLock = new ReentrantReadWriteLock();

// Read operations (penalty calculation)
upQueueLock.readLock().lock();
try {
    return upQueue.contains(floor);
} finally {
    upQueueLock.readLock().unlock();
}

// Write operations (request addition)
upQueueLock.writeLock().lock();
try {
    upQueue.add(floor);
} finally {
    upQueueLock.writeLock().unlock();
}
```

---

## ⚡ Performance Considerations

### Time Complexity Analysis:

| Operation | Complexity | Explanation |
|-----------|------------|-------------|
| **Request Addition** | O(log n) | PriorityQueue insertion |
| **Next Floor Retrieval** | O(log n) | PriorityQueue poll() |
| **Elevator Selection** | O(m × k) | m=elevators, k=queue analysis |
| **Penalty Calculation** | O(k) | k=queue size for analysis |

### Space Complexity:
- **Per Elevator**: O(f) where f = max floors in queues
- **Total System**: O(m × f) where m = number of elevators

### Optimization Strategies:

#### 1. Queue Size Limits
```java
private static final int MAX_QUEUE_SIZE = 50;

public synchronized void addRequest(int floor) {
    if(upQueue.size() + downQueue.size() >= MAX_QUEUE_SIZE) {
        // Handle overflow - reject or queue externally
        return;
    }
    // Normal processing
}
```

#### 2. Penalty Caching
```java
// Cache penalty calculations for frequently accessed elevators
private final Map<ElevatorRequest, Integer> penaltyCache = new ConcurrentHashMap<>();
```

#### 3. Batch Processing
```java
// Process multiple requests in batches during peak hours
private final List<ElevatorRequest> pendingRequests = new ArrayList<>();
```

---

## 🧠 How to Think About Similar Problems

### 1. 🎯 Problem Decomposition Strategy

When approaching **similar distributed system problems**, follow this thinking process:

#### Step 1: Identify Core Entities
```
Ask yourself:
- What are the main "actors" in the system?
- What states do they maintain?
- How do they interact?

Example: Elevator System
- Entities: Elevators, Floors, Requests, Users
- States: Position, Direction, Queue, Capacity
- Interactions: Request submission, Movement, Notifications
```

#### Step 2: Define Request Types
```
Categorize different types of operations:
- Who initiates them?
- What information is available?
- What information is missing?
- Do they require coordination?

Example: Outside vs Inside requests
- Outside: Need elevator selection (coordination required)
- Inside: Direct to specific elevator (no coordination)
```

#### Step 3: Identify Coordination Points
```
Where does the system need to make decisions?
- Resource allocation (which elevator?)
- Conflict resolution (multiple requests)
- State synchronization (elevator positions)
```

### 2. 🏗️ Design Pattern Selection

#### For Resource Allocation Problems:
- **Strategy Pattern**: Multiple allocation algorithms
- **Factory Pattern**: Create appropriate strategies
- **Observer Pattern**: Notify interested parties of state changes

#### For State Management:
- **State Pattern**: Complex state transitions
- **Command Pattern**: Encapsulate operations
- **Memento Pattern**: Save/restore states

#### For Concurrent Systems:
- **Producer-Consumer**: Request queuing
- **Thread Pool**: Manage concurrent operations
- **Lock Patterns**: Ensure thread safety

### 3. 🎲 Algorithm Design Approach

#### Step 1: Start Simple
```java
// Begin with naive approach
Elevator findNearestElevator(List<Elevator> elevators, int floor) {
    return elevators.stream()
        .min(Comparator.comparing(e -> Math.abs(e.getCurrentFloor() - floor)))
        .orElse(elevators.get(0));
}
```

#### Step 2: Add Sophistication
```java
// Gradually add complexity
- Consider direction compatibility
- Add penalty-based scoring
- Include queue analysis
- Factor in load balancing
```

#### Step 3: Optimize for Real-World
```java
// Add production concerns
- Handle edge cases
- Add performance monitoring
- Include failure handling
- Consider scalability
```

### 4. 📊 Similar Problem Domains

This design approach applies to many **resource allocation systems**:

#### 🚗 Ride Sharing (Uber/Lyft)
```
Entities: Drivers, Riders, Trips
Coordination: Driver-Rider matching
Algorithms: Distance, ETA, driver rating
Patterns: Strategy (matching), Observer (trip updates)
```

#### 🏥 Hospital Resource Management
```
Entities: Doctors, Patients, Rooms, Equipment
Coordination: Resource scheduling
Algorithms: Priority-based, availability matching
Patterns: Strategy (scheduling), Command (operations)
```

#### 🍕 Food Delivery Systems
```
Entities: Restaurants, Delivery Agents, Orders
Coordination: Order-agent assignment
Algorithms: Distance, capacity, delivery time
Patterns: Strategy (assignment), Observer (status updates)
```

#### 🖥️ Load Balancer Design
```
Entities: Servers, Requests, Health Checkers
Coordination: Request routing
Algorithms: Round-robin, weighted, least-connections
Patterns: Strategy (balancing), Observer (health monitoring)
```

### 5. 🔍 Interview Approach Framework

#### Phase 1: Clarification (5-10 minutes)
```
Questions to Ask:
1. Scale: How many elevators/floors?
2. Requirements: What features are must-have vs nice-to-have?
3. Constraints: Performance requirements, memory limits?
4. Use Cases: Peak vs off-peak usage patterns?
```

#### Phase 2: High-Level Design (10-15 minutes)
```
1. Draw system architecture
2. Identify main components
3. Show data flow
4. Discuss design patterns
```

#### Phase 3: Detailed Design (15-20 minutes)
```
1. Design key classes
2. Define interfaces
3. Show important methods
4. Discuss algorithms
```

#### Phase 4: Deep Dives (10-15 minutes)
```
1. Concurrency handling
2. Performance optimization
3. Failure scenarios
4. Scalability considerations
```

### 6. 🚨 Common Pitfalls to Avoid

#### ❌ Over-Engineering Early
```
Don't start with:
- Complex distributed systems
- Advanced optimization
- Every possible edge case

Do start with:
- Simple, working solution
- Clear interfaces
- Basic functionality
```

#### ❌ Ignoring Concurrency
```
Always consider:
- Thread safety from the beginning
- Race condition scenarios
- Deadlock possibilities
- Performance under load
```

#### ❌ Forgetting Edge Cases
```
Consider:
- Empty states (no elevators, no requests)
- Boundary conditions (top/bottom floors)
- Invalid inputs
- System failures
```

### 7. 🎯 Key Takeaways for Interviews

#### ✅ Demonstrate Systems Thinking
- Show understanding of distributed systems concepts
- Discuss trade-offs between different approaches
- Consider real-world production concerns

#### ✅ Code Quality Matters
- Use meaningful variable names
- Add appropriate comments
- Show understanding of SOLID principles
- Demonstrate design pattern knowledge

#### ✅ Think About Operations
- How would you monitor this system?
- What metrics would you track?
- How would you handle failures?
- How would you scale it?

---

## 🎉 Conclusion

This elevator system demonstrates **production-grade system design** with:

- **Scalable Architecture**: Supports multiple elevators and floors
- **Intelligent Algorithms**: Real-world penalty-based scheduling
- **Thread Safety**: Proper concurrency handling
- **Extensibility**: Easy to add new features and algorithms
- **Performance**: Optimized for high-throughput scenarios

The design patterns and principles used here are **directly applicable** to many other distributed systems problems, making this a valuable learning experience for system design interviews and real-world development.

---

*💡 **Pro Tip**: When explaining this design in interviews, focus on the **thought process** and **trade-offs** rather than just the implementation details. Interviewers want to see how you think about complex systems!*