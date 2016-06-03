package org.binu.ai.boardscoring;

import org.binu.ai.framework.BoardClearer;
import org.binu.ai.framework.BoardClearerImpl;
import org.binu.ai.framework.CellArrayHelper;
import org.binu.ai.framework.CellArrayHelperImpl;
import org.binu.board.Board;
import org.binu.board.Cell;
import org.binu.data.CellStatus;
import org.binu.integration.DataParser;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Test for {@link BoardClearer}
 */
public class BoardClearerTest {
    private DataParser dataParser;
    private Board board;
    private BoardClearer boardClearer;

    @Before
    public void setUp() throws Exception {
        dataParser = new DataParser();
        final CellArrayHelper cellArrayHelper = new CellArrayHelperImpl();
        boardClearer = new BoardClearerImpl(cellArrayHelper);
    }

    @Test
    public void should_clear_a_2x2_square() throws Exception {
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
                "0212..",
                "01111.",
                "011000"
        };

        board = dataParser.createBoard(boardString);

        boardClearer.clearBoardBySquare(board);
        final Cell[] firstRow = board.getRow(0);
        final Cell[] secondRow = board.getRow(1);
        final Cell[] thirdRow = board.getRow(2);

        assertRowStatus("First Row", firstRow, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.BLOCKED);
        assertRowStatus("Second Row", secondRow, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY);
        assertRowStatus("Third Row", thirdRow, CellStatus.BLOCKED, CellStatus.OCCUPIED, CellStatus.EMPTY, CellStatus.OCCUPIED, CellStatus.EMPTY, CellStatus.EMPTY);
    }

    @Test
    public void should_clear_a_2x2_square_across_the_board() throws Exception {
        final String[] boardString = {
                "......",
                "......",
                "......",
                "......",
                "......",
                "......",
                "......",
                ".44...",
                ".44.33",
                "021233",
                "011113",
                "011000"
        };

        board = dataParser.createBoard(boardString);

        boardClearer.clearBoardBySquare(board);
        final Cell[] firstRow = board.getRow(0);
        final Cell[] secondRow = board.getRow(1);
        final Cell[] thirdRow = board.getRow(2);
        final Cell[] fourthRow = board.getRow(3);
        final Cell[] fifthRow = board.getRow(4);

        assertRowStatus("First Row", firstRow, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY);
        assertRowStatus("Second Row", secondRow, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY);
        assertRowStatus("Third Row", thirdRow, CellStatus.BLOCKED, CellStatus.OCCUPIED, CellStatus.EMPTY, CellStatus.OCCUPIED, CellStatus.EMPTY, CellStatus.EMPTY);
        assertRowStatus("Fourth Row", fourthRow, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY);
        assertRowStatus("FIFTH Row", fifthRow, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY);
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

        boardClearer.clearBoardByLine(board);
        final Cell[] firstRow = board.getRow(0);

        assertRowStatus("First Row", firstRow, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY);
    }

    @Test
    public void should_clear_a_4_vertically_column() throws Exception {
        final String[] boardString = {
                "......",
                "......",
                "......",
                "1.....",
                "1.....",
                "1.....",
                "1.....",
                "0000..",
                "2010..",
                "2010..",
                "2010..",
                "2010.."
        };

        board = dataParser.createBoard(boardString);

        boardClearer.clearBoardByLine(board);
        final Cell[] firstColumn = board.getColumn(0);
        final Cell[] secondColumn = board.getColumn(1);
        final Cell[] thirdColumn = board.getColumn(2);
        final Cell[] fourthColumn = board.getColumn(3);

        assertRowStatus("First Column", firstColumn, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY);
        assertRowStatus("Second Column", secondColumn, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.BLOCKED, CellStatus.EMPTY);
        assertRowStatus("Third Column", thirdColumn, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY);
        assertRowStatus("Fourth Column", fourthColumn, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.BLOCKED, CellStatus.EMPTY);
        assertThat("The second vertical line on first column is cleared", board.getCell(0, 5).getCellStatus(), is(CellStatus.EMPTY));
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

        boardClearer.clearBoardByLine(board);
        final Cell[] firstRow = board.getRow(0);
        final Cell[] secondRow = board.getRow(1);

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

        boardClearer.clearBoardByLine(board);
        final Cell[] firstRow = board.getRow(0);
        final Cell[] secondRow = board.getRow(1);
        final Cell[] thirdRow = board.getRow(2);

        assertRowStatus("First row", firstRow, CellStatus.BLOCKED, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.BLOCKED);
        assertRowStatus("Second row", secondRow, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY);
        assertRowStatus("Third row", thirdRow, CellStatus.BLOCKED, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.BLOCKED);
    }

    @Test
    public void should_clear_a_4_T_and_L_block() throws Exception {
        final String[] boardString = {
                "......",
                "......",
                "......",
                "......",
                "......",
                "......",
                "......",
                "......",
                "00....",
                "100020",
                "110020",
                "100022"
        };

        board = dataParser.createBoard(boardString);

        boardClearer.clearBoardByTAndL(board);
        final Cell[] firstRow = board.getRow(0);
        final Cell[] secondRow = board.getRow(1);
        final Cell[] thirdRow = board.getRow(2);
        final Cell[] fourthRow = board.getRow(3);

        assertRowStatus("First row", firstRow, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.BLOCKED, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY);
        assertRowStatus("Second row", secondRow, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY);
        assertRowStatus("Third row", thirdRow, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.BLOCKED, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY);
        assertRowStatus("Fourth row", fourthRow, CellStatus.EMPTY, CellStatus.BLOCKED, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY);
    }

    @Test
    public void should_clear_all_shapes_from_board() throws Exception {
        final String[] boardString = {
                "......",
                "......",
                "......",
                "......",
                "......",
                "......",
                "......",
                "......",
                "444111",
                "453312",
                "555332",
                "111122"
        };

        board = dataParser.createBoard(boardString);

        boardClearer.clearBoard(board);
        final Cell[] firstRow = board.getRow(0);
        final Cell[] secondRow = board.getRow(1);
        final Cell[] thirdRow = board.getRow(2);
        final Cell[] fourthRow = board.getRow(3);

        assertRowStatus("First row", firstRow, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY);
        assertRowStatus("Second row", secondRow, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY);
        assertRowStatus("Third row", thirdRow, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY);
        assertRowStatus("Fourth row", fourthRow, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY);
    }

    @Test
    public void should_clear_a_Z_block() throws Exception {
        final String[] boardString = {
                "......",
                "......",
                "......",
                "......",
                "......",
                "......",
                "......",
                ".3..44",
                ".3344.",
                "013000",
                "110220",
                "100022"
        };

        board = dataParser.createBoard(boardString);

        boardClearer.clearBoardByZ(board);
        final Cell[] firstRow = board.getRow(0);
        final Cell[] secondRow = board.getRow(1);
        final Cell[] thirdRow = board.getRow(2);
        final Cell[] fourthRow = board.getRow(3);
        final Cell[] fifthRow = board.getRow(3);

        assertRowStatus("First row", firstRow, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.BLOCKED, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY);
        assertRowStatus("Second row", secondRow, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY);
        assertRowStatus("Third row", thirdRow, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.BLOCKED);
        assertRowStatus("Fourth row", fourthRow, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY);
        assertRowStatus("Fifth row", fifthRow, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY);
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
