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
        final int score = getScore(column);
        return score * 10;
    }

    private int getScore(int column) {
        final Board tempBoardBefore = new Board(board);
        //TODO Extract into board comparator?
        final Cell[] cells = cellArrayHelper.dropBlockIntoColumn(tempBoardBefore.getColumn(column), blockQueue.getNext());
        tempBoardBefore.setColumn(column, cells);
        final Board tempBoardAfter = new Board(tempBoardBefore);
        boardClearer.clearBoard(tempBoardAfter);

        int score = 0;
        for (int i = 0; i < Board.ROW_LENGTH; i++) {
            for (int j = 0; j < Board.COLUMN_LENGTH; j++) {
                if (tempBoardAfter.getCell(i, j).getCellStatus() != tempBoardBefore.getCell(i, j).getCellStatus()) {
                    score++;
                }
            }
        }
        return score;
    }
}
