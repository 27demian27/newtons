package nl.demiannieuwenhuis.view;

import nl.demiannieuwenhuis.physics.World;
import nl.demiannieuwenhuis.simulation.Simulation;
import nl.demiannieuwenhuis.view.controls.KeyControls;
import nl.demiannieuwenhuis.view.controls.MouseControls;

import javax.swing.*;
import java.awt.*;

public class GUI {

    private final JFrame frame;
    private final World world;
    private final Sandbox sandbox;
    private final Simulation simulation;
    private final MouseControls mouseControls;
    private final KeyControls keyControls;

    public GUI(World world) {
        this.world = world;

        simulation = new Simulation(world);

        frame = new JFrame("Sandbox");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        sandbox = new Sandbox(world, simulation);
        mouseControls = new MouseControls(world, sandbox);
        keyControls = new KeyControls(world, sandbox);
        configureControls();

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
            mouseControls.incrementLastDragUpdate(0.016f);
            mouseControls.handleBodyHold();
        });
        timer.setCoalesce(false);
        timer.start();
    }

    private void configureControls() {

        sandbox.addMouseListener(mouseControls);
        sandbox.addMouseWheelListener(mouseControls);
        sandbox.addMouseMotionListener(mouseControls);


        keyControls.configure();
    }
}
