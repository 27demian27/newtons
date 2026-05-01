package com.demian.physics.rigidbody.shapes;

import com.demian.physics.rigidbody.Body;
import com.demian.physics.util.Vector2D;
import lombok.Getter;

@Getter
public class Rect extends Body {

    private static final double drag_c = 1.05;

    private double width;
    private double height;

    public Rect(double mass, double x, double y, double width, double height) {
        super(mass, x, y, drag_c);

        this.width = width;
        this.height = height;
    }

    @Override
    public Vector2D getCenterOfMass() {
        return new Vector2D(x + width / 2.0, y + height / 2.0);
    }

    @Override
    protected double getReferenceArea() {
        double e = 0.0001;
        if (velocity_vec.normalized().x > -e && velocity_vec.normalized().x < e)
            return width;
        if (velocity_vec.normalized().y > -e && velocity_vec.normalized().y < e)
            return height;

        return width + height;
    }
}
