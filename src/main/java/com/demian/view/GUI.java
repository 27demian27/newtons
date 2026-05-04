package com.demian.view;

import com.demian.physics.World;
import com.demian.physics.rigidbody.Body;
import com.demian.physics.util.Vector2D;
import com.demian.simulation.Simulation;

import javax.swing.*;
import java.awt.*;

public class GUI {

    private final JFrame frame;
    private final World world;
    private final Sandbox sandbox;
    private final Simulation simulation;

    public GUI(World world) {
        this.world = world;

        simulation = new Simulation(world);

        frame = new JFrame("Sandbox");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        sandbox = new Sandbox(world, simulation);
        sandbox.configure();
        sandbox.initializeWorld();

        frame.add(sandbox, BorderLayout.CENTER);

        frame.setSize(1280, 720);
        frame.setLocationRelativeTo(null);
    }

    public void show() {
        frame.setVisible(true);
    }

    public void startSimulation() {
        Thread.ofPlatform().start(simulation);

    }

    public void startPainting() {
        Timer timer = new Timer(16, e -> {
            sandbox.repaint();
            sandbox.incrementLastDragUpdate(0.016f);
            sandbox.handleBodyHold();
        });
        timer.setCoalesce(false);
        timer.start();
    }
}
