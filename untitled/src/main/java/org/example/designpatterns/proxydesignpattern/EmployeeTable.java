package org.example.designpatterns.proxydesignpattern;

import java.util.List;

/**
 * Subject interface that defines the contract for employee table operations
 */
public interface EmployeeTable {

    /**
     * Create a new employee
     * @param employee Employee object to create
     * @return true if creation successful, false otherwise
     */
    boolean createEmployee(Employee employee);

    /**
     * Get employee by ID
     * @param employeeId ID of the employee
     * @return Employee object if found, null otherwise
     */
    Employee getEmployee(int employeeId);

    /**
     * Update existing employee
     * @param employee Employee object with updated information
     * @return true if update successful, false otherwise
     */
    boolean updateEmployee(Employee employee);

    /**
     * Delete employee by ID
     * @param employeeId ID of the employee to delete
     * @return true if deletion successful, false otherwise
     */
    boolean deleteEmployee(int employeeId);

    /**
     * Get all employees in a department
     * @param department Department name
     * @return List of employees in the department
     */
    List<Employee> getEmployeesByDepartment(String department);

    /**
     * Get all employees
     * @return List of all employees
     */
    List<Employee> getAllEmployees();
}
