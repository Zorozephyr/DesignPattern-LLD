package org.example.elevatorsystemlld;

public class ElevatorSystem {
    public static void main(String[] args){
        ElevatorSelectionStrategy elevatorSelectionStrategy = ElevatorStrategyFactory.getStrategy("group-collective");
        ElevatorController controller = new ElevatorController(3,10,elevatorSelectionStrategy);
        //Simulate requests
        controller.pressUpButtonAtFloor(2,0);
        controller.pressUpButtonAtFloor(1,0);
        controller.pressInsideButton(3,0);
        controller.pressInsideButton(9,0);
        controller.pressDownButtonAtFloor(4,0);
        controller.pressInsideButton(5,0);
    }
}
