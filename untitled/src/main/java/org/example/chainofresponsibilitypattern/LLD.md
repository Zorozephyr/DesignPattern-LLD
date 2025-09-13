# Chain of Responsibility Pattern

## Overview
The Chain of Responsibility pattern allows you to pass requests along a chain of handlers. Upon receiving a request, each handler decides either to process the request or to pass it to the next handler in the chain.

## Core Concept
If a sender sends a request and doesn't care which specific receiver fulfills the request, we use this pattern. The request travels through a chain of potential handlers until one can handle it.

## Why Use Chain of Responsibility Over Switch Statements?

### Problems with Switch Statements:
1. **Violation of Open/Closed Principle**: Adding new log levels requires modifying the switch statement
2. **Single Point of Failure**: All logic is concentrated in one place
3. **Hard to Test**: Difficult to unit test individual handlers
4. **Coupling**: All handlers are tightly coupled in one method
5. **No Runtime Flexibility**: Cannot change the chain at runtime

### Benefits of Chain of Responsibility:
1. **Extensibility**: Easy to add new handlers without modifying existing code
2. **Loose Coupling**: Sender doesn't need to know which handler will process the request
3. **Flexibility**: Can change the order of handlers or add/remove handlers at runtime
4. **Single Responsibility**: Each handler has one specific responsibility
5. **Testability**: Each handler can be tested independently

## Why Perfect for Logging Systems?

### Logging Requirements:
- **Multiple Log Levels**: INFO, DEBUG, ERROR, WARN, FATAL
- **Different Outputs**: Console, file, database, remote server
- **Conditional Processing**: Some logs go to multiple destinations
- **Runtime Configuration**: Log levels can change during application runtime

### Chain of Responsibility Benefits for Logging:
```java
// Instead of this switch statement approach:
switch(logLevel) {
    case INFO: 
        if(infoEnabled) console.log(message);
        if(fileLoggingEnabled) file.log(message);
        break;
    case ERROR:
        console.log(message);
        file.log(message);
        email.sendAlert(message);
        break;
    // ... more cases
}

// We get this clean, extensible chain:
LogProcessor chain = new InfoLogProcessor(
    new DebugLogProcessor(
        new ErrorLogProcessor(null)
    )
);
chain.log(LogLevel.INFO, message); // Automatically finds right handler
```

## Real-World Examples

### 1. **Web Server Request Processing**
```
HTTP Request -> Authentication Handler -> Authorization Handler -> 
Rate Limiting Handler -> Business Logic Handler -> Response Handler
```
**When to identify**: Multiple sequential checks/processing steps for incoming requests

### 2. **Payment Processing Systems**
```
Payment Request -> Fraud Detection -> Balance Check -> 
Payment Gateway Selection -> Transaction Processing -> Receipt Generation
```
**When to identify**: Financial transactions requiring multiple validation steps

### 3. **Customer Support Ticket Systems**
```
Support Ticket -> Level 1 Support -> Level 2 Support -> 
Senior Engineer -> Manager -> External Vendor
```
**When to identify**: Escalation workflows where requests move up hierarchy

### 4. **Email Filtering Systems**
```
Incoming Email -> Spam Filter -> Virus Scanner -> 
Content Filter -> Priority Classifier -> Inbox Routing
```
**When to identify**: Sequential filtering/processing pipelines

### 5. **ATM Cash Dispensing**
```
Withdraw Request -> 2000₹ Handler -> 500₹ Handler -> 
200₹ Handler -> 100₹ Handler -> Insufficient Funds
```
**When to identify**: Resource allocation with different denominations/sizes

### 6. **Game Event Processing**
```
Game Event -> Input Handler -> Physics Handler -> 
Collision Handler -> Animation Handler -> Sound Handler
```
**When to identify**: Event-driven systems with multiple processors

### 7. **Middleware in Web Frameworks**
```
HTTP Request -> CORS Middleware -> Authentication Middleware -> 
Logging Middleware -> Compression Middleware -> Route Handler
```
**When to identify**: Pipeline processing in web frameworks

## How to Identify Chain of Responsibility Scenarios

### Key Indicators:
1. **Sequential Processing**: Request needs to pass through multiple processors
2. **Optional Handling**: Not every handler needs to process every request
3. **Unknown Handler**: You don't know in advance which handler will process the request
4. **Escalation Patterns**: Requests can be escalated to higher-level handlers
5. **Pipeline Processing**: Data flows through a series of transformations
6. **Conditional Processing**: Processing depends on runtime conditions

### Questions to Ask:
- "Can this request be handled by multiple different processors?"
- "Do I need to try multiple approaches until one succeeds?"
- "Should the processing order be configurable?"
- "Can new processors be added without changing existing code?"
- "Is there a natural hierarchy or sequence of handlers?"

### Anti-Patterns (When NOT to Use):
- **Single Handler**: Only one possible way to handle the request
- **All-or-Nothing**: Every handler must process every request
- **Fixed Logic**: Processing logic never changes
- **Performance Critical**: The chain traversal overhead is unacceptable
- **Simple Conditional**: A simple if-else would suffice

## Implementation Structure
```
Client -> Handler/Processor/Receiver
           ↓
       ConcreteHandler1 -> ConcreteHandler2 -> ConcreteHandler3 -> null
```

## Key Components:
1. **Handler Interface**: Defines the interface for handling requests
2. **Concrete Handlers**: Implement specific handling logic
3. **Client**: Initiates the request to the chain
4. **Chain Setup**: Links handlers together in a specific order

## Best Practices:
1. **Null Safety**: Always check if next handler exists before calling
2. **Fail Fast**: Validate inputs early in the chain
3. **Logging**: Log when requests are passed along the chain
4. **Performance**: Consider caching for frequently accessed chains
5. **Configuration**: Make chain composition configurable
6. **Error Handling**: Decide how to handle exceptions in the chain

## Example Usage in Our Code:
```java
// ATM Cash Dispensing Example:
Client -(Withdraw 2000Rs)-> ATM System[2000Rs Handler, 500Rs Handler, 100Rs Handler]
// It will go to each handler one by one and try to fulfill the request

// Logging Example:
LogProcessor logProcessor = new InfoLogProcessor(
    new DebugLogProcessor(
        new ErrorLogProcessor(null)
    )
);
logProcessor.log(LogProcessor.INFO, "This is an info message");
```

The pattern ensures that the client doesn't need to know which specific handler will process the request - it just sends it to the chain and lets the appropriate handler take care of it.