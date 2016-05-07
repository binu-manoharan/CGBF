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

    //TODO add test for clearBoard()
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
        Board clearedSqareBoard = board;
        for (int[] cell : cellList) {
            clearedSqareBoard = clearFromCellAndSurroundingCells(cell[0], cell[1], board);
        }
        return clearedSqareBoard;
    }

    @Override
    public Board clearBoardBySquare(Board board) {
        final List<int[]> cellList = cellArrayHelper.getIndexOf4BlockGroup(board.getBoard());
        Board clearedSqareBoard = board;
        for (int[] cell : cellList) {
            clearedSqareBoard = clearFromCellAndSurroundingCells(cell[0], cell[1], board);
        }
        return clearedSqareBoard;
    }

    @Override
    public Board clearBoardByTAndL(Board board) {
        final List<int[]> cellList = cellArrayHelper.getIndexOfLAndT(board.getBoard());
        Board clearedTLBoard = board;
        for (int[] cell : cellList) {
            clearedTLBoard = clearFromCellAndSurroundingCells(cell[0], cell[1], board);
        }
        return clearedTLBoard;
    }

    @Override
    public Board clearBoardByZ(Board board) {
        final List<int[]> cellList = cellArrayHelper.getIndexOfZ(board.getBoard());
        Board clearedZBoard = board;
        for (int[] cell : cellList) {
            clearedZBoard = clearFromCellAndSurroundingCells(cell[0], cell[1], board);
        }
        return clearedZBoard;
    }

    private Board clearFromCellAndSurroundingCells(int rowId, int columnId, Board board) {
        final Cell collapsingCell = board.getCell(rowId, columnId);
        final CellColour cellColour = collapsingCell.getCellColour();

        final Board clearedBoard = clearSurroundingCell(rowId, columnId, board, cellColour);

        return clearedBoard;
    }

    private Board clearSurroundingCell(int rowId, int columnId, Board board, CellColour cellColour) {
        clearCell(rowId, columnId, board, cellColour);
        clearCell(rowId - 1, columnId, board, cellColour);
        clearCell(rowId, columnId - 1, board, cellColour);
        clearCell(rowId, columnId + 1, board, cellColour);
        clearCell(rowId + 1, columnId, board, cellColour);
        return board;
    }

    private Board clearCell(int rowId, int columnId, Board board, CellColour cellColour) {
        if (rowId >= 0 && rowId < Board.ROW_LENGTH && columnId >= 0 && columnId < Board.COLUMN_LENGTH) {
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
}
