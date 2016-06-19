package org.binu.integration;

import org.binu.board.Block;
import org.binu.board.BlockQueue;
import org.binu.board.Board;
import org.binu.board.Cell;
import org.binu.data.CellColour;
import org.binu.data.CellStatus;
import org.binu.data.Orientation;
import org.binu.data.ScoreNode;
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
        final Block redYellowBlock = dataParser.createColourBlock(5, 4);
        assertBlockData(redYellowBlock, CellColour.YELLOW, CellColour.RED);

        final Block greenPurpleBlock = dataParser.createColourBlock(3, 2);
        assertBlockData(greenPurpleBlock, CellColour.PURPLE, CellColour.GREEN);

        final Block blueGreenBlock = dataParser.createColourBlock(2, 1);
        assertBlockData(blueGreenBlock, CellColour.GREEN, CellColour.BLUE);
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

    @Test(expected = AssertionError.class)
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

    @Test
    public void should_prettify_row() throws Exception {
        final String[] boardRow = new String[1];
        boardRow[0] = "..11..";
        final String[] prettifiedString = dataParser.prettifyBoardStringForTests(boardRow);

        final String expectedPrettification = "|  .  .11  .  .|";
        assertThat("String is prettified", prettifiedString[0].equals(expectedPrettification), is(true));
    }

    @Test
    public void should_play_the_scoring_path_on_the_board_without_clearing() throws Exception {
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
        final Board board = dataParser.createBoard(boardString);
        final BlockQueue blockQueue = new BlockQueue();
        blockQueue.add(getBlock(CellColour.RED, CellStatus.OCCUPIED, CellColour.GREEN, CellStatus.OCCUPIED));
        blockQueue.add(getBlock(CellColour.PURPLE, CellStatus.OCCUPIED, CellColour.YELLOW, CellStatus.OCCUPIED));
        blockQueue.add(getBlock(CellColour.BLUE, CellStatus.OCCUPIED, CellColour.RED, CellStatus.OCCUPIED));
        blockQueue.add(getBlock(CellColour.RED, CellStatus.OCCUPIED, CellColour.GREEN, CellStatus.OCCUPIED));
        blockQueue.add(getBlock(CellColour.RED, CellStatus.OCCUPIED, CellColour.GREEN, CellStatus.OCCUPIED));
        blockQueue.add(getBlock(CellColour.RED, CellStatus.OCCUPIED, CellColour.GREEN, CellStatus.OCCUPIED));
        blockQueue.add(getBlock(CellColour.RED, CellStatus.OCCUPIED, CellColour.GREEN, CellStatus.OCCUPIED));
        blockQueue.add(getBlock(CellColour.RED, CellStatus.OCCUPIED, CellColour.GREEN, CellStatus.OCCUPIED));

        //Create score nodes
        final ScoreNode scoreNode = new ScoreNode();
        final ScoreNode eighthChild = new ScoreNode(5, 0, Orientation.HORIZONTAL_REVERSED);
        final ScoreNode seventhChild = getScoreNode(eighthChild, 2, Orientation.VERTICAL);
        final ScoreNode sixthChild = getScoreNode(seventhChild, 2, Orientation.HORIZONTAL);
        final ScoreNode fifthChild = getScoreNode(sixthChild, 3, Orientation.HORIZONTAL_REVERSED);
        final ScoreNode fourthChild = getScoreNode(fifthChild, 1, Orientation.HORIZONTAL);
        final ScoreNode thirdChild = getScoreNode(fourthChild, 5, Orientation.HORIZONTAL_REVERSED);
        final ScoreNode secondChild = getScoreNode(thirdChild, 4, Orientation.HORIZONTAL);
        final ScoreNode firstChild = getScoreNode(secondChild, 3, Orientation.VERTICAL);
        scoreNode.addChild(firstChild);

        final Board boardFollowingScoringPath = dataParser.followPath(board, blockQueue, eighthChild);
        assertBoardRowCell(boardFollowingScoringPath.getCell(0, 1), CellColour.RED, CellStatus.OCCUPIED);
        assertBoardRowCell(boardFollowingScoringPath.getCell(0, 2), CellColour.GREEN, CellStatus.OCCUPIED);
        assertBoardRowCell(boardFollowingScoringPath.getCell(0, 3), CellColour.GREEN, CellStatus.OCCUPIED);
        assertBoardRowCell(boardFollowingScoringPath.getCell(0, 4), CellColour.PURPLE, CellStatus.OCCUPIED);
        assertBoardRowCell(boardFollowingScoringPath.getCell(0, 5), CellColour.YELLOW, CellStatus.OCCUPIED);

        assertBoardRowCell(boardFollowingScoringPath.getCell(1, 2), CellColour.GREEN, CellStatus.OCCUPIED);
        assertBoardRowCell(boardFollowingScoringPath.getCell(1, 3), CellColour.RED, CellStatus.OCCUPIED);
        assertBoardRowCell(boardFollowingScoringPath.getCell(1, 4), CellColour.RED, CellStatus.OCCUPIED);
        assertBoardRowCell(boardFollowingScoringPath.getCell(1, 5), CellColour.BLUE, CellStatus.OCCUPIED);

        assertBoardRowCell(boardFollowingScoringPath.getCell(2, 2), CellColour.RED, CellStatus.OCCUPIED);
        assertBoardRowCell(boardFollowingScoringPath.getCell(2, 3), CellColour.RED, CellStatus.OCCUPIED);
        assertBoardRowCell(boardFollowingScoringPath.getCell(2, 4), CellColour.GREEN, CellStatus.OCCUPIED);
        assertBoardRowCell(boardFollowingScoringPath.getCell(2, 5), CellColour.RED, CellStatus.OCCUPIED);

        assertBoardRowCell(boardFollowingScoringPath.getCell(3, 2), CellColour.GREEN, CellStatus.OCCUPIED);
        assertBoardRowCell(boardFollowingScoringPath.getCell(3, 3), CellColour.GREEN, CellStatus.OCCUPIED);

        assertBoardRowCell(boardFollowingScoringPath.getCell(4, 2), CellColour.RED, CellStatus.OCCUPIED);
    }

    private ScoreNode getScoreNode(ScoreNode eighthChild, int nodeIndex, Orientation vertical) {
        final ScoreNode seventhChild = new ScoreNode(nodeIndex, 0, vertical);
        seventhChild.addChild(eighthChild);
        return seventhChild;
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

    private void assertBoardRowCell(Cell cell, CellColour cellColour, CellStatus cellStatus) {
        assertThat("Board first element is " + cellColour, cell.getCellColour(), is(cellColour));
        assertThat("Board first element is " + cellStatus, cell.getCellStatus(), is(cellStatus));
    }

    private void assertBlockData(Block block, CellColour firstColour, CellColour secondColour) {
        final Cell[] cells = block.getCells();
        final CellColour topCellColour = cells[0].getCellColour();
        final CellColour bottomCellColour = cells[1].getCellColour();
        assertThat("Top cell should be " + firstColour, topCellColour, is(firstColour));
        assertThat("Bottom cell should be " + secondColour, bottomCellColour, is(secondColour));
    }
}
