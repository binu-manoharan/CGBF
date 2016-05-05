package org.binu.ai;

import org.binu.ai.simple.CellArrayParser;
import org.binu.ai.simple.CellArrayParserImpl;
import org.binu.board.Block;
import org.binu.board.Board;
import org.binu.board.Cell;
import org.binu.data.CellColour;
import org.binu.data.CellStatus;
import org.binu.integration.DataParser;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Test for {@link CellArrayParser}.
 */
public class CellArrayParserTest {

    private Cell[] cells;
    private CellArrayParser cellArrayParser;
    private Block block;

    @Before
    public void setUp() throws Exception {
        cells = new Cell[12];
        cellArrayParser = new CellArrayParserImpl();

        initEmptyCells();
    }

    @Test
    public void should_return_top_of_column_as_2() throws Exception {
        cells[0] = getCell(CellColour.GREEN, CellStatus.OCCUPIED);
        cells[1] = getCell(CellColour.RED, CellStatus.OCCUPIED);

        final int firstEmptyPosition = cellArrayParser.getFirstEmptyPosition(cells);

        assertThat("First empty position is 2 (zero indexed)", firstEmptyPosition, is(2));
    }

    @Test
    public void should_return_top_of_column_as_3() throws Exception {
        cells[0] = getCell(CellColour.GREEN, CellStatus.OCCUPIED);
        cells[1] = getCell(CellColour.RED, CellStatus.OCCUPIED);
        cells[2] = getCell(CellColour.BLUE, CellStatus.OCCUPIED);

        final int firstEmptyPosition = cellArrayParser.getFirstEmptyPosition(cells);

        assertThat("First empty position is 3 (zero indexed)", firstEmptyPosition, is(3));
    }

    @Test
    public void should_provide_score_of_0_if_the_column_is_empty() throws Exception {
        block = getBlock();
        final int score = cellArrayParser.getCellArrayScore(cells, block);
        assertThat("Score should be 0 for an empty column", score, is(0));
    }

    @Test
    public void should_provide_score_of_minus_1_for_a_column_with_1_existing_cell_with_different_colour() throws Exception {
        cells[0] = getCell(CellColour.RED, CellStatus.OCCUPIED);

        block = getBlock();

        final int score = cellArrayParser.getCellArrayScore(cells, block);
        assertThat("Score should be -1 for a column with an existing cell with different colour", score, is(-1));
    }

    @Test
    public void should_provide_score_of_1_for_a_column_with_4_consecutive_blocks_with_same_colour() throws Exception {
        cells[0] = getCell(CellColour.GREEN, CellStatus.OCCUPIED);
        cells[1] = getCell(CellColour.GREEN, CellStatus.OCCUPIED);

        block = getBlock();

        final int score = cellArrayParser.getCellArrayScore(cells, block);

        assertThat("Score should be 1 for a match of 4", score, is(1));
    }

    @Test
    public void should_provide_the_index_of_first_repeated_4th_element() {
        cells[0] = getCell(CellColour.GREEN, CellStatus.OCCUPIED);
        cells[1] = getCell(CellColour.GREEN, CellStatus.OCCUPIED);
        cells[2] = getCell(CellColour.GREEN, CellStatus.OCCUPIED);
        cells[3] = getCell(CellColour.GREEN, CellStatus.OCCUPIED);
        cells[4] = getCell(CellColour.RED, CellStatus.OCCUPIED);
        cells[5] = getCell(CellColour.BLUE, CellStatus.OCCUPIED);

        final int firstIndexOfRepeatOf4Group = cellArrayParser.getFirstIndexOfRepeatOf4Group(cells);
        assertThat("First index of repeated group is 3", firstIndexOfRepeatOf4Group, is(3));
    }

    @Test
    public void should_provide_the_index_of_minus_one_as_there_are_no_repeated_groups_of_4() {
        cells[0] = getCell(CellColour.RED, CellStatus.OCCUPIED);
        cells[1] = getCell(CellColour.RED, CellStatus.OCCUPIED);
        cells[2] = getCell(CellColour.RED, CellStatus.OCCUPIED);
        cells[3] = getCell(CellColour.GREEN, CellStatus.OCCUPIED);
        cells[4] = getCell(CellColour.GREEN, CellStatus.OCCUPIED);
        cells[5] = getCell(CellColour.GREEN, CellStatus.OCCUPIED);

        final int firstIndexOfRepeatOf4Group = cellArrayParser.getFirstIndexOfRepeatOf4Group(cells);
        assertThat("First index of repeated group is 3", firstIndexOfRepeatOf4Group, is(-1));
    }

    @Test
    public void should_provide_the_index_of_minus_one_as_there_is_an_empty_cell_in_between() {
        cells[0] = getCell(CellColour.RED, CellStatus.OCCUPIED);
        cells[1] = getCell(CellColour.RED, CellStatus.OCCUPIED);
        cells[2] = getCell(CellColour.RED, CellStatus.OCCUPIED);
        cells[3] = getCell(null, CellStatus.EMPTY);
        cells[4] = getCell(CellColour.RED, CellStatus.OCCUPIED);
        cells[5] = getCell(CellColour.RED, CellStatus.OCCUPIED);

        final int firstIndexOfRepeatOf4Group = cellArrayParser.getFirstIndexOfRepeatOf4Group(cells);
        assertThat("First index of repeated group is 3", firstIndexOfRepeatOf4Group, is(-1));
    }

    @Test
    public void should_provide_the_indexes_of_the_elements_in_the_form_of_2x2_grid() throws Exception {
        final Cell[][] cellArray = new Cell[2][6];
        cellArray[0][0] = getCell(CellColour.RED, CellStatus.OCCUPIED);
        cellArray[0][1] = getCell(CellColour.RED, CellStatus.OCCUPIED);
        cellArray[0][2] = getCell(CellColour.RED, CellStatus.OCCUPIED);
        cellArray[0][3] = getCell(CellColour.GREEN, CellStatus.OCCUPIED);
        cellArray[0][4] = getCell(CellColour.RED, CellStatus.OCCUPIED);
        cellArray[0][5] = getCell(CellColour.RED, CellStatus.OCCUPIED);

        cellArray[1][0] = getCell(CellColour.RED, CellStatus.OCCUPIED);
        cellArray[1][1] = getCell(CellColour.RED, CellStatus.OCCUPIED);
        cellArray[1][2] = getCell(CellColour.RED, CellStatus.OCCUPIED);
        cellArray[1][3] = getCell(CellColour.RED, CellStatus.OCCUPIED);
        cellArray[1][4] = getCell(CellColour.RED, CellStatus.OCCUPIED);
        cellArray[1][5] = getCell(CellColour.RED, CellStatus.OCCUPIED);

        final List<int[]> indexOf4BlockGroup = cellArrayParser.getIndexOf4BlockGroup(cellArray);

        assertThat("Index of block of 4 is", indexOf4BlockGroup.size(), is(2));
        assertBlockItems(indexOf4BlockGroup.get(0)[0], 0, indexOf4BlockGroup.get(0)[1], 0);
        assertBlockItems(indexOf4BlockGroup.get(1)[0], 0, indexOf4BlockGroup.get(1)[1], 4);

    }

    @Test
    public void should_provide_the_indexes_of_the_elements_in_the_form_of_2x2_grid_from_the_board() throws Exception {
        final String[] boardString = {
                "....33",
                "....33",
                "......",
                "......",
                "..44..",
                "..44..",
                ".11...",
                ".12...",
                ".22...",
                "022...",
                "111000",
                "111000"
        };
        final DataParser dataParser = new DataParser();
        final CellArrayParser cellArrayParser = new CellArrayParserImpl();
        final Board board = dataParser.createBoard(boardString);

        final List<int[]> indexOf4BlockGroup = cellArrayParser.getIndexOf4BlockGroup(board.getBoard());

        assertThat("Index of block of 4 is", indexOf4BlockGroup.size(), is(4));

        assertBlockItems(indexOf4BlockGroup.get(0)[0], 0, indexOf4BlockGroup.get(0)[1], 0);
        assertBlockItems(indexOf4BlockGroup.get(1)[0], 2, indexOf4BlockGroup.get(1)[1], 1);
        assertBlockItems(indexOf4BlockGroup.get(2)[0], 6, indexOf4BlockGroup.get(2)[1], 2);
        assertBlockItems(indexOf4BlockGroup.get(3)[0], 10, indexOf4BlockGroup.get(3)[1], 4);
    }

    @Test
    public void should_provide_the_indexes_of_the_elements_in_the_form_of_2x2_grid_from_the_board_including_consecutive_ones() throws Exception {
        final String[] boardString = {
                "....33",
                "....33",
                "......",
                "......",
                "..44..",
                "..44..",
                ".22...",
                ".22...",
                ".22...",
                "022...",
                "111000",
                "111000"
        };
        final DataParser dataParser = new DataParser();
        final CellArrayParser cellArrayParser = new CellArrayParserImpl();
        final Board board = dataParser.createBoard(boardString);

        final List<int[]> indexOf4BlockGroup = cellArrayParser.getIndexOf4BlockGroup(board.getBoard());

        //ideally it should be 5 but the extra point comes from the consecutive 2x2 blocks
        assertThat("Index of block of 4 is", indexOf4BlockGroup.size(), is(6));

        assertBlockItems(indexOf4BlockGroup.get(0)[0], 0, indexOf4BlockGroup.get(0)[1], 0);
        assertBlockItems(indexOf4BlockGroup.get(1)[0], 2, indexOf4BlockGroup.get(1)[1], 1);
        assertBlockItems(indexOf4BlockGroup.get(2)[0], 3, indexOf4BlockGroup.get(2)[1], 1);
        assertBlockItems(indexOf4BlockGroup.get(3)[0], 4, indexOf4BlockGroup.get(3)[1], 1);
        assertBlockItems(indexOf4BlockGroup.get(4)[0], 6, indexOf4BlockGroup.get(4)[1], 2);
        assertBlockItems(indexOf4BlockGroup.get(5)[0], 10, indexOf4BlockGroup.get(5)[1], 4);
    }

    private void assertBlockItems(int actual, int row, int actual2, int col) {
        assertThat("Index of block of block contains row cell index [0, 0]", actual, is(row));
        assertThat("Index of block of block contains row cell index [0, 0]", actual2, is(col));
    }

    private Block getBlock() {
        final Cell[] cells = new Cell[2];
        cells[0] = new Cell(CellColour.GREEN, CellStatus.OCCUPIED);
        cells[1] = new Cell(CellColour.GREEN, CellStatus.OCCUPIED);

        final Block block = new Block(cells);
        return block;
    }

    private void initEmptyCells() {
        for (int i = 0; i < cells.length; i++) {
            cells[i] = new Cell(null, CellStatus.EMPTY);
        }
    }

    @NotNull
    private Cell getCell(CellColour cellColour, CellStatus cellStatus) {
        return new Cell(cellColour, cellStatus);
    }
}
