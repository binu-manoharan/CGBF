package org.binu.ai.treemaker;

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
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Test for {@link RandomEightLevelTreeMaker}
 */
public class RandomEightLevelTreeMakerTest {

    private Board board;
    private DataParser dataParser;
    private BlockQueue blockQueue;
    private RandomEightLevelTreeMaker randomEightLevelTreeMaker;

    @Test
    public void should_create_tree() throws Exception {
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

        randomEightLevelTreeMaker = new RandomEightLevelTreeMaker();
        final ScoreNode rootNode = randomEightLevelTreeMaker.makeScoreTree(board, blockQueue, null, 50);
        assertThat("Should create a non-null root node", rootNode, is(notNullValue()));
    }

    private void addBlockToBlockQueue(CellColour cell1Colour, CellColour cell2Colour) {
        final Cell[] cells = new Cell[2];
        cells[0] = new Cell(cell1Colour, CellStatus.OCCUPIED);
        cells[1] = new Cell(cell2Colour, CellStatus.OCCUPIED);
        final Block block = new Block(cells);
        blockQueue.add(block);
    }
}
