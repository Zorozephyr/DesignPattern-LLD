package org.example.elevatorsystemlld;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class Elevator implements Runnable {
    private int id;

    private int currentFloor=0;

    private Direction direction = Direction.IDLE;

    private InsidePanel insidePanel;

    private volatile boolean running = true;

    private PriorityQueue<Integer> upQueue = new PriorityQueue<>();
    private PriorityQueue<Integer> downQueue = new PriorityQueue<>(Comparator.reverseOrder());

    private List<ElevatorObserver> observers = new ArrayList<>();

    public Elevator(int id) {
        this.id = id;
        this.insidePanel = new InsidePanel(this);
        addObserver(insidePanel);
    }

    public void addObserver(ElevatorObserver elevatorObserver){
        observers.add(elevatorObserver);
    }

    public void notify(ElevatorEvent.Type elevatorEventType){
        ElevatorEvent event = new ElevatorEvent(currentFloor,direction,id, elevatorEventType);
        for(ElevatorObserver elevatorObserver: observers){
            elevatorObserver.update(event);
        }
    }
    public synchronized void addRequest(int floor){
        if(floor < currentFloor){
            downQueue.add(floor);
        }else if(floor > currentFloor){
            upQueue.add(floor);
        }
        else{
            System.out.println("Elevator " + id + " is already at floor "  + floor);
        }
        if(direction == Direction.IDLE){
            direction = floor > currentFloor ? Direction.UP: Direction.DOWN;
        }
    }

    @Override
    public void run() {
        System.out.println("Elevator "+ id + " is running");
        while(running){
            Integer nextFloor = getNextFloor();
            if(nextFloor!=null){
                //elevator has to move to the floor
                try {
                    moveToFloor(nextFloor);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private synchronized Integer getNextFloor(){
        return switch (direction){
            case UP -> upQueue.poll();
            case DOWN -> downQueue.poll();
            case IDLE -> null;
        };
    }

    private void moveToFloor(int floor) throws InterruptedException {
        while (currentFloor != floor){
            currentFloor += direction == Direction.UP ? 1: -1;
            notify(ElevatorEvent.Type.MOVING);
            System.out.println("Elevator "+ id + " reached at a floor " + currentFloor);
            Thread.sleep(100);
        }
        notify(ElevatorEvent.Type.ARRIVED);
        Thread.sleep(200);
        notify(ElevatorEvent.Type.DOOR_OPEN);
        System.out.println("Opening doors");
        Thread.sleep(500);
        //if queue is empty
        synchronized (this){
            if(upQueue.isEmpty() && downQueue.isEmpty()){
                this.direction = Direction.IDLE;
                notify(ElevatorEvent.Type.IDLE);
            }
            else if(upQueue.isEmpty() && direction == Direction.UP && !downQueue.isEmpty()){
                direction = Direction.DOWN;
            }
            else if(!upQueue.isEmpty() && direction == Direction.DOWN && downQueue.isEmpty()){
                direction = Direction.UP;
            }
        }

    }

    public int getId() {
        return id;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public Direction getDirection() {
        return direction;
    }

    public InsidePanel getInsidePanel() {
        return insidePanel;
    }

    public PriorityQueue<Integer> getUpQueue() {
        return upQueue;
    }

    public PriorityQueue<Integer> getDownQueue() {
        return downQueue;
    }

    public int getUpQueueSize(){
        return upQueue.size();
    }

    public int getDownQueueSize(){
        return downQueue.size();
    }
}



