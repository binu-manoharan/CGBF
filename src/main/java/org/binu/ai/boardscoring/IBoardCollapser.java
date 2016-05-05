package org.binu.ai.boardscoring;

import org.binu.board.Board;

/**
 * Collapses a board where there is a block of 4 colours together.
 * This formation can be either vertical or horizontal or a square of 4.
 */
interface IBoardCollapser {
    /**
     * Collapse the board based on the 4 together rule.
     * @param board board to parse
     * @return parsedBoard
     */
    Board collapseBoard(Board board);

    /**
     * Empty out cells of the board checking for 4 match in column vertically
     * @param board board to parse
     * @return Board
     */
    Board clearBoardByColumn(Board board);

    /**
     * Empty out cells of the board checking for 4 match in row horizontally
     * @param board board to parse
     * @return Board
     */
    Board clearBoardByRow(Board board);
}
