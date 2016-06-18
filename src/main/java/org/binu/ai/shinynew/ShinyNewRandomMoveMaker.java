package org.binu.ai.shinynew;

import org.binu.board.Block;
import org.binu.board.BlockQueue;
import org.binu.board.Board;
import org.binu.data.Orientation;
import org.binu.data.ScoreNode;
import org.binu.framework.*;
import org.binu.scoring.BoardScorerImpl;

import java.util.List;

/**
 * Make random moves
 */
public class ShinyNewRandomMoveMaker {
    private CellArrayHelper cellArrayHelper;
    private BoardScorerImpl boardScorer;
    private OrientationHelper orientationHelper;

    public ShinyNewRandomMoveMaker() {
        orientationHelper = new OrientationHelper();
        cellArrayHelper = new CellArrayHelperImpl();
        boardScorer = new BoardScorerImpl(new ChainClearerImpl(cellArrayHelper), new BoardCollapserImpl(cellArrayHelper));
    }

    public void makeRandomMove(Board board, BlockQueue blockQueue, ScoreNode scoreNode, int level) {

        final OrientationAndIndex randomOrientationWithDropIndex = orientationHelper.getRandomOrientationWithDropIndex();
        final Orientation orientation = randomOrientationWithDropIndex.getOrientation();
        final int nodeIndex = randomOrientationWithDropIndex.getNodeIndex();
        final Block block = blockQueue.getNextAndPop();

        if (block == null) {
            return;
        }

        final boolean droppedSuccessfully = cellArrayHelper.dropBlockIntoBoard(board, block, nodeIndex, orientation);
        if (droppedSuccessfully) {
            final int score = boardScorer.scoreBoardAndRecursivelyClearAndCollapse(board, true);
            ScoreNode currentNode = new ScoreNode(nodeIndex, score, orientation, level);
            final List<ScoreNode> children = scoreNode.getChildren();
            if (!children.contains(currentNode)) {
                scoreNode.addChild(currentNode);
                makeRandomMove(board, blockQueue, currentNode, ++level);
            } else {
                final int currentNodeIndex = children.indexOf(currentNode);
                currentNode = children.get(currentNodeIndex);
                makeRandomMove(board, blockQueue, currentNode, ++level);
            }
        }
    }
}
