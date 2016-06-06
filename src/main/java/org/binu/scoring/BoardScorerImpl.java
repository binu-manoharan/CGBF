package org.binu.scoring;

import org.binu.board.Board;
import org.binu.board.Cell;
import org.binu.data.CellColour;
import org.binu.data.CellStatus;
import org.binu.framework.BoardCollapser;
import org.binu.framework.ChainClearer;

import java.util.HashMap;
import java.util.Map;

/**
 * Board scoring implementation
 */
public class BoardScorerImpl implements BoardScorer {

    private ChainClearer chainClearer;
    private BoardCollapser boardCollapser;

    public BoardScorerImpl(ChainClearer chainClearer, BoardCollapser boardCollapser) {
        this.chainClearer = chainClearer;
        this.boardCollapser = boardCollapser;
    }

    //TODO Invalidated
    @Override
    public int scoreBoard(Board board) {
        final int score = calculateScore(board, 0);
        return score;
    }

    private int calculateScore(Board board, int chainPower) {
        final boolean clearable = chainClearer.isClearable(board);
        if (clearable) {
            final Board currentBoardState = new Board(board);
            chainClearer.clearBoard(board);

            int score = 0;

            final HashMap<CellColour, Integer> colourSizeMap = new HashMap<>();

            for (int row = 0; row < Board.ROW_LENGTH; row++) {
                for (int col = 0; col < Board.COLUMN_LENGTH; col++) {
                    final Cell newCell = board.getCell(row, col);
                    final Cell oldCell = currentBoardState.getCell(row, col);
                    if (oldCell.getCellStatus() != CellStatus.EMPTY && oldCell.getCellStatus() != CellStatus.BLOCKED && newCell.getCellStatus() == CellStatus.EMPTY) {

                        final CellColour cellColour = oldCell.getCellColour();
                        if (!colourSizeMap.containsKey(cellColour)) {
                            colourSizeMap.put(cellColour, 1);
                        } else {
                            final Integer integer = colourSizeMap.get(cellColour);
                            colourSizeMap.put(cellColour, integer + 1);
                        }
                    }
                }
            }

            boardCollapser.collapseBoard(board);

            final int colourBonus = getColourBonus(colourSizeMap.size());
            int groupBonus = 0;
            for (Integer integer : colourSizeMap.values()) {
                if (integer.intValue() > 4) {
                    groupBonus += integer - 4;
                }
            }

            for (Map.Entry<CellColour, Integer> cellColourIntegerEntry : colourSizeMap.entrySet()) {
                final Integer numberOfBlock = cellColourIntegerEntry.getValue();
                final int comboTotal = chainPower + colourBonus + groupBonus;
                score += (numberOfBlock * 10) * (comboTotal == 0 ? 1: comboTotal);
            }

            final int updateChainPower = chainPower == 0 ? 8 : chainPower * 2;
            return score + calculateScore(board, updateChainPower);
        }
        else {
            return 0;
        }
    }

    private int getColourBonus(int numColours) {
        switch (numColours) {
            case 1:
                return 0;
            case 2:
                return 2;
            case 3:
                return 4;
            case 4:
                return 8;
            case 5:
                return 16;
            default:
                assert false;
                return 0;
        }
    }
}
