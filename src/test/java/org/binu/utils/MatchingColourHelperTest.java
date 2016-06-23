package org.binu.utils;

import org.binu.ai.movemaker.TestHelper;
import org.binu.board.Block;
import org.binu.board.Board;
import org.binu.board.Cell;
import org.binu.data.CellColour;
import org.binu.data.CellStatus;
import org.binu.data.Orientation;
import org.binu.data.OrientationAndIndex;
import org.binu.framework.CellArrayHelperImpl;
import org.binu.integration.DataParser;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Test for {@link MatchingColourHelper}
 */
public class MatchingColourHelperTest {

    private TestHelper testHelper;
    private DataParser dataParser;
    private MatchingColourHelper matchingColourHelper;
    private Board board;
    private String[] emptyBoardString;
    private Block block;
    private CellArrayHelperImpl cellArrayHelper;

    @Before
    public void setUp() throws Exception {
        testHelper = new TestHelper();
        dataParser = new DataParser();
        cellArrayHelper = new CellArrayHelperImpl();
        matchingColourHelper = new MatchingColourHelper(cellArrayHelper);
        emptyBoardString = testHelper.getEmptyBoardString();
    }

    /*
        Vertical tests
     */
    @Test
    public void should_have_no_matching_cells_in_first_column_for_green_red_block_vertically() throws Exception {
        emptyBoardString[11] = "..2...";
        board = dataParser.createBoard(emptyBoardString);

        final Cell[] cells = getCells(CellColour.GREEN, CellColour.RED);
        block = new Block(cells);

        final int columnIndex = 0;
        final OrientationAndIndex orientationAndIndex = new OrientationAndIndex(Orientation.VERTICAL, columnIndex);
        final boolean hasMatchingColour = matchingColourHelper.columnHasMatchingColour(board, block, orientationAndIndex);

        assertThat("First column has no matching cells.", hasMatchingColour, is(false));
    }

    @Test
    public void should_have_no_matching_cells_in_first_column_for_green_red_block_vertically2() throws Exception {
        emptyBoardString[11] = ".2....";
        board = dataParser.createBoard(emptyBoardString);

        final Cell[] cells = getCells(CellColour.GREEN, CellColour.RED);
        block = new Block(cells);

        final int columnIndex = 0;
        final OrientationAndIndex orientationAndIndex = new OrientationAndIndex(Orientation.VERTICAL, columnIndex);
        final boolean hasMatchingColour = matchingColourHelper.columnHasMatchingColour(board, block, orientationAndIndex);

        assertThat("First column has no matching cells.", hasMatchingColour, is(false));
    }

    @Test
    public void should_have_matching_cells_in_first_column_for_red_green_block_vertically_for_second_cell() throws Exception {
        emptyBoardString[11] = ".2....";
        board = dataParser.createBoard(emptyBoardString);

        final Cell[] cells = getCells(CellColour.RED, CellColour.GREEN);
        block = new Block(cells);

        final int columnIndex = 0;
        final OrientationAndIndex orientationAndIndex = new OrientationAndIndex(Orientation.VERTICAL, columnIndex);
        final boolean hasMatchingColour = matchingColourHelper.columnHasMatchingColour(board, block, orientationAndIndex);

        assertThat("First column has matching cells.", hasMatchingColour, is(true));
    }

    @Test
    public void should_have_matching_cells_in_first_column_for_red_green_block_vertically() throws Exception {
        emptyBoardString[10] = ".4....";
        emptyBoardString[11] = ".1....";
        board = dataParser.createBoard(emptyBoardString);

        final Cell[] cells = getCells(CellColour.RED, CellColour.GREEN);
        block = new Block(cells);

        final int columnIndex = 0;
        final OrientationAndIndex orientationAndIndex = new OrientationAndIndex(Orientation.VERTICAL, columnIndex);
        final boolean hasMatchingColour = matchingColourHelper.columnHasMatchingColour(board, block, orientationAndIndex);

        assertThat("First column has matching cells.", hasMatchingColour, is(true));
    }

    @Test
    public void should_have_matching_cells_in_fifth_column_for_red_green_block_vertically() throws Exception {
        emptyBoardString[10] = "....4.";
        emptyBoardString[11] = "....1.";
        board = dataParser.createBoard(emptyBoardString);

        final Cell[] cells = getCells(CellColour.RED, CellColour.GREEN);
        block = new Block(cells);

        final int columnIndex = 5;
        final OrientationAndIndex orientationAndIndex = new OrientationAndIndex(Orientation.VERTICAL, columnIndex);
        final boolean hasMatchingColour = matchingColourHelper.columnHasMatchingColour(board, block, orientationAndIndex);

        assertThat("First column has matching cells.", hasMatchingColour, is(true));
    }

    @Test
    public void should_have_matching_cells_in_second_column_for_red_green_block_vertically() throws Exception {
        emptyBoardString[11] = "..2...";
        board = dataParser.createBoard(emptyBoardString);

        final Cell[] cells = getCells(CellColour.RED, CellColour.GREEN);
        block = new Block(cells);

        final int columnIndex = 1;
        final OrientationAndIndex orientationAndIndex = new OrientationAndIndex(Orientation.VERTICAL, columnIndex);
        final boolean hasMatchingColour = matchingColourHelper.columnHasMatchingColour(board, block, orientationAndIndex);

        assertThat("First column has no matching cells.", hasMatchingColour, is(true));
    }

    @Test
    public void should_have_matching_cells_in_third_column_for_red_green_block_vertically() throws Exception {
        emptyBoardString[10] = ".4....";
        emptyBoardString[11] = ".1....";
        board = dataParser.createBoard(emptyBoardString);

        final Cell[] cells = getCells(CellColour.RED, CellColour.GREEN);
        block = new Block(cells);

        final int columnIndex = 2;
        final OrientationAndIndex orientationAndIndex = new OrientationAndIndex(Orientation.VERTICAL, columnIndex);
        final boolean hasMatchingColour = matchingColourHelper.columnHasMatchingColour(board, block, orientationAndIndex);

        assertThat("First column has matching cells.", hasMatchingColour, is(true));
    }

    @Test
    public void should_have_matching_cells_in_second_column_for_red_green_block_vertically_right_below_the_cell() throws Exception {
        emptyBoardString[10] = ".2....";
        emptyBoardString[11] = ".1....";
        board = dataParser.createBoard(emptyBoardString);

        final Cell[] cells = getCells(CellColour.RED, CellColour.GREEN);
        block = new Block(cells);

        final int columnIndex = 1;
        final OrientationAndIndex orientationAndIndex = new OrientationAndIndex(Orientation.VERTICAL, columnIndex);
        final boolean hasMatchingColour = matchingColourHelper.columnHasMatchingColour(board, block, orientationAndIndex);

        assertThat("First column has matching cells.", hasMatchingColour, is(true));
    }

    /*
        Reversed vertical tests
     */
    @Test
    public void should_have_no_matching_cells_in_first_column_for_red_green_block_vertically() throws Exception {
        emptyBoardString[11] = "..2...";
        board = dataParser.createBoard(emptyBoardString);

        final Cell[] cells = getCells(CellColour.RED, CellColour.GREEN);
        block = new Block(cells);

        final int columnIndex = 1;
        final OrientationAndIndex orientationAndIndex = new OrientationAndIndex(Orientation.VERTICAL_REVERSED, columnIndex);
        final boolean hasMatchingColour = matchingColourHelper.columnHasMatchingColour(board, block, orientationAndIndex);

        assertThat("First column has no matching cells.", hasMatchingColour, is(false));
    }

    @Test
    public void should_have_matching_cells_in_first_column_for_green_red_block_reverse_vertically_for_second_cell() throws Exception {
        emptyBoardString[11] = ".2....";
        board = dataParser.createBoard(emptyBoardString);

        final Cell[] cells = getCells(CellColour.GREEN, CellColour.RED);
        block = new Block(cells);

        final int columnIndex = 0;
        final OrientationAndIndex orientationAndIndex = new OrientationAndIndex(Orientation.VERTICAL_REVERSED, columnIndex);
        final boolean hasMatchingColour = matchingColourHelper.columnHasMatchingColour(board, block, orientationAndIndex);

        assertThat("First column has matching cells.", hasMatchingColour, is(true));
    }

    @Test
    public void should_have_matching_cells_in_first_column_for_green_red_block_reverse_vertically() throws Exception {
        emptyBoardString[10] = ".4....";
        emptyBoardString[11] = ".1....";
        board = dataParser.createBoard(emptyBoardString);

        final Cell[] cells = getCells(CellColour.GREEN, CellColour.RED);
        block = new Block(cells);

        final int columnIndex = 0;
        final OrientationAndIndex orientationAndIndex = new OrientationAndIndex(Orientation.VERTICAL_REVERSED, columnIndex);
        final boolean hasMatchingColour = matchingColourHelper.columnHasMatchingColour(board, block, orientationAndIndex);

        assertThat("First column has matching cells.", hasMatchingColour, is(true));
    }

    @Test
    public void should_have_matching_cells_in_fifth_column_for_green_red_block_reverse_vertically() throws Exception {
        emptyBoardString[10] = "....4.";
        emptyBoardString[11] = "....1.";
        board = dataParser.createBoard(emptyBoardString);

        final Cell[] cells = getCells(CellColour.GREEN, CellColour.RED);
        block = new Block(cells);

        final int columnIndex = 5;
        final OrientationAndIndex orientationAndIndex = new OrientationAndIndex(Orientation.VERTICAL_REVERSED, columnIndex);
        final boolean hasMatchingColour = matchingColourHelper.columnHasMatchingColour(board, block, orientationAndIndex);

        assertThat("First column has matching cells.", hasMatchingColour, is(true));
    }

    @Test
    public void should_have_matching_cells_in_second_column_for_green_red_block_reverse_vertically() throws Exception {
        emptyBoardString[11] = "..2...";
        board = dataParser.createBoard(emptyBoardString);

        final Cell[] cells = getCells(CellColour.GREEN, CellColour.RED);
        block = new Block(cells);

        final int columnIndex = 1;
        final OrientationAndIndex orientationAndIndex = new OrientationAndIndex(Orientation.VERTICAL_REVERSED, columnIndex);
        final boolean hasMatchingColour = matchingColourHelper.columnHasMatchingColour(board, block, orientationAndIndex);

        assertThat("First column has no matching cells.", hasMatchingColour, is(true));
    }

    @Test
    public void should_have_matching_cells_in_third_column_for_green_red_block_reverse_vertically() throws Exception {
        emptyBoardString[10] = ".4....";
        emptyBoardString[11] = ".1....";
        board = dataParser.createBoard(emptyBoardString);

        final Cell[] cells = getCells(CellColour.GREEN, CellColour.RED);
        block = new Block(cells);

        final int columnIndex = 2;
        final OrientationAndIndex orientationAndIndex = new OrientationAndIndex(Orientation.VERTICAL_REVERSED, columnIndex);
        final boolean hasMatchingColour = matchingColourHelper.columnHasMatchingColour(board, block, orientationAndIndex);

        assertThat("First column has matching cells.", hasMatchingColour, is(true));
    }

    @Test
    public void should_have_matching_cells_in_second_column_for_red_green_block_reverse_vertically_right_below_the_cell() throws Exception {
        emptyBoardString[10] = ".4....";
        emptyBoardString[11] = ".1....";
        board = dataParser.createBoard(emptyBoardString);

        final Cell[] cells = getCells(CellColour.RED, CellColour.GREEN);
        block = new Block(cells);

        final int columnIndex = 1;
        final OrientationAndIndex orientationAndIndex = new OrientationAndIndex(Orientation.VERTICAL_REVERSED, columnIndex);
        final boolean hasMatchingColour = matchingColourHelper.columnHasMatchingColour(board, block, orientationAndIndex);

        assertThat("First column has matching cells.", hasMatchingColour, is(true));
    }

    /*
        Reversed Horizontal tests
     */
    @Test
    public void should_have_no_matching_cells_in_third_column_for_red_green_block_reverse_horizontally() throws Exception {
        emptyBoardString[11] = ".32...";
        board = dataParser.createBoard(emptyBoardString);

        final Cell[] cells = getCells(CellColour.RED, CellColour.GREEN);
        block = new Block(cells);

        final int columnIndex = 2;
        final OrientationAndIndex orientationAndIndex = new OrientationAndIndex(Orientation.HORIZONTAL_REVERSED, columnIndex);
        final boolean hasMatchingColour = matchingColourHelper.columnHasMatchingColour(board, block, orientationAndIndex);

        assertThat("First column has no matching cells.", hasMatchingColour, is(false));
    }

    @Test
    public void should_have_matching_cells_in_fourth_column_for_red_green_block_reverse_horizontally() throws Exception {
        emptyBoardString[11] = ".32...";
        board = dataParser.createBoard(emptyBoardString);

        final Cell[] cells = getCells(CellColour.RED, CellColour.GREEN);
        block = new Block(cells);

        final int columnIndex = 3;
        final OrientationAndIndex orientationAndIndex = new OrientationAndIndex(Orientation.HORIZONTAL_REVERSED, columnIndex);
        final boolean hasMatchingColour = matchingColourHelper.columnHasMatchingColour(board, block, orientationAndIndex);

        assertThat("First column has no matching cells.", hasMatchingColour, is(true));
    }

    @Test
    public void should_have_matching_cells_in_fourth_column_for_red_green_block_reverse_horizontally2() throws Exception {
        emptyBoardString[10] = "...3..";
        emptyBoardString[11] = ".2.3..";
        board = dataParser.createBoard(emptyBoardString);

        final Cell[] cells = getCells(CellColour.RED, CellColour.GREEN);
        block = new Block(cells);

        final int columnIndex = 3;
        final OrientationAndIndex orientationAndIndex = new OrientationAndIndex(Orientation.HORIZONTAL_REVERSED, columnIndex);
        final boolean hasMatchingColour = matchingColourHelper.columnHasMatchingColour(board, block, orientationAndIndex);

        assertThat("First column has no matching cells.", hasMatchingColour, is(true));
    }

    @Test
    public void should_have_matching_cells_in_sixth_column_for_red_green_block_reverse_horizontally() throws Exception {
        emptyBoardString[11] = "...32.";
        board = dataParser.createBoard(emptyBoardString);

        final Cell[] cells = getCells(CellColour.RED, CellColour.GREEN);
        block = new Block(cells);

        final int columnIndex = 5;
        final OrientationAndIndex orientationAndIndex = new OrientationAndIndex(Orientation.HORIZONTAL_REVERSED, columnIndex);
        final boolean hasMatchingColour = matchingColourHelper.columnHasMatchingColour(board, block, orientationAndIndex);

        assertThat("First column has no matching cells.", hasMatchingColour, is(true));
    }

    @Test
    public void should_have_matching_cells_in_first_column_for_red_green_block_reverse_horizontally() throws Exception {
        emptyBoardString[11] = "211...";
        board = dataParser.createBoard(emptyBoardString);

        final Cell[] cells = getCells(CellColour.RED, CellColour.GREEN);
        block = new Block(cells);

        final int columnIndex = 1;
        final OrientationAndIndex orientationAndIndex = new OrientationAndIndex(Orientation.HORIZONTAL_REVERSED, columnIndex);
        final boolean hasMatchingColour = matchingColourHelper.columnHasMatchingColour(board, block, orientationAndIndex);

        assertThat("First column has no matching cells.", hasMatchingColour, is(true));
    }
    
    /*
        Horizontal tests
     */
    @Test
    public void should_have_no_matching_cells_in_first_column_for_green_red_block_horizontally() throws Exception {
        emptyBoardString[11] = ".32...";
        board = dataParser.createBoard(emptyBoardString);

        final Cell[] cells = getCells(CellColour.GREEN, CellColour.RED);
        block = new Block(cells);

        final int columnIndex = 0;
        final OrientationAndIndex orientationAndIndex = new OrientationAndIndex(Orientation.HORIZONTAL, columnIndex);
        final boolean hasMatchingColour = matchingColourHelper.columnHasMatchingColour(board, block, orientationAndIndex);

        assertThat("First column has no matching cells.", hasMatchingColour, is(false));
    }

    @Test
    public void should_have_matching_cells_in_third_column_for_green_red_block_horizontally() throws Exception {
        emptyBoardString[11] = ".32...";
        board = dataParser.createBoard(emptyBoardString);

        final Cell[] cells = getCells(CellColour.GREEN, CellColour.RED);
        block = new Block(cells);

        final int columnIndex = 2;
        final OrientationAndIndex orientationAndIndex = new OrientationAndIndex(Orientation.HORIZONTAL, columnIndex);
        final boolean hasMatchingColour = matchingColourHelper.columnHasMatchingColour(board, block, orientationAndIndex);

        assertThat("First column has no matching cells.", hasMatchingColour, is(true));
    }

    @Test
    public void should_have_matching_cells_in_fourth_column_for_green_red_block_horizontally() throws Exception {
        emptyBoardString[11] = ".32...";
        board = dataParser.createBoard(emptyBoardString);

        final Cell[] cells = getCells(CellColour.GREEN, CellColour.RED);
        block = new Block(cells);

        final int columnIndex = 3;
        final OrientationAndIndex orientationAndIndex = new OrientationAndIndex(Orientation.HORIZONTAL, columnIndex);
        final boolean hasMatchingColour = matchingColourHelper.columnHasMatchingColour(board, block, orientationAndIndex);

        assertThat("First column has no matching cells.", hasMatchingColour, is(true));
    }


    @Test
    public void should_have_matching_cells_in_third_column_for_green_red_block_horizontally2() throws Exception {
        emptyBoardString[10] = "...3..";
        emptyBoardString[11] = ".2.3..";
        board = dataParser.createBoard(emptyBoardString);

        final Cell[] cells = getCells(CellColour.GREEN, CellColour.RED);
        block = new Block(cells);

        final int columnIndex = 2;
        final OrientationAndIndex orientationAndIndex = new OrientationAndIndex(Orientation.HORIZONTAL, columnIndex);
        final boolean hasMatchingColour = matchingColourHelper.columnHasMatchingColour(board, block, orientationAndIndex);

        assertThat("First column has no matching cells.", hasMatchingColour, is(true));
    }

    @Test
    public void should_have_matching_cells_in_sixth_column_for_green_red_block_horizontally() throws Exception {
        emptyBoardString[11] = "...32.";
        board = dataParser.createBoard(emptyBoardString);

        final Cell[] cells = getCells(CellColour.GREEN, CellColour.RED);
        block = new Block(cells);

        final int columnIndex = 4;
        final OrientationAndIndex orientationAndIndex = new OrientationAndIndex(Orientation.HORIZONTAL, columnIndex);
        final boolean hasMatchingColour = matchingColourHelper.columnHasMatchingColour(board, block, orientationAndIndex);

        assertThat("First column has no matching cells.", hasMatchingColour, is(true));
    }

    @Test
    public void should_have_matching_cells_in_first_column_for_green_red_block_horizontally() throws Exception {
        emptyBoardString[11] = "211...";
        board = dataParser.createBoard(emptyBoardString);

        final Cell[] cells = getCells(CellColour.GREEN, CellColour.RED);
        block = new Block(cells);

        final int columnIndex = 0;
        final OrientationAndIndex orientationAndIndex = new OrientationAndIndex(Orientation.HORIZONTAL, columnIndex);
        final boolean hasMatchingColour = matchingColourHelper.columnHasMatchingColour(board, block, orientationAndIndex);

        assertThat("First column has no matching cells.", hasMatchingColour, is(true));
    }

    @NotNull
    private Cell[] getCells(CellColour cellColour1, CellColour cellColour2) {
        final Cell[] cells = new Cell[2];
        cells[0] = new Cell(cellColour1, CellStatus.OCCUPIED);
        cells[1] = new Cell(cellColour2, CellStatus.OCCUPIED);
        return cells;
    }
}
