package com.demian.view.controls;

import com.demian.io.WorldLoader;
import com.demian.io.WorldWriter;
import com.demian.physics.World;
import com.demian.view.Sandbox;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

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
        sandbox.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control S"), "saveWorld");
        sandbox.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control O"), "loadWorld");


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
        sandbox.getActionMap().put("saveWorld", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    WorldWriter.write(world);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        sandbox.getActionMap().put("loadWorld", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    World loadedWorld = WorldLoader.load();
                    world.setBodies(loadedWorld.getBodies());
                    world.setRopes(loadedWorld.getRopes());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
