package org.binu.ai.shinynew;

import org.binu.board.BlockQueue;
import org.binu.board.Board;
import org.binu.data.ScoreNode;
import org.binu.framework.ScoreNodeHelper;

import java.util.ArrayList;

/**
 * Shiny!!!
 */
public class ShinyNewGameAI {

    private ScoreNodeHelper scoreNodeHelper;
    private Board board;
    private BlockQueue blockQueue;

    public ShinyNewGameAI(Board board, BlockQueue blockQueue) {
        this.board = board;
        this.blockQueue = blockQueue;
        scoreNodeHelper = new ScoreNodeHelper();
    }

    public ScoreNode calculateNextMove(ScoreNode rootNode) {
        final ShinyNewMoveAnalyser shinyNewMoveAnalyser = new ShinyNewMoveAnalyser();
        final ScoreNode rootNode1 = shinyNewMoveAnalyser.makeScoreTree(board, blockQueue, rootNode);

        final ArrayList<ScoreNode> scoreNodes = new ArrayList<>();
        int highestScore = 0;
        int highestIndex = 0;
        for (int i = 1; i < 9; i++) {
            final ScoreNode bestScoreNodeForLevel = scoreNodeHelper.getBestScoreNodeForLevel(rootNode1, i);


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
        while (scoreNode.getParent() != null && scoreNode.getTreeLevel() > 1) {
            System.err.print("Path: " + scoreNode.getNodeIndex());
            scoreNode = scoreNode.getParent();
        }
        System.err.println("Path: " + scoreNode.getNodeIndex());

        System.err.println("Highest index: " + highestIndex);
        return scoreNode;
    }
}
