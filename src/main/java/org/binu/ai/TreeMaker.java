package org.binu.ai;

import org.binu.board.BlockQueue;
import org.binu.board.Board;
import org.binu.data.ScoreNode;

/**
 * Make a tree of score nodes.
 */
public interface TreeMaker {

    /**
     * Make a score tree
     * @param board board to parse
     * @param blockQueue blockqueue to drop into board
     * @param rootNode current root node
     * @param timeLimitInMS length of time to execute for
     * @return root node
     */
    ScoreNode makeScoreTree(Board board, BlockQueue blockQueue, ScoreNode rootNode, int timeLimitInMS);
}
