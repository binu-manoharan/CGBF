package org.binu.ai;

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

    @Before
    public void setUp() throws Exception {
        cells = new Cell[12];
        initEmptyCells();
    }

    @Test
    public void should_return_top_of_column_as_2() throws Exception {
        cells[0] = getCell(CellColour.GREEN, CellStatus.OCCUPIED);
        cells[1] = getCell(CellColour.RED, CellStatus.OCCUPIED);

        CellArrayParser cellArrayParser = new CellArrayParserImpl();
        int firstEmptyPosition = cellArrayParser.getFirstEmptyPosition(cells);

        assertThat("First empty position is 2 (zero indexed)", firstEmptyPosition, is(2));
    }

    @Test
    public void should_return_top_of_column_as_3() throws Exception {
        cells[0] = getCell(CellColour.GREEN, CellStatus.OCCUPIED);
        cells[1] = getCell(CellColour.RED, CellStatus.OCCUPIED);
        cells[2] = getCell(CellColour.BLUE, CellStatus.OCCUPIED);

        CellArrayParser cellArrayParser = new CellArrayParserImpl();
        int firstEmptyPosition = cellArrayParser.getFirstEmptyPosition(cells);

        assertThat("First empty position is 3 (zero indexed)", firstEmptyPosition, is(3));
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