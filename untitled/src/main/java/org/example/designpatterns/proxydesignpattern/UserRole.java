package org.example.designpatterns.proxydesignpattern;

/**
 * Enum representing different user roles for access control
 */
public enum UserRole {
    ADMIN,      // Full access to all operations
    HR,         // Can create, read, update employees
    MANAGER,    // Can read and update employees in their department
    EMPLOYEE    // Can only read employee information
}
