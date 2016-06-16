package org.binu.framework;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test for {@link OrientationHelper}
 */
public class OrientationHelperTest {
    @Test
    public void should_return_vertical_orientation() throws Exception {
        OrientationHelper orientationHelper = new OrientationHelper();
        orientationHelper.getRandomOrientationWithDropIndex();


    }
}
