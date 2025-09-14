package org.example.designpatterns.factorypattern.factory;

import org.example.designpatterns.factorypattern.uicomponents.button.ButtonGui;
import org.example.designpatterns.factorypattern.uicomponents.checkBox.checkBoxGui;

public interface GUIFactory {
    ButtonGui createButton();
    checkBoxGui createCheckBox();
}
