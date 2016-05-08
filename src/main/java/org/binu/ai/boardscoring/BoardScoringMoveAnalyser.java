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
    private ScoreNodeTreeFactory scoreNodeTreeFactory;
    private ScoreNode rootNode;
    private ScoreNodeTreeParser scoreNodeTreeParser;

    private int level;

    public BoardScoringMoveAnalyser(Board board, BlockQueue blockQueue) {
        this.board = board;
        this.blockQueue = blockQueue;
        rootNode = new ScoreNode();
        level = 4;
        scoreNodeTreeParser = new ScoreNodeTreeParser();
        scoreNodeTreeFactory = new ScoreNodeTreeFactory();
    }

    @Override
    public int findBestMove() {
        scoreNodeTreeFactory.populateRootNodeTree(board, blockQueue, rootNode, level);
        final int[] bestScoringPath = scoreNodeTreeParser.findBestScoringPath(rootNode, level);
        System.err.println("1: " + bestScoringPath[0]);
        System.err.println("2: " + bestScoringPath[1]);
        System.err.println("3: " + bestScoringPath[2]);
        return bestScoringPath[0];
    }

    @NotNull
    private ScoreNode getScoreNode(BoardScoreCalculatorImpl boardScoreCalculator, int columnIndex, ScoreNode parent) {
        final ScoreNode child = new ScoreNode();
        child.nodeIndex = columnIndex;
        child.nodeScore = boardScoreCalculator.calculateColumnScore(columnIndex);
        child.setParent(parent);
        return child;
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

    public void setLevel(int level) {
        this.level = level;
    }
}
