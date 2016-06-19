package org.binu.ai.shinynew;

import org.binu.board.BlockQueue;
import org.binu.board.Board;
import org.binu.data.ScoreNode;
import org.binu.framework.TimeHelper;

/**
 * Shiny!!! move analysis.
 */
public class ShinyNewMoveAnalyser {
    public ScoreNode makeScoreTree(Board board, BlockQueue blockQueue, ScoreNode rootNode) {
        final ShinyNewRandomMoveMaker shinyNewRandomMoveMaker = new ShinyNewRandomMoveMaker();
        final ScoreNode calculatedRootNode;
        if (rootNode == null) {
            calculatedRootNode = new ScoreNode();
        } else {
            calculatedRootNode = rootNode;
        }

        final TimeHelper timeHelper = new TimeHelper();
        int count = 0;
        while (timeHelper.getTimeSinceStartInMills() < 50) {
            shinyNewRandomMoveMaker.makeRandomMove(new Board(board), new BlockQueue(blockQueue), calculatedRootNode, 0);
            count++;
        }
        System.err.println("Number of searches: " + count);

        return calculatedRootNode;
    }
}
