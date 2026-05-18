package com.demian.physics;

import com.demian.physics.rigidbody.Body;
import com.demian.physics.util.Vector2D;
import lombok.Getter;

import java.io.Serializable;

public class Rope implements Serializable {

    @Getter
    private Vector2D pos1;
    @Getter
    private Vector2D pos2;

    private Body attachedBody1;
    private Body attachedBody2;

    private double base_length;
    private double stretch;

    private double stiffness;

    public Rope(Vector2D pos1, Vector2D pos2) {
        this.pos1 = pos1;
        this.pos2 = pos2;

        stiffness = 6.0;
        base_length = pos1.subtract(pos2).length();
        stretch = 0.0;
    }

    public void attachBodies(World world) {
        world.findBody(pos1.x, pos1.y).ifPresent(b -> attachedBody1 = b);
        world.findBody(pos2.x, pos2.y).ifPresent(b -> attachedBody2 = b);
    }

    public void update(float dt) {

        pos1 = attachedBody1.getCenterOfMass();
        pos2 = attachedBody2.getCenterOfMass();

        stretch = Math.max(0, pos1.subtract(pos2).length() - base_length);

        Vector2D accelDirection1 = pos2.subtract(pos1).normalized();
        Vector2D accelDirection2 = pos1.subtract(pos2).normalized();

        attachedBody1.setAccel_vec(
                accelDirection1
                        .scale(stretch * stiffness / attachedBody1.mass)
                        .add(new Vector2D(0, World.GRAVITY_CONSTANT))
        );

        attachedBody2.setAccel_vec(
                accelDirection2
                        .scale(stretch * stiffness / attachedBody2.mass)
                        .add(new Vector2D(0, World.GRAVITY_CONSTANT))
        );
        System.out.println("Stretch of rope: " + stretch);
    }

}
