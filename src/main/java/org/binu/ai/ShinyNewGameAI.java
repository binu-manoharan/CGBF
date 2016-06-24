package org.binu.ai;

import org.binu.ai.treemaker.ColourMatchingTreeMaker;
import org.binu.ai.treemaker.RandomEightLevelTreeMaker;
import org.binu.ai.treemaker.TreeMaker;
import org.binu.board.BlockQueue;
import org.binu.board.Board;
import org.binu.data.ScoreNode;
import org.binu.framework.ScoreNodeHelper;

import java.util.ArrayList;

/**
 * Shiny!!!
 */
public class ShinyNewGameAI {

    private static final int TIME_LIMIT_IN_MS_FOR_COLOUR_MATCHING = 70;
    private static final int GREED_LIMIT = 1260;
    private static final int TIME_LIMIT_IN_MS_FOR_RANDOM_MOVES = 20;
    private final TreeMaker randomEightLevelTreeMaker;
    private ScoreNodeHelper scoreNodeHelper;
    private Board board;
    private BlockQueue blockQueue;
    private TreeMaker levelTreeMaker;
    private String message;

    public ShinyNewGameAI(Board board, BlockQueue blockQueue) {
        this.board = board;
        this.blockQueue = blockQueue;
        scoreNodeHelper = new ScoreNodeHelper();
        levelTreeMaker = new ColourMatchingTreeMaker();
        randomEightLevelTreeMaker = new RandomEightLevelTreeMaker();
    }

    /**
     * Find the next move to make.
     * @param rootNode rootNode to start the tree from.
     * @return ScoreNode with information about the next move to make
     */
    public ScoreNode calculateNextMove(ScoreNode rootNode) {
        final ScoreNode rootNodeAfterRandom = randomEightLevelTreeMaker.makeScoreTree(board, blockQueue, rootNode, TIME_LIMIT_IN_MS_FOR_RANDOM_MOVES);
        final ScoreNode rootNodeAfterColourMatching = levelTreeMaker.makeScoreTree(board, blockQueue, rootNodeAfterRandom, TIME_LIMIT_IN_MS_FOR_COLOUR_MATCHING);

        final ScoreNode highestScoreNode = getHighestScoreNode(rootNodeAfterColourMatching);
        final ScoreNode nextMoveForHighestScoringNode = getNextMoveForHighestScoringNode(highestScoreNode);

        message = " " + highestScoreNode.getTreeLevel() + ": " + highestScoreNode.getNodeScore();
        return nextMoveForHighestScoringNode;
    }

    public ScoreNode getNextMoveForHighestScoringNode(ScoreNode highestScoreNode) {
        while (highestScoreNode.getTreeLevel() > 1) {
            System.err.println("Path: " + highestScoreNode.getNodeIndex() + "("+highestScoreNode.getOrientation()+")");
            highestScoreNode = highestScoreNode.getParent();
        }
        System.err.println("Path: " + highestScoreNode.getNodeIndex() + "("+highestScoreNode.getOrientation()+")");
        return highestScoreNode;
    }

    public ScoreNode getHighestScoreNode(ScoreNode rootNode) {
        final ArrayList<ScoreNode> scoreNodes = new ArrayList<>();
        int highestScore = 0;
        int highestIndex = 0;
        for (int i = 1; i < 9; i++) {
            final ScoreNode bestScoreNodeForLevel = scoreNodeHelper.getBestScoreNodeForLevel(rootNode, i);


            if (bestScoreNodeForLevel != null) {
                final int nodeScore = bestScoreNodeForLevel.getNodeScore();
                if (nodeScore > highestScore && highestScore < GREED_LIMIT) {
                    highestScore = nodeScore;
                    highestIndex = scoreNodes.size();
                }
                scoreNodes.add(bestScoreNodeForLevel);

                System.err.println(i + "  " + nodeScore);
            }
        }

        final ScoreNode scoreNode = scoreNodes.get(highestIndex);
        System.err.println("Highest index: " + highestIndex);
        return scoreNode;
    }

    public String getMessage() {
        return message;
    }
}
