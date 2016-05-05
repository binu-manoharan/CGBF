package org.binu.ai.boardscoring;

import org.binu.board.Block;
import org.binu.board.BlockQueue;
import org.binu.board.Board;
import org.binu.board.Cell;
import org.binu.data.CellColour;
import org.binu.data.CellStatus;
import org.binu.integration.DataParser;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Test for {@link IBoardScoreCalculator}.
 */
public class IBoardScoreCalculatorTest {
    private DataParser dataParser;
    private BlockQueue blockQueue;

    @Before
    public void setUp() throws Exception {
        dataParser = new DataParser();
        blockQueue = new BlockQueue();
    }

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
                ".....1",
                ".1..01",
                ".1..01"
        };

        final Board board = dataParser.createBoard(boardString);

        final Block block = getBlock(CellColour.BLUE, CellStatus.OCCUPIED, CellColour.BLUE, CellStatus.OCCUPIED);
        blockQueue.add(block);

        final IBoardScoreCalculator boardScoreCalculator = new BoardScoreCalculatorImpl(board, blockQueue);
        assertThat("The score for column 1 is 0", boardScoreCalculator.calculateColumnScore(0), is(40));
        assertThat("The score for column 2 is 40", boardScoreCalculator.calculateColumnScore(1), is(40));
        assertThat("The score for column 3 is 0", boardScoreCalculator.calculateColumnScore(2), is(40));
        assertThat("The score for column 4 is 0", boardScoreCalculator.calculateColumnScore(3), is(0));
        assertThat("The score for column 5 is 0", boardScoreCalculator.calculateColumnScore(4), is(0));
        assertThat("The score for column 6 is 0", boardScoreCalculator.calculateColumnScore(5), is(70));
    }

    private Block getBlock(CellColour cell1Colour, CellStatus cell1Status, CellColour cell2Colour, CellStatus cell2Status) {
        final Cell[] blockCells = getCells(cell1Colour, cell1Status, cell2Colour, cell2Status);
        return new Block(blockCells);
    }

    private Cell[] getCells(CellColour cell1Colour, CellStatus cell1Status, CellColour cell2Colour, CellStatus cell2Status) {
        final Cell[] cells = new Cell[2];
        cells[0] = new Cell(cell1Colour, cell1Status);
        cells[1] = new Cell(cell2Colour, cell2Status);
        return cells;
    }
}
