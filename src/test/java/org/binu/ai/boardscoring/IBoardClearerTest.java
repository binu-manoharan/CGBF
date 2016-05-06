package org.binu.ai.boardscoring;

import org.binu.ai.simple.CellArrayHelper;
import org.binu.ai.simple.CellArrayHelperImpl;
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

        final Board clearedBoard = boardClearer.clearBoardBySquare(board);
        final Cell[] firstRow = clearedBoard.getRow(0);
        final Cell[] secondRow = clearedBoard.getRow(1);
        final Cell[] thirdRow = clearedBoard.getRow(2);

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

        final Board clearedBoard = boardClearer.clearBoardBySquare(board);
        final Cell[] firstRow = clearedBoard.getRow(0);
        final Cell[] secondRow = clearedBoard.getRow(1);
        final Cell[] thirdRow = clearedBoard.getRow(2);
        final Cell[] fourthRow = clearedBoard.getRow(3);
        final Cell[] fifthRow = clearedBoard.getRow(4);

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

        final Board clearedBoard = boardClearer.clearBoardByRow(board);
        final Cell[] firstRow = clearedBoard.getRow(0);

        assertRowStatus("First Row", firstRow, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY);
    }

    //TODO What if there are two of these in a column / convert to list points
    @Test
    public void should_clear_a_4_vertically_column() throws Exception {
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

        final Board clearedBoard = boardClearer.clearBoardByTAndL(board);
        final Cell[] firstRow = clearedBoard.getRow(0);
        final Cell[] secondRow = clearedBoard.getRow(1);
        final Cell[] thirdRow = clearedBoard.getRow(2);
        final Cell[] fourthRow = clearedBoard.getRow(3);

        assertRowStatus("First row", firstRow, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.BLOCKED, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY);
        assertRowStatus("Second row", secondRow, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY);
        assertRowStatus("Third row", thirdRow, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.BLOCKED, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY);
        assertRowStatus("Fourth row", fourthRow, CellStatus.EMPTY, CellStatus.BLOCKED, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY);
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
