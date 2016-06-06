package org.binu.framework;

import org.binu.board.Board;

/**
 * Simple board collapser implementation.
 */
public class BoardCollapserImpl implements BoardCollapser {

    private CellArrayHelper cellArrayHelper;

    public BoardCollapserImpl(CellArrayHelper cellArrayHelper) {
        this.cellArrayHelper = cellArrayHelper;
    }

    /** {@inheritDoc} */
    @Override
    public Board collapseBoard(Board board) {
        cellArrayHelper.collapseEmptyCells(board);
        return board;
    }
}
