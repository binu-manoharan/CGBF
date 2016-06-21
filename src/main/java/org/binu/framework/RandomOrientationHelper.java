package org.binu.framework;

import org.binu.data.Orientation;

/**
 * RandomOrientationHelper uses a random value generator to determine the random orientation and index
 * for the next move.
 * 0 -> horizontal (0 <= col index < 5) [rand values 12-16]
 * 1 -> vertical reversed [rand values 6-11]
 * 2 -> horizontal reversed (1 <= col index < 6) [rand values 17-22]
 * 3 -> vertical [rand values 0-5]
 */
public class RandomOrientationHelper {

    private static final int MIN_VALUE = 0;
    private static final int MAX_VALUE = 21;
    private static final int MIN_ACCEPTED_VALUE = 0;
    private static final int VERTICAL_LIMIT = 6;
    private static final int VERTICAL_REV_LIMIT = 12;
    private static final int HORIZONTAL_LIMIT = 17;
    private static final int HORIZONTAL_REV_LIMIT = 22;
    private static final int HORIZONTAL_REV_OFFSET = 1;

    public OrientationAndIndex getRandomOrientationWithDropIndex() {
        final RandomValueGenerator randomValueGenerator = new RandomValueGenerator();
        final int randomValue = randomValueGenerator.getRandomValue(MIN_VALUE, MAX_VALUE);
        return getOrientationAndIndexForValue(randomValue);
    }

    public OrientationAndIndex getOrientationAndIndexForValue(int value) {
        OrientationAndIndex orientationAndIndex = null;
        if (value < MIN_ACCEPTED_VALUE) {
            assert false : "Random value should not be less than 0";
        } else if (value < VERTICAL_LIMIT) {
            orientationAndIndex = new OrientationAndIndex(Orientation.VERTICAL, value);
        } else if (value < VERTICAL_REV_LIMIT) {
            orientationAndIndex = new OrientationAndIndex(Orientation.VERTICAL_REVERSED, value - VERTICAL_LIMIT);
        } else if (value < HORIZONTAL_LIMIT) {
            orientationAndIndex = new OrientationAndIndex(Orientation.HORIZONTAL, value - VERTICAL_REV_LIMIT);
        } else if (value < HORIZONTAL_REV_LIMIT) {
            orientationAndIndex = new OrientationAndIndex(Orientation.HORIZONTAL_REVERSED, value - HORIZONTAL_LIMIT + HORIZONTAL_REV_OFFSET);
        } else {
            assert false: "Random value should not be more than 21";
        }
        return orientationAndIndex;
    }
}
