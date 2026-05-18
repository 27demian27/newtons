package com.demian.view.controls;

import com.demian.physics.World;
import com.demian.view.Sandbox;

import javax.swing.*;
import java.awt.event.ActionEvent;

import static javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW;

public class KeyControls {

    private final Sandbox sandbox;
    private final World world;

    public KeyControls(World world, Sandbox sandbox) {
        this.world = world;
        this.sandbox = sandbox;
    }

    public void configure() {
        sandbox.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("SPACE"), "pauseSim");
        sandbox.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control I"), "insertionMode");
        sandbox.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control shift R"), "resetWorld");
        sandbox.getActionMap().put("pauseSim", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (sandbox.getSimulation().isPaused())
                    sandbox.getSimulation().unpause();
                else
                    sandbox.getSimulation().pause();
            }
        });
        sandbox.getActionMap().put("insertionMode", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                sandbox.setInInsertionMode(!sandbox.isInInsertionMode());
                System.out.println("switching " + (sandbox.isInInsertionMode() ? "to Insertion mode" : "to Normal mode"));
            }
        });
        sandbox.getActionMap().put("resetWorld", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                world.destroy();
                sandbox.initializeWorld();
            }
        });
    }
}
