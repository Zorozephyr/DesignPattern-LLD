package org.example.factorypattern.uicomponents.button;

public class MacButton implements ButtonGui{
    @Override
    public void renderButton() {
        System.out.println("Rendering Mac Button");
    }
}

