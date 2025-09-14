package org.example.designpatterns.proxydesignpattern;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Real Subject - Concrete implementation of EmployeeTable that simulates expensive database operations
 */
public class EmployeeTableImpl implements EmployeeTable {

    // Simulating database storage
    private Map<Integer, Employee> employeeDatabase;
    private boolean isInitialized = false;

    public EmployeeTableImpl() {
        System.out.println("EmployeeTableImpl: Expensive database connection established");
        this.employeeDatabase = new HashMap<>();
        initializeWithSampleData();
    }

    /**
     * Simulates expensive database initialization with sample data
     */
    private void initializeWithSampleData() {
        System.out.println("EmployeeTableImpl: Loading initial data from database...");
        simulateDelay(2000); // Simulate 2 second database load time

        // Add sample employees
        employeeDatabase.put(1, new Employee(1, "John Doe", "IT", 75000, "john.doe@company.com"));
        employeeDatabase.put(2, new Employee(2, "Jane Smith", "HR", 65000, "jane.smith@company.com"));
        employeeDatabase.put(3, new Employee(3, "Mike Johnson", "IT", 80000, "mike.johnson@company.com"));
        employeeDatabase.put(4, new Employee(4, "Sarah Wilson", "Finance", 70000, "sarah.wilson@company.com"));
        employeeDatabase.put(5, new Employee(5, "David Brown", "IT", 85000, "david.brown@company.com"));

        isInitialized = true;
        System.out.println("EmployeeTableImpl: Database initialized with " + employeeDatabase.size() + " employees");
    }

    @Override
    public boolean createEmployee(Employee employee) {
        System.out.println("EmployeeTableImpl: Creating employee in database - " + employee.getName());
        simulateDelay(500); // Simulate database write delay

        if (employeeDatabase.containsKey(employee.getId())) {
            System.out.println("EmployeeTableImpl: Employee with ID " + employee.getId() + " already exists");
            return false;
        }

        employeeDatabase.put(employee.getId(), employee);
        System.out.println("EmployeeTableImpl: Employee created successfully");
        return true;
    }

    @Override
    public Employee getEmployee(int employeeId) {
        System.out.println("EmployeeTableImpl: Fetching employee from database - ID: " + employeeId);
        simulateDelay(300); // Simulate database read delay

        Employee employee = employeeDatabase.get(employeeId);
        if (employee != null) {
            System.out.println("EmployeeTableImpl: Employee found - " + employee.getName());
        } else {
            System.out.println("EmployeeTableImpl: Employee not found with ID: " + employeeId);
        }

        return employee;
    }

    @Override
    public boolean updateEmployee(Employee employee) {
        System.out.println("EmployeeTableImpl: Updating employee in database - " + employee.getName());
        simulateDelay(400); // Simulate database update delay

        if (!employeeDatabase.containsKey(employee.getId())) {
            System.out.println("EmployeeTableImpl: Employee not found for update - ID: " + employee.getId());
            return false;
        }

        employeeDatabase.put(employee.getId(), employee);
        System.out.println("EmployeeTableImpl: Employee updated successfully");
        return true;
    }

    @Override
    public boolean deleteEmployee(int employeeId) {
        System.out.println("EmployeeTableImpl: Deleting employee from database - ID: " + employeeId);
        simulateDelay(350); // Simulate database delete delay

        Employee removed = employeeDatabase.remove(employeeId);
        if (removed != null) {
            System.out.println("EmployeeTableImpl: Employee deleted successfully - " + removed.getName());
            return true;
        } else {
            System.out.println("EmployeeTableImpl: Employee not found for deletion - ID: " + employeeId);
            return false;
        }
    }

    @Override
    public List<Employee> getEmployeesByDepartment(String department) {
        System.out.println("EmployeeTableImpl: Fetching employees by department from database - " + department);
        simulateDelay(600); // Simulate complex query delay

        List<Employee> result = employeeDatabase.values().stream()
                .filter(emp -> emp.getDepartment().equalsIgnoreCase(department))
                .collect(Collectors.toList());

        System.out.println("EmployeeTableImpl: Found " + result.size() + " employees in " + department + " department");
        return result;
    }

    @Override
    public List<Employee> getAllEmployees() {
        System.out.println("EmployeeTableImpl: Fetching all employees from database");
        simulateDelay(800); // Simulate full table scan delay

        List<Employee> result = new ArrayList<>(employeeDatabase.values());
        System.out.println("EmployeeTableImpl: Retrieved " + result.size() + " employees");
        return result;
    }

    /**
     * Simulates database operation delay
     */
    private void simulateDelay(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
