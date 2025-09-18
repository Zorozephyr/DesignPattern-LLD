package org.example.lowleveldesignexamples.atmlld;

public class UserBankAccount {
    int balance;

    public void withdrawalBalance(int amount) {
        balance = balance - amount;
    }

}
