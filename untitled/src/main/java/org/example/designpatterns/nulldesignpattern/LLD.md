# Null Object Design Pattern

## Overview

Instead of null checks everywhere, we use null object.
- A null object replaces NULL return type
- No need to put if check for checking NULL
- Null objects reflect "do Nothing" or Default behaviour

## Pattern Structure

```
Client -----> AbstractObject[method()]-----Null Object[method()]
                        |   
                        |
                        |
                    Real Object[method()]
```

Returns default value

## 1. Null Object Pattern

**What it is:** Instead of returning null, you return a special object that represents "nothing" but still behaves safely.

**Where it's used:**
- When you want to avoid NullPointerException
- When the "absence" of an object should still allow safe method calls

**Example:**

```java
interface Logger {
    void log(String msg);
}

class ConsoleLogger implements Logger {
    public void log(String msg) { 
        System.out.println(msg); 
    }
}

class NullLogger implements Logger {
    public void log(String msg) { 
        /* do nothing */ 
    }
}

// Usage
Logger logger = config.isLoggingEnabled() ? new ConsoleLogger() : new NullLogger();
logger.log("Hello"); // Safe, no null checks
```

**Why:** Makes code cleaner by avoiding `if (logger != null)` checks everywhere.

## 2. Optional

**What it is:** A container that may or may not hold a value (`Optional<T>`).

**Where it's used:**
- For return types, especially when something might not be present
- Encourages explicit handling of missing values instead of accidental null

**Example:**

```java
Optional<User> findUserById(int id) {
    return users.containsKey(id) ? Optional.of(users.get(id)) : Optional.empty();
}

// Usage
findUserById(10).ifPresent(user -> System.out.println(user.getName()));
```

**Why:** Forces the caller to handle the "no value" case explicitly, making APIs safer.

## 3. When to Use Which

### Use Null Object when:
- You want to keep object-oriented polymorphism intact
- You want to call methods safely even if "nothing" is present
- The "absence" is a behavioral substitute (e.g., no-op logger)

### Use Optional when:
- You want to represent presence/absence as part of the data/return value
- The caller must decide what to do if no value exists
- Example: DAO methods (`Optional<User> findById()`)

## Summary

👉 **In short:**
- **Null Object** = "default safe behavior when nothing is there"
- **Optional** = "explicit signal that nothing may be there, caller must decide"