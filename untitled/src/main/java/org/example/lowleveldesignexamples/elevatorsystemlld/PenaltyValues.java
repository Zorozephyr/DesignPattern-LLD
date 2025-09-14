package org.example.lowleveldesignexamples.elevatorsystemlld;

/**
 * Realistic penalty values for elevator scheduling based on real-world lift systems
 * Lower values = higher priority, Higher values = lower priority
 */
public class PenaltyValues {
    // Base penalty per floor distance (realistic: 2-5 seconds per floor)
    public static final int DISTANCE_PER_FLOOR = 3;
    
    // Penalty for having to pass by the requested floor (minor inefficiency)
    public static final int PASS_BY_FLOOR = 8;
    
    // Penalty for inserting a new stop in the current direction (slight delay)
    public static final int INSERT_PENALTY = 12;
    
    // High penalty for opposite direction (must complete current journey first)
    public static final int OPPOSITE_DIRECTION = 200;
    
    // Penalty per request in opposite direction queue (increased waiting time)
    public static final int OPPOSITE_QUEUE_PENALTY = 15;
    
    // Low penalty for idle elevator (best case - immediate response)
    public static final int IDLE_PENALTY = 1;
    
    // Additional realistic penalties for advanced scenarios
    public static final int CAPACITY_NEAR_FULL_PENALTY = 25; // 80%+ capacity
    public static final int CAPACITY_FULL_PENALTY = 1000;    // 100% capacity
    public static final int MAINTENANCE_PENALTY = 2000;      // Under maintenance
    public static final int EMERGENCY_STOP_PENALTY = 3000;   // Emergency mode
    
    // Time-based penalties (peak hours consideration)
    public static final int PEAK_HOUR_MULTIPLIER = 2;        // Double penalties during peak
    public static final int OFF_PEAK_DISCOUNT = 1;           // Normal penalties off-peak
}
