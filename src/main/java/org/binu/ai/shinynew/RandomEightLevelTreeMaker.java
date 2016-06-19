package org.binu.ai.shinynew;

import org.binu.board.BlockQueue;
import org.binu.board.Board;
import org.binu.data.ScoreNode;
import org.binu.framework.TimeHelper;

/**
 * Random 8 level move maker.
 */
public class RandomEightLevelTreeMaker {
    public ScoreNode makeScoreTree(Board board, BlockQueue blockQueue, ScoreNode rootNode, int timeLimitInMS) {
        final ShinyNewRandomMoveMaker shinyNewRandomMoveMaker = new ShinyNewRandomMoveMaker();
        final ScoreNode calculatedRootNode;
        if (rootNode == null) {
            calculatedRootNode = new ScoreNode();
        } else {
            calculatedRootNode = rootNode;
        }

        final TimeHelper timeHelper = new TimeHelper();
        int count = 0;
        while (timeHelper.getTimeSinceStartInMills() < timeLimitInMS) {
            shinyNewRandomMoveMaker.makeRandomMove(new Board(board), new BlockQueue(blockQueue), calculatedRootNode, 0);
            count++;
        }
        System.err.println("Number of searches: " + count);

        return calculatedRootNode;
    }
}
