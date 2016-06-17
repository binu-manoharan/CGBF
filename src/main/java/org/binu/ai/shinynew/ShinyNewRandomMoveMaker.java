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
    private static final int MIN_VALUE = 0;
    private static final int MAX_VALUE = 5;
    private RandomValueGenerator randomValueGenerator;
    private CellArrayHelper cellArrayHelper;
    private BoardScorerImpl boardScorer;

    public ShinyNewRandomMoveMaker() {
        randomValueGenerator = new RandomValueGenerator();
        cellArrayHelper = new CellArrayHelperImpl();
        boardScorer = new BoardScorerImpl(new ChainClearerImpl(cellArrayHelper), new BoardCollapserImpl(cellArrayHelper));
    }

    public void makeRandomMove(Board board, BlockQueue blockQueue, ScoreNode scoreNode, int level) {

        //TODO move stuffs to Orientation helper.
        final int randomValue = randomValueGenerator.getRandomValue(MIN_VALUE, MAX_VALUE);
        final Block block = blockQueue.getNextAndPop();

        if (block == null) {
            return;
        }

        final boolean droppedSuccessfully = cellArrayHelper.dropBlockIntoBoard(board, block, randomValue, Orientation.VERTICAL);
        if (droppedSuccessfully) {
            final int score = boardScorer.scoreBoardAndRecursivelyClearAndCollapse(board, true);
            ScoreNode currentNode = new ScoreNode(randomValue, score, Orientation.VERTICAL, level);
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
