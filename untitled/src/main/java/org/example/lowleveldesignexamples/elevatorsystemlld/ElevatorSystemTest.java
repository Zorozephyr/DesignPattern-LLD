package org.example.lowleveldesignexamples.elevatorsystemlld;

/**
 * Comprehensive test cases for the Elevator System
 * Tests various real-world scenarios to validate system behavior
 */
public class ElevatorSystemTest {
    
    public static void main(String[] args) {
        System.out.println("=== ELEVATOR SYSTEM COMPREHENSIVE TEST SUITE ===\n");
        
        // Run all test scenarios
        testBasicFunctionality();
        testMultipleElevatorSelection();
        testDirectionAwareness();
        testPeakHourScenario();
        testEdgeCases();
        testPerformanceScenario();
        
        System.out.println("=== ALL TESTS COMPLETED ===");
    }
    
    /**
     * Test Case 1: Basic Elevator Functionality
     * Validates basic up/down requests and internal button presses
     */
    public static void testBasicFunctionality() {
        System.out.println("🔧 TEST 1: Basic Functionality");
        System.out.println("Testing basic elevator operations...\n");
        
        ElevatorSelectionStrategy strategy = ElevatorStrategyFactory.getStrategy("group-collective");
        ElevatorController controller = new ElevatorController(2, 10, strategy);
        
        try {
            // Basic up request from ground floor
            System.out.println("📍 Request: Up from Floor 0");
            controller.pressUpButtonAtFloor(0, 0);
            Thread.sleep(500);
            
            // Internal button press to go to floor 5
            System.out.println("📍 Request: Inside button to Floor 5");
            controller.pressInsideButton(5, 0);
            Thread.sleep(2000);
            
            // Down request from floor 8
            System.out.println("📍 Request: Down from Floor 8");
            controller.pressDownButtonAtFloor(8, 0);
            Thread.sleep(3000);
            
        } catch (InterruptedException e) {
            System.err.println("Test interrupted: " + e.getMessage());
        }
        
        System.out.println("✅ Basic functionality test completed\n");
    }
    
    /**
     * Test Case 2: Multiple Elevator Selection
     * Tests the system's ability to select the best elevator among multiple options
     */
    public static void testMultipleElevatorSelection() {
        System.out.println("🏢 TEST 2: Multiple Elevator Selection");
        System.out.println("Testing elevator selection strategy with 4 elevators...\n");
        
        ElevatorSelectionStrategy strategy = ElevatorStrategyFactory.getStrategy("group-collective");
        ElevatorController controller = new ElevatorController(4, 15, strategy);
        
        try {
            // Create different scenarios for elevator selection
            System.out.println("📍 Scenario: Multiple simultaneous requests");
            
            // Elevator 0: Send to floor 3 (will be going UP)
            controller.pressInsideButton(3, 0);
            Thread.sleep(200);
            
            // Elevator 1: Send to floor 10 (will be going UP)
            controller.pressInsideButton(10, 1);
            Thread.sleep(200);
            
            // Elevator 2: Send to floor 2 (will be going UP)
            controller.pressInsideButton(2, 2);
            Thread.sleep(200);
            
            // Now make requests that should be intelligently assigned
            System.out.println("📍 Request: Up from Floor 4 (should pick nearest going up)");
            controller.pressUpButtonAtFloor(4, 0); // Should pick elevator going to 10
            Thread.sleep(500);
            
            System.out.println("📍 Request: Up from Floor 1 (should pick nearest)");
            controller.pressUpButtonAtFloor(1, 0); // Should pick elevator going to 2 or 3
            Thread.sleep(500);
            
            System.out.println("📍 Request: Down from Floor 12 (should pick idle elevator)");
            controller.pressDownButtonAtFloor(12, 0); // Should pick idle elevator
            Thread.sleep(3000);
            
        } catch (InterruptedException e) {
            System.err.println("Test interrupted: " + e.getMessage());
        }
        
        System.out.println("✅ Multiple elevator selection test completed\n");
    }
    
    /**
     * Test Case 3: Direction Awareness
     * Tests the system's understanding of elevator direction and optimal scheduling
     */
    public static void testDirectionAwareness() {
        System.out.println("🧭 TEST 3: Direction Awareness");
        System.out.println("Testing direction-aware penalty system...\n");
        
        ElevatorSelectionStrategy strategy = ElevatorStrategyFactory.getStrategy("group-collective");
        ElevatorController controller = new ElevatorController(3, 12, strategy);
        
        try {
            // Set up elevators in different states
            System.out.println("📍 Setup: Creating directional scenarios");
            
            // Elevator 0: Going UP (0 -> 8)
            controller.pressInsideButton(8, 0);
            Thread.sleep(300);
            
            // Elevator 1: Going DOWN (start from top, simulate by going up first then down)
            controller.pressInsideButton(10, 1);
            Thread.sleep(500);
            controller.pressInsideButton(2, 1);
            Thread.sleep(300);
            
            // Test same direction optimization
            System.out.println("📍 Request: Up from Floor 5 (elevator 0 should handle - same direction)");
            controller.pressUpButtonAtFloor(5, 0);
            Thread.sleep(500);
            
            // Test opposite direction penalty
            System.out.println("📍 Request: Down from Floor 3 (should avoid busy elevators)");
            controller.pressDownButtonAtFloor(3, 0);
            Thread.sleep(500);
            
            // Test already passed floor scenario
            Thread.sleep(2000); // Let elevator 0 pass floor 5
            System.out.println("📍 Request: Up from Floor 3 (after elevator passed - should penalize)");
            controller.pressUpButtonAtFloor(3, 0);
            Thread.sleep(3000);
            
        } catch (InterruptedException e) {
            System.err.println("Test interrupted: " + e.getMessage());
        }
        
        System.out.println("✅ Direction awareness test completed\n");
    }
    
    /**
     * Test Case 4: Peak Hour Simulation
     * Simulates high-traffic scenarios typical during rush hours
     */
    public static void testPeakHourScenario() {
        System.out.println("⏰ TEST 4: Peak Hour Simulation");
        System.out.println("Simulating rush hour with high request volume...\n");
        
        ElevatorSelectionStrategy strategy = ElevatorStrategyFactory.getStrategy("group-collective");
        ElevatorController controller = new ElevatorController(3, 20, strategy);
        
        try {
            System.out.println("📍 Simulating morning rush hour (many up requests)");
            
            // Simulate multiple people wanting to go up from different floors
            for (int floor = 1; floor <= 5; floor++) {
                System.out.println("📍 Rush request: Up from Floor " + floor);
                controller.pressUpButtonAtFloor(floor, 0);
                Thread.sleep(100); // Quick succession
            }
            
            // Add some high-floor destinations
            controller.pressInsideButton(15, 0);
            controller.pressInsideButton(18, 1);
            controller.pressInsideButton(12, 2);
            Thread.sleep(500);
            
            System.out.println("📍 Simulating evening rush hour (many down requests)");
            
            // Simulate people going down from high floors
            for (int floor = 15; floor >= 10; floor--) {
                System.out.println("📍 Rush request: Down from Floor " + floor);
                controller.pressDownButtonAtFloor(floor, 0);
                Thread.sleep(150);
            }
            
            Thread.sleep(4000);
            
        } catch (InterruptedException e) {
            System.err.println("Test interrupted: " + e.getMessage());
        }
        
        System.out.println("✅ Peak hour simulation completed\n");
    }
    
    /**
     * Test Case 5: Edge Cases
     * Tests boundary conditions and unusual scenarios
     */
    public static void testEdgeCases() {
        System.out.println("⚠️ TEST 5: Edge Cases");
        System.out.println("Testing boundary conditions and edge cases...\n");
        
        ElevatorSelectionStrategy strategy = ElevatorStrategyFactory.getStrategy("group-collective");
        ElevatorController controller = new ElevatorController(2, 10, strategy);
        
        try {
            // Test same floor request
            System.out.println("📍 Edge Case: Request same floor as elevator");
            controller.pressInsideButton(0, 0); // Already at floor 0
            Thread.sleep(300);
            
            // Test top floor requests
            System.out.println("📍 Edge Case: Request to top floor");
            controller.pressUpButtonAtFloor(9, 0);
            controller.pressInsideButton(9, 0);
            Thread.sleep(1000);
            
            // Test ground floor requests
            System.out.println("📍 Edge Case: Request to ground floor from top");
            controller.pressDownButtonAtFloor(9, 0);
            controller.pressInsideButton(0, 1);
            Thread.sleep(500);
            
            // Test multiple requests to same floor
            System.out.println("📍 Edge Case: Multiple requests to same floor");
            controller.pressUpButtonAtFloor(5, 0);
            controller.pressInsideButton(5, 0);
            controller.pressInsideButton(5, 1);
            Thread.sleep(2000);
            
        } catch (InterruptedException e) {
            System.err.println("Test interrupted: " + e.getMessage());
        }
        
        System.out.println("✅ Edge cases test completed\n");
    }
    
    /**
     * Test Case 6: Performance Scenario
     * Tests system performance under various load conditions
     */
    public static void testPerformanceScenario() {
        System.out.println("🚀 TEST 6: Performance Scenario");
        System.out.println("Testing system performance and efficiency...\n");
        
        ElevatorSelectionStrategy strategy = ElevatorStrategyFactory.getStrategy("group-collective");
        ElevatorController controller = new ElevatorController(4, 25, strategy);
        
        try {
            System.out.println("📍 Performance Test: Rapid sequential requests");
            
            long startTime = System.currentTimeMillis();
            
            // Rapid fire requests to test system responsiveness
            for (int i = 0; i < 10; i++) {
                int floor = (i % 20) + 1; // Distribute across floors 1-20
                if (i % 2 == 0) {
                    controller.pressUpButtonAtFloor(floor, 0);
                } else {
                    controller.pressDownButtonAtFloor(floor, 0);
                }
                Thread.sleep(50); // Very rapid requests
            }
            
            long endTime = System.currentTimeMillis();
            System.out.println("⏱️ Request processing time: " + (endTime - startTime) + "ms");
            
            System.out.println("📍 Performance Test: Complex routing scenario");
            
            // Create a complex routing scenario
            controller.pressInsideButton(20, 0); // Elevator 0 to top
            controller.pressInsideButton(15, 0);
            controller.pressInsideButton(10, 0);
            controller.pressInsideButton(5, 0);
            
            controller.pressInsideButton(1, 1);  // Elevator 1 to bottom
            controller.pressInsideButton(8, 1);
            controller.pressInsideButton(12, 1);
            
            // Add conflicting requests
            controller.pressUpButtonAtFloor(7, 0);   // Should be optimally assigned
            controller.pressDownButtonAtFloor(18, 0); // Should be optimally assigned
            
            Thread.sleep(5000);
            
        } catch (InterruptedException e) {
            System.err.println("Test interrupted: " + e.getMessage());
        }
        
        System.out.println("✅ Performance scenario test completed\n");
    }
}
