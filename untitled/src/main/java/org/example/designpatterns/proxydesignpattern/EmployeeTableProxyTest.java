package org.example.designpatterns.proxydesignpattern;

import java.util.List;

/**
 * Test class to validate the Proxy Design Pattern implementation
 */
public class EmployeeTableProxyTest {

    public static void main(String[] args) {
        System.out.println("=== PROXY DESIGN PATTERN - EMPLOYEE TABLE TESTS ===\n");

        runAllTests();
    }

    public static void runAllTests() {
        testLazyLoading();
        testAccessControl();
        testCaching();
        testInputValidation();
        testCRUDOperations();

        System.out.println("\n=== ALL TESTS COMPLETED ===");
    }

    /**
     * Test lazy loading functionality
     */
    private static void testLazyLoading() {
        System.out.println("TEST 1: Lazy Loading");
        System.out.println("-".repeat(30));

        System.out.println("Creating proxy (real object should NOT be created yet)...");
        EmployeeTableProxy proxy = new EmployeeTableProxy(UserRole.ADMIN, "IT");

        System.out.println("Making first call (should trigger lazy loading)...");
        Employee emp = proxy.getEmployee(1);

        System.out.println("Test Result: " + (emp != null ? "PASS" : "FAIL"));
        System.out.println("Expected: Real object created only on first database access\n");
    }

    /**
     * Test access control for different user roles
     */
    private static void testAccessControl() {
        System.out.println("TEST 2: Access Control");
        System.out.println("-".repeat(30));

        // Test EMPLOYEE role (read-only)
        EmployeeTableProxy employeeProxy = new EmployeeTableProxy(UserRole.EMPLOYEE, "IT");
        Employee testEmp = new Employee(999, "Test Employee", "IT", 50000, "test@test.com");

        boolean canRead = employeeProxy.getEmployee(1) != null;
        boolean canCreate = employeeProxy.createEmployee(testEmp);
        boolean canDelete = employeeProxy.deleteEmployee(999);

        System.out.println("EMPLOYEE role - Read: " + (canRead ? "ALLOWED" : "DENIED"));
        System.out.println("EMPLOYEE role - Create: " + (canCreate ? "ALLOWED" : "DENIED"));
        System.out.println("EMPLOYEE role - Delete: " + (canDelete ? "ALLOWED" : "DENIED"));

        // Test ADMIN role (full access)
        EmployeeTableProxy adminProxy = new EmployeeTableProxy(UserRole.ADMIN, "IT");
        boolean adminCanCreate = adminProxy.createEmployee(testEmp);
        boolean adminCanDelete = adminProxy.deleteEmployee(999);

        System.out.println("ADMIN role - Create: " + (adminCanCreate ? "ALLOWED" : "DENIED"));
        System.out.println("ADMIN role - Delete: " + (adminCanDelete ? "ALLOWED" : "DENIED"));

        boolean accessControlWorking = !canCreate && !canDelete && adminCanCreate;
        System.out.println("Access Control Test: " + (accessControlWorking ? "PASS" : "FAIL") + "\n");
    }

    /**
     * Test caching mechanism
     */
    private static void testCaching() {
        System.out.println("TEST 3: Caching Mechanism");
        System.out.println("-".repeat(30));

        EmployeeTableProxy proxy = new EmployeeTableProxy(UserRole.ADMIN, "IT");

        // First call - should be slow (cache miss)
        long start1 = System.currentTimeMillis();
        Employee emp1 = proxy.getEmployee(1);
        long time1 = System.currentTimeMillis() - start1;

        // Second call - should be fast (cache hit)
        long start2 = System.currentTimeMillis();
        Employee emp2 = proxy.getEmployee(1);
        long time2 = System.currentTimeMillis() - start2;

        boolean cachingWorking = time2 < time1 && emp1.equals(emp2);
        System.out.println("First call time: " + time1 + "ms");
        System.out.println("Second call time: " + time2 + "ms");
        System.out.println("Caching Test: " + (cachingWorking ? "PASS" : "FAIL"));
        System.out.println("Performance improvement: " + ((time1 - time2) * 100 / time1) + "%\n");
    }

    /**
     * Test input validation
     */
    private static void testInputValidation() {
        System.out.println("TEST 4: Input Validation");
        System.out.println("-".repeat(30));

        EmployeeTableProxy proxy = new EmployeeTableProxy(UserRole.ADMIN, "IT");

        // Test null employee
        boolean nullTest = !proxy.createEmployee(null);

        // Test employee with null name
        Employee invalidEmp = new Employee(1000, null, "IT", 50000, "test@test.com");
        boolean nullNameTest = !proxy.createEmployee(invalidEmp);

        // Test employee with empty name
        Employee emptyNameEmp = new Employee(1001, "", "IT", 50000, "test@test.com");
        boolean emptyNameTest = !proxy.createEmployee(emptyNameEmp);

        boolean validationWorking = nullTest && nullNameTest && emptyNameTest;
        System.out.println("Null employee validation: " + (nullTest ? "PASS" : "FAIL"));
        System.out.println("Null name validation: " + (nullNameTest ? "PASS" : "FAIL"));
        System.out.println("Empty name validation: " + (emptyNameTest ? "PASS" : "FAIL"));
        System.out.println("Input Validation Test: " + (validationWorking ? "PASS" : "FAIL") + "\n");
    }

    /**
     * Test CRUD operations
     */
    private static void testCRUDOperations() {
        System.out.println("TEST 5: CRUD Operations");
        System.out.println("-".repeat(30));

        EmployeeTableProxy proxy = new EmployeeTableProxy(UserRole.ADMIN, "IT");

        // Create
        Employee newEmp = new Employee(2000, "John Test", "QA", 65000, "john.test@company.com");
        boolean created = proxy.createEmployee(newEmp);

        // Read
        Employee retrieved = proxy.getEmployee(2000);
        boolean read = retrieved != null && retrieved.getName().equals("John Test");

        // Update
        if (retrieved != null) {
            retrieved.setSalary(70000);
        }
        boolean updated = proxy.updateEmployee(retrieved);
        Employee updatedEmp = proxy.getEmployee(2000);
        boolean updateVerified = updatedEmp != null && updatedEmp.getSalary() == 70000;

        // Delete
        boolean deleted = proxy.deleteEmployee(2000);
        Employee deletedEmp = proxy.getEmployee(2000);
        boolean deleteVerified = deletedEmp == null;

        // Test department query
        List<Employee> itEmployees = proxy.getEmployeesByDepartment("IT");
        boolean departmentQuery = itEmployees.size() > 0;

        // Test get all employees
        List<Employee> allEmployees = proxy.getAllEmployees();
        boolean getAllQuery = allEmployees.size() > 0;

        boolean crudWorking = created && read && updated && updateVerified &&
                             deleted && deleteVerified && departmentQuery && getAllQuery;

        System.out.println("Create: " + (created ? "PASS" : "FAIL"));
        System.out.println("Read: " + (read ? "PASS" : "FAIL"));
        System.out.println("Update: " + (updated && updateVerified ? "PASS" : "FAIL"));
        System.out.println("Delete: " + (deleted && deleteVerified ? "PASS" : "FAIL"));
        System.out.println("Department Query: " + (departmentQuery ? "PASS" : "FAIL"));
        System.out.println("Get All Query: " + (getAllQuery ? "PASS" : "FAIL"));
        System.out.println("CRUD Operations Test: " + (crudWorking ? "PASS" : "FAIL") + "\n");

        // Show performance statistics
        proxy.printPerformanceStats();
    }
}
