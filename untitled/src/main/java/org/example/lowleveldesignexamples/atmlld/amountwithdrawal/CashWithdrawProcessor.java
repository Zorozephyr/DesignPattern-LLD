package org.example.lowleveldesignexamples.atmlld.amountwithdrawal;

import org.example.lowleveldesignexamples.atmlld.ATM;

public class CashWithdrawProcessor {
    CashWithdrawProcessor nextCashWithdrawalProcessor;

    public CashWithdrawProcessor(CashWithdrawProcessor nextCashWithdrawalProcessor) {
        this.nextCashWithdrawalProcessor = nextCashWithdrawalProcessor;
    }

    public void withdraw(ATM atm, int remainingAmount){
        if(nextCashWithdrawalProcessor != null){
            nextCashWithdrawalProcessor.withdraw(atm, remainingAmount);
        }
    }
}
