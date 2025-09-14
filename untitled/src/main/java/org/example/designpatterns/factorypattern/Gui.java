package org.example.designpatterns.factorypattern;


import org.example.designpatterns.factorypattern.uicomponents.button.ButtonGui;
import org.example.designpatterns.factorypattern.uicomponents.checkBox.checkBoxGui;
import org.example.designpatterns.factorypattern.factory.GUIFactory;

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