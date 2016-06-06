package org.binu.scoring;

import org.binu.board.Board;

/**
 * Scores the board
 */
public interface BoardScorer {

    /**
     * Calculate score for the given board
     *
     * @param board board to compute score for
     * @param updateBoard if to work on a copy or update the board which passed by ref
     * @return score value
     */
    int scoreBoardAndRecursivelyClearAndCollapse(Board board, boolean updateBoard);
}
