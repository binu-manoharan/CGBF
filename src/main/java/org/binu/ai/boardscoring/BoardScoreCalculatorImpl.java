package org.binu.ai.boardscoring;

import org.binu.ai.simple.CellArrayHelper;
import org.binu.ai.simple.CellArrayHelperImpl;
import org.binu.board.BlockQueue;
import org.binu.board.Board;
import org.binu.board.Cell;

/**
 * Basic board scoring.
 */
public class BoardScoreCalculatorImpl implements IBoardScoreCalculator {
    private Board board;
    private BlockQueue blockQueue;
    private final CellArrayHelper cellArrayHelper;
    private BoardClearerImpl boardClearer;

    public BoardScoreCalculatorImpl(Board board, BlockQueue blockQueue) {
        this.board = board;
        this.blockQueue = blockQueue;
        cellArrayHelper = new CellArrayHelperImpl();
        boardClearer = new BoardClearerImpl(cellArrayHelper);
    }

    @Override
    public int calculateColumnScore(int column) {
        int score = getScore(column);
        return score * 10;
    }

    private int getScore(int column) {
        final Board tempBoardBefore = new Board(board);
        final Cell[] cells = cellArrayHelper.dropBlockIntoColumn(board.getColumn(column), blockQueue.getNext());
        tempBoardBefore.setColumn(column, cells);
        final Board tempBoardAfter = new Board(tempBoardBefore);
        final Board clearedBoard = boardClearer.clearBoard(tempBoardBefore);

        int score = 0;
        for (int i = 0; i < Board.ROW_LENGTH; i++) {
            for (int j = 0; j < Board.COLUMN_LENGTH; j++) {
                if (clearedBoard.getCell(i, j).getCellStatus() != tempBoardAfter.getCell(i, j).getCellStatus()) {
                    score++;
                }
            }
        }
        return score;
    }
}
