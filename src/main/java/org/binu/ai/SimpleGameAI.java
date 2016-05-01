package org.binu.ai;

import org.binu.board.Board;

/**
 * Simple AI
 */
class SimpleGameAI extends GameAI {
    SimpleGameAI(Board board) {
        super(board);
    }

    /** {@inheritDoc} */
    public int calculateNextMove() {
        canConnectFour();
        return 0;
    }

    private void canConnectFour() {
        for (int i = 0; i < Board.COLUMN_LENGTH; i++) {

        }
    }
}
