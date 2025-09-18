package org.example.lowleveldesignexamples.atmlld.atmstates;

import org.example.lowleveldesignexamples.atmlld.ATM;
import org.example.lowleveldesignexamples.atmlld.Card;

public class IdleState extends ATMState{

    @Override
    public void insertCard(ATM atm, Card card){
        System.out.println("Card is inserted");
        atm.setCurrentATMState(new HasCardState());
    }
}
