package org.binu.ai.boardscoring;

import org.binu.board.BlockQueue;
import org.binu.board.Board;

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
        int highestColumnScore = 0;
        for (int col = 0; col < Board.COLUMN_LENGTH; col++) {
            final int columnScore = boardScoreCalculator.calculateColumnScore(col);
            columnScores[col][0] = col;
            columnScores[col][1] = columnScore;
            if (columnScore > highestColumnScore) {
                highestColumnScore = columnScore;
            }
        }

        final ArrayList<Integer> highestColumnIndex = new ArrayList<>();
        for (int col = 0; col < Board.COLUMN_LENGTH; col++) {
            if (columnScores[col][1] == highestColumnScore) {
                highestColumnIndex.add(columnScores[col][0]);
            }
        }

        final double randomMultiplier = Math.random();
        final double randomVal = randomMultiplier * highestColumnIndex.size();

        return highestColumnIndex.get((int) randomVal);
    }
}
