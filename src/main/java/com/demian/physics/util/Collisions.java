package com.demian.physics.util;

import com.demian.physics.rigidbody.Body;

public class Collisions {

    public static Vector2D calculateReflection() {
        return null;
    }

    public static void resolveCollision(Body body1, Body body2) {
        if (body1.isImmovable() && body2.isImmovable()) {
            return;
        }

        if (body1.isImmovable()) {
//            body2.set
        }
    }

}
