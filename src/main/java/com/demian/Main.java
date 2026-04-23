package com.demian;

import com.demian.physics.World;
import com.demian.simulation.Simulation;
import com.demian.view.GUI;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        World world = new World();
        SwingUtilities.invokeLater(() -> {
            GUI gui = new GUI(world);
            gui.show();
            gui.startSimulation();
        });
    }
}
