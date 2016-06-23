package org.binu.ai.movemaker;

import org.binu.board.Block;
import org.binu.board.BlockQueue;
import org.binu.board.Cell;
import org.binu.data.CellColour;
import org.binu.data.CellStatus;

/**
 * Test helper for commonly used items in tests.
 */
public class TestHelper {
    private final String[] boardString = {
            "......",
            "......",
            "......",
            "......",
            "......",
            "......",
            "......",
            "......",
            "......",
            "......",
            "......",
            "......"
    };

    public String[] getEmptyBoardString() {
        return boardString;
    }

    public void addBlockToBlockQueue(BlockQueue blockQueue, CellColour cell1Colour, CellColour cell2Colour) {
        final Cell[] cells = new Cell[2];
        cells[0] = new Cell(cell1Colour, CellStatus.OCCUPIED);
        cells[1] = new Cell(cell2Colour, CellStatus.OCCUPIED);
        final Block block = new Block(cells);
        blockQueue.add(block);
    }
}
