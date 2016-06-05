package org.binu.framework;

import org.binu.board.Block;
import org.binu.board.Board;
import org.binu.board.Cell;

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
     * Collapses any empty cells that exists between other types of cells
     *
     * @param cells cell array to parse
     * @return collapsed cell array
     */
    Cell[] collapseEmptyCells(Cell[] cells);

    /**
     * Collapse empty cells on a board
     *
     * @param board 2d cell array to collapse empty cells on
     */
    void collapseEmptyCells(Board board);

    /**
     * Drop block into cell[]
     *
     * @param cells cells to drop into
     * @param block blocks to drop into the cell
     * @return dropped cell array
     */
    Cell[] dropBlockIntoColumn(Cell[] cells, Block block);

    /**
     * Is there space to fit in the block
     *
     * @param board       board against which is droppable is checked
     * @param block       block that needs to be placed on the board
     * @param columnIndex index of the column to drop on  @return true if there is space to drop
     */
    boolean blockIsDroppableOnColumn(Board board, Block block, int columnIndex);

    /**
     * Drop the block into the board at a certain index
     *
     * @param board       board on which the block is dropped
     * @param block       block to be dropped
     * @param columnIndex column index where the block is dropped
     * @return true if the column was dropped successful, false when there is no space to drop it
     */
    boolean dropBlockIntoBoard(Board board, Block block, int columnIndex);

    /**
     * Returns int [] containing two values which is the x and y within the 2d array for an element with the first
     * repeated group block
     *
     * @param cellArray 2d array
     * @return x and y containing the repeated groups
     */
    List<int[]> getIndexOf4BlockGroup(Cell[][] cellArray);

    /**
     * Get cell indexes of L and T formations
     *
     * @param cellArray cellArray to parse
     * @return list of x and y co-ordinates of at least a cell within the shape
     */
    List<int[]> getIndexOfLAndT(Cell[][] cellArray);

    /**
     * Get cell indexes of Z formations
     *
     * @param cellArray cellArray to parse
     * @return list of x and y co-ordinates of at least a cell within the shape
     */
    List<int[]> getIndexOfZ(Cell[][] cellArray);

    /**
     * Get cell indexes of line formations
     *
     * @param cellArray cellArray to parse
     * @return list of x and y co-ordinates of at least a cell within the shape
     */
    List<int[]> getIndexOfLines(Cell[][] cellArray);

    /**
     * Get cell indexes of all formations
     *
     * @param cellArray cellArray to parse
     * @return list of x and y co-ordinates of at least a cell within the shape
     */
    List<int[]> getIndexesOfAllShapes(Cell[][] cellArray);

    /**
     * does the board contain any desirable shape
     *
     * @param board board to check the status for
     * @return true if the board contains any of the shapes
     */
    boolean isClearable(Board board);
}
