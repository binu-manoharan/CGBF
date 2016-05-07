package org.binu.integration;

import org.binu.board.Block;
import org.binu.board.BlockQueue;
import org.binu.board.Board;
import org.binu.board.Cell;
import org.binu.data.CellColour;
import org.binu.data.CellStatus;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Test for {@link DataParser}
 */
public class DataParserTest {

    private DataParser dataParser;

    @Before
    public void setUp() throws Exception {
        dataParser = new DataParser();
    }

    @Test
    public void should_provide_cell_with_matching_colours() throws Exception {
        final Block redYellowBlock = dataParser.createColourBlock(4, 5);
        assertBlockData(redYellowBlock, CellColour.RED, CellColour.YELLOW);

        final Block greenPurpleBlock = dataParser.createColourBlock(2, 3);
        assertBlockData(greenPurpleBlock, CellColour.GREEN, CellColour.PURPLE);

        final Block blueGreenBlock = dataParser.createColourBlock(1, 2);
        assertBlockData(blueGreenBlock, CellColour.BLUE, CellColour.GREEN);
    }

    @Test
    public void should_provide_a_block_queue() throws Exception {
        final int[][] blockQueueData = {{1, 2}, {2, 3}, {4, 5}};
        final BlockQueue blockQueue = dataParser.createBlockQueue(blockQueueData);
        assertThat("The block queue is not null. ", blockQueue, is(not(nullValue())));
        assertBlockData(blockQueue.getBlock(0), CellColour.BLUE, CellColour.GREEN);
        assertBlockData(blockQueue.getBlock(1), CellColour.GREEN, CellColour.PURPLE);
        assertBlockData(blockQueue.getBlock(2), CellColour.RED, CellColour.YELLOW);
    }

    @Test (expected = AssertionError.class)
    public void should_fail_when_there_is_no_valid_board_row_to_provide() throws Exception {
        final String rowString = "";
        dataParser.createBoardRow(rowString);
    }

    @Test
    public void should_provide_a_board_row_with_corresponding_cells() throws Exception {
        final String rowString = ".01234";
        final Cell[] boardRow = dataParser.createBoardRow(rowString);

        assertThat("Board row has 6 cells", boardRow.length, is(6));
        assertBoardRowCell(boardRow[0], null, CellStatus.EMPTY);
        assertBoardRowCell(boardRow[1], null, CellStatus.BLOCKED);
        assertBoardRowCell(boardRow[2], CellColour.BLUE, CellStatus.OCCUPIED);
        assertBoardRowCell(boardRow[3], CellColour.GREEN, CellStatus.OCCUPIED);
        assertBoardRowCell(boardRow[4], CellColour.PURPLE, CellStatus.OCCUPIED);
        assertBoardRowCell(boardRow[5], CellColour.RED, CellStatus.OCCUPIED);
    }

    @Test
    public void should_provide_a_board() throws Exception {
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
                ".0350.",
                "012414"
            };

        final Board board = dataParser.createBoard(boardString);
        //row 1
        assertBoardRowCell(board.getCell(0, 0), null, CellStatus.BLOCKED);
        assertBoardRowCell(board.getCell(0, 1), CellColour.BLUE, CellStatus.OCCUPIED);
        assertBoardRowCell(board.getCell(0, 2), CellColour.GREEN, CellStatus.OCCUPIED);
        assertBoardRowCell(board.getCell(0, 3), CellColour.RED, CellStatus.OCCUPIED);
        assertBoardRowCell(board.getCell(0, 4), CellColour.BLUE, CellStatus.OCCUPIED);
        assertBoardRowCell(board.getCell(0, 5), CellColour.RED, CellStatus.OCCUPIED);
        //row 2
        assertBoardRowCell(board.getCell(1, 0), null, CellStatus.EMPTY);
        assertBoardRowCell(board.getCell(1, 1), null, CellStatus.BLOCKED);
        assertBoardRowCell(board.getCell(1, 2), CellColour.PURPLE, CellStatus.OCCUPIED);
        assertBoardRowCell(board.getCell(1, 3), CellColour.YELLOW, CellStatus.OCCUPIED);
        assertBoardRowCell(board.getCell(1, 4), null, CellStatus.BLOCKED);
        assertBoardRowCell(board.getCell(1, 5), null, CellStatus.EMPTY);
    }

    @Test
    public void should_provide_a_board_string() throws Exception {
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
                ".0350.",
                "012414"
        };

        final Board board = dataParser.createBoard(boardString);
        final String[] outputBoardString = dataParser.createBoardString(board);

        for (int i = 0; i < boardString.length; i++) {
            assertThat("Board strings match", outputBoardString[i].equals(boardString[i]), is(true));
        }
    }

    private void assertBoardRowCell(Cell cell, CellColour cellColour, CellStatus cellStatus) {
        assertThat("Board first element is " + cellColour, cell.getCellColour(), is(cellColour));
        assertThat("Board first element is " + cellStatus, cell.getCellStatus(), is(cellStatus));
    }



    private void assertBlockData(Block block, CellColour firstColour, CellColour secondColour) {
        final Cell[] cells = block.getCells();
        final CellColour bottomCellColour = cells[0].getCellColour();
        final CellColour topCellColour = cells[1].getCellColour();
        assertThat("Bottom cell should be " + firstColour, bottomCellColour, is(firstColour));
        assertThat("Top cell should be " + secondColour, topCellColour, is(secondColour));
    }
}
