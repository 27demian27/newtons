package com.demian.physics.util;

public class Vector2D {
    public double x;
    public double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getAngle() {
        return Math.atan2(y, x);
    }

    public Vector2D normalized() {
        double magnitude = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        return new Vector2D(x / magnitude, y / magnitude);
    }
}
