package org.binu.ai;

import org.binu.board.Block;
import org.binu.board.Cell;
import org.binu.data.CellColour;
import org.binu.data.CellStatus;
import org.jetbrains.annotations.Nullable;

/**
 * Implementation of CellArrayParser
 */
class CellArrayParserImpl implements CellArrayParser {
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

    @Override
    public int getCellArrayScore(Cell[] cells, @Nullable Block block) {
        Cell[] blockCells = block.getCells();

        Cell bottomCell = blockCells[0];

        int firstEmptyPosition = getFirstEmptyPosition(cells);

        int topElementPosition = firstEmptyPosition - 1;

        if (topElementPosition >= 0) {
            CellColour topElementColour = cells[topElementPosition].getCellColour();
            int numberOfElementsWithSameColourAsTopElement = 1;

            numberOfElementsWithSameColourAsTopElement = getNumberOfElementsWithSameColourAsTopElement(cells, topElementPosition, topElementColour, numberOfElementsWithSameColourAsTopElement);

            if (topElementColour == bottomCell.getCellColour() && numberOfElementsWithSameColourAsTopElement > 1) {
                return numberOfElementsWithSameColourAsTopElement - 1;
            }

            if (topElementColour != bottomCell.getCellColour()) {
                return numberOfElementsWithSameColourAsTopElement * -1;
            }


        }
        return 0;
    }

    private int getNumberOfElementsWithSameColourAsTopElement(Cell[] cells, int topElementPosition, CellColour topElementColour, int numberOfElementsWithSameColourAsTopElement) {
        for (int i = topElementPosition - 1; i >= 0; i--) {
            if (topElementColour == cells[i].getCellColour()) {
                numberOfElementsWithSameColourAsTopElement++;
            } else {
                break;
            }
        }
        return numberOfElementsWithSameColourAsTopElement;
    }
}
