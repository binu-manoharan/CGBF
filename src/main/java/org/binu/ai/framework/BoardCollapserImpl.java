package org.binu.ai.framework;

import org.binu.board.Board;

/**
 * Simple board collapser implementation.
 */
public class BoardCollapserImpl implements BoardCollapser {

    private CellArrayHelper cellArrayHelper;
    private BoardClearerImpl boardClearer;

    public BoardCollapserImpl(CellArrayHelper cellArrayHelper) {
        this.cellArrayHelper = cellArrayHelper;
        boardClearer = new BoardClearerImpl(cellArrayHelper);
    }

    /** {@inheritDoc} */
    @Override
    public Board collapseBoard(Board board) {
        boardClearer.clearBoard(board);
        cellArrayHelper.collapseEmptyCells(board);
        return board;
    }
}
