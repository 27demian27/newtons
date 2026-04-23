package com.demian.physics.shapes;

import com.demian.physics.Body;
import lombok.Getter;

public class Square extends Body {

    @Getter
    private final double size;

    public Square(double size, double mass, double x, double y) {
        super(mass, x, y);

        this.size = size;
    }


}
