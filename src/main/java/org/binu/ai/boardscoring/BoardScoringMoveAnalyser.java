package org.binu.ai.boardscoring;

import org.binu.board.BlockQueue;
import org.binu.board.Board;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Move analyser
 */
public class BoardScoringMoveAnalyser extends AbstractMoveAnalyser {
    private Board board;
    private BlockQueue blockQueue;

    public BoardScoringMoveAnalyser(Board board, BlockQueue blockQueue) {
        this.board = board;
        this.blockQueue = blockQueue;
    }

    @Override
    public int findBestMove() {
        final BoardScoreCalculatorImpl boardScoreCalculator = new BoardScoreCalculatorImpl(board, blockQueue);

        final int[][] columnScores = new int[6][2];
        final int highestColumnScore = getHighestColumnScore(boardScoreCalculator, columnScores);

        final ArrayList<Integer> highestColumnIndex = getHighestScoringIndexes(columnScores, highestColumnScore);

        final Integer winningIndex = getWinningIndex(highestColumnIndex);
        return winningIndex;
    }

    private Integer getWinningIndex(ArrayList<Integer> highestColumnIndex) {
        final double randomMultiplier = Math.random();
        final double randomVal = randomMultiplier * highestColumnIndex.size();
        return highestColumnIndex.get((int) randomVal);
    }

    @NotNull
    private ArrayList<Integer> getHighestScoringIndexes(int[][] columnScores, int highestColumnScore) {
        final ArrayList<Integer> highestColumnIndex = new ArrayList<>();
        for (int col = 0; col < Board.COLUMN_LENGTH; col++) {
            if (columnScores[col][1] == highestColumnScore) {
                highestColumnIndex.add(columnScores[col][0]);
            }
        }
        return highestColumnIndex;
    }

    private int getHighestColumnScore(BoardScoreCalculatorImpl boardScoreCalculator, int[][] columnScores) {
        int highestColumnScore = 0;
        for (int col = 0; col < Board.COLUMN_LENGTH; col++) {
            final int columnScore = boardScoreCalculator.calculateColumnScore(col);
            columnScores[col][0] = col;
            columnScores[col][1] = columnScore;
            if (columnScore > highestColumnScore) {
                highestColumnScore = columnScore;
            }
        }
        return highestColumnScore;
    }
}
