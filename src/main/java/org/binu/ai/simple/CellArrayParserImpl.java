package org.binu.ai.simple;

import org.binu.board.Block;
import org.binu.board.Cell;
import org.binu.data.CellColour;
import org.binu.data.CellStatus;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of CellArrayParser
 */
public class CellArrayParserImpl implements CellArrayParser {
    @Override
    public int getFirstEmptyPosition(Cell[] cells) {
        final int length = cells.length;
        for (int i = 0; i < length; i++) {
            final CellStatus cellStatus = cells[i].getCellStatus();
            if (cellStatus != CellStatus.OCCUPIED && cellStatus != CellStatus.BLOCKED) {
                return i;
            }
        }
        return length;
    }

    @Override
    public int getCellArrayScore(Cell[] cells, @Nullable Block block) {
        final Cell[] blockCells = block.getCells();

        final Cell bottomCell = blockCells[0];

        final int firstEmptyPosition = getFirstEmptyPosition(cells);

        final int topElementPosition = firstEmptyPosition - 1;

        if (topElementPosition >= 0) {
            final CellColour topElementColour = cells[topElementPosition].getCellColour();
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

    @Override
    public int getFirstIndexOfRepeatOf4Group(Cell[] cells) {
        int numberOfSameAdjacentCells = 1;
        CellColour lastColour = null;
        for (int elementIndex = 0; elementIndex < cells.length; elementIndex++) {
            if (cells[elementIndex].getCellStatus() == CellStatus.OCCUPIED) {
                final CellColour currentCellColour = cells[elementIndex].getCellColour();

                if (lastColour == currentCellColour) {
                    numberOfSameAdjacentCells++;

                    if (numberOfSameAdjacentCells == 4) {
                        return elementIndex;
                    }
                } else {
                    numberOfSameAdjacentCells = 1;
                }

                lastColour = currentCellColour;
            } else {
                numberOfSameAdjacentCells = 1;
            }
        }
        return -1;
    }

    @Override
    public List<int[]> getIndexOf4BlockGroup(Cell[][] cellArray) {
        final int rowLength = cellArray.length;
        final int colLength = cellArray[0].length;

        final List<int[]> resultList = new ArrayList<>();

        for (int row = 0; row < rowLength - 1; row++) {
            for (int col = 0; col < colLength - 1; col++) {
                final CellColour cellBotLeft = cellArray[row][col].getCellColour();
                final CellColour cellTopLeft = cellArray[row + 1][col].getCellColour();
                final CellColour cellTopRight = cellArray[row + 1][col + 1].getCellColour();
                final CellColour cellBotRight = cellArray[row][col + 1].getCellColour();

                if(cellBotLeft != null && cellBotLeft == cellBotRight && cellTopLeft == cellTopRight && cellBotLeft == cellTopRight) {
                    final int[] matchingResult = new int[2];
                    matchingResult[0] = row;
                    matchingResult[1] = col;
                    resultList.add(matchingResult);
                    col++;
                }
            }
        }
        return resultList;
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
