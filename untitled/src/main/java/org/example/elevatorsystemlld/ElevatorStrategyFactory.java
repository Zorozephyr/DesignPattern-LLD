package org.example.elevatorsystemlld;

public class ElevatorStrategyFactory {

    public static ElevatorSelectionStrategy getStrategy(String strategy){
        return switch (strategy){
            case "nearest" -> new NearestElevatorStrategy();
            case "group-collective" -> new GroupCollectiveBatchingStrategy();
            default -> throw new IllegalArgumentException("Unknown Strategy");
        };
    }
}
