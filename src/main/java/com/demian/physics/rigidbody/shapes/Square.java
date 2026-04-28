package com.demian.physics.rigidbody.shapes;

import com.demian.physics.rigidbody.Body;
import com.demian.physics.Collidable;
import com.demian.physics.util.Vector2D;
import lombok.Getter;

public class Square extends Body implements Collidable {

    @Getter
    private final double size;

    public Square(double size, double mass, double x, double y) {
        super(mass, x, y);

        this.size = size;
    }


    @Override
    public boolean collision(Collidable collidable) {
        if (collidable instanceof Square square) {
            if (square.x > x + size)
                return false;
            if (square.x + square.getSize() < x)
                return false;
            if (square.y > y + size)
                return false;
            if (square.y + square.getSize() < y)
                return false;

            return true;
        }
        else if (collidable instanceof Rect rect) {
            if (rect.getX() > x + size)
                return false;
            if (rect.getX() + rect.getWidth() < x)
                return false;
            if (rect.getY() > y + size)
                return false;
            if (rect.getY() + rect.getHeight() < y)
                return false;

            return true;
        }
        return false;
    }

    @Override
    public Vector2D getCenterOfMass() {
        return new Vector2D(x + size / 2, y + size / 2);
    }
}
