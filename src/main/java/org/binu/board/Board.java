package org.binu.board;

import org.binu.data.CellStatus;

/**
 * Contains board and corresponding data.
 */
class Board {

    static final int WIDTH = 6;
    static final int HEIGHT = 12;
    private Cell[][] cells;

    Board() {
        cells = new Cell[HEIGHT][WIDTH];
        initEmptyBoard();
    }

    private void initEmptyBoard() {
        for (int i = 0; i < Board.HEIGHT; i++) {
            for (int j = 0; j < Board.WIDTH; j++) {
                cells[i][j] = new Cell(null, CellStatus.EMPTY);
            }
        }
    }

    Cell[][] getBoard() {
        return cells;
    }
}
