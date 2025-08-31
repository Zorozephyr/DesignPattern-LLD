package org.example.factorypattern.factory;

import org.example.factorypattern.uicomponents.button.WindowsButton;
import org.example.factorypattern.uicomponents.checkBox.WindowsCheckBox;
import org.example.factorypattern.uicomponents.button.ButtonGui;
import org.example.factorypattern.uicomponents.checkBox.checkBoxGui;

public class WindowsFactory implements GUIFactory {
    public ButtonGui createButton() {
        return new WindowsButton();
    }

    public checkBoxGui createCheckBox() {
        return new WindowsCheckBox();
    }
}