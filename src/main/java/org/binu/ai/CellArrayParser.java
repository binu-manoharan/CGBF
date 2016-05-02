package org.binu.ai;

import org.binu.board.Cell;

/**
 * Logic for parsing a cell array.
 */
public interface CellArrayParser {
    int getFirstEmptyPosition(Cell[] cells);
}
