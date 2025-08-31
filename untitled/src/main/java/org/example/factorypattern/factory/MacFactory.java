package org.example.factorypattern.factory;

import org.example.factorypattern.uicomponents.button.MacButton;
import org.example.factorypattern.uicomponents.checkBox.MacCheckBox;
import org.example.factorypattern.uicomponents.button.ButtonGui;
import org.example.factorypattern.uicomponents.checkBox.checkBoxGui;

public class MacFactory implements GUIFactory {
    public ButtonGui createButton() {
        return new MacButton();
    }

    public checkBoxGui createCheckBox() {
        return new MacCheckBox();
    }
}
