package org.binu.ai;

import org.binu.board.BlockQueue;
import org.binu.board.Board;

/**
 * Common AI features.
 */
public abstract class GameAI implements IGameAI {
    protected Board board;
    protected BlockQueue blockQueue;


    public GameAI(Board board, BlockQueue blockQueue) {
        this.board = board;
        this.blockQueue = blockQueue;


    }
}
