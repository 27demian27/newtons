package com.demian.physics.rigidbody;

import com.demian.physics.World;
import com.demian.physics.util.Vector2D;
import lombok.Getter;
import lombok.Setter;

@Getter
public abstract class Body {

    private boolean immovable;

    public final double mass;
    private final double rotation;

    @Setter
    private Vector2D velocity_vec;


    private final Vector2D accel_vec;

    @Setter protected double x;
    @Setter protected double y;


    public Body(double mass, double x, double y) {
        this.mass = mass;
        this.x = x;
        this.y = y;
        velocity_vec = new Vector2D(0, 0);
        accel_vec = new Vector2D(0, World.GRAVITY_CONSTANT);
        rotation = 0.0;
        if (Double.isInfinite(mass))
            immovable = true;
    }

    /**
     * Updates this body for a certain delta time passed.
     * @param dt delta time that has been passed in seconds
     */
    public void update(float dt) {
        if (immovable) return;

        velocity_vec.x = velocity_vec.x + accel_vec.x * dt;
        velocity_vec.y = velocity_vec.y + accel_vec.y * dt;

        x = x + velocity_vec.x * dt;
        y = y + velocity_vec.y * dt;

        System.out.println("[" + x + ", " + y + "]");
    }

    /**
     * Calculates and gets center of mass for a uniformly dense rigid body.
     * @return Vector2D point where center of mass resides
     */
    public abstract Vector2D getCenterOfMass();
}
