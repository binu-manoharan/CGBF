package org.binu.ai.boardscoring;

import org.binu.ai.simple.CellArrayHelper;
import org.binu.board.Board;

/**
 * Simple board collapser implementation.
 */
class BoardCollapserImpl implements IBoardCollapser {

    private CellArrayHelper cellArrayHelper;
    private BoardClearerImpl boardClearer;

    public BoardCollapserImpl(CellArrayHelper cellArrayHelper) {
        this.cellArrayHelper = cellArrayHelper;
        boardClearer = new BoardClearerImpl(cellArrayHelper);
    }

    /** {@inheritDoc} */
    @Override
    public Board collapseBoard(Board board) {
        final long startTime = System.nanoTime();

        final Board clearedBoard = boardClearer.clearBoard(board);

        for (int col = 0; col < Board.COLUMN_LENGTH; col++) {
            for (int row = 0; row < Board.ROW_LENGTH; row++) {

            }
        }

        final long endTime = System.nanoTime();
        System.out.println(endTime - startTime);
        return clearedBoard;
    }
}
