package org.binu.framework;

import org.binu.data.Orientation;

/**
 * Created by binu on 16/06/16.
 */
public class OrientationHelper {

    private static final int MIN_VALUE = 0;
    private static final int MAX_VALUE = 5;

    public OrientationAndIndex getRandomOrientationWithDropIndex() {
        RandomValueGenerator randomValueGenerator = new RandomValueGenerator();
        final int randomValue = randomValueGenerator.getRandomValue(MIN_VALUE, MAX_VALUE);
        return null;
    }

    public class OrientationAndIndex {
        Orientation orientation;
        int nodeIndex;
    }
}
