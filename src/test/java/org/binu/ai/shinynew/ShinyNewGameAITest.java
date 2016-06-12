package org.binu.ai.shinynew;

import org.binu.board.Block;
import org.binu.board.BlockQueue;
import org.binu.board.Board;
import org.binu.board.Cell;
import org.binu.data.CellColour;
import org.binu.data.CellStatus;
import org.binu.integration.DataParser;
import org.junit.Test;

/**
 * Test for {@link ShinyNewGameAI}
 */
public class ShinyNewGameAITest {

    private Board board;
    private DataParser dataParser;
    private BlockQueue blockQueue;
    private ShinyNewGameAI shinyNewGameAI;

    @Test
    public void should_identify_node_scores() throws Exception {
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

        shinyNewGameAI = new ShinyNewGameAI(board, blockQueue);
        shinyNewGameAI.calculateNextMove(null);
        //TODO assert
    }

    private void addBlockToBlockQueue(CellColour cell1Colour, CellColour cell2Colour) {
        final Cell[] cells = new Cell[2];
        cells[0] = new Cell(cell1Colour, CellStatus.OCCUPIED);
        cells[1] = new Cell(cell2Colour, CellStatus.OCCUPIED);
        final Block block = new Block(cells);
        blockQueue.add(block);
    }
}
