package org.binu.ai.shinynew;

import org.binu.ai.GameAI;
import org.binu.board.BlockQueue;
import org.binu.board.Board;

/**
 * Shiny!!!
 */
public class ShinyNewGameAI extends GameAI{
    public ShinyNewGameAI(Board board, BlockQueue blockQueue) {
        super(board, blockQueue);
    }

    @Override
    public int calculateNextMove() {
        return 0;
    }
}
