package org.example.elevatorsystemlld.models;

import java.util.*;

public class ElevatorController {
    private Elevator elevator;  // Embedded elevator object

    // LOOK Algorithm Data Structures - 4 logical queues for proper passenger separation
    private PriorityQueue<Integer> upRequests_UpPassengers;     // UP passengers, floors > current (MinHeap - ascending)
    private PriorityQueue<Integer> upRequests_DownPassengers;   // DOWN passengers, floors > current (MaxHeap - descending)
    private PriorityQueue<Integer> downRequests_UpPassengers;   // UP passengers, floors < current (MinHeap - ascending)
    private PriorityQueue<Integer> downRequests_DownPassengers; // DOWN passengers, floors < current (MaxHeap - descending)
    private Set<Integer> processedRequests;        // To avoid duplicate processing

    public ElevatorController(Elevator elevator) {
        this.elevator = elevator;
        this.upRequests_UpPassengers = new PriorityQueue<>();                    // Min heap - serve 6 first
        this.upRequests_DownPassengers = new PriorityQueue<>(Collections.reverseOrder()); // Max heap - serve 8,7,5
        this.downRequests_UpPassengers = new PriorityQueue<>();                  // Min heap - ascending order
        this.downRequests_DownPassengers = new PriorityQueue<>(Collections.reverseOrder()); // Max heap - descending order
        this.processedRequests = new HashSet<>();
    }

    public void addRequest(int floor, Direction requestDirection) {
        // Don't add request for current floor
        if (floor == elevator.getCurrentFloor()) {
            return;
        }

        // Route to appropriate queue based on floor position AND passenger direction
        routeRequestToQueue(floor, requestDirection);

        System.out.println("Added request: Floor " + floor + " Direction " + requestDirection);
        processRequests();
    }

    private void routeRequestToQueue(int floor, Direction passengerDirection) {
        int currentFloor = elevator.getCurrentFloor();

        if (floor > currentFloor) {
            // Floors above current floor - will be served during UP phase
            if (passengerDirection == Direction.UP) {
                upRequests_UpPassengers.offer(floor);    // UP passengers: serve in ascending order
            } else {
                upRequests_DownPassengers.offer(floor);  // DOWN passengers: serve in descending order
            }
        } else {
            // Floors below current floor - will be served during DOWN phase (after direction change)
            if (passengerDirection == Direction.UP) {
                downRequests_UpPassengers.offer(floor);    // UP passengers: serve in ascending order
            } else {
                downRequests_DownPassengers.offer(floor);  // DOWN passengers: serve in descending order
            }
        }
    }

    public void processRequests() {
        if (elevator.getStatus() == ElevatorStatus.MOVING) {
            return; // Don't process if already moving
        }

        Direction currentDirection = elevator.getCurrentDirection();
        int nextFloor = getNextFloor(currentDirection);

        if (nextFloor != -1) {
            moveElevatorToFloor(nextFloor);
            processedRequests.add(nextFloor);
        }

        // Continue processing if more requests exist
        if (!upRequests_UpPassengers.isEmpty() || !upRequests_DownPassengers.isEmpty() ||
            !downRequests_UpPassengers.isEmpty() || !downRequests_DownPassengers.isEmpty()) {
            processRequests();
        }
    }

    private void moveElevatorToFloor(int targetFloor) {
        int currentFloor = elevator.getCurrentFloor();

        if (targetFloor > currentFloor) {
            elevator.setCurrentDirection(Direction.UP);
        } else if (targetFloor < currentFloor) {
            elevator.setCurrentDirection(Direction.DOWN);
        }

        elevator.setStatus(ElevatorStatus.MOVING);
        elevator.setCurrentFloor(targetFloor);
        System.out.println("Elevator " + elevator.getElevatorId() + " moved to floor: " + targetFloor);
        elevator.setStatus(ElevatorStatus.IDLE);
    }

    private int getNextFloor(Direction currentDirection) {
        if (currentDirection == Direction.UP) {
            if (!upRequests_UpPassengers.isEmpty()) {
                return upRequests_UpPassengers.poll(); // Get minimum (closest floor above) for UP passengers
            } else if (!upRequests_DownPassengers.isEmpty()) {
                return upRequests_DownPassengers.poll(); // Get maximum (closest floor above) for DOWN passengers
            }
        } else if (currentDirection == Direction.DOWN) {
            if (!downRequests_UpPassengers.isEmpty()) {
                return downRequests_UpPassengers.poll(); // Get minimum (closest floor below) for UP passengers
            } else if (!downRequests_DownPassengers.isEmpty()) {
                return downRequests_DownPassengers.poll(); // Get maximum (closest floor below) for DOWN passengers
            }
        }

        // Change direction and get next request if no requests in current direction
        return changeDirectionAndGetNext();
    }

    private int changeDirectionAndGetNext() {
        if (!downRequests_DownPassengers.isEmpty()) {
            elevator.setCurrentDirection(Direction.DOWN);
            return downRequests_DownPassengers.poll();
        } else if (!downRequests_UpPassengers.isEmpty()) {
            elevator.setCurrentDirection(Direction.DOWN);
            return downRequests_UpPassengers.poll();
        } else if (!upRequests_DownPassengers.isEmpty()) {
            elevator.setCurrentDirection(Direction.UP);
            return upRequests_DownPassengers.poll();
        } else if (!upRequests_UpPassengers.isEmpty()) {
            elevator.setCurrentDirection(Direction.UP);
            return upRequests_UpPassengers.poll();
        }
        return -1; // No more requests
    }

    // Getter methods for testing and debugging
    public Elevator getElevator() {
        return elevator;
    }

    public boolean hasRequests() {
        return !upRequests_UpPassengers.isEmpty() || !upRequests_DownPassengers.isEmpty() ||
               !downRequests_UpPassengers.isEmpty() || !downRequests_DownPassengers.isEmpty();
    }

    public int getUpRequestsCount() {
        return upRequests_UpPassengers.size() + upRequests_DownPassengers.size();
    }

    public int getDownRequestsCount() {
        return downRequests_UpPassengers.size() + downRequests_DownPassengers.size();
    }
}
