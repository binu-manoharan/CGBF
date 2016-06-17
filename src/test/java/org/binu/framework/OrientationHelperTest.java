package org.binu.framework;

import org.binu.data.Orientation;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * 0 -> horizontal (0 <= col index < 5) [rand values 12-16]
 * 1 -> vertical reversed [rand values 6-11]
 * 2 -> horizontal reversed (1 <= col index < 6) [rand values 17-22]
 * 3 -> vertical [rand values 0-5]
 * Test for {@link OrientationHelper}
 */
public class OrientationHelperTest {

    private OrientationHelper orientationHelper;
    private OrientationAndIndex orientationAndIndex;

    @Before
    public void setUp() throws Exception {
        orientationHelper = new OrientationHelper();
    }

    @Test
    public void should_return_vertical_orientation_for_random_index_of_0() throws Exception {
        orientationAndIndex = orientationHelper.getOrientationAndIndexForValue(0);
        final int nodeIndex = orientationAndIndex.getNodeIndex();
        final Orientation orientation = orientationAndIndex.getOrientation();

        assertThat("The orientation should be vertical", orientation, is(Orientation.VERTICAL));
        assertThat("The nodeIndex should be 0", nodeIndex, is(0));
    }

    @Test
    public void should_return_vertical_orientation_for_random_index_of_5() throws Exception {
        orientationAndIndex = orientationHelper.getOrientationAndIndexForValue(5);
        final int nodeIndex = orientationAndIndex.getNodeIndex();
        final Orientation orientation = orientationAndIndex.getOrientation();

        assertThat("The orientation should be vertical", orientation, is(Orientation.VERTICAL));
        assertThat("The nodeIndex should be 0", nodeIndex, is(5));
    }

    @Test
    public void should_return_vertical_rev_orientation_for_random_index_of_6() throws Exception {
        orientationAndIndex = orientationHelper.getOrientationAndIndexForValue(6);
        final int nodeIndex = orientationAndIndex.getNodeIndex();
        final Orientation orientation = orientationAndIndex.getOrientation();

        assertThat("The orientation should be vertical", orientation, is(Orientation.VERTICAL_REVERSED));
        assertThat("The nodeIndex should be 0", nodeIndex, is(0));
    }

    @Test
    public void should_return_vertical_orientation_for_random_index_of_11() throws Exception {
        orientationAndIndex = orientationHelper.getOrientationAndIndexForValue(11);
        final int nodeIndex = orientationAndIndex.getNodeIndex();
        final Orientation orientation = orientationAndIndex.getOrientation();

        assertThat("The orientation should be vertical", orientation, is(Orientation.VERTICAL_REVERSED));
        assertThat("The nodeIndex should be 0", nodeIndex, is(5));
    }

    @Test
    public void should_return_horizontal_orientation_for_random_index_of_12() throws Exception {
        orientationAndIndex = orientationHelper.getOrientationAndIndexForValue(12);
        final int nodeIndex = orientationAndIndex.getNodeIndex();
        final Orientation orientation = orientationAndIndex.getOrientation();

        assertThat("The orientation should be vertical", orientation, is(Orientation.HORIZONTAL));
        assertThat("The nodeIndex should be 0", nodeIndex, is(0));
    }

    @Test
    public void should_return_horizontal_orientation_for_random_index_of_16() throws Exception {
        orientationAndIndex = orientationHelper.getOrientationAndIndexForValue(16);
        final int nodeIndex = orientationAndIndex.getNodeIndex();
        final Orientation orientation = orientationAndIndex.getOrientation();

        assertThat("The orientation should be vertical", orientation, is(Orientation.HORIZONTAL));
        assertThat("The nodeIndex should be 0", nodeIndex, is(4));
    }

    @Test
    public void should_return_horizontal_rev_orientation_for_random_index_of_17() throws Exception {
        orientationAndIndex = orientationHelper.getOrientationAndIndexForValue(17);
        final int nodeIndex = orientationAndIndex.getNodeIndex();
        final Orientation orientation = orientationAndIndex.getOrientation();

        assertThat("The orientation should be vertical", orientation, is(Orientation.HORIZONTAL_REVERSED));
        assertThat("The nodeIndex should be 0", nodeIndex, is(1));
    }

    @Test
    public void should_return_horizontal_rev_orientation_for_random_index_of_21() throws Exception {
        orientationAndIndex = orientationHelper.getOrientationAndIndexForValue(21);
        final int nodeIndex = orientationAndIndex.getNodeIndex();
        final Orientation orientation = orientationAndIndex.getOrientation();

        assertThat("The orientation should be vertical", orientation, is(Orientation.HORIZONTAL_REVERSED));
        assertThat("The nodeIndex should be 0", nodeIndex, is(5));
    }
}
