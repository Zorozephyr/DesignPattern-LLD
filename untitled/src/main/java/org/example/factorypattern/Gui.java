package org.example.factorypattern;


import org.example.factorypattern.uicomponents.button.ButtonGui;
import org.example.factorypattern.uicomponents.checkBox.checkBoxGui;
import org.example.factorypattern.factory.GUIFactory;

public class Gui {

    private ButtonGui buttonGui;
    private checkBoxGui checkBoxGui;

    public Gui(GUIFactory factory) {
        this.buttonGui = factory.createButton();
        this.checkBoxGui = factory.createCheckBox();
    }

    public void render() {
        buttonGui.renderButton();
        checkBoxGui.renderCheckBox();
    }
}