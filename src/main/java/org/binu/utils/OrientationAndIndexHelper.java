package org.binu.utils;

import org.binu.data.Orientation;
import org.binu.data.OrientationAndIndex;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper functions for orientations.
 */
public class OrientationAndIndexHelper {

    private static final int MIN_VALUE = 0;
    private static final int MAX_VALUE = 21;
    private static final int MIN_ACCEPTED_VALUE = 0;
    private static final int VERTICAL_LIMIT = 6;
    private static final int VERTICAL_REV_LIMIT = 12;
    private static final int HORIZONTAL_LIMIT = 17;
    private static final int HORIZONTAL_REV_LIMIT = 22;
    private static final int HORIZONTAL_REV_OFFSET = 1;

    public List<OrientationAndIndex> getAllOrientationAndIndexCombinations() {
        final ArrayList<OrientationAndIndex> orientationAndIndices = new ArrayList<>();
        for (int counter = 0; counter < 22; counter++) {
            OrientationAndIndex orientationAndIndex = null;
            if (counter < VERTICAL_LIMIT) {
                orientationAndIndex = new OrientationAndIndex(Orientation.VERTICAL, counter);
            } else if (counter < VERTICAL_REV_LIMIT) {
                orientationAndIndex = new OrientationAndIndex(Orientation.VERTICAL_REVERSED, counter - VERTICAL_LIMIT);
            } else if (counter < HORIZONTAL_LIMIT) {
                orientationAndIndex = new OrientationAndIndex(Orientation.HORIZONTAL, counter - VERTICAL_REV_LIMIT);
            } else if (counter < HORIZONTAL_REV_LIMIT) {
                orientationAndIndex = new OrientationAndIndex(Orientation.HORIZONTAL_REVERSED, counter - HORIZONTAL_LIMIT + HORIZONTAL_REV_OFFSET);
            }
            orientationAndIndices.add(orientationAndIndex);
        }
        return orientationAndIndices;
    }
}
