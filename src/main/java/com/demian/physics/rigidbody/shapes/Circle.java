package com.demian.physics.rigidbody.shapes;

import com.demian.physics.rigidbody.Body;
import com.demian.physics.util.Vector2D;

public class Circle extends Body {

    private static final double drag_c = 0.47;

    public final double radius;

    public Circle(double mass, double x, double y, double radius) {
        super(mass, x, y, drag_c);

        this.radius = radius;
    }


    @Override
    public Vector2D getCenterOfMass() {
        return new Vector2D(x + radius, y + radius);
    }

    @Override
    protected double getReferenceArea() {
        return 2 * radius;
    }
}
