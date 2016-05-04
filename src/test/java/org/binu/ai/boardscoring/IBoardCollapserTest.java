package org.binu.ai.boardscoring;

import org.binu.ai.simple.CellArrayParser;
import org.binu.ai.simple.CellArrayParserImpl;
import org.binu.board.Board;
import org.binu.board.Cell;
import org.binu.data.CellStatus;
import org.binu.integration.DataParser;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

/**
 * Test for {@link IBoardCollapser}
 */
public class IBoardCollapserTest {

    private DataParser dataParser;
    private Board board;
    private CellArrayParser cellArrayParser;
    private IBoardCollapser boardCollapser;

    @Before
    public void setUp() throws Exception {
        dataParser = new DataParser();
        cellArrayParser = new CellArrayParserImpl();
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
                "......",
                ".1111."
        };

        board = dataParser.createBoard(boardString);

        final Board collapsedBoard = boardCollapser.collapseBoard(board);
        final Cell[] firstRow = collapsedBoard.getRow(0);

        assertThat("The first/bottom row should exist.", firstRow, is(not(nullValue())));
        assertRowStatus("First Row", firstRow, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY);

    }

    @Test
    public void should_collapse_a_4_horizontal_row_and_remove_blocks() throws Exception {
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
                "000...",
                "011110"
        };

        board = dataParser.createBoard(boardString);

        final Board collapsedBoard = boardCollapser.collapseBoard(board);
        final Cell[] firstRow = collapsedBoard.getRow(0);
        final Cell[] secondRow = collapsedBoard.getRow(1);

        assertThat("The first/bottom row should exist.", firstRow, is(not(nullValue())));
        assertRowStatus("First row", firstRow, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY);
        assertRowStatus("Second row", secondRow, CellStatus.BLOCKED, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY);
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


}