package org.example.factorypattern.uicomponents.button;

public class WindowsButton implements ButtonGui{
    @Override
    public void renderButton() {
        System.out.println("Rendering Windows Button");
    }
}
