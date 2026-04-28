package com.demian.physics.rigidbody.shapes;

import com.demian.physics.Collidable;
import com.demian.physics.rigidbody.Body;
import com.demian.physics.util.Vector2D;
import lombok.Getter;

@Getter
public class Rect extends Body implements Collidable {

    private double width;
    private double height;

    public Rect(double mass, double x, double y, double width, double height) {
        super(mass, x, y);

        this.width = width;
        this.height = height;

    }

    @Override
    public boolean collision(Collidable collidable) {
        if (collidable instanceof Square square) {
            if (square.getX() > x + width)
                return false;
            if (square.getX() + square.getSize() < x)
                return false;
            if (square.getY() > y + height)
                return false;
            if (square.getY() + square.getSize() < y)
                return false;

            return true;
        } else if (collidable instanceof Rect rect) {
            if (rect.x > x + width)
                return false;
            if (rect.x + rect.width < x)
                return false;
            if (rect.y > y + height)
                return false;
            if (rect.y + rect.height < y)
                return false;

            return true;
        }
        return false;
    }

    @Override
    public Vector2D getCenterOfMass() {
        return null;
    }
}
