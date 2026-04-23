package com.demian.physics;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class World {

    private final static double GRAVITY_CONSTANT = 9.81;

    @Getter
    private List<Body> bodies;

    public World() {
        bodies = new ArrayList<>();
    }

    public void addBody(Body body) {
        bodies.add(body);
    }
}
