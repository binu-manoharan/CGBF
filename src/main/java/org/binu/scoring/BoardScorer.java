package org.binu.scoring;

import org.binu.board.Board;

/**
 * Scores the board
 */
public interface BoardScorer {

    /**
     * //TODO Invalidated
     * Calculate score for the given board
     *
     * @param board board to compute score for
     * @return score value
     */
    int scoreBoard(Board board);
}
