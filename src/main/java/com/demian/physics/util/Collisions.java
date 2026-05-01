package com.demian.physics.util;

import com.demian.physics.rigidbody.Body;
import com.demian.physics.rigidbody.shapes.Circle;
import com.demian.physics.rigidbody.shapes.Rect;

public class Collisions {

    public static final double epsilon = 1.0;
    public static final double slop = 0.01;
    public static final double baumgarte = 0.6;


    public static void correctPosition(Body body1, Body body2, CollisionData collisionData) {

        Vector2D correction = collisionData.normal.scale(
                baumgarte *
                        Math.max(collisionData.penetration - slop, 0) / (1/body1.mass + 1/body2.mass)
        );

        System.out.println("Resolving collision...");
        System.out.println("body1: [" +body1.getX() + ", " +body1.getY() +"], body2: [" + body2.getX() + ", " + body2.getY() + "]");
        System.out.println(collisionData);
        System.out.println("correction: "+ correction);

        double ivMass1 = (1/body1.mass) / (1/body1.mass + 1/body2.mass);
        body1.setX(body1.getX() + correction.x * ivMass1);
        body1.setY(body1.getY() + correction.y * ivMass1);

        double ivMass2 = (1/body2.mass) / (1/body1.mass + 1/body2.mass);
        body2.setX(body2.getX() - correction.x * ivMass2);
        body2.setY(body2.getY() - correction.y * ivMass2);
    }

    public static CollisionData circleCircle(Circle c1, Circle c2) {

        if (c1.getCenterOfMass().subtract(c2.getCenterOfMass()).getLength() >= c1.radius + c2.radius)
            return new CollisionData(false, new Vector2D(0, 0), 0);

        Vector2D diff = c1.getCenterOfMass().subtract(c2.getCenterOfMass());
        Vector2D normal = diff.normalized();
        double penetration = c1.radius + c2.radius - diff.getLength();

        return new CollisionData(true, normal, penetration);

    }

    public static CollisionData rectCircle(Rect r, Circle c) {
        double ax1 = r.getX();
        double ay1 = r.getY();
        double ax2 = r.getX() + r.getWidth();
        double ay2 = r.getY() + r.getHeight();

        double bx1 = c.getX();
        double by1 = c.getY();
        double bx2 = c.getX() + 2 * c.radius;
        double by2 = c.getY() + 2 * c.radius;

        double overlapX = Math.min(ax2, bx2) - Math.max(ax1, bx1);
        double overlapY = Math.min(ay2, by2) - Math.max(ay1, by1);

        if (overlapX <= 0 || overlapY <= 0) {
            return new CollisionData(false, new Vector2D(0, 0), 0);
        }

        if (overlapX < overlapY) {

            double direction = (r.getCenterOfMass().x < c.getCenterOfMass().x) ? -1 : 1;
            Vector2D normal = new Vector2D(direction, 0);

            return new CollisionData(true, normal, overlapX);

        } else {
            double direction = (r.getCenterOfMass().y < c.getCenterOfMass().y) ? -1 : 1;
            Vector2D normal = new Vector2D(0, direction);

            return new CollisionData(true, normal, overlapY);
        }

    }

    public static CollisionData rectRect(Rect r1, Rect r2) {
        double ax1 = r1.getX();
        double ay1 = r1.getY();
        double ax2 = r1.getX() + r1.getWidth();
        double ay2 = r1.getY() + r1.getHeight();

        double bx1 = r2.getX();
        double by1 = r2.getY();
        double bx2 = r2.getX() + r2.getWidth();
        double by2 = r2.getY() + r2.getHeight();

        double overlapX = Math.min(ax2, bx2) - Math.max(ax1, bx1);
        double overlapY = Math.min(ay2, by2) - Math.max(ay1, by1);

        if (overlapX <= 0 || overlapY <= 0) {
            return new CollisionData(false, new Vector2D(0, 0), 0);
        }

        if (overlapX < overlapY) {

            double direction = (r1.getCenterOfMass().x < r2.getCenterOfMass().x) ? -1 : 1;
            Vector2D normal = new Vector2D(direction, 0);

            return new CollisionData(true, normal, overlapX);

        } else {
            double direction = (r1.getCenterOfMass().y < r2.getCenterOfMass().y) ? -1 : 1;
            Vector2D normal = new Vector2D(0, direction);

            return new CollisionData(true, normal, overlapY);
        }
    }

    public static void resolveCollision(Body body1, Body body2, CollisionData collisionData) {
        Vector2D normal = collisionData.normal;

        Vector2D relativeVelocity =
                body1.getVelocity_vec().subtract(body2.getVelocity_vec());

        double velocityAlongNormal = Vector2D.dot(relativeVelocity, normal);

        if (velocityAlongNormal > 0) return;

        double j = -(1 + epsilon) * velocityAlongNormal;
        j /= (1.0 / body1.mass + 1.0 / body2.mass);

        Vector2D impulse = normal.scale(j);

        body1.setVelocity_vec(
                body1.getVelocity_vec().add(impulse.scale(1.0 / body1.mass))
        );

        body2.setVelocity_vec(
                body2.getVelocity_vec().subtract(impulse.scale(1.0 / body2.mass))
        );
    }

}
