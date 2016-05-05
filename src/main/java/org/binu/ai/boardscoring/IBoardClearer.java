package org.binu.ai.boardscoring;

import org.binu.board.Board;

/**
 * Clears board pieces that make 4 and ones surrounding it.
 */
public interface IBoardClearer {
    /**
     * Clear the board based on the 4 together rule.
     * @param board board to parse
     * @return parsedBoard
     */
    Board clearBoard(Board board);

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

    /**
     * Empty out cells of a board checking for 4 square match
     * @param board board to parse
     * @return Board
     */
    Board clearBoardBySquare(Board board);
}
