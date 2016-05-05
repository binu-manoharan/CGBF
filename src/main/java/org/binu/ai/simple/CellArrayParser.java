package org.binu.ai.simple;

import org.binu.board.Block;
import org.binu.board.Cell;
import org.jetbrains.annotations.Nullable;

/**
 * Logic for parsing a cell array.
 */
public interface CellArrayParser {
    int getFirstEmptyPosition(Cell[] cells);

    /**
     * Get Array score. This probably doesn't belong to this method.
     * @param column cell array
     * @param block block that is being checked against the column
     * @return score for the block landing on the column
     */
    int getCellArrayScore(Cell[] column, @Nullable Block block);


    /**
     * Returns index of an element which forms four of the same colour
     * @param cells cell array to check for repeated group
     * @return max index of an element within the group
     */
    int getFirstIndexOfRepeatOf4Group(Cell[] cells);
}