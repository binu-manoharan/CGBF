package org.binu.ai;

import org.binu.board.BlockQueue;
import org.binu.board.Board;
import org.binu.board.Cell;

/**
 * Simple AI with column scoring
 */
class SimpleGameAI extends GameAI {
    SimpleGameAI(Board board, BlockQueue blockQueue) {
        super(board, blockQueue);
    }

    /** {@inheritDoc} */
    public int calculateNextMove() {
        final int columnMatch = bestColumnMatch();
        return columnMatch;
    }

    private int bestColumnMatch() {
        int highestColoumnScore = -10;
        int highestScoreIndex = -1;

        for (int i = 0; i < Board.COLUMN_LENGTH; i++) {
            final Cell[] column = board.getColumn(i);
            final int columnScore = cellArrayParser.getCellArrayScore(column, blockQueue.getNext());

            if (columnScore > highestColoumnScore) {
                highestColoumnScore = columnScore;
                highestScoreIndex = i;
            }
        }

        assert highestScoreIndex != -1;
        return highestScoreIndex;
    }
}
