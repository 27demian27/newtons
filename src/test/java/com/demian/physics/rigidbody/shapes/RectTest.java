package nl.demiannieuwenhuis.physics.rigidbody.shapes;

import nl.demiannieuwenhuis.physics.util.Vector2D;
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
}
