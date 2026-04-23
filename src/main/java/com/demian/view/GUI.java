package com.demian.view;

import com.demian.physics.World;
import com.demian.simulation.Simulation;

import javax.swing.*;
import java.awt.*;

public class GUI {

    private final JFrame frame;
    private final World world;
    private final Sandbox sandbox;

    public GUI(World world) {
        this.world = world;

        frame = new JFrame("Sandbox");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        sandbox = new Sandbox(world);
        sandbox.configure();
        sandbox.initializeWorld();

        frame.add(sandbox, BorderLayout.CENTER);

        frame.setSize(800, 800);
        frame.setLocationRelativeTo(null);
    }

    public void show() {
        frame.setVisible(true);
    }

    public void startSimulation() {
        Thread.ofPlatform().start(new Simulation(world, sandbox));

    }
}
