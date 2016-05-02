package org.binu.ai;

import org.binu.board.Cell;
import org.binu.data.CellStatus;

/**
 * Implementation of CellArrayParser
 */
public class CellArrayParserImpl implements CellArrayParser {
    @Override
    public int getFirstEmptyPosition(Cell[] cells) {
        int length = cells.length;
        for (int i = 0; i < length; i++) {
            if (cells[i].getCellStatus() != CellStatus.OCCUPIED) {
                return i;
            }
        }
        return length;
    }
}
