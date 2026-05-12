package com.demian.simulation;

import com.demian.physics.World;

import java.util.concurrent.atomic.AtomicBoolean;

public class Simulation implements Runnable{

    private static final float TIME_STEP = 0.008f;

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
                Thread.sleep((long) (TIME_STEP * 1000));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (isRunning.get()) {
                world.updateBodies(TIME_STEP);
                world.updateRopes(TIME_STEP);
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
