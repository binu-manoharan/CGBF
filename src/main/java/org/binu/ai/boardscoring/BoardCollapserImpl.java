package org.binu.ai.boardscoring;

import org.binu.ai.simple.CellArrayParser;
import org.binu.board.Board;
import org.binu.board.Cell;
import org.binu.data.CellColour;
import org.binu.data.CellStatus;

/**
 * Simple board collapser implementation.
 */
class BoardCollapserImpl implements IBoardCollapser {

    CellArrayParser cellArrayParser;

    public BoardCollapserImpl(CellArrayParser cellArrayParser) {
        this.cellArrayParser = cellArrayParser;
    }

    /** {@inheritDoc} */
    @Override
    public Board collapseBoard(Board board) {
        final int rowId = 0;
        final Cell[] row = board.getRow(rowId);
        final int columnId = cellArrayParser.getFirstIndexOfRepeatOf4Group(row);
        final Board collapsedBoard = clearFromCell(rowId, columnId, board);
        return collapsedBoard;
    }

    private Board clearFromCell(int rowId, int columnId, Board board) {
        final Cell collapsingCell = board.getCell(rowId, columnId);
        final CellColour cellColour = collapsingCell.getCellColour();

        final Board clearedBoard = clearSurroundingCell(rowId, columnId, board, cellColour);

        return clearedBoard;
    }

    private Board clearSurroundingCell(int rowId, int columnId, Board board, CellColour cellColour) {
        clearCell(rowId, columnId, board, cellColour);
//        clearCell(rowId - 1, columnId, board, cellColour);
        clearCell(rowId, columnId - 1, board, cellColour);
        clearCell(rowId, columnId + 1, board, cellColour);
        clearCell(rowId + 1, columnId, board, cellColour);
        return board;
    }

    private Board clearCell(int rowId, int columnId, Board board, CellColour cellColour) {
        if (rowId >= 0 && rowId < Board.ROW_LENGTH && columnId >= 0 && columnId < Board.COLUMN_LENGTH) {
            final Cell cell = board.getCell(rowId, columnId);
            final CellStatus cellStatus = cell.getCellStatus();
            if (cellColour == cell.getCellColour() || cellStatus == CellStatus.BLOCKED) {
                board.setCell(rowId, columnId, CellStatus.EMPTY, null);
                if (cellStatus != CellStatus.BLOCKED) {
                    clearSurroundingCell(rowId, columnId, board, cellColour);
                }
            }
        }
        return board;
    }
}
