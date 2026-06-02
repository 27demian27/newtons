package nl.demiannieuwenhuis.physics;

import nl.demiannieuwenhuis.physics.rigidbody.Body;
import nl.demiannieuwenhuis.physics.util.Vector2D;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class Rope implements Serializable {

    private Vector2D pos1;
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

        attachedBody1.setNext_accel_vec(
                attachedBody1.getNext_accel_vec().add(
                        accelDirection1.scale(stretch * stiffness / attachedBody1.mass)
                )
        );

        attachedBody2.setNext_accel_vec(
                attachedBody2.getNext_accel_vec().add(
                        accelDirection2.scale(stretch * stiffness / attachedBody2.mass)
                )
        );
    }

}
