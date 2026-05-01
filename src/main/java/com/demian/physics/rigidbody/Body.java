package com.demian.physics.rigidbody;

import com.demian.physics.World;
import com.demian.physics.util.Vector2D;
import lombok.Getter;
import lombok.Setter;

@Getter
public abstract class Body {

    protected boolean immovable;

    public final double mass;
    protected double rotation;

    @Setter
    protected double angular_v;
    protected double angular_a;
    
    private final double drag_c;

    @Setter
    protected Vector2D velocity_vec;


    protected final Vector2D accel_vec;

    @Setter protected double x;
    @Setter protected double y;


    public Body(double mass, double x, double y, double dragC) {
        this.mass = mass;
        this.drag_c = dragC;
        this.x = x;
        this.y = y;
        velocity_vec = new Vector2D(0, 0);
        accel_vec = new Vector2D(0, World.GRAVITY_CONSTANT);
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

        double airResistanceX = 1 / 2.0 * World.AIR_DENSITY * Math.pow(velocity_vec.x, 2) * getReferenceArea() * drag_c;
        double airResistanceY = 1 / 2.0 * World.AIR_DENSITY * Math.pow(velocity_vec.y, 2) * getReferenceArea() * drag_c;

        accel_vec.subtract(new Vector2D(airResistanceX, airResistanceY));

        angular_v = angular_v + angular_a * dt;

        velocity_vec.x = velocity_vec.x + accel_vec.x * dt;
        velocity_vec.y = velocity_vec.y + accel_vec.y * dt;

        rotation = rotation + angular_v * dt;

        x = x + velocity_vec.x * dt;
        y = y + velocity_vec.y * dt;

    }

    /**
     * Calculates and gets center of mass for a uniformly dense rigid body.
     * @return Vector2D point where center of mass resides
     */
    public abstract Vector2D getCenterOfMass();

    protected abstract double getReferenceArea();
}
