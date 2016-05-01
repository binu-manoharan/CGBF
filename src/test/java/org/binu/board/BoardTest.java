package org.binu.board;

import org.binu.data.CellColour;
import org.binu.data.CellStatus;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
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

    private void iterateBoard(Cell[][] currentBoard) {
        for (int i = 0; i < Board.HEIGHT; i++) {
            for (int j = 0; j < Board.WIDTH; j++) {
                assertEachBoardCellStatus(currentBoard[i][j], CellStatus.EMPTY);
            }
        }
    }

    private void assertEachBoardCellStatus(Cell cell, CellStatus currentCellStatus) {
        CellColour cellColour = cell.getCellColour();
        CellStatus cellStatus = cell.getCellStatus();
        assertNull(cellColour);
        assertThat("Cell status is empty.", cellStatus, is(currentCellStatus));
    }
}