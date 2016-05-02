package org.binu.ai;

import org.binu.board.BlockQueue;
import org.binu.board.Board;

/**
 * Common AI features.
 */
abstract class GameAI implements IGameAI {
    Board board;
    BlockQueue blockQueue;

    public GameAI(Board board, BlockQueue blockQueue) {
        this.board = board;
        this.blockQueue = blockQueue;
    }
}
