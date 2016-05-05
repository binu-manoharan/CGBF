package org.binu.ai.simple;

import org.binu.board.Block;
import org.binu.board.Board;
import org.binu.board.Cell;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Logic for parsing a cell array.
 */
public interface CellArrayHelper {

    /**
     * Returns index of the first non empty position in the cell array where new block can sit
     *
     * @param cells cell array to check empty position for
     * @return index position
     */
    int getFirstEmptyPosition(Cell[] cells);

    /**
     * Get Array score. This probably doesn't belong to this method.
     *
     * @param column cell array
     * @param block  block that is being checked against the column
     * @return score for the block landing on the column
     */
    int getCellArrayScore(Cell[] column, @Nullable Block block);


    /**
     * Returns index of an element which forms four of the same colour
     *
     * @param cells cell array to check for repeated group
     * @return max index of an element within the group
     */
    int getFirstIndexOfRepeatOf4Group(Cell[] cells);

    /**
     * Returns int [] containing two values which is the x and y within the 2d array for an element with the first
     * repeated group block
     *
     * @param cellArray 2d array
     * @return x and y containing the repeated groups
     */
    List<int[]> getIndexOf4BlockGroup(Cell[][] cellArray);

    /**
     * Collapses any empty cells that exists between other types of cells
     * @param cells cell array to parse
     * @return collapsed cell array
     */
    Cell[] collapseEmptyCells(Cell[] cells);

    /**
     * Collapse empty cells on a board
     * @param board 2d cell array
     * @return collapsed board
     */
    Board collapseEmptyCells(Board board);

    /**
     * Drop block into cell[]
     * @param cells cells to drop into
     * @param block blocks to drop into the cell
     * @return dropped cell array
     */
    Cell[] dropBlockIntoColumn(Cell[] cells, Block block);
}
