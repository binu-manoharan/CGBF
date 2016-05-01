package org.binu.ai;

import org.binu.board.Board;

/**
 * Common AI features.
 */
abstract class GameAI implements IGameAI {
    Board board;

    public GameAI(Board board) {
        this.board = board;
    }
}
