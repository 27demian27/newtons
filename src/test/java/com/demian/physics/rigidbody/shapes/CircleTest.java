package com.demian.physics.rigidbody.shapes;

import com.demian.physics.util.Vector2D;
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class CircleTest {

    @Test
    public void testContains() {
        Circle c1 = new Circle(10, 0, 0, 10);

        assertThat(c1.contains(5, 5), is(true));
        assertThat(c1.contains(4, 6), is(true));
        assertThat(c1.contains(12, 12), is(true));
        assertThat(c1.contains(1, 1), is(false));
        assertThat(c1.contains(-1, -1), is(false));
    }

    @Test
    public void testGetCenterOfMass() {
        Circle c1 = new Circle(10, 0, 0, 10);
        Vector2D center1 = new Vector2D(10, 10);

        assertThat(c1.getCenterOfMass(), is(center1));


        Circle c2 = new Circle(10, 25, 25, 2);
        Vector2D center2 = new Vector2D(27, 27);

        assertThat(c2.getCenterOfMass(), is(center2));
    }

    @Test
    public void testGetReferenceArea() {
        Circle c1 = new Circle(10, 0, 0, 17);

        assertThat(c1.getReferenceArea(), is(17*2.0));
    }
}