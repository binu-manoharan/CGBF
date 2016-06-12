package org.binu.ai.shinynew;

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
 * Test for {@link ShinyNewRandomMoveMaker}
 */
public class ShinyNewRandomMoveMakerTest {

    public static final int ROOT_NODE_LEVEL = 0;
    private Board board;
    private DataParser dataParser;
    private BlockQueue blockQueue;
    private ShinyNewRandomMoveMaker shinyNewRandomMoveMaker;

    @Test
    public void should_compile() throws Exception {
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

        shinyNewRandomMoveMaker = new ShinyNewRandomMoveMaker();
        final ScoreNode rootNode = new ScoreNode();
        shinyNewRandomMoveMaker.makeRandomMove(new Board(board), new BlockQueue(blockQueue), rootNode, ROOT_NODE_LEVEL);
        shinyNewRandomMoveMaker.makeRandomMove(new Board(board), new BlockQueue(blockQueue), rootNode, ROOT_NODE_LEVEL);
        shinyNewRandomMoveMaker.makeRandomMove(new Board(board), new BlockQueue(blockQueue), rootNode, ROOT_NODE_LEVEL);
        shinyNewRandomMoveMaker.makeRandomMove(new Board(board), new BlockQueue(blockQueue), rootNode, ROOT_NODE_LEVEL);

        assertThat("Root node has at most 4 children. ", rootNode.getChildren().size() <= 4, is(true));
    }

    private void addBlockToBlockQueue(CellColour cell1Colour, CellColour cell2Colour) {
        final Cell[] cells = new Cell[2];
        cells[0] = new Cell(cell1Colour, CellStatus.OCCUPIED);
        cells[1] = new Cell(cell2Colour, CellStatus.OCCUPIED);
        final Block block = new Block(cells);
        blockQueue.add(block);
    }
}
