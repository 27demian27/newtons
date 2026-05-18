package com.demian.physics.rigidbody;

import com.demian.physics.World;
import com.demian.physics.util.Vector2D;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
public abstract class Body implements Serializable {

    protected boolean immovable;

    public final double mass;
    protected double rotation;

    @Setter
    protected double angular_v;
    protected double angular_a;

    @Setter
    protected Vector2D velocity_vec;

    @Setter
    protected Vector2D accel_vec;

    @Setter
    private Vector2D next_accel_vec;

    @Setter protected double x;
    @Setter protected double y;

    private static final double velocity_dampening = 0.05;

    public Body(double mass, double x, double y) {
        this.mass = mass;
        this.x = x;
        this.y = y;
        velocity_vec = new Vector2D(0, 0);
        accel_vec = new Vector2D(0, World.GRAVITY_CONSTANT);
        next_accel_vec = accel_vec;
        rotation = 0.0;
        angular_v = 0.0;
        angular_a = 0.0;
        if (Double.isInfinite(mass))
            immovable = true;
    }

    /**
     * Updates this body for a certain delta time passed.
     * @param dt delta time that has been passed in seconds
     */
    public void update(float dt) {
        if (immovable) return;

        accel_vec = next_accel_vec;

        angular_v = angular_v + angular_a * dt;

        velocity_vec = new Vector2D(
                velocity_vec.x + accel_vec.x * dt,
                velocity_vec.y + accel_vec.y * dt
        );

        introduceFakeDrag(dt);

        rotation = rotation + angular_v * dt;

        x = x + velocity_vec.x * dt;
        y = y + velocity_vec.y * dt;

        next_accel_vec = new Vector2D(0, World.GRAVITY_CONSTANT);
    }

    /**
     * Introduces a fake linear drag based on a velocity dampening factor.
     */
    private void introduceFakeDrag(float dt) {
        velocity_vec = velocity_vec.scale(1 - velocity_dampening * dt);
    }

    public abstract boolean contains(double x, double y);

    /**
     * Calculates and gets center of mass for a uniformly dense rigid body.
     * @return Vector2D point where center of mass resides
     */
    public abstract Vector2D getCenterOfMass();
}
