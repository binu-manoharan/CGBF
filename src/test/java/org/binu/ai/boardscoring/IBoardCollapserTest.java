package org.binu.ai.boardscoring;

import org.binu.ai.simple.CellArrayHelperImpl;
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
 * Test for {@link IBoardCollapser}
 */
public class IBoardCollapserTest {

    private DataParser dataParser;
    private Board board;
    private CellArrayHelperImpl cellArrayParser;
    private BoardCollapserImpl boardCollapser;

    @Before
    public void setUp() throws Exception {
        dataParser = new DataParser();
        cellArrayParser = new CellArrayHelperImpl();
        boardCollapser = new BoardCollapserImpl(cellArrayParser);
    }

    @Test
    public void should_collapse_a_4_horizontal_row() throws Exception {
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
                ".2345.",
                ".1111."
        };

        board = dataParser.createBoard(boardString);
        final Board collapsedBoard = boardCollapser.collapseBoard(boardCollapser.collapseBoard(board));
        final Cell[] firstRow = collapsedBoard.getRow(0);

        assertRowStatus("First row", firstRow, CellStatus.EMPTY, CellStatus.OCCUPIED, CellStatus.OCCUPIED, CellStatus.OCCUPIED,
                CellStatus.OCCUPIED, CellStatus.EMPTY);
        assertRowColour("Second row", firstRow, null, CellColour.GREEN, CellColour.PURPLE, CellColour.RED, CellColour.YELLOW, null);
    }

    private void assertRowStatus(String messagePrefix, Cell[] firstRow, CellStatus firstCellStatus, CellStatus secondCellStatus,
                                 CellStatus thirdCellStatus, CellStatus fourthCellStatus, CellStatus fifthCellStatus,
                                 CellStatus sixthCellStatus) {
        assertThat(messagePrefix + " [0] should be " + firstCellStatus, firstRow[0].getCellStatus(), is(firstCellStatus));
        assertThat(messagePrefix + " [1] should be " + secondCellStatus, firstRow[1].getCellStatus(), is(secondCellStatus));
        assertThat(messagePrefix + " [2] should be " + thirdCellStatus, firstRow[2].getCellStatus(), is(thirdCellStatus));
        assertThat(messagePrefix + " [3] should be " + fourthCellStatus, firstRow[3].getCellStatus(), is(fourthCellStatus));
        assertThat(messagePrefix + " [4] should be " + fifthCellStatus, firstRow[4].getCellStatus(), is(fifthCellStatus));
        assertThat(messagePrefix + " [5] should be " + sixthCellStatus, firstRow[5].getCellStatus(), is(sixthCellStatus));
    }

    private void assertRowColour(String messagePrefix, Cell[] firstRow, CellColour firstCellColour, CellColour secondCellColour,
                                 CellColour thirdCellColour, CellColour fourthCellColour, CellColour fifthCellColour,
                                 CellColour sixthCellColour) {
        assertThat(messagePrefix + " [0] should be " + firstCellColour, firstRow[0].getCellColour(), is(firstCellColour));
        assertThat(messagePrefix + " [1] should be " + secondCellColour, firstRow[1].getCellColour(), is(secondCellColour));
        assertThat(messagePrefix + " [2] should be " + thirdCellColour, firstRow[2].getCellColour(), is(thirdCellColour));
        assertThat(messagePrefix + " [3] should be " + fourthCellColour, firstRow[3].getCellColour(), is(fourthCellColour));
        assertThat(messagePrefix + " [4] should be " + fifthCellColour, firstRow[4].getCellColour(), is(fifthCellColour));
        assertThat(messagePrefix + " [5] should be " + sixthCellColour, firstRow[5].getCellColour(), is(sixthCellColour));
    }
}
