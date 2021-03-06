package org.binu.framework;

import org.binu.board.Board;

/**
 * Collapses a board where there is a block of 4 colours together.
 * This formation can be either vertical or horizontal or a square of 4.
 */
public interface BoardCollapser {
    /**
     * Collapse the board based on the 4 together rule.
     * @param board board to parse
     * @return parsedBoard
     */
    Board collapseBoard(Board board);
}
