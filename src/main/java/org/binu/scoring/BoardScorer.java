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
     * @return score value
     */
    int scoreBoardAndRecursivelyClearAndCollapse(Board board);
}
