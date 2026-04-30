package com.demian.physics.rigidbody.shapes;

import com.demian.physics.rigidbody.Body;
import com.demian.physics.util.Vector2D;
import lombok.Getter;

@Getter
public class Rect extends Body {

    private double width;
    private double height;

    public Rect(double mass, double x, double y, double width, double height) {
        super(mass, x, y);

        this.width = width;
        this.height = height;
    }

    @Override
    public Vector2D getCenterOfMass() {
        return new Vector2D(x + width / 2.0, y + height / 2.0);
    }
}
