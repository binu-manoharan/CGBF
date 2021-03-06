package org.binu.framework;

import org.binu.board.Board;

/**
 * Clears board pieces that make 4 and ones surrounding it.
 */
public interface ChainClearer {
    /**
     * Clear the board based on the 4 together rule.
     *
     * @param board board to parse
     */
    void clearBoard(Board board);

    /**
     * Empty out cells of the board checking for 4 match in column vertically
     *
     * @param board board to parse
     */
    void clearBoardByLine(Board board);

    /**
     * Empty out cells of a board checking for 4 square match
     *
     * @param board board to parse
     */
    void clearBoardBySquare(Board board);

    /**
     * Empty out cells making a 4 with a T or L shape
     *
     * @param board board to parse
     */
    void clearBoardByTAndL(Board board);

    /**
     * Empty out cells making a 4 with a Z shape
     *
     * @param board board to parse
     */
    void clearBoardByZ(Board board);

    /**
     * Board has at least one step to clear
     *
     * @param board board to check against
     * @return true if board has at least one item to clear
     */
    boolean isClearable(Board board);
}
