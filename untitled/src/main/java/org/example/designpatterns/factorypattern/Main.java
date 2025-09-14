package org.example.designpatterns.factorypattern;

import org.example.designpatterns.factorypattern.factory.GUIFactory;
import org.example.designpatterns.factorypattern.factory.MacFactory;
import org.example.designpatterns.factorypattern.factory.WindowsFactory;
import org.example.factorypattern.factory.*;

public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please provide an argument: 'windows' or 'mac'");
            return;
        }

        String osType = args[0].toLowerCase();
        GUIFactory factory;

        switch (osType) {
            case "windows" -> factory = new WindowsFactory();
            case "mac" -> factory = new MacFactory();
            default -> {
                System.out.println("Unsupported OS type");
                return;
            }
        }

        Gui gui = new Gui(factory);
        gui.render();
    }
}
