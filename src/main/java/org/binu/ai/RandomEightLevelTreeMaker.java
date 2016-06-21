package org.binu.ai;

import org.binu.board.BlockQueue;
import org.binu.board.Board;
import org.binu.data.ScoreNode;
import org.binu.utils.TimeHelper;

/**
 * Random 8 level move maker.
 */
public class RandomEightLevelTreeMaker extends AbstractTreeMaker {

    @Override
    public ScoreNode makeScoreTree(Board board, BlockQueue blockQueue, ScoreNode rootNode, int timeLimitInMS) {
        final RandomMoveMaker randomMoveMaker = new RandomMoveMaker();
        final ScoreNode calculatedRootNode;
        calculatedRootNode = createRootNodeIfNull(rootNode);

        final TimeHelper timeHelper = new TimeHelper();
        int count = 0;
        while (timeHelper.getTimeSinceStartInMills() < timeLimitInMS) {
            randomMoveMaker.makeRandomMove(new Board(board), new BlockQueue(blockQueue), calculatedRootNode);
            count++;
        }
        System.err.println("Number of searches: " + count);

        return calculatedRootNode;
    }
}
