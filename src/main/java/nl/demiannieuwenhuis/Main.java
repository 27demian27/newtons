package nl.demiannieuwenhuis;

import nl.demiannieuwenhuis.physics.World;
import nl.demiannieuwenhuis.view.GUI;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        World world = new World();
        SwingUtilities.invokeLater(() -> {
            GUI gui = new GUI(world);
            gui.show();
            gui.startSimulation();
            gui.startPainting();
        });
    }
}
