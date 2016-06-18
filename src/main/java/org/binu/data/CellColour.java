package org.binu.data;

/**
 * Cell Colours for occupied rows.
 */
public enum CellColour {
    BLUE,
    GREEN,
    PURPLE,
    RED,
    YELLOW;

    public static CellColour getEquivalentColour(int colourIndex) {
        switch (colourIndex) {
            case 1:
                return BLUE;
            case 2:
                return GREEN;
            case 3:
                return PURPLE;
            case 4:
                return RED;
            case 5:
                return YELLOW;
            default:
                return null;
        }
    }
}
