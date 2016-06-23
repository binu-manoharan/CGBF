package org.binu.utils;

import org.binu.data.OrientationAndIndex;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

/**
 * Test for {@link OrientationAndIndexHelper}
 */
public class OrientationAndIndexHelperTest {
    @Test
    public void should_return_all_orientation_and_index_combinations() throws Exception {
        final OrientationAndIndexHelper orientationAndIndexHelper = new OrientationAndIndexHelper();
        final List<OrientationAndIndex> allOrientationAndIndexCombinations = orientationAndIndexHelper.getAllOrientationAndIndexCombinations();

        assertThat("All orientations and index combinations is not null", allOrientationAndIndexCombinations, is(notNullValue()));
        assertThat("Has 22 combinations of orientation and index", allOrientationAndIndexCombinations.size(), is(22));
    }
}
