package org.binu.ai.boardscoring;

/**
 * Score calculator for possible moves based on current board position.
 */
interface BoardScoreCalculator {

    int calculateColumnScore(int column);
}
