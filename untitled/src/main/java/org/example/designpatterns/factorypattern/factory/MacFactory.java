package org.example.designpatterns.factorypattern.factory;

import org.example.designpatterns.factorypattern.uicomponents.button.MacButton;
import org.example.designpatterns.factorypattern.uicomponents.checkBox.MacCheckBox;
import org.example.designpatterns.factorypattern.uicomponents.button.ButtonGui;
import org.example.designpatterns.factorypattern.uicomponents.checkBox.checkBoxGui;

public class MacFactory implements GUIFactory {
    public ButtonGui createButton() {
        return new MacButton();
    }

    public checkBoxGui createCheckBox() {
        return new MacCheckBox();
    }
}
