package org.binu.ai.movemaker;

import org.binu.board.Block;
import org.binu.board.BlockQueue;
import org.binu.board.Board;
import org.binu.board.Cell;
import org.binu.data.CellColour;
import org.binu.data.CellStatus;
import org.binu.data.ScoreNode;
import org.binu.integration.DataParser;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Test for {@link RandomMoveMaker}
 */
public class RandomMoveMakerTest {

    private Board board;
    private DataParser dataParser;
    private BlockQueue blockQueue;
    private RandomMoveMaker randomMoveMaker;

    @Test
    public void should_have_at_most_22_children_while_making_random_moves() throws Exception {
        final String[] boardString = {
                "......",
                "......",
                "......",
                "......",
                "......",
                "......",
                "......",
                "......",
                "......",
                "......",
                "......",
                "......"
        };
        dataParser = new DataParser();
        board = dataParser.createBoard(boardString);
        blockQueue = new BlockQueue();
        addBlockToBlockQueue(CellColour.GREEN, CellColour.GREEN);
        addBlockToBlockQueue(CellColour.RED, CellColour.RED);
        addBlockToBlockQueue(CellColour.GREEN, CellColour.GREEN);
        addBlockToBlockQueue(CellColour.RED, CellColour.RED);
        addBlockToBlockQueue(CellColour.RED, CellColour.RED);
        addBlockToBlockQueue(CellColour.GREEN, CellColour.GREEN);
        addBlockToBlockQueue(CellColour.RED, CellColour.RED);
        addBlockToBlockQueue(CellColour.RED, CellColour.RED);

        randomMoveMaker = new RandomMoveMaker();
        final ScoreNode rootNode = new ScoreNode();
        for (int i = 0; i < 1000; i++) {
            randomMoveMaker.makeRandomMove(new Board(board), new BlockQueue(blockQueue), rootNode);
        }

        assertThat("Root node has at most 22 children. ", rootNode.getChildren().size() <= 22, is(true));
    }

    private void addBlockToBlockQueue(CellColour cell1Colour, CellColour cell2Colour) {
        final Cell[] cells = new Cell[2];
        cells[0] = new Cell(cell1Colour, CellStatus.OCCUPIED);
        cells[1] = new Cell(cell2Colour, CellStatus.OCCUPIED);
        final Block block = new Block(cells);
        blockQueue.add(block);
    }
}
