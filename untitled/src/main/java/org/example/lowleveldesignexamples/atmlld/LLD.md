# ATM Low-Level Design (LLD)

## Overview
This document presents a low-level design for an Automated Teller Machine (ATM) system using **State Design Pattern** for managing ATM states and **Chain of Responsibility Pattern** for cash withdrawal operations.

## Design Patterns Used

### 1. State Design Pattern
- Manages different states of the ATM
- Ensures clean state transitions
- Encapsulates state-specific behavior

### 2. Chain of Responsibility Pattern
- Handles cash withdrawal with different denominations
- Allows flexible addition of new denominations
- Processes requests through a chain of handlers

## System Architecture

### State Flow Diagram
```
┌─────────────┐
│ IDLE STATE  │
└─────┬───────┘
      │ Insert Card
      ▼
┌─────────────┐
│ HAS CARD    │
│ STATE       │
└─────┬───────┘
      │ Enter PIN
      ▼
┌─────────────┐
│ SELECT      │
│ OPERATION   │
│ STATE       │
└─────┬───────┘
      │ Choose Operation
      ▼
┌─────────────┐
│ PERFORM     │
│ OPERATION   │
│ STATE       │
└─────┬───────┘
      │ Complete/Cancel
      ▼
┌─────────────┐
│ IDLE STATE  │
└─────────────┘
```

### Class Diagram Overview
```
ATM
├── ATMState (Interface)
│   ├── IdleState
│   ├── HasCardState
│   ├── SelectOperationState
│   └── PerformOperationState
├── Operations
│   ├── CashWithdrawal
│   ├── CashDeposit
│   ├── BalanceInquiry
│   └── ChangePIN
└── CashDispenser (Chain of Responsibility)
    ├── TwoThousandNoteHandler
    ├── FiveHundredNoteHandler
    └── OneHundredNoteHandler
```

## Detailed Design

### 1. Core ATM Class

```java
public class ATM {
    private ATMState currentState;
    private Card currentCard;
    private BankAccount currentAccount;
    private CashDispenser cashDispenser;
    
    public ATM() {
        currentState = new IdleState();
        cashDispenser = new CashDispenser();
    }
    
    // State transition methods
    public void insertCard(Card card) {
        currentState.insertCard(this, card);
    }
    
    public void authenticatePIN(String pin) {
        currentState.authenticatePIN(this, pin);
    }
    
    public void selectOperation(OperationType operation) {
        currentState.selectOperation(this, operation);
    }
    
    public void performOperation(OperationRequest request) {
        currentState.performOperation(this, request);
    }
    
    public void cancelOperation() {
        currentState.cancel(this);
    }
    
    // State management
    public void setState(ATMState state) {
        this.currentState = state;
    }
    
    public ATMState getState() {
        return currentState;
    }
    
    // Getters and setters
    public void setCurrentCard(Card card) { this.currentCard = card; }
    public Card getCurrentCard() { return currentCard; }
    public void setCurrentAccount(BankAccount account) { this.currentAccount = account; }
    public BankAccount getCurrentAccount() { return currentAccount; }
    public CashDispenser getCashDispenser() { return cashDispenser; }
}
```

### 2. State Pattern Implementation

#### ATM State Interface
```java
public interface ATMState {
    void insertCard(ATM atm, Card card);
    void authenticatePIN(ATM atm, String pin);
    void selectOperation(ATM atm, OperationType operation);
    void performOperation(ATM atm, OperationRequest request);
    void cancel(ATM atm);
    void displayMessage();
}
```

#### Concrete States

##### Idle State
```java
public class IdleState implements ATMState {
    @Override
    public void insertCard(ATM atm, Card card) {
        System.out.println("Card inserted. Please enter your PIN.");
        atm.setCurrentCard(card);
        atm.setState(new HasCardState());
    }
    
    @Override
    public void authenticatePIN(ATM atm, String pin) {
        System.out.println("Please insert your card first.");
    }
    
    @Override
    public void selectOperation(ATM atm, OperationType operation) {
        System.out.println("Please insert your card first.");
    }
    
    @Override
    public void performOperation(ATM atm, OperationRequest request) {
        System.out.println("Please insert your card first.");
    }
    
    @Override
    public void cancel(ATM atm) {
        System.out.println("No operation to cancel.");
    }
    
    @Override
    public void displayMessage() {
        System.out.println("Welcome! Please insert your card.");
    }
}
```

##### Has Card State
```java
public class HasCardState implements ATMState {
    private int pinAttempts = 0;
    private static final int MAX_PIN_ATTEMPTS = 3;
    
    @Override
    public void insertCard(ATM atm, Card card) {
        System.out.println("Card already inserted. Please enter your PIN.");
    }
    
    @Override
    public void authenticatePIN(ATM atm, String pin) {
        Card card = atm.getCurrentCard();
        if (card.validatePIN(pin)) {
            System.out.println("PIN authenticated successfully!");
            BankAccount account = BankService.getAccount(card.getAccountNumber());
            atm.setCurrentAccount(account);
            atm.setState(new SelectOperationState());
        } else {
            pinAttempts++;
            if (pinAttempts >= MAX_PIN_ATTEMPTS) {
                System.out.println("Too many incorrect PIN attempts. Card blocked.");
                ejectCard(atm);
            } else {
                System.out.println("Incorrect PIN. Please try again. Attempts remaining: " 
                    + (MAX_PIN_ATTEMPTS - pinAttempts));
            }
        }
    }
    
    @Override
    public void selectOperation(ATM atm, OperationType operation) {
        System.out.println("Please authenticate your PIN first.");
    }
    
    @Override
    public void performOperation(ATM atm, OperationRequest request) {
        System.out.println("Please authenticate your PIN first.");
    }
    
    @Override
    public void cancel(ATM atm) {
        ejectCard(atm);
    }
    
    @Override
    public void displayMessage() {
        System.out.println("Please enter your PIN.");
    }
    
    private void ejectCard(ATM atm) {
        System.out.println("Card ejected. Thank you!");
        atm.setCurrentCard(null);
        atm.setState(new IdleState());
    }
}
```

##### Select Operation State
```java
public class SelectOperationState implements ATMState {
    @Override
    public void insertCard(ATM atm, Card card) {
        System.out.println("Please complete current session first.");
    }
    
    @Override
    public void authenticatePIN(ATM atm, String pin) {
        System.out.println("Already authenticated. Please select an operation.");
    }
    
    @Override
    public void selectOperation(ATM atm, OperationType operation) {
        System.out.println("Operation selected: " + operation);
        atm.setState(new PerformOperationState(operation));
    }
    
    @Override
    public void performOperation(ATM atm, OperationRequest request) {
        System.out.println("Please select an operation first.");
    }
    
    @Override
    public void cancel(ATM atm) {
        ejectCard(atm);
    }
    
    @Override
    public void displayMessage() {
        System.out.println("Please select an operation:");
        System.out.println("1. Cash Withdrawal");
        System.out.println("2. Cash Deposit");
        System.out.println("3. Balance Inquiry");
        System.out.println("4. Change PIN");
        System.out.println("5. Cancel");
    }
    
    private void ejectCard(ATM atm) {
        System.out.println("Transaction cancelled. Card ejected. Thank you!");
        atm.setCurrentCard(null);
        atm.setCurrentAccount(null);
        atm.setState(new IdleState());
    }
}
```

##### Perform Operation State
```java
public class PerformOperationState implements ATMState {
    private OperationType selectedOperation;
    
    public PerformOperationState(OperationType operation) {
        this.selectedOperation = operation;
    }
    
    @Override
    public void insertCard(ATM atm, Card card) {
        System.out.println("Please complete current transaction first.");
    }
    
    @Override
    public void authenticatePIN(ATM atm, String pin) {
        System.out.println("Please complete current transaction first.");
    }
    
    @Override
    public void selectOperation(ATM atm, OperationType operation) {
        System.out.println("Please complete current transaction first.");
    }
    
    @Override
    public void performOperation(ATM atm, OperationRequest request) {
        OperationFactory factory = new OperationFactory();
        Operation operation = factory.createOperation(selectedOperation);
        
        boolean success = operation.execute(atm, request);
        
        if (success) {
            System.out.println("Transaction completed successfully!");
        } else {
            System.out.println("Transaction failed. Please try again.");
        }
        
        // Return to select operation state for another transaction
        atm.setState(new SelectOperationState());
    }
    
    @Override
    public void cancel(ATM atm) {
        ejectCard(atm);
    }
    
    @Override
    public void displayMessage() {
        System.out.println("Please enter transaction details for: " + selectedOperation);
    }
    
    private void ejectCard(ATM atm) {
        System.out.println("Transaction cancelled. Card ejected. Thank you!");
        atm.setCurrentCard(null);
        atm.setCurrentAccount(null);
        atm.setState(new IdleState());
    }
}
```

### 3. Chain of Responsibility for Cash Withdrawal

#### Cash Dispenser Handler Interface
```java
public abstract class CashDispenserHandler {
    protected CashDispenserHandler nextHandler;
    
    public void setNextHandler(CashDispenserHandler nextHandler) {
        this.nextHandler = nextHandler;
    }
    
    public abstract boolean dispense(Currency currency, int amount);
    
    protected abstract int getDenomination();
    protected abstract int getAvailableNotes();
    protected abstract void updateNoteCount(int notesDispensed);
}
```

#### Concrete Handlers

##### Two Thousand Note Handler
```java
public class TwoThousandNoteHandler extends CashDispenserHandler {
    private int availableNotes;
    
    public TwoThousandNoteHandler(int availableNotes) {
        this.availableNotes = availableNotes;
    }
    
    @Override
    public boolean dispense(Currency currency, int amount) {
        int requiredNotes = amount / 2000;
        
        if (requiredNotes > 0 && requiredNotes <= availableNotes) {
            int notesToDispense = Math.min(requiredNotes, availableNotes);
            int dispensedAmount = notesToDispense * 2000;
            
            updateNoteCount(notesToDispense);
            System.out.println("Dispensed " + notesToDispense + " notes of ₹2000");
            
            int remainingAmount = amount - dispensedAmount;
            
            if (remainingAmount > 0 && nextHandler != null) {
                return nextHandler.dispense(currency, remainingAmount);
            }
            
            return remainingAmount == 0;
        } else if (nextHandler != null) {
            return nextHandler.dispense(currency, amount);
        }
        
        return false;
    }
    
    @Override
    protected int getDenomination() {
        return 2000;
    }
    
    @Override
    protected int getAvailableNotes() {
        return availableNotes;
    }
    
    @Override
    protected void updateNoteCount(int notesDispensed) {
        this.availableNotes -= notesDispensed;
    }
}
```

##### Five Hundred Note Handler
```java
public class FiveHundredNoteHandler extends CashDispenserHandler {
    private int availableNotes;
    
    public FiveHundredNoteHandler(int availableNotes) {
        this.availableNotes = availableNotes;
    }
    
    @Override
    public boolean dispense(Currency currency, int amount) {
        int requiredNotes = amount / 500;
        
        if (requiredNotes > 0 && requiredNotes <= availableNotes) {
            int notesToDispense = Math.min(requiredNotes, availableNotes);
            int dispensedAmount = notesToDispense * 500;
            
            updateNoteCount(notesToDispense);
            System.out.println("Dispensed " + notesToDispense + " notes of ₹500");
            
            int remainingAmount = amount - dispensedAmount;
            
            if (remainingAmount > 0 && nextHandler != null) {
                return nextHandler.dispense(currency, remainingAmount);
            }
            
            return remainingAmount == 0;
        } else if (nextHandler != null) {
            return nextHandler.dispense(currency, amount);
        }
        
        return false;
    }
    
    @Override
    protected int getDenomination() {
        return 500;
    }
    
    @Override
    protected int getAvailableNotes() {
        return availableNotes;
    }
    
    @Override
    protected void updateNoteCount(int notesDispensed) {
        this.availableNotes -= notesDispensed;
    }
}
```

##### One Hundred Note Handler
```java
public class OneHundredNoteHandler extends CashDispenserHandler {
    private int availableNotes;
    
    public OneHundredNoteHandler(int availableNotes) {
        this.availableNotes = availableNotes;
    }
    
    @Override
    public boolean dispense(Currency currency, int amount) {
        int requiredNotes = amount / 100;
        
        if (requiredNotes > 0 && requiredNotes <= availableNotes) {
            int notesToDispense = Math.min(requiredNotes, availableNotes);
            int dispensedAmount = notesToDispense * 100;
            
            updateNoteCount(notesToDispense);
            System.out.println("Dispensed " + notesToDispense + " notes of ₹100");
            
            int remainingAmount = amount - dispensedAmount;
            
            if (remainingAmount == 0) {
                return true;
            }
        }
        
        System.out.println("Cannot dispense remaining amount: ₹" + amount);
        return false;
    }
    
    @Override
    protected int getDenomination() {
        return 100;
    }
    
    @Override
    protected int getAvailableNotes() {
        return availableNotes;
    }
    
    @Override
    protected void updateNoteCount(int notesDispensed) {
        this.availableNotes -= notesDispensed;
    }
}
```

#### Cash Dispenser
```java
public class CashDispenser {
    private CashDispenserHandler dispenserChain;
    
    public CashDispenser() {
        setupDispenserChain();
    }
    
    private void setupDispenserChain() {
        // Initialize handlers with available notes
        CashDispenserHandler twoThousandHandler = new TwoThousandNoteHandler(10);
        CashDispenserHandler fiveHundredHandler = new FiveHundredNoteHandler(20);
        CashDispenserHandler oneHundredHandler = new OneHundredNoteHandler(30);
        
        // Setup chain: 2000 -> 500 -> 100
        twoThousandHandler.setNextHandler(fiveHundredHandler);
        fiveHundredHandler.setNextHandler(oneHundredHandler);
        
        this.dispenserChain = twoThousandHandler;
    }
    
    public boolean dispenseCash(int amount) {
        if (amount % 100 != 0) {
            System.out.println("Amount should be multiple of 100");
            return false;
        }
        
        return dispenserChain.dispense(Currency.INR, amount);
    }
}
```

### 4. Operations

#### Operation Interface
```java
public interface Operation {
    boolean execute(ATM atm, OperationRequest request);
    OperationType getType();
}
```

#### Cash Withdrawal Operation
```java
public class CashWithdrawal implements Operation {
    @Override
    public boolean execute(ATM atm, OperationRequest request) {
        WithdrawalRequest withdrawalRequest = (WithdrawalRequest) request;
        BankAccount account = atm.getCurrentAccount();
        
        // Validate withdrawal amount
        if (withdrawalRequest.getAmount() <= 0) {
            System.out.println("Invalid withdrawal amount");
            return false;
        }
        
        // Check account balance
        if (account.getBalance() < withdrawalRequest.getAmount()) {
            System.out.println("Insufficient balance");
            return false;
        }
        
        // Check daily withdrawal limit
        if (account.getDailyWithdrawalLimit() < withdrawalRequest.getAmount()) {
            System.out.println("Daily withdrawal limit exceeded");
            return false;
        }
        
        // Attempt cash dispensing
        boolean cashDispensed = atm.getCashDispenser().dispenseCash(withdrawalRequest.getAmount());
        
        if (cashDispensed) {
            // Deduct amount from account
            account.debit(withdrawalRequest.getAmount());
            account.updateDailyWithdrawalLimit(withdrawalRequest.getAmount());
            System.out.println("Cash withdrawn successfully. Remaining balance: ₹" + account.getBalance());
            return true;
        } else {
            System.out.println("Unable to dispense cash. Please try a different amount.");
            return false;
        }
    }
    
    @Override
    public OperationType getType() {
        return OperationType.CASH_WITHDRAWAL;
    }
}
```

### 5. Supporting Classes

#### Enums and Data Classes
```java
public enum OperationType {
    CASH_WITHDRAWAL,
    CASH_DEPOSIT,
    BALANCE_INQUIRY,
    CHANGE_PIN
}

public enum Currency {
    INR, USD, EUR
}

public class Card {
    private String cardNumber;
    private String pin;
    private String accountNumber;
    private CardType cardType;
    
    public boolean validatePIN(String enteredPin) {
        return this.pin.equals(enteredPin);
    }
    
    // Getters and setters
}

public class BankAccount {
    private String accountNumber;
    private double balance;
    private double dailyWithdrawalLimit;
    private double dailyWithdrawnAmount;
    
    public void debit(double amount) {
        this.balance -= amount;
    }
    
    public void credit(double amount) {
        this.balance += amount;
    }
    
    public void updateDailyWithdrawalLimit(double amount) {
        this.dailyWithdrawnAmount += amount;
    }
    
    // Getters and setters
}

public abstract class OperationRequest {
    protected String accountNumber;
    protected double amount;
    
    // Getters and setters
}

public class WithdrawalRequest extends OperationRequest {
    public WithdrawalRequest(String accountNumber, double amount) {
        this.accountNumber = accountNumber;
        this.amount = amount;
    }
}
```

## Usage Example

```java
public class ATMDemo {
    public static void main(String[] args) {
        // Initialize ATM
        ATM atm = new ATM();
        
        // Create a card
        Card userCard = new Card("1234567890", "1234", "ACC001", CardType.DEBIT);
        
        // ATM Usage Flow
        atm.insertCard(userCard);  // IdleState -> HasCardState
        atm.authenticatePIN("1234");  // HasCardState -> SelectOperationState
        atm.selectOperation(OperationType.CASH_WITHDRAWAL);  // SelectOperationState -> PerformOperationState
        
        // Create withdrawal request
        WithdrawalRequest request = new WithdrawalRequest("ACC001", 2500.0);
        atm.performOperation(request);  // Execute withdrawal
        
        // Cancel to complete session
        atm.cancelOperation();  // Return to IdleState
    }
}
```

## Key Benefits

### State Pattern Benefits
1. **Clean State Management**: Each state handles only relevant operations
2. **Easy Extension**: New states can be added without modifying existing code
3. **Clear State Transitions**: Well-defined state flow prevents invalid operations
4. **Maintainability**: State-specific logic is encapsulated

### Chain of Responsibility Benefits
1. **Flexible Cash Dispensing**: Easy to add/remove denominations
2. **Optimal Note Distribution**: Processes largest denominations first
3. **Scalable**: Can handle different currency systems
4. **Decoupled**: Each handler is independent and reusable

## Future Enhancements

1. **Multi-language Support**: Add language selection state
2. **Biometric Authentication**: Extend authentication mechanisms
3. **Receipt Printing**: Add receipt generation operations
4. **Network Operations**: Add remote transaction processing
5. **Admin Operations**: Add maintenance and refill states
6. **Transaction History**: Add statement printing functionality

## Conclusion

This LLD demonstrates a robust ATM system using State Pattern for managing different operational states and Chain of Responsibility for efficient cash dispensing. The design ensures maintainability, extensibility, and clear separation of concerns while providing a realistic simulation of ATM operations.