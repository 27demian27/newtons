package com.demian.physics;

import com.demian.physics.util.Vector2D;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Body {

    @Setter private boolean immovable;

    private final double mass;

    private Vector2D velocity_vec;

    @Setter
    private Vector2D accel_vec;

    private double sx;
    private double sy;


    public Body(double mass, double x, double y) {
        this.mass = mass;
        velocity_vec = new Vector2D(0, 0);
        accel_vec = new Vector2D(0, World.GRAVITY_CONSTANT);
        sx = x; sy = y;
        immovable = false;
    }

    public void update(float dt) {
        if (immovable) return;

        velocity_vec.x = velocity_vec.x + accel_vec.x * dt;
        velocity_vec.y = velocity_vec.y + accel_vec.y * dt;

        sx = sx + velocity_vec.x * dt;
        sy = sy + velocity_vec.y * dt;
    }


}
