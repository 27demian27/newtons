package com.demian.simulation;

import com.demian.physics.Body;
import com.demian.physics.World;
import com.demian.view.Sandbox;

public class Simulation implements Runnable{

    private final World world;
    private final Sandbox sandbox;

    public Simulation(World world, Sandbox sandbox) {
        this.world = world;
        this.sandbox = sandbox;
    }

    public void run () {
        while (true) {
            try {
                Thread.sleep(17);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            world.updateBodiesPosition();
            world.checkCollisions();
            sandbox.repaint();
        }
    }
}
