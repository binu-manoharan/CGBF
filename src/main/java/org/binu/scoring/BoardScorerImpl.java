package org.binu.scoring;

import org.binu.board.Board;
import org.binu.board.Cell;
import org.binu.data.CellColour;
import org.binu.data.CellStatus;
import org.binu.framework.CellArrayHelperImpl;
import org.binu.framework.ChainClearer;
import org.binu.framework.ChainClearerImpl;

import java.util.HashMap;

/**
 * Board scoring implementation
 */
public class BoardScorerImpl implements BoardScorer {
    //TODO Invalidated
    @Override
    public int scoreBoard(Board board) {
        final ChainClearer chainClearer = new ChainClearerImpl(new CellArrayHelperImpl());

        calculateScore(board, chainClearer);
        return 0;
    }

    private void calculateScore(Board board, ChainClearer chainClearer) {
        final Board currentBoardState = new Board(board);
        chainClearer.clearBoard(board);
        //Chain step needs to be known here
        //Step group

        boardStepDiff(board, currentBoardState, 0);
    }

    private BoardStepDifference boardStepDiff(Board board, Board currentBoardState, int chainPower) {
        int cellCount = 0;
        final HashMap<CellColour, Integer> colourSizeMap = new HashMap<>();
        for (int row = 0; row < Board.ROW_LENGTH; row++) {
            for (int col = 0; col < Board.COLUMN_LENGTH; col++) {
                final Cell newCell = board.getCell(row, col);
                final Cell oldCell = currentBoardState.getCell(row, col);
                if (oldCell.getCellStatus() != CellStatus.EMPTY && newCell.getCellStatus() == CellStatus.EMPTY) {

                    final CellColour cellColour = oldCell.getCellColour();
                    if (!colourSizeMap.containsKey(cellColour)) {
                        colourSizeMap.put(cellColour, 1);
                    } else {
                        final Integer integer = colourSizeMap.get(cellColour);
                        colourSizeMap.put(cellColour, integer + 1);
                    }

                    if (oldCell.getCellStatus() != CellStatus.BLOCKED) {
                        cellCount++;
                    }
                    //TODO find different colours
                    //TODO find number of cells
                }
            }
        }
        return new BoardStepDifference(cellCount, chainPower, colourSizeMap.size());
    }
}
