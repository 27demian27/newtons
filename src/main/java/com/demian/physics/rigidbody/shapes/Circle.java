package com.demian.physics.rigidbody.shapes;

import com.demian.physics.rigidbody.Body;
import com.demian.physics.util.Vector2D;

public class Circle extends Body {

    public final double radius;

    public Circle(double mass, double x, double y, double radius) {
        super(mass, x, y);

        this.radius = radius;
    }


    @Override
    public Vector2D getCenterOfMass() {
        return new Vector2D(x + radius, y + radius);
    }
}
