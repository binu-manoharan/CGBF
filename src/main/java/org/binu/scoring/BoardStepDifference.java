package org.binu.scoring;

/**
 * Board step difference.
 */
public class BoardStepDifference {
    //TODO Invalidated
    /**
     * Number of blocks cleared this step
     */
    private int blocksCleared = 0;

    /**
     * Chain power from this step
     * <ul>
     * <li>First step 0</li>
     * <li>Second step 8</li>
     * <li>Second step 16</li>
     * <li>Third step 32</li>
     * <li>Fourth step 64</li>
     * </ul>
     */
    private int chainPower = 0;

    /**
     * <ul>
     * <li>Colour bonus for this step</li>
     * <li>One colour got cleared 0</li>
     * <li>Two colour got cleared 2</li>
     * <li>Three colour got cleared 4</li>
     * <li>Four colour got cleared 8</li>
     * <li>Fifth colour got cleared 16</li>
     * </ul>
     */
    private int colourBonus = 0;

    BoardStepDifference(int blocksCleared, int chainPower, int colourBonus) {
        this.blocksCleared = blocksCleared;
        this.chainPower = chainPower;
        this.colourBonus = colourBonus;
    }
}
