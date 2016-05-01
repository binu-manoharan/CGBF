package org.binu.board;

import org.binu.data.CellStatus;

/**
 * Contains board and corresponding data.
 */
class Board {

    static final int ROW_LENGTH = 12;
    static final int COLUMN_LENGTH = 6;
    private Cell[][] cells;

    Board() {
        cells = new Cell[ROW_LENGTH][COLUMN_LENGTH];
        initEmptyBoard();
    }

    private void initEmptyBoard() {
        for (int i = 0; i < Board.ROW_LENGTH; i++) {
            for (int j = 0; j < Board.COLUMN_LENGTH; j++) {
                cells[i][j] = new Cell(null, CellStatus.EMPTY);
            }
        }
    }

    Cell[][] getBoard() {
        return cells;
    }

    public Cell getCell(int row, int column) {
        return cells[row][column];
    }
}
