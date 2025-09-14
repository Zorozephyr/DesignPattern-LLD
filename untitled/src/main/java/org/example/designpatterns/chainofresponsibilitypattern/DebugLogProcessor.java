package org.example.designpatterns.chainofresponsibilitypattern;

public class DebugLogProcessor extends LogProcessor {

    public DebugLogProcessor(LogProcessor nextLogProcessor) {
        super(nextLogProcessor);
    }

    public void log(int logLevel, String message){
        if(logLevel == LogProcessor.DEBUG){
            System.out.println("Debug: " + message);
        }
        else{
            super.log(logLevel, message);
        }
    }
    
}
