package org.binu.ai.framework;

import org.binu.board.Board;
import org.binu.board.Cell;
import org.binu.data.CellColour;
import org.binu.data.CellStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple board clearer implementation.
 */
public class ChainClearerImpl implements ChainClearer {

    private CellArrayHelper cellArrayHelper;

    public ChainClearerImpl(CellArrayHelper cellArrayHelper) {
        this.cellArrayHelper = cellArrayHelper;
    }

    @Override
    public void clearBoard(Board board) {
        clearBoardForAllShapes(board);
    }

    private void clearBoardForAllShapes(Board board) {
        final ArrayList<int[]> cellList = new ArrayList<>();
        cellList.addAll(cellArrayHelper.getIndexesOfAllShapes(board.getBoard()));
        for (int[] cell : cellList) {
            clearFromCell(cell[0], cell[1], board);
        }
    }

    @Override
    public void clearBoardByLine(Board board) {
        final List<int[]> cellList = cellArrayHelper.getIndexOfLines(board.getBoard());
        for (int[] cell : cellList) {
            clearFromCell(cell[0], cell[1], board);
        }
    }

    @Override
    public void clearBoardBySquare(Board board) {
        final List<int[]> cellList = cellArrayHelper.getIndexOf4BlockGroup(board.getBoard());
        for (int[] cell : cellList) {
            clearFromCell(cell[0], cell[1], board);
        }
    }

    @Override
    public void clearBoardByTAndL(Board board) {
        final List<int[]> cellList = cellArrayHelper.getIndexOfLAndT(board.getBoard());
        for (int[] cell : cellList) {
            clearFromCell(cell[0], cell[1], board);
        }
    }

    @Override
    public void clearBoardByZ(Board board) {
        final List<int[]> cellList = cellArrayHelper.getIndexOfZ(board.getBoard());
        for (int[] cell : cellList) {
            clearFromCell(cell[0], cell[1], board);
        }
    }

    private void clearFromCell(int rowId, int columnId, Board board) {
        final Cell collapsingCell = board.getCell(rowId, columnId);
        final CellColour cellColour = collapsingCell.getCellColour();

        clearCellAndAround(rowId, columnId, board, cellColour);
    }

    private void clearCellAndAround(int rowId, int columnId, Board board, CellColour cellColour) {
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
    }

    private void clearSurroundingCell(int rowId, int columnId, Board board, CellColour cellColour) {
        clearCellAndAround(rowId - 1, columnId, board, cellColour);
        clearCellAndAround(rowId, columnId - 1, board, cellColour);
        clearCellAndAround(rowId, columnId + 1, board, cellColour);
        clearCellAndAround(rowId + 1, columnId, board, cellColour);
    }
}
