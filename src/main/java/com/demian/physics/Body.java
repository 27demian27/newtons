package com.demian.physics;

import lombok.Getter;

@Getter
public class Body {

    private final double mass;
    private double velocity;

    private double x;
    private double y;


    public Body(double mass, double x, double y) {
        this.mass = mass;
        this.velocity = 0;
        this.x = x;
        this.y = y;
    }


}
