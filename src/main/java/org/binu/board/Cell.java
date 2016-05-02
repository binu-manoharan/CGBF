package org.binu.board;

import org.binu.data.CellColour;
import org.binu.data.CellStatus;

/**
 * Represents cell on the board.
 */
public class Cell {

    private CellColour cellColour;
    private CellStatus cellStatus;

    public Cell(CellColour cellColour, CellStatus cellStatus) {
        this.cellColour = cellColour;
        this.cellStatus = cellStatus;
    }

    public CellStatus getCellStatus() {
        return cellStatus;
    }

    CellColour getCellColour() {
        return cellColour;
    }

    public void setCellColour(CellColour cellColour) {
        this.cellColour = cellColour;
    }

    public void setCellStatus(CellStatus cellStatus) {
        this.cellStatus = cellStatus;
    }
}
