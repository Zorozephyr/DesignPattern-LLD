Requirements:

Object:
1.Elevator Car - Display, Current floor, Direction,Internal Button, Doors
2.Building
3.Floor
4.External Button -> Up and Down
5.Display
6.Direction - ENUM Up Down
7.Multiple Lifts - Lift Dispatch Algorithm - Strategy Pattern
8.Status - ENUM standy, moving

Base Case: 3 Lifts

Elevator Car ->
    int CurrentFloor
    Direction direction
    InternalButton internalButton
    move(int destinationFloor,Direction dir)
    Status status

InternalButton
    InternalButtonDispatcher
    pressButton(int button)

ElevatorController
    Elevator obj
    acceptNewRequest(int floor,Direction dir)
    controlElev()
    //One elevator has many requests
    
InternalButtonDispatcher
    List<ElevatorController>
    submitRequest(int liftId)

ExternalButtonDispatcher
    List<ElevatorController>
    submitRequest(int floor, Direction dir)

ExternalButton
    ExternalButtonDispatcher
    pressButton(floor, direction)

Floor
    floorId
    ExternalButton

Building
    List<Floor>

Algorithm:
    ExternalDispatcherAlgo - Need to figure out
    ElevatorControllerAlgo - Use LOOK


LOOK Algorithm
1.MaxPQ - Down
2.MinPQ - Up
3.PendingJobs - Just an array

    