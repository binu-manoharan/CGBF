package org.binu.ai.simple;

import org.binu.board.Block;
import org.binu.board.Cell;
import org.jetbrains.annotations.Nullable;

/**
 * Logic for parsing a cell array.
 */
public interface CellArrayParser {
    int getFirstEmptyPosition(Cell[] cells);

    int getCellArrayScore(Cell[] column, @Nullable Block block);
}
