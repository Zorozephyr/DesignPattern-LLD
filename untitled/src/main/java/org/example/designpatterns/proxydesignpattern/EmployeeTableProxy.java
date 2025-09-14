package org.example.designpatterns.proxydesignpattern;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Proxy class that implements caching, access control, lazy loading, and monitoring
 * This demonstrates multiple proxy pattern types in one implementation
 */
public class EmployeeTableProxy implements EmployeeTable {

    // Lazy loading - real object created only when needed
    private EmployeeTableImpl realEmployeeTable;

    // Caching mechanism
    private Map<Integer, Employee> cache;
    private Map<String, List<Employee>> departmentCache;
    private List<Employee> allEmployeesCache;
    private long cacheTimestamp;
    private static final long CACHE_EXPIRY_TIME = 300000; // 5 minutes

    // Access control
    private UserRole currentUserRole;
    private String currentUserDepartment;

    // Performance monitoring
    private Map<String, Integer> operationCount;
    private Map<String, Long> totalExecutionTime;

    public EmployeeTableProxy(UserRole userRole, String userDepartment) {
        this.currentUserRole = userRole;
        this.currentUserDepartment = userDepartment;
        this.cache = new ConcurrentHashMap<>();
        this.departmentCache = new ConcurrentHashMap<>();
        this.operationCount = new ConcurrentHashMap<>();
        this.totalExecutionTime = new ConcurrentHashMap<>();
        this.cacheTimestamp = System.currentTimeMillis();

        System.out.println("EmployeeTableProxy: Initialized for user role - " + userRole);
    }

    /**
     * Lazy loading implementation - creates real object only when needed
     */
    private EmployeeTableImpl getRealEmployeeTable() {
        if (realEmployeeTable == null) {
            System.out.println("EmployeeTableProxy: Lazy loading - Creating real EmployeeTable instance");
            realEmployeeTable = new EmployeeTableImpl();
        }
        return realEmployeeTable;
    }

    /**
     * Access control validation
     */
    private boolean hasPermission(String operation) {
        switch (currentUserRole) {
            case ADMIN:
                return true; // Admin has all permissions
            case HR:
                return !operation.equals("DELETE") || operation.equals("CREATE") ||
                       operation.equals("READ") || operation.equals("UPDATE");
            case MANAGER:
                return operation.equals("READ") || operation.equals("UPDATE");
            case EMPLOYEE:
                return operation.equals("READ");
            default:
                return false;
        }
    }

    /**
     * Cache validation - checks if cache is still valid
     */
    private boolean isCacheValid() {
        return (System.currentTimeMillis() - cacheTimestamp) < CACHE_EXPIRY_TIME;
    }

    /**
     * Clear expired cache
     */
    private void clearCacheIfExpired() {
        if (!isCacheValid()) {
            System.out.println("EmployeeTableProxy: Cache expired, clearing cache");
            cache.clear();
            departmentCache.clear();
            allEmployeesCache = null;
            cacheTimestamp = System.currentTimeMillis();
        }
    }

    /**
     * Performance monitoring helper
     */
    private void recordOperation(String operation, long executionTime) {
        operationCount.merge(operation, 1, Integer::sum);
        totalExecutionTime.merge(operation, executionTime, Long::sum);
    }

    @Override
    public boolean createEmployee(Employee employee) {
        long startTime = System.currentTimeMillis();
        String operation = "CREATE";

        System.out.println("EmployeeTableProxy: Processing create request for - " + employee.getName());

        // Access control
        if (!hasPermission(operation)) {
            System.out.println("EmployeeTableProxy: Access denied - " + currentUserRole + " cannot perform " + operation);
            return false;
        }

        // Input validation
        if (employee == null || employee.getName() == null || employee.getName().trim().isEmpty()) {
            System.out.println("EmployeeTableProxy: Invalid employee data");
            return false;
        }

        // Clear cache as we're modifying data
        clearCacheIfExpired();
        cache.remove(employee.getId());
        departmentCache.clear();
        allEmployeesCache = null;

        // Delegate to real object
        boolean result = getRealEmployeeTable().createEmployee(employee);

        // Update cache if creation successful
        if (result) {
            cache.put(employee.getId(), employee);
            System.out.println("EmployeeTableProxy: Employee cached after creation");
        }

        long executionTime = System.currentTimeMillis() - startTime;
        recordOperation(operation, executionTime);
        System.out.println("EmployeeTableProxy: " + operation + " completed in " + executionTime + "ms");

        return result;
    }

    @Override
    public Employee getEmployee(int employeeId) {
        long startTime = System.currentTimeMillis();
        String operation = "READ";

        System.out.println("EmployeeTableProxy: Processing get request for employee ID - " + employeeId);

        // Access control
        if (!hasPermission(operation)) {
            System.out.println("EmployeeTableProxy: Access denied - " + currentUserRole + " cannot perform " + operation);
            return null;
        }

        clearCacheIfExpired();

        // Check cache first
        if (cache.containsKey(employeeId)) {
            System.out.println("EmployeeTableProxy: Cache HIT - Returning employee from cache");
            long executionTime = System.currentTimeMillis() - startTime;
            recordOperation(operation + "_CACHE", executionTime);
            return cache.get(employeeId);
        }

        System.out.println("EmployeeTableProxy: Cache MISS - Fetching from database");

        // Fetch from real object
        Employee employee = getRealEmployeeTable().getEmployee(employeeId);

        // Cache the result if found
        if (employee != null) {
            cache.put(employeeId, employee);
            System.out.println("EmployeeTableProxy: Employee cached for future requests");
        }

        long executionTime = System.currentTimeMillis() - startTime;
        recordOperation(operation, executionTime);
        System.out.println("EmployeeTableProxy: " + operation + " completed in " + executionTime + "ms");

        return employee;
    }

    @Override
    public boolean updateEmployee(Employee employee) {
        long startTime = System.currentTimeMillis();
        String operation = "UPDATE";

        System.out.println("EmployeeTableProxy: Processing update request for - " + employee.getName());

        // Access control
        if (!hasPermission(operation)) {
            System.out.println("EmployeeTableProxy: Access denied - " + currentUserRole + " cannot perform " + operation);
            return false;
        }

        // Additional access control for managers (can only update their department)
        if (currentUserRole == UserRole.MANAGER) {
            Employee existingEmployee = getEmployee(employee.getId());
            if (existingEmployee != null && !existingEmployee.getDepartment().equals(currentUserDepartment)) {
                System.out.println("EmployeeTableProxy: Manager can only update employees in their department");
                return false;
            }
        }

        // Input validation
        if (employee == null || employee.getId() <= 0) {
            System.out.println("EmployeeTableProxy: Invalid employee data for update");
            return false;
        }

        // Clear affected cache
        cache.remove(employee.getId());
        departmentCache.clear();
        allEmployeesCache = null;

        // Delegate to real object
        boolean result = getRealEmployeeTable().updateEmployee(employee);

        // Update cache if successful
        if (result) {
            cache.put(employee.getId(), employee);
            System.out.println("EmployeeTableProxy: Updated employee cached");
        }

        long executionTime = System.currentTimeMillis() - startTime;
        recordOperation(operation, executionTime);
        System.out.println("EmployeeTableProxy: " + operation + " completed in " + executionTime + "ms");

        return result;
    }

    @Override
    public boolean deleteEmployee(int employeeId) {
        long startTime = System.currentTimeMillis();
        String operation = "DELETE";

        System.out.println("EmployeeTableProxy: Processing delete request for employee ID - " + employeeId);

        // Access control
        if (!hasPermission(operation)) {
            System.out.println("EmployeeTableProxy: Access denied - " + currentUserRole + " cannot perform " + operation);
            return false;
        }

        // Clear cache
        cache.remove(employeeId);
        departmentCache.clear();
        allEmployeesCache = null;

        // Delegate to real object
        boolean result = getRealEmployeeTable().deleteEmployee(employeeId);

        long executionTime = System.currentTimeMillis() - startTime;
        recordOperation(operation, executionTime);
        System.out.println("EmployeeTableProxy: " + operation + " completed in " + executionTime + "ms");

        return result;
    }

    @Override
    public List<Employee> getEmployeesByDepartment(String department) {
        long startTime = System.currentTimeMillis();
        String operation = "READ_DEPARTMENT";

        System.out.println("EmployeeTableProxy: Processing department query for - " + department);

        // Access control
        if (!hasPermission("READ")) {
            System.out.println("EmployeeTableProxy: Access denied - " + currentUserRole + " cannot perform READ");
            return new ArrayList<>();
        }

        clearCacheIfExpired();

        // Check department cache
        if (departmentCache.containsKey(department)) {
            System.out.println("EmployeeTableProxy: Department cache HIT");
            long executionTime = System.currentTimeMillis() - startTime;
            recordOperation(operation + "_CACHE", executionTime);
            return new ArrayList<>(departmentCache.get(department));
        }

        System.out.println("EmployeeTableProxy: Department cache MISS");

        // Fetch from real object
        List<Employee> employees = getRealEmployeeTable().getEmployeesByDepartment(department);

        // Cache the result
        departmentCache.put(department, new ArrayList<>(employees));
        System.out.println("EmployeeTableProxy: Department query result cached");

        long executionTime = System.currentTimeMillis() - startTime;
        recordOperation(operation, executionTime);
        System.out.println("EmployeeTableProxy: " + operation + " completed in " + executionTime + "ms");

        return employees;
    }

    @Override
    public List<Employee> getAllEmployees() {
        long startTime = System.currentTimeMillis();
        String operation = "READ_ALL";

        System.out.println("EmployeeTableProxy: Processing get all employees request");

        // Access control
        if (!hasPermission("READ")) {
            System.out.println("EmployeeTableProxy: Access denied - " + currentUserRole + " cannot perform READ");
            return new ArrayList<>();
        }

        clearCacheIfExpired();

        // Check all employees cache
        if (allEmployeesCache != null) {
            System.out.println("EmployeeTableProxy: All employees cache HIT");
            long executionTime = System.currentTimeMillis() - startTime;
            recordOperation(operation + "_CACHE", executionTime);
            return new ArrayList<>(allEmployeesCache);
        }

        System.out.println("EmployeeTableProxy: All employees cache MISS");

        // Fetch from real object
        List<Employee> employees = getRealEmployeeTable().getAllEmployees();

        // Cache the result
        allEmployeesCache = new ArrayList<>(employees);
        System.out.println("EmployeeTableProxy: All employees cached");

        long executionTime = System.currentTimeMillis() - startTime;
        recordOperation(operation, executionTime);
        System.out.println("EmployeeTableProxy: " + operation + " completed in " + executionTime + "ms");

        return employees;
    }

    /**
     * Get performance statistics
     */
    public void printPerformanceStats() {
        System.out.println("\n=== EmployeeTableProxy Performance Statistics ===");
        System.out.println("Operation Counts:");
        operationCount.forEach((operation, count) ->
            System.out.println("  " + operation + ": " + count + " times"));

        System.out.println("\nAverage Execution Times:");
        operationCount.forEach((operation, count) -> {
            long totalTime = totalExecutionTime.getOrDefault(operation, 0L);
            long avgTime = totalTime / count;
            System.out.println("  " + operation + ": " + avgTime + "ms average");
        });

        System.out.println("\nCache Status:");
        System.out.println("  Individual employees cached: " + cache.size());
        System.out.println("  Department queries cached: " + departmentCache.size());
        System.out.println("  All employees cached: " + (allEmployeesCache != null ? "Yes" : "No"));
        System.out.println("  Cache valid: " + isCacheValid());
        System.out.println("================================================\n");
    }
}
