package org.binu.ai.framework;

import org.binu.board.Board;

/**
 * Simple board collapser implementation.
 */
public class BoardCollapserImpl implements BoardCollapser {

    private CellArrayHelper cellArrayHelper;
    private ChainClearer chainClearer;

    public BoardCollapserImpl(CellArrayHelper cellArrayHelper) {
        this.cellArrayHelper = cellArrayHelper;
        chainClearer = new ChainClearerImpl(cellArrayHelper);
    }

    /** {@inheritDoc} */
    @Override
    public Board collapseBoard(Board board) {
        chainClearer.clearBoard(board);
        cellArrayHelper.collapseEmptyCells(board);
        return board;
    }
}
