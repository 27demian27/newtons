package com.demian.physics;

import com.demian.physics.util.Collisions;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class World {

    public final static double GRAVITY_CONSTANT = -9.81;

    @Getter
    private List<Body> bodies;

    public World() {
        bodies = new ArrayList<>();
    }

    public void updateBodiesPosition() {
        for (Body body : bodies) {
            body.update(1f/60);
        }
    }

    public void checkCollisions() {
        for (Body body1 : bodies) {
            if (body1 instanceof Collidable collidable1) {
                for (Body body2 : bodies) {
                    if (body1 != body2 && body2 instanceof Collidable collidable2) {
                        if (collidable1.collision(collidable2))
                            Collisions.resolveCollision((Body) collidable1, (Body) collidable2);
                    }
                }
            }
        }
    }


    public void addBody(Body body) {
        bodies.add(body);
    }
}
