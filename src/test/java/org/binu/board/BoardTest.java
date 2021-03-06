package org.binu.board;

import org.binu.data.CellColour;
import org.binu.data.CellStatus;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Test for {@link Board}
 */
public class BoardTest {

    private Board board;
    private Cell[][] currentBoard;

    @Before
    public void setUp() throws Exception {
        board = new Board();
        currentBoard = board.getBoard();
    }

    @Test
    public void should_create_an_empty_grid() throws Exception {
        assertThat("Board width is 6", currentBoard[0].length, is(6));
        assertThat("Board length 12", currentBoard[11].length, is(6));

        iterateBoard(currentBoard);
    }

    @Test
    public void should_create_a_grid_with_first_cell_being_red() throws Exception {
        board.setCell(0,0, CellStatus.OCCUPIED, CellColour.RED);
        final Cell cell = board.getCell(0, 0);

        assertCellStatus(cell, CellStatus.OCCUPIED, CellColour.RED);
    }

    @Test
    public void should_return_columns() throws Exception {
        final Cell[] columns = board.getColumn(0);
        assertThat("Has 12 elements", columns.length, is(12));
    }

    @Test
    public void should_return_rows() throws Exception {
        final Cell[] rows = board.getRow(0);
        assertThat("Has 6 elements", rows.length, is(6));
    }

    @Test
    public void should_set_new_board_row() throws Exception {
        board.setCell(1, 1, CellStatus.OCCUPIED, CellColour.BLUE);
        board.setRow(0, board.getRow(1));
        final Cell cell = board.getCell(0, 1);
        assertThat("Cell (0,1) is occupied", cell.getCellStatus(), is(CellStatus.OCCUPIED));
        assertThat("Cell (0,1) is BLUE", cell.getCellColour(), is(CellColour.BLUE));
    }

    private void iterateBoard(Cell[][] currentBoard) {
        for (int i = 0; i < Board.ROW_LENGTH; i++) {
            for (int j = 0; j < Board.COLUMN_LENGTH; j++) {
                assertCellStatus(currentBoard[i][j], CellStatus.EMPTY, null);
            }
        }
    }

    private void assertCellStatus(Cell cell, CellStatus currentCellStatus, CellColour currentCellColour) {
        final CellColour cellColour = cell.getCellColour();
        final CellStatus cellStatus = cell.getCellStatus();
        assertThat("Cell colour is " + currentCellColour, cellColour,  is(currentCellColour));
        assertThat("Cell status is " + currentCellStatus, cellStatus, is(currentCellStatus));
    }
}