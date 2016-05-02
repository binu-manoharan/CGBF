package org.binu.board;

import org.binu.data.CellColour;
import org.binu.data.CellStatus;

/**
 * Factory to create blocks
 */
public class BlockFactory {

    /**
     * Creates a falling 2x1 block (assumed to be vertical with first block at the bottom)
     * @param cellColour cellColour of the block
     * @return 2x1 block
     */
    public static Block create2x1SameColourBlock(CellColour cellColour) {
        Cell[] cells = new Cell[2];
        cells[0] = new Cell(cellColour, CellStatus.OCCUPIED);
        cells[1] = new Cell(cellColour, CellStatus.OCCUPIED);
        return new Block(cells);
    }
}
