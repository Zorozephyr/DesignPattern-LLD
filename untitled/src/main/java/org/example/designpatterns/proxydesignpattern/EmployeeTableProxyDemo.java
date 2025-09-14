package org.example.designpatterns.proxydesignpattern;

import java.util.List;

/**
 * Demo class that demonstrates all features of the Proxy Design Pattern
 * Shows lazy loading, caching, access control, and performance monitoring
 */
public class EmployeeTableProxyDemo {

    public static void main(String[] args) {
        System.out.println("=== Proxy Design Pattern Demo - Employee Table Use Case ===\n");

        // Demonstrate different user roles and their access levels
        demonstrateAccessControl();

        System.out.println("\n" + "=".repeat(60) + "\n");

        // Demonstrate caching and performance benefits
        demonstrateCachingAndPerformance();

        System.out.println("\n" + "=".repeat(60) + "\n");

        // Demonstrate lazy loading
        demonstrateLazyLoading();
    }

    /**
     * Demonstrates access control for different user roles
     */
    private static void demonstrateAccessControl() {
        System.out.println("1. DEMONSTRATING ACCESS CONTROL");
        System.out.println("-".repeat(40));

        // Test different user roles
        UserRole[] roles = {UserRole.ADMIN, UserRole.HR, UserRole.MANAGER, UserRole.EMPLOYEE};

        for (UserRole role : roles) {
            System.out.println("\n--- Testing " + role + " Role ---");
            EmployeeTableProxy proxy = new EmployeeTableProxy(role, "IT");

            // Test read operation (should work for all roles)
            System.out.println("Attempting to read employee...");
            Employee emp = proxy.getEmployee(1);
            System.out.println("Read result: " + (emp != null ? "SUCCESS" : "FAILED"));

            // Test create operation
            System.out.println("Attempting to create employee...");
            Employee newEmp = new Employee(99, "Test User", "IT", 60000, "test@company.com");
            boolean createResult = proxy.createEmployee(newEmp);
            System.out.println("Create result: " + (createResult ? "SUCCESS" : "FAILED"));

            // Test delete operation (should only work for ADMIN)
            System.out.println("Attempting to delete employee...");
            boolean deleteResult = proxy.deleteEmployee(99);
            System.out.println("Delete result: " + (deleteResult ? "SUCCESS" : "FAILED"));
        }
    }

    /**
     * Demonstrates caching mechanism and performance improvements
     */
    private static void demonstrateCachingAndPerformance() {
        System.out.println("2. DEMONSTRATING CACHING AND PERFORMANCE");
        System.out.println("-".repeat(40));

        EmployeeTableProxy proxy = new EmployeeTableProxy(UserRole.ADMIN, "IT");

        System.out.println("\n--- First Request (Cache Miss) ---");
        long startTime = System.currentTimeMillis();
        Employee emp1 = proxy.getEmployee(1);
        long firstRequestTime = System.currentTimeMillis() - startTime;
        System.out.println("First request took: " + firstRequestTime + "ms");

        System.out.println("\n--- Second Request (Cache Hit) ---");
        startTime = System.currentTimeMillis();
        Employee emp2 = proxy.getEmployee(1);
        long secondRequestTime = System.currentTimeMillis() - startTime;
        System.out.println("Second request took: " + secondRequestTime + "ms");

        System.out.println("\nPerformance improvement: " +
            ((firstRequestTime - secondRequestTime) * 100 / firstRequestTime) + "% faster");

        System.out.println("\n--- Testing Department Caching ---");
        startTime = System.currentTimeMillis();
        List<Employee> itEmployees1 = proxy.getEmployeesByDepartment("IT");
        long firstDeptTime = System.currentTimeMillis() - startTime;

        startTime = System.currentTimeMillis();
        List<Employee> itEmployees2 = proxy.getEmployeesByDepartment("IT");
        long secondDeptTime = System.currentTimeMillis() - startTime;

        System.out.println("First department query: " + firstDeptTime + "ms");
        System.out.println("Second department query: " + secondDeptTime + "ms");

        // Show performance statistics
        proxy.printPerformanceStats();
    }

    /**
     * Demonstrates lazy loading of the real object
     */
    private static void demonstrateLazyLoading() {
        System.out.println("3. DEMONSTRATING LAZY LOADING");
        System.out.println("-".repeat(40));

        System.out.println("Creating proxy object (real object not created yet)...");
        EmployeeTableProxy proxy = new EmployeeTableProxy(UserRole.HR, "HR");

        System.out.println("Proxy created successfully without expensive database connection.");
        System.out.println("Real object will be created only when first database operation is needed.\n");

        System.out.println("Now making first database call - this will trigger lazy loading:");
        Employee emp = proxy.getEmployee(2);
        System.out.println("Employee retrieved: " + (emp != null ? emp.getName() : "Not found"));

        System.out.println("\nSubsequent calls will use the same real object instance.");
        Employee emp2 = proxy.getEmployee(3);
        System.out.println("Second employee retrieved: " + (emp2 != null ? emp2.getName() : "Not found"));
    }

    /**
     * Helper method to demonstrate various proxy features
     */
    public static void demonstrateProxyFeatures() {
        System.out.println("\n=== COMPREHENSIVE PROXY FEATURES DEMO ===");

        EmployeeTableProxy adminProxy = new EmployeeTableProxy(UserRole.ADMIN, "IT");

        // Test all CRUD operations
        System.out.println("\n1. Testing CRUD Operations:");

        // Create
        Employee newEmployee = new Employee(100, "Alice Johnson", "Marketing", 72000, "alice@company.com");
        boolean created = adminProxy.createEmployee(newEmployee);
        System.out.println("Employee created: " + created);

        // Read
        Employee retrieved = adminProxy.getEmployee(100);
        System.out.println("Employee retrieved: " + (retrieved != null ? retrieved.getName() : "Not found"));

        // Update
        if (retrieved != null) {
            retrieved.setSalary(75000);
            boolean updated = adminProxy.updateEmployee(retrieved);
            System.out.println("Employee updated: " + updated);
        }

        // Delete
        boolean deleted = adminProxy.deleteEmployee(100);
        System.out.println("Employee deleted: " + deleted);

        // Test complex queries
        System.out.println("\n2. Testing Complex Queries:");
        List<Employee> itEmployees = adminProxy.getEmployeesByDepartment("IT");
        System.out.println("IT Department employees: " + itEmployees.size());

        List<Employee> allEmployees = adminProxy.getAllEmployees();
        System.out.println("Total employees: " + allEmployees.size());

        // Show final performance statistics
        adminProxy.printPerformanceStats();
    }
}
