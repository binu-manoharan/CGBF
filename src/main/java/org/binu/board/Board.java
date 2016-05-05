package org.binu.board;

import org.binu.data.CellColour;
import org.binu.data.CellStatus;

/**
 * Contains board and corresponding data.
 */
public class Board {

    public static final int ROW_LENGTH = 12;
    public static final int COLUMN_LENGTH = 6;
    private final Cell[][] cells;

    public Board() {
        cells = new Cell[ROW_LENGTH][COLUMN_LENGTH];
        initEmptyBoard();
    }

    public Board(Board board) {
        this.cells = new Cell[ROW_LENGTH][COLUMN_LENGTH];

        for (int row = 0; row < ROW_LENGTH; row++) {
            for (int col = 0; col < COLUMN_LENGTH; col++) {
                final Cell cell = board.getCell(row, col);
                cells[row][col] = new Cell(cell.getCellColour(), cell.getCellStatus());
            }
        }
    }

    private void initEmptyBoard() {
        for (int i = 0; i < Board.ROW_LENGTH; i++) {
            for (int j = 0; j < Board.COLUMN_LENGTH; j++) {
                cells[i][j] = new Cell(null, CellStatus.EMPTY);
            }
        }
    }

    /**
     * returns the current board as a 2d array
     *
     * @return entire board data
     */
    public Cell[][] getBoard() {
        return cells;
    }

    /**
     * Get cell data from the board
     *
     * @param row    row index
     * @param column column index
     * @return Cell corresponding to the indexes on the board
     */
    public Cell getCell(int row, int column) {
        return cells[row][column];
    }

    /**
     * Set colour and status
     *
     * @param row        row index
     * @param column     column index
     * @param cellStatus new cell status
     * @param cellColour new cell colour
     */
    public void setCell(int row, int column, CellStatus cellStatus, CellColour cellColour) {
        cells[row][column].setCellColour(cellColour);
        cells[row][column].setCellStatus(cellStatus);
    }

    /**
     * Get the column for the given column index
     *
     * @param column column index
     * @return cell array for the column
     */
    public Cell[] getColumn(int column) {
        final Cell[] columns = new Cell[ROW_LENGTH];
        for (int i = 0; i < ROW_LENGTH; i++) {
            columns[i] = cells[i][column];
        }
        return columns;
    }

    /**
     * Set the column for the given column index
     *
     * @param column column index
     * @param cells column data
     */
    public void setColumn(int column, Cell[] cells) {
        for (int row = 0; row < ROW_LENGTH; row++) {
            setCell(row, column, cells[row].getCellStatus(), cells[row].getCellColour());
        }
    }

    /**
     * Get the row for the given row index
     *
     * @param row row index
     * @return cell array for the row
     */
    public Cell[] getRow(int row) {
        return cells[row];
    }

    /**
     * Set the row for the given row index
     *
     * @param row row index
     */
    public void setRow(int row, Cell[] rowCells) {
        cells[row] = rowCells;
    }
}
