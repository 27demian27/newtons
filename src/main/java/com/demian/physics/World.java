package com.demian.physics;

import com.demian.physics.rigidbody.Body;
import com.demian.physics.rigidbody.shapes.Rect;
import com.demian.physics.util.CollisionData;
import com.demian.physics.util.Collisions;
import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class World {

    public final static double GRAVITY_CONSTANT = -9.81;

    @Getter
    private List<Body> bodies;

    public World() {
        bodies = new ArrayList<>();
    }

    public void updateBodiesPosition(float dt) {
        for (Body body : bodies) {
            body.update(dt);
        }
    }

    public void checkCollisions() {

        for (int i = 0; i < bodies.size(); i++) {
            for (int j = i + 1; j < bodies.size(); j++) {
                if (bodies.get(i) instanceof Rect rect1 && bodies.get(j) instanceof Rect rect2) {
                    CollisionData collision = Collisions.rectRect(rect1, rect2);
                    if (collision.colliding) {
                        Collisions.resolveCollision(rect1, rect2, collision);
                        Collisions.correctPosition(rect1, rect2, collision);
                    }
                }
            }
        }
    }


    public void addBody(Body body) {
        bodies.add(body);
    }
}
