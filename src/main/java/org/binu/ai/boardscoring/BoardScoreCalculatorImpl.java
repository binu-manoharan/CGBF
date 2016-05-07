package org.binu.ai.boardscoring;

import org.binu.ai.simple.CellArrayHelper;
import org.binu.ai.simple.CellArrayHelperImpl;
import org.binu.board.BlockQueue;
import org.binu.board.Board;
import org.binu.board.Cell;
import org.binu.data.CellStatus;

/**
 * Basic board scoring.
 */
public class BoardScoreCalculatorImpl implements IBoardScoreCalculator {
    private final Board board;
    private final BlockQueue blockQueue;
    private final CellArrayHelper cellArrayHelper;
    private final BoardClearerImpl boardClearer;
    private final BoardCollapserImpl boardCollapser;

    public BoardScoreCalculatorImpl(Board board, BlockQueue blockQueue) {
        this.board = board;
        this.blockQueue = blockQueue;
        cellArrayHelper = new CellArrayHelperImpl();
        boardCollapser = new BoardCollapserImpl(cellArrayHelper);
        boardClearer = new BoardClearerImpl(cellArrayHelper);
    }

    @Override
    public int calculateColumnScore(int column) {
        final int score = getScore(column);
        return score * 10;
    }

    private int getScore(int column) {
        final Board tempBoardBeforeClear;

        //TODO clean up try catch mess later?
        try {
            tempBoardBeforeClear = getBoardWithDroppedQueue(column);
        } catch (ArrayIndexOutOfBoundsException e) {
            //never take this path
            return -10000;
        }

        final Board tempBoardAfterClear = new Board(tempBoardBeforeClear);
        boardClearer.clearBoard(tempBoardAfterClear);

        final int score = getScore(tempBoardBeforeClear, tempBoardAfterClear);

        boardCollapser.collapseBoard(tempBoardAfterClear);
        final Board tempBoardAfterCollapse = new Board(tempBoardAfterClear);
        boardClearer.clearBoard(tempBoardAfterCollapse);

        final int comboScore = getScore(tempBoardAfterClear, tempBoardAfterCollapse) * 8;
        final int totalScore = comboScore + score;
        return totalScore;
    }

    private int getScore(Board tempBoardBefore, Board tempBoardAfter) {
        int score = 0;
        for (int i = 0; i < Board.ROW_LENGTH; i++) {
            for (int j = 0; j < Board.COLUMN_LENGTH; j++) {
                final CellStatus beforeCellStatus = tempBoardBefore.getCell(i, j).getCellStatus();
                //TODO: Don't agressively remove blocks might be helpful for combos.
                if (tempBoardAfter.getCell(i, j).getCellStatus() != beforeCellStatus && beforeCellStatus != CellStatus.BLOCKED) {
                    score++;
                }
            }
        }
        return score;
    }

    private Board getBoardWithDroppedQueue(int column) throws ArrayIndexOutOfBoundsException{
        final Board tempBoardBefore = new Board(board);
        final Cell[] columnToDropInto = tempBoardBefore.getColumn(column);

        final Cell[] cells = cellArrayHelper.dropBlockIntoColumn(columnToDropInto, blockQueue.getNext());
        tempBoardBefore.setColumn(column, cells);
        return tempBoardBefore;
    }
}
