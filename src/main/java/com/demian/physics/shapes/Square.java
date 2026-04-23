package com.demian.physics.shapes;

import com.demian.physics.Body;
import com.demian.physics.Collidable;
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
            if (square.getSx() > getSx() + size)
                return false;
            if (square.getSx() + square.getSize() < getSx())
                return false;
            if (square.getSy() > getSy() + size)
                return false;
            if (square.getSy() + square.getSize() < getSy())
                return false;

            return true;
        }
        return false;
    }
}
