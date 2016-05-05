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
import static org.junit.Assert.*;

/**
 * Test for {@link IBoardClearer}
 */
public class IBoardClearerTest {
    private DataParser dataParser;
    private Board board;
    private IBoardClearer boardClearer;

    @Before
    public void setUp() throws Exception {
        dataParser = new DataParser();
        final CellArrayParser cellArrayParser = new CellArrayParserImpl();
        boardClearer = new BoardClearerImpl(cellArrayParser);
    }

    @Test
    public void should_clear_a_4_horizontal_row() throws Exception {
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

        final Board clearedBoard = boardClearer.clearBoardByRow(board);
        final Cell[] firstRow = clearedBoard.getRow(0);

        assertRowStatus("First Row", firstRow, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY);
    }

    @Test
    public void should_clear_a_4_vertically_row() throws Exception {
        final String[] boardString = {
                "......",
                "......",
                "......",
                "......",
                "......",
                "......",
                "......",
                "0000..",
                "2010..",
                "2010..",
                "2010..",
                "2010.."
        };

        board = dataParser.createBoard(boardString);

        final Board clearedBoard = boardClearer.clearBoardByColumn(board);
        final Cell[] firstColumn = clearedBoard.getColumn(0);
        final Cell[] secondColumn = clearedBoard.getColumn(1);
        final Cell[] thirdColumn = clearedBoard.getColumn(2);
        final Cell[] fourthColumn = clearedBoard.getColumn(3);

        assertRowStatus("First Column", firstColumn, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY);
        assertRowStatus("Second Column", secondColumn, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.BLOCKED, CellStatus.EMPTY);
        assertRowStatus("Third Column", thirdColumn, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY);
        assertRowStatus("Fourth Column", fourthColumn, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.BLOCKED, CellStatus.EMPTY);
    }

    @Test
    public void should_clear_a_4_horizontal_row_and_remove_blocks() throws Exception {
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

        final Board clearedBoard = boardClearer.clearBoardByRow(board);
        final Cell[] firstRow = clearedBoard.getRow(0);
        final Cell[] secondRow = clearedBoard.getRow(1);

        assertRowStatus("First row", firstRow, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY);
        assertRowStatus("Second row", secondRow, CellStatus.BLOCKED, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY);
    }

    @Test
    public void should_clear_a_4_horizontal_row_and_remove_blocks_from_below() throws Exception {
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
                "000000",
                "011110",
                "000000"
        };

        board = dataParser.createBoard(boardString);

        final Board clearedBoard = boardClearer.clearBoardByRow(board);
        final Cell[] firstRow = clearedBoard.getRow(0);
        final Cell[] secondRow = clearedBoard.getRow(1);
        final Cell[] thirdRow = clearedBoard.getRow(2);

        assertRowStatus("First row", firstRow, CellStatus.BLOCKED, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.BLOCKED);
        assertRowStatus("Second row", secondRow, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY);
        assertRowStatus("Third row", thirdRow, CellStatus.BLOCKED, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.BLOCKED);
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
