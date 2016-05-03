package org.binu.ai;

import org.binu.board.BlockQueue;
import org.binu.board.Board;

/**
 * GameAI capable of scoring the board.
 */
public class BoardScoringGameAI extends GameAI {
    public BoardScoringGameAI(Board board, BlockQueue blockQueue) {
        super(board, blockQueue);
    }

    @Override
    public int calculateNextMove() {
        return 0;
    }
}
