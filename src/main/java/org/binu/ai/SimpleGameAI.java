package org.binu.ai;

import org.binu.board.BlockQueue;
import org.binu.board.Board;
import org.binu.board.Cell;

/**
 * Simple AI
 */
class SimpleGameAI extends GameAI {
    SimpleGameAI(Board board, BlockQueue blockQueue) {
        super(board, blockQueue);
    }

    /** {@inheritDoc} */
    public int calculateNextMove() {
        int highestEmptyPosition = 0;
        int highestEmptyIndex = 0;
        for (int i = 0; i < Board.COLUMN_LENGTH; i++) {
            Cell[] column = board.getColumn(i);
            int firstEmptyPosition = cellArrayParser.getFirstEmptyPosition(column);

            if (firstEmptyPosition > highestEmptyPosition) {
                highestEmptyPosition = firstEmptyPosition;
                highestEmptyIndex = i;
            }
        }
        return highestEmptyIndex;
    }
}
