package com.demian.physics.rigidbody.shapes;

import com.demian.physics.util.Vector2D;
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class RectTest {

    @Test
    public void testContains() {
        Rect rect1 = new Rect(10, 0, 0, 20, 10);
        assertThat(rect1.contains(15, 9), is(true));
        assertThat(rect1.contains(0.001, 0.001), is(true));
        assertThat(rect1.contains(0, 0), is(false));
        assertThat(rect1.contains(201, 999), is(false));

    }

    @Test
    public void testGetCenterOfMass() {
        Rect rect1 = new Rect(10, 10, 10, 10, 10);
        Vector2D center = new Vector2D(15, 15);
        assertThat(rect1.getCenterOfMass(), is(center));
    }

    @Test
    public void testGetReferenceArea() {
        Rect rect1 = new Rect(10, 0, 0, 30, 20);
        assertThat(rect1.getReferenceArea(), is(0.0));

        rect1.setVelocity_vec(new Vector2D(-1, 0));
        assertThat(rect1.getReferenceArea(), is(20.0));

        rect1.setVelocity_vec(new Vector2D(1, 1));
        assertThat(rect1.getReferenceArea(), is(50.0));

        rect1.setVelocity_vec(new Vector2D(0, 1));
        assertThat(rect1.getReferenceArea(), is(30.0));
    }
}
