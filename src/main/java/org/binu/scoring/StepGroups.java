package org.binu.scoring;

/**
 * Group for each step
 *
 * A step can have two colours disappear at the same time. This would impact
 * colour bonus and group bonus.
 */
public class StepGroups {
    //TODO Invalidated
    /**
     * Group bonus for this step
     * <p>
     * <ul>
     * <li>4 blocks gives 0</li>
     * <li>5 blocks gives 1</li>
     * <li>6 blocks gives 2</li>
     * <li>7 blocks gives 3</li>
     * <li>8 blocks gives 4</li>
     * <li>9 blocks gives 5</li>
     * <li>10 blocks gives 6</li>
     * <li>11 blocks gives 8</li>
     * </ul>
     */
    private int groupBonus = 0;
}
