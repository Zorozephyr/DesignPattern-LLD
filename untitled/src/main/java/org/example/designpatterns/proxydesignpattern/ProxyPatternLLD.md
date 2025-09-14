# Proxy Design Pattern - Low Level Design

## Overview
The Proxy Design Pattern provides a placeholder or surrogate for another object to control access to it. It acts as an intermediary between the client and the real object.

## Structure Diagram
```
Client -> ProxyObject -> RealObject

    ┌────────┐      ┌─────────────┐      ┌─────────────┐
    │ Client │─────→│    Proxy    │─────→│ RealSubject │
    └────────┘      └─────────────┘      └─────────────┘
                           │                     │
                           ├─ controls access ───┤
                           ├─ caching          ──┤
                           ├─ lazy loading     ──┤
                           └─ security        ──┘
```

## UML Class Diagram
```
    ┌─────────────────────┐
    │   <<interface>>     │
    │     Subject         │
    ├─────────────────────┤
    │ + request(): void   │
    └─────────────────────┘
             △
             │
    ┌────────┴────────┐
    │                 │
┌───▼─────────────┐   ┌▼──────────────┐
│     Proxy       │   │  RealSubject  │
├─────────────────┤   ├───────────────┤
│ - realSubject   │◆──│               │
├─────────────────┤   ├───────────────┤
│ + request()     │   │ + request()   │
└─────────────────┘   └───────────────┘
```

## Types of Proxy Patterns

### 1. **Virtual Proxy (Lazy Loading)**
- Delays creation of expensive objects until needed
- Example: Loading large images only when displayed

### 2. **Protection Proxy (Access Control)**
- Controls access based on permissions
- Example: User authentication and authorization

### 3. **Remote Proxy**
- Represents objects in different address spaces
- Example: RMI, Web Services

### 4. **Cache Proxy**
- Provides caching mechanism
- Example: Database query caching

### 5. **Smart Reference Proxy**
- Provides additional functionality like reference counting
- Example: Garbage collection assistance

## When to Use Proxy Pattern

### ✅ Use Cases:
1. **Expensive Object Creation**: When object creation is costly
2. **Access Control**: When you need to control access to an object
3. **Caching**: When you want to cache results of expensive operations
4. **Lazy Loading**: When you want to defer object creation
5. **Logging/Monitoring**: When you need to add logging without changing original code
6. **Network Communication**: When dealing with remote objects
7. **Security**: When you need to add security layers

### ❌ Avoid When:
1. **Simple Objects**: When the overhead of proxy exceeds benefits
2. **Performance Critical**: When every millisecond matters
3. **Direct Access Needed**: When client needs direct object manipulation

## Real-World Examples

### Database Connection Proxy
```java
// Virtual Proxy for expensive database connections
DatabaseProxy -> DatabaseConnection
```

### Image Loading Proxy
```java
// Lazy loading of large images
ImageProxy -> HighResolutionImage
```

### Security Proxy
```java
// Access control for sensitive operations
SecurityProxy -> BankAccount
```

## Employee Table Use Case Implementation

### Problem Statement:
Create an employee management system where:
- Database operations are expensive
- Need caching for frequently accessed employees
- Access control for different operations
- Lazy loading of employee data

### Class Structure:
```
EmployeeTable (Interface)
├── EmployeeTableImpl (Real Implementation)
└── EmployeeTableProxy (Proxy Implementation)
    ├── Caching Layer
    ├── Access Control
    └── Lazy Loading
```

## Implementation Details

### Core Components:
1. **EmployeeTable Interface**: Defines contract for employee operations
2. **EmployeeTableImpl**: Real implementation with database operations
3. **EmployeeTableProxy**: Proxy with caching, access control, and lazy loading
4. **Employee Model**: Data structure for employee information

### Key Features:
- **Caching**: Stores frequently accessed employees in memory
- **Access Control**: Role-based permissions for CRUD operations
- **Lazy Loading**: Creates real object only when needed
- **Performance Monitoring**: Tracks operation times
- **Validation**: Input validation before delegating to real object

## Benefits in Employee Table Context:

1. **Performance**: Caching reduces database hits
2. **Security**: Role-based access control
3. **Resource Management**: Lazy loading saves memory
4. **Transparency**: Client doesn't know about proxy existence
5. **Extensibility**: Easy to add new features without changing client code

## Sequence Diagram for Employee Retrieval:
```
Client -> Proxy -> Cache -> RealObject -> Database

Client          Proxy           Cache       RealObject      Database
  │               │               │             │             │
  │─getEmployee()─│               │             │             │
  │               │─checkCache()──│             │             │
  │               │◄──not found───│             │             │
  │               │─createReal()──│─────────────│             │
  │               │               │             │─query()─────│
  │               │               │             │◄──result────│
  │               │               │◄─employee───│             │
  │               │─storeCache()──│             │             │
  │◄──employee────│               │             │             │
```

## Testing Strategy:
1. **Unit Tests**: Test each component individually
2. **Integration Tests**: Test proxy with real implementation
3. **Performance Tests**: Measure caching effectiveness
4. **Security Tests**: Verify access control mechanisms
