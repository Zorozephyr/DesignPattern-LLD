package org.example.designpatterns.factorypattern.factory;

import org.example.designpatterns.factorypattern.uicomponents.button.WindowsButton;
import org.example.designpatterns.factorypattern.uicomponents.checkBox.WindowsCheckBox;
import org.example.designpatterns.factorypattern.uicomponents.button.ButtonGui;
import org.example.designpatterns.factorypattern.uicomponents.checkBox.checkBoxGui;

public class WindowsFactory implements GUIFactory {
    public ButtonGui createButton() {
        return new WindowsButton();
    }

    public checkBoxGui createCheckBox() {
        return new WindowsCheckBox();
    }
}