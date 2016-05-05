package org.binu.ai.boardscoring;

import org.binu.ai.simple.CellArrayParser;
import org.binu.board.Board;

/**
 * Simple board collapser implementation.
 */
class BoardCollapserImpl implements IBoardCollapser {

    private CellArrayParser cellArrayParser;
    private BoardClearerImpl boardClearer;

    public BoardCollapserImpl(CellArrayParser cellArrayParser) {
        this.cellArrayParser = cellArrayParser;
        boardClearer = new BoardClearerImpl(cellArrayParser);
    }

    /** {@inheritDoc} */
    @Override
    public Board collapseBoard(Board board) {
        final long startTime = System.nanoTime();

        final Board clearedBoard = boardClearer.clearBoard(board);

        final long endTime = System.nanoTime();
        System.out.println(endTime - startTime);
        return clearedBoard;
    }
}
