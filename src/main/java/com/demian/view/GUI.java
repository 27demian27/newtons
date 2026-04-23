package com.demian.view;

import com.demian.physics.World;

import javax.swing.*;
import java.awt.*;

public class GUI {

    private final JFrame frame;

    public GUI(World world) {
        frame = new JFrame("Sandbox");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        Sandbox sandbox = new Sandbox(world);
        sandbox.configure();
        sandbox.initializeWorld();

        frame.add(sandbox, BorderLayout.CENTER);

        frame.setSize(800, 800);
        frame.setLocationRelativeTo(null);
    }

    public void show() {
        frame.setVisible(true);
    }
}
