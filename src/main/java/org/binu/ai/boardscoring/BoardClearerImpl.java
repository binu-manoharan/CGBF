package org.binu.ai.boardscoring;

import org.binu.ai.simple.CellArrayHelper;
import org.binu.board.Board;
import org.binu.board.Cell;
import org.binu.data.CellColour;
import org.binu.data.CellStatus;

import java.util.List;

/**
 * Simple board clearer implementation.
 */
public class BoardClearerImpl implements IBoardClearer {

    private CellArrayHelper cellArrayHelper;

    public BoardClearerImpl(CellArrayHelper cellArrayHelper) {
        this.cellArrayHelper = cellArrayHelper;
    }

    @Override
    public Board clearBoard(Board board) {
        final Board lineClearedBoard = clearBoardByLine(board);
        final Board squareClearedBoard = clearBoardBySquare(lineClearedBoard);
        final Board lAndTClearedBoard = clearBoardByTAndL(squareClearedBoard);
        final Board zClearedBoard = clearBoardByZ(lAndTClearedBoard);
        return zClearedBoard;
    }

    @Override
    public Board clearBoardByLine(Board board) {
        final List<int[]> cellList = cellArrayHelper.getIndexOfLines(board.getBoard());
        Board clearedLineBoard = board;
        for (int[] cell : cellList) {
            clearedLineBoard = clearFromCell(cell[0], cell[1], board);
        }
        return clearedLineBoard;
    }

    @Override
    public Board clearBoardBySquare(Board board) {
        final List<int[]> cellList = cellArrayHelper.getIndexOf4BlockGroup(board.getBoard());
        Board clearedSquareBoard = board;
        for (int[] cell : cellList) {
            clearedSquareBoard = clearFromCell(cell[0], cell[1], board);
        }
        return clearedSquareBoard;
    }

    @Override
    public Board clearBoardByTAndL(Board board) {
        final List<int[]> cellList = cellArrayHelper.getIndexOfLAndT(board.getBoard());
        Board clearedTLBoard = board;
        for (int[] cell : cellList) {
            clearedTLBoard = clearFromCell(cell[0], cell[1], board);
        }
        return clearedTLBoard;
    }

    @Override
    public Board clearBoardByZ(Board board) {
        final List<int[]> cellList = cellArrayHelper.getIndexOfZ(board.getBoard());
        Board clearedZBoard = board;
        for (int[] cell : cellList) {
            clearedZBoard = clearFromCell(cell[0], cell[1], board);
        }
        return clearedZBoard;
    }

    private Board clearFromCell(int rowId, int columnId, Board board) {
        final Cell collapsingCell = board.getCell(rowId, columnId);
        final CellColour cellColour = collapsingCell.getCellColour();

        final Board clearedBoard = clearCellAndAround(rowId, columnId, board, cellColour);

        return clearedBoard;
    }

    private Board clearCellAndAround(int rowId, int columnId, Board board, CellColour cellColour) {
        if (rowId >= 0 && rowId < Board.ROW_LENGTH && columnId >= 0 && columnId < Board.COLUMN_LENGTH && cellColour != null) {
            final Cell cell = board.getCell(rowId, columnId);
            final CellStatus currentCellStatus = cell.getCellStatus();
            final CellColour currentCellColour = cell.getCellColour();
            if (cellColour == currentCellColour || currentCellStatus == CellStatus.BLOCKED) {
                board.setCell(rowId, columnId, CellStatus.EMPTY, null);
                if (currentCellColour == cellColour) {
                    clearSurroundingCell(rowId, columnId, board, cellColour);
                }
            }
        }
        return board;
    }

    private Board clearSurroundingCell(int rowId, int columnId, Board board, CellColour cellColour) {
        clearCellAndAround(rowId - 1, columnId, board, cellColour);
        clearCellAndAround(rowId, columnId - 1, board, cellColour);
        clearCellAndAround(rowId, columnId + 1, board, cellColour);
        clearCellAndAround(rowId + 1, columnId, board, cellColour);
        return board;
    }
}
