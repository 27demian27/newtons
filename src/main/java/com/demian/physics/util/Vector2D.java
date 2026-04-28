package com.demian.physics.util;

/**
 * Class representing a vector on a 2D plane.
 */
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

    public double getLength() {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    public Vector2D normalized() {
        double magnitude = getLength();
        return new Vector2D(x / magnitude, y / magnitude);
    }

    public Vector2D scale(double multiplier) {
        return new Vector2D(x * multiplier, y * multiplier);
    }

    public Vector2D rotate(double theta) {
        return new Vector2D(x * Math.cos(theta) - y * Math.sin(theta), x * Math.sin(theta) + y * Math.cos(theta));
    }

    public Vector2D subtract(Vector2D vec) {
        return new Vector2D(x - vec.x, y - vec.y);
    }

    public Vector2D add(Vector2D vec) {
        return new Vector2D(x + vec.x, y + vec.y);
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
