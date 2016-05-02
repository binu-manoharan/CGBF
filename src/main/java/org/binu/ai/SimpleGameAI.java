package org.binu.ai;

import org.binu.board.BlockQueue;
import org.binu.board.Board;

/**
 * Simple AI
 */
class SimpleGameAI extends GameAI {
    SimpleGameAI(Board board, BlockQueue blockQueue) {
        super(board, blockQueue);
    }

    /** {@inheritDoc} */
    public int calculateNextMove() {
        for (int i = 0; i < Board.COLUMN_LENGTH; i++) {
            board.getColumn(i);
        }
        return 0;
    }
}
