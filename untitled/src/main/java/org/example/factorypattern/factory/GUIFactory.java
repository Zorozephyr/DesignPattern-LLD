package org.example.factorypattern.factory;

import org.example.factorypattern.uicomponents.button.ButtonGui;
import org.example.factorypattern.uicomponents.checkBox.checkBoxGui;

public interface GUIFactory {
    ButtonGui createButton();
    checkBoxGui createCheckBox();
}
