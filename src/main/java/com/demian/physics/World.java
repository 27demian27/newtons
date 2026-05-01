package com.demian.physics;

import com.demian.physics.rigidbody.Body;
import com.demian.physics.rigidbody.shapes.Circle;
import com.demian.physics.rigidbody.shapes.Rect;
import com.demian.physics.util.CollisionData;
import com.demian.physics.util.Collisions;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class World {

    public final static double GRAVITY_CONSTANT = -9.81;
    public final static double AIR_DENSITY = 1.225;

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
                Body body1 = bodies.get(i);
                Body body2 = bodies.get(j);
                if (body1.isImmovable() && body2.isImmovable()) continue;

                if (body1 instanceof Rect rect1 && body2 instanceof Rect rect2) {
                    CollisionData collision = Collisions.rectRect(rect1, rect2);
                    if (collision.colliding) {
                        Collisions.resolveCollision(rect1, rect2, collision);
                        Collisions.correctPosition(rect1, rect2, collision);
                    }
                } else if (body1 instanceof Rect rect && body2 instanceof Circle circle) {
                    CollisionData collision = Collisions.rectCircle(rect, circle);
                    if (collision.colliding) {
                        Collisions.resolveCollision(rect, circle, collision);
                        Collisions.correctPosition(rect, circle, collision);
                    }
                } else if (body1 instanceof Circle circle && body2 instanceof Rect rect) {
                    CollisionData collision = Collisions.rectCircle(rect, circle);
                    if (collision.colliding) {
                        Collisions.resolveCollision(rect, circle, collision);
                        Collisions.correctPosition(rect, circle, collision);
                    }
                } else if (body1 instanceof Circle circle1 && body2 instanceof Circle circle2) {
                    CollisionData collision = Collisions.circleCircle(circle1, circle2);
                    if (collision.colliding) {
                        Collisions.resolveCollision(circle1, circle2, collision);
                        Collisions.correctPosition(circle1, circle2, collision);
                    }
                }

            }
        }
    }


    public void addBody(Body body) {
        bodies.add(body);
    }

    public void addBodies(Body body, Body... bodies) {
        this.bodies.add(body);
        this.bodies.addAll(List.of(bodies));
    }
}
