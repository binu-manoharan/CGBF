package org.binu.board;

import org.binu.data.CellColour;
import org.binu.data.CellStatus;
import org.junit.Test;

import static org.binu.data.CellColour.BLUE;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Test for {@link Cell}.
 */
public class CellTest {

    @Test
    public void should_get_a_blue_occupied_cell() throws Exception {
        Cell cell = new Cell(BLUE, CellStatus.OCCUPIED);

        CellStatus cellStatus = cell.getCellStatus();
        CellColour cellColour = cell.getCellColour();

        assertThat("Cell is an occupied.", cellStatus, is(CellStatus.OCCUPIED));
        assertThat("Cell is an blue.", cellColour, is(CellColour.BLUE));
    }
}