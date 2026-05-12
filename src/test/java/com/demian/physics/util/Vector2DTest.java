package com.demian.physics.util;

import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

class Vector2DTest {

    @Test
    public void testDot() {
        assertThat(Vector2D.dot(new Vector2D(1, 2), new Vector2D(3, 4)), is(11.0));
        assertThat(Vector2D.dot(new Vector2D(1234, 5678), new Vector2D(200, 400)), is(2518000.0));
    }

    @Test
    public void testAngle() {
        assertThat(new Vector2D(1, 0).angle(), is(0.0));
        assertThat(new Vector2D(-1, 0).angle(), is(Math.PI));
        assertThat(new Vector2D(0, 1).angle(), is(Math.PI / 2.0));
        assertThat(new Vector2D(0, -1).angle(), is(-Math.PI / 2.0));
        assertThat(new Vector2D(1, 1).angle(), is(Math.PI / 4.0));
        assertThat(new Vector2D(1, 5).angle(), is(Math.atan(5)));

    }

    @Test
    public void testLength() {
        assertThat(new Vector2D(1, 0).length(), is(1.0));
        assertThat(new Vector2D(-1, 0).length(), is(1.0));
        assertThat(new Vector2D(0, 1).length(), is(1.0));
        assertThat(new Vector2D(0, -1).length(), is(1.0));
        assertThat(new Vector2D(1, 1).length(), is(Math.sqrt(2)));

        assertThat(new Vector2D(58, 241).length(), is(Math.sqrt(Math.pow(58, 2) + Math.pow(241, 2))));
    }

    @Test
    public void testNormalized() {
        assertThat(new Vector2D(1, 0).normalized(), is(new Vector2D(1, 0)));
        assertThat(new Vector2D(-1, 0).normalized(), is(new Vector2D(-1, 0)));
        assertThat(new Vector2D(0, 1).normalized(), is(new Vector2D(0, 1)));
        assertThat(new Vector2D(0, -1).normalized(), is(new Vector2D(0, -1)));
        assertThat(new Vector2D(1, 1).normalized(), is(new Vector2D(1 / Math.sqrt(2), 1 / Math.sqrt(2))));

        assertThat(new Vector2D(55, 2).normalized(), is(new Vector2D(55 / Math.sqrt(3029), 2 / Math.sqrt(3029))));
    }

    @Test
    public void testScale() {
        assertThat(new Vector2D(5, 5).scale(5), is(new Vector2D(25, 25)));
        assertThat(new Vector2D(5, 5).scale(0), is(new Vector2D(0, 0)));
        assertThat(new Vector2D(-2, 7).scale(2), is(new Vector2D(-4, 14)));
        assertThat(new Vector2D(-2, 8).scale(-3), is(new Vector2D(6, -24)));
    }

    @Test
    public void testRotate() {
        double e = 0.0000001;
        assertThat(new Vector2D(1, 0).rotate(Math.PI).x, both(greaterThan(-1.0 - e)).and(lessThan(-1.0 + e)));
        assertThat(new Vector2D(1, 0).rotate(Math.PI).y, both(greaterThan(0.0 - e)).and(lessThan(0.0 + e)));

        assertThat(new Vector2D(1, 0).rotate(-Math.PI).x, both(greaterThan(-1.0 - e)).and(lessThan(-1.0 + e)));
        assertThat(new Vector2D(1, 0).rotate(-Math.PI).y, both(greaterThan(0.0 - e)).and(lessThan(0.0 + e)));

        assertThat(new Vector2D(1, 0).rotate(Math.PI / 2.0).x, both(greaterThan(0.0 - e)).and(lessThan(0.0 + e)));
        assertThat(new Vector2D(1, 0).rotate(Math.PI / 2.0).y, both(greaterThan(1.0 - e)).and(lessThan(1.0 + e)));

        assertThat(new Vector2D(1, 0).rotate(-Math.PI / 2.0).x, both(greaterThan(0.0 - e)).and(lessThan(0.0 + e)));
        assertThat(new Vector2D(1, 0).rotate(-Math.PI / 2.0).y, both(greaterThan(-1.0 - e)).and(lessThan(-1.0 + e)));


    }

    @Test
    public void testSubtract() {
        assertThat(new Vector2D(20, 20).subtract(new Vector2D(15, 15)), is(new Vector2D(5, 5)));
        assertThat(new Vector2D(0, 0).subtract(new Vector2D(23, 0)), is(new Vector2D(-23, 0)));
        assertThat(new Vector2D(0, 0).subtract(new Vector2D(0, 0)), is(new Vector2D(0, 0)));
        assertThat(new Vector2D(1, 5).subtract(new Vector2D(1, 5)), is(new Vector2D(0, 0)));
        assertThat(new Vector2D(1.1, 1.1).subtract(new Vector2D(2, 2)), is(new Vector2D(1.1 - 2, 1.1 - 2)));
    }

    @Test
    public void testAdd() {
        assertThat(new Vector2D(20, 20).add(new Vector2D(15, 15)), is(new Vector2D(35, 35)));
        assertThat(new Vector2D(-23, 0).add(new Vector2D(46, 0)), is(new Vector2D(23, 0)));
        assertThat(new Vector2D(0, 0).add(new Vector2D(0, 0)), is(new Vector2D(0, 0)));
        assertThat(new Vector2D(-1, -5).add(new Vector2D(1, 5)), is(new Vector2D(0, 0)));
        assertThat(new Vector2D(-1.1, -1.1).add(new Vector2D(2, 2)), is(new Vector2D(-1.1 + 2, -1.1 + 2)));
    }
}