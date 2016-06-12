package org.binu.framework;

/**
 * Helper to get random values
 */
public class RandomValueGenerator {

    private static final int OFFSET_VALUE_TO_ROUND_UP = 1;

    public int getRandomValue(int minValue, int maxValue) {
        final double random = Math.random();
        final double randomValue = random * (maxValue - minValue + OFFSET_VALUE_TO_ROUND_UP);
        return (int) (minValue + randomValue);
    }
}
