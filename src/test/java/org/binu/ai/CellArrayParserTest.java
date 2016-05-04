package org.binu.ai;

import org.binu.ai.simple.CellArrayParser;
import org.binu.ai.simple.CellArrayParserImpl;
import org.binu.board.Block;
import org.binu.board.Cell;
import org.binu.data.CellColour;
import org.binu.data.CellStatus;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

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