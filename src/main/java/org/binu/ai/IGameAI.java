package org.binu.ai;

/**
 * Game AI class.
 */
interface IGameAI {

    /**
     * Calculate the next move to make
     * @return index of the column to play in
     */
    int calculateNextMove();
}
