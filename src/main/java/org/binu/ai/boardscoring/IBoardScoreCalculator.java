package org.binu.ai.boardscoring;

/**
 * Score calculator for possible moves based on current board position.
 */
interface IBoardScoreCalculator {

    int calculateColumnScore(int column);
}
