package org.binu.utils;

/**
 * Helper to control execution time of moves
 */
public class TimeHelper {

    private final long startTime;

    public TimeHelper() {
        startTime = System.currentTimeMillis();
    }

    public long getTimeSinceStartInMills() {
        final long currentTimeMillis = System.currentTimeMillis();
        return currentTimeMillis - startTime;
    }
}
