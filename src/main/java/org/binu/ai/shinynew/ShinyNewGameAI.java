package org.binu.ai.shinynew;

import org.binu.board.BlockQueue;
import org.binu.board.Board;
import org.binu.data.ScoreNode;
import org.binu.framework.ScoreNodeHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Shiny!!!
 */
public class ShinyNewGameAI {

    private ScoreNodeHelper scoreNodeHelper;
    private Board board;
    private BlockQueue blockQueue;
    private ShinyNewMoveAnalyser shinyNewMoveAnalyser;

    public ShinyNewGameAI(Board board, BlockQueue blockQueue) {
        this.board = board;
        this.blockQueue = blockQueue;
        scoreNodeHelper = new ScoreNodeHelper();
        shinyNewMoveAnalyser = new ShinyNewMoveAnalyser();
    }

    public List<ScoreNode> calculateNextMove(ScoreNode rootNode) {
        final ScoreNode rootNode1 = shinyNewMoveAnalyser.makeScoreTree(board, blockQueue, rootNode);

        ScoreNode highestScoreNode = getHighestScoreNode(rootNode1);
        highestScoreNode = getNextMoveForHighestScoringNode(highestScoreNode);

        final ArrayList<ScoreNode> scoreNodes = new ArrayList<>();
        scoreNodes.add(rootNode1);
        scoreNodes.add(highestScoreNode);
        return scoreNodes;
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
                if (nodeScore > highestScore) {
                    highestScore = nodeScore;
                    highestIndex = scoreNodes.size();
                }
                scoreNodes.add(bestScoreNodeForLevel);

                System.err.println(i + "  " + nodeScore);
            }
        }

        ScoreNode scoreNode = scoreNodes.get(highestIndex);
        System.err.println("Highest index: " + highestIndex);
        return scoreNode;
    }
}
