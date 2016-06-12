package org.binu.ai.shinynew;

/**
 * Helper to get random values
 */
public class RandomValueGenerator {

    private static final int OFFSET_VALUE_TO_ROUND_UP = 1;

    public int getRandomValueBetween(int minValue, int maxValue) {
        final double random = Math.random();
        final double randomValue = random * (maxValue - minValue + OFFSET_VALUE_TO_ROUND_UP);
        System.out.println(random + " " + randomValue);
        return (int) (minValue + randomValue);
    }
}
