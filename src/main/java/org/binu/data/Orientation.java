package org.binu.data;

/**
 * Enum for Rotation of the block
 * <ul>
 * <li>HORIZONTAL 1 -> 4</li>
 * <li>VERTICAL_REVERSED 4 below 1</li>
 * <li>HORIZONTAL_REVERSED 4 -> 1</li>
 * <li>VERTCIAL 1 below 4</li>
 * </ul>
 */
public enum Orientation {
    HORIZONTAL,
    VERTICAL_REVERSED,
    VERTICAL,
    HORIZONTAL_REVERSED;

    public int getEquivalentInt() {
        switch (this) {
            case HORIZONTAL:
                return 0;
            case VERTICAL_REVERSED:
                return 1;
            case HORIZONTAL_REVERSED:
                return 2;
            case VERTICAL:
                return 3;
        }
        return 0;
    }
}
