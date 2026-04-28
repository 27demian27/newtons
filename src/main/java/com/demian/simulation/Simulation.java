package com.demian.simulation;

import com.demian.physics.World;
import com.demian.view.Sandbox;

import java.util.concurrent.atomic.AtomicBoolean;

public class Simulation implements Runnable{

    private final World world;

    private AtomicBoolean isRunning;

    public Simulation(World world) {
        this.world = world;
        isRunning = new AtomicBoolean(false);
    }

    public void run () {
        isRunning.set(true);

        while (true) {
            try {
                Thread.sleep(8);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (isRunning.get()) {
                world.updateBodiesPosition(0.008f);
                world.checkCollisions();
            }
        }
    }

    public void pause() {
        isRunning.set(false);
    }

    public void unpause() {
        isRunning.set(true);
    }
    public boolean isPaused() {
        return !isRunning.get();
    }
}
