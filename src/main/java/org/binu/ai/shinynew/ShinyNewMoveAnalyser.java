package org.binu.ai.shinynew;

import org.binu.board.BlockQueue;
import org.binu.board.Board;
import org.binu.data.ScoreNode;

/**
 * Shiny!!! move analysis.
 */
public class ShinyNewMoveAnalyser {
    public ScoreNode makeScoreTree(Board board, BlockQueue blockQueue) {
        final ShinyNewRandomMoveMaker shinyNewRandomMoveMaker = new ShinyNewRandomMoveMaker();
        final ScoreNode rootNode = new ScoreNode();
        final long startTime = System.currentTimeMillis();
        long currentTime = System.currentTimeMillis();
        int count = 0;
        while (currentTime - startTime < 50) {
            shinyNewRandomMoveMaker.makeRandomMove(new Board(board), new BlockQueue(blockQueue), rootNode, 0);
            currentTime = System.currentTimeMillis();
            count++;
        }
        System.out.println(count);

        return rootNode;
    }
}
