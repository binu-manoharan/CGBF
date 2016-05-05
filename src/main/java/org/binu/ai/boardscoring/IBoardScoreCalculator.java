package org.binu.ai.boardscoring;

/**
 * Score calculator for possible moves based on current board position.
 */
public interface IBoardScoreCalculator {

    public int calculateColumnScore(int column);
}
