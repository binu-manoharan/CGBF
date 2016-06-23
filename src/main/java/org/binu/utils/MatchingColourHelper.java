package org.binu.utils;

import org.binu.board.Block;
import org.binu.board.Board;
import org.binu.board.Cell;
import org.binu.data.CellColour;
import org.binu.data.Orientation;
import org.binu.data.OrientationAndIndex;
import org.binu.framework.CellArrayHelperImpl;
import org.jetbrains.annotations.Nullable;

/**
 * Helper to find if drop to a column with certain orientation and index for
 * a block has matching colours close to it.
 */
public class MatchingColourHelper {

    public static final int HEIGHT_UPPER_LIMIT_FOR_VERTICAL_ORIENTATION = 10;
    public static final int HEIGHT_LOWER_LIMIT_FOR_BOARD = 0;
    public static final int HEIGHT_UPPER_LIMIT_FOR_HORIZONTAL_ORIENTATION = 11;
    private CellArrayHelperImpl cellArrayHelper;

    public MatchingColourHelper(CellArrayHelperImpl cellArrayHelper) {
        this.cellArrayHelper = cellArrayHelper;
    }

    public boolean columnHasMatchingColour(Board board, Block block, OrientationAndIndex orientationAndIndex) {
        final Orientation orientation = orientationAndIndex.getOrientation();
        final int nodeIndex = orientationAndIndex.getNodeIndex();
        final Cell[] cells = block.getCells();
        final CellColour firstCellColour = cells[0].getCellColour();
        final CellColour secondCellColour = cells[1].getCellColour();

        //TODO think about having check left and right methods instead?
        switch (orientation) {
            case VERTICAL_REVERSED:
                //cell[0] is at bottom
                //cell[1] is at top
                return hasVerticalReversedMatchingColour(board, nodeIndex, firstCellColour, secondCellColour);
            case VERTICAL:
                //bit of a not so pleasant logic but we could just reverse the cell colours on VERTICAL_REVERSED
                return hasVerticalReversedMatchingColour(board, nodeIndex, secondCellColour, firstCellColour);
            case HORIZONTAL_REVERSED:
                return hasHorizontalReversedMatchingColour(board, nodeIndex, firstCellColour, secondCellColour);
            case HORIZONTAL:
                //not very happy with the logic but this should work.
                final int offsetedNodeIndexForReverse = nodeIndex + 1;
                return hasHorizontalReversedMatchingColour(board, offsetedNodeIndexForReverse, secondCellColour, firstCellColour);
        }
        return false;
    }

    private boolean hasHorizontalReversedMatchingColour(Board board, int nodeIndex, CellColour firstCellColour, CellColour secondCellColour) {
        if (nodeIndex > 0 && nodeIndex < 6) {
            final Cell[] column1 = board.getColumn(nodeIndex);
            final Cell[] column2 = board.getColumn(nodeIndex - 1);
            final int col2FirstEmptyPosition = cellArrayHelper.getFirstEmptyPosition(column1);
            final int col1FirstEmptyPosition = cellArrayHelper.getFirstEmptyPosition(column2);

            if (col2FirstEmptyPosition > HEIGHT_UPPER_LIMIT_FOR_HORIZONTAL_ORIENTATION || col2FirstEmptyPosition < HEIGHT_LOWER_LIMIT_FOR_BOARD
                    || col1FirstEmptyPosition > HEIGHT_UPPER_LIMIT_FOR_HORIZONTAL_ORIENTATION || col1FirstEmptyPosition < HEIGHT_LOWER_LIMIT_FOR_BOARD) {
                return false;
            }

            switch (nodeIndex) {
                case 1:
                    if (hasHorizontalReverseMatchingColourForNodeIndex1(board, nodeIndex, firstCellColour, secondCellColour, col1FirstEmptyPosition, col2FirstEmptyPosition)) {
                        return true;
                    }

                    break;
                case 2:
                case 3:
                case 4:
                    if (hasHorizontalReverseMatchingColourForNodeIndex234(board, nodeIndex, firstCellColour, secondCellColour, col1FirstEmptyPosition, col2FirstEmptyPosition)) {
                        return true;
                    }
                    break;
                case 5:
                    if (hasHorizontalReverseMatchingColourForNodeIndex5(board, nodeIndex, firstCellColour, secondCellColour, col1FirstEmptyPosition, col2FirstEmptyPosition)) {
                        return true;
                    }
                    break;
            }
        }
        return false;
    }

    private boolean hasHorizontalReverseMatchingColourForNodeIndex234(Board board, int nodeIndex, CellColour firstCellColour, CellColour secondCellColour, int col1FirstEmptyPosition, int col2FirstEmptyPosition) {
        //check below and to the right with cell1
        //cell1 is the left cell of horizontal reversed
        //cell2 is the right cell of horizontal reversed
        final Cell leftOfCell1 = board.getCell(col1FirstEmptyPosition, nodeIndex - 2);
        final Cell rightOfCell1 = board.getCell(col1FirstEmptyPosition, nodeIndex);
        if (rightOfCell1.getCellColour() == secondCellColour || leftOfCell1.getCellColour() == secondCellColour) {
            return true;
        }

        final Cell leftOfCell2 = board.getCell(col2FirstEmptyPosition, nodeIndex - 1);
        final Cell rightOfCell2 = board.getCell(col2FirstEmptyPosition, nodeIndex + 1);
        if (leftOfCell2.getCellColour() == firstCellColour || rightOfCell2.getCellColour() == firstCellColour) {
            return true;
        }

        if (col1FirstEmptyPosition > 0) {
            final Cell botOfCell1 = board.getCell(col1FirstEmptyPosition - 1, nodeIndex - 1);
            if (botOfCell1.getCellColour() == secondCellColour) {
                return true;
            }
        }

        if (col2FirstEmptyPosition > 0) {
            final Cell botOfCell1 = board.getCell(col2FirstEmptyPosition - 1, nodeIndex);
            if (botOfCell1.getCellColour() == firstCellColour) {
                return true;
            }
        }
        return false;
    }

    private boolean hasHorizontalReverseMatchingColourForNodeIndex5(Board board, int nodeIndex, CellColour firstCellColour, CellColour secondCellColour, int col1FirstEmptyPosition, int col2FirstEmptyPosition) {
        //check below and to the right with cell1
        //cell1 is the left cell of horizontal reversed
        //cell2 is the right cell of horizontal reversed
        final Cell leftOfCell1 = board.getCell(col1FirstEmptyPosition, nodeIndex - 2);
        final Cell rightOfCell1 = board.getCell(col1FirstEmptyPosition, nodeIndex);
        if (rightOfCell1.getCellColour() == secondCellColour || leftOfCell1.getCellColour() == secondCellColour) {
            return true;
        }

        final Cell leftOfCell2 = board.getCell(col2FirstEmptyPosition, nodeIndex - 1);
        if (leftOfCell2.getCellColour() == firstCellColour) {
            return true;
        }

        if (col1FirstEmptyPosition > 0) {
            final Cell botOfCell1 = board.getCell(col1FirstEmptyPosition - 1, nodeIndex - 1);
            if (botOfCell1.getCellColour() == secondCellColour) {
                return true;
            }
        }

        if (col2FirstEmptyPosition > 0) {
            final Cell botOfCell1 = board.getCell(col2FirstEmptyPosition - 1, nodeIndex);
            if (botOfCell1.getCellColour() == firstCellColour) {
                return true;
            }
        }
        return false;
    }

    private boolean hasHorizontalReverseMatchingColourForNodeIndex1(Board board, int nodeIndex, CellColour firstCellColour, CellColour secondCellColour, int col1FirstEmptyPosition, int col2FirstEmptyPosition) {
        //check below and to the right with cell2
        //cell1 is the left cell of horizontal reversed
        //cell2 is the right cell of horizontal reversed
        final Cell rightOfCell1 = board.getCell(col1FirstEmptyPosition, nodeIndex);
        if (rightOfCell1.getCellColour() == secondCellColour) {
            return true;
        }

        final Cell leftOfCell2 = board.getCell(col2FirstEmptyPosition, nodeIndex - 1);
        final Cell rightOfCell2 = board.getCell(col2FirstEmptyPosition, nodeIndex + 1);

        if (leftOfCell2.getCellColour() == firstCellColour || rightOfCell2.getCellColour() == firstCellColour) {
            return true;
        }

        if (col1FirstEmptyPosition > 0) {
            final Cell botOfCell1 = board.getCell(col1FirstEmptyPosition - 1, nodeIndex - 1);
            if (botOfCell1.getCellColour() == secondCellColour) {
                return true;
            }
        }

        if (col2FirstEmptyPosition > 0) {
            final Cell botOfCell1 = board.getCell(col2FirstEmptyPosition - 1, nodeIndex);
            if (botOfCell1.getCellColour() == firstCellColour) {
                return true;
            }
        }
        return false;
    }

    private boolean hasVerticalReversedMatchingColour(Board board, int nodeIndex, CellColour firstCellColour, CellColour secondCellColour) {
        if (nodeIndex >= 0 && nodeIndex < 6) {
            final Cell[] column1 = board.getColumn(nodeIndex);
            final int firstEmptyPosition = cellArrayHelper.getFirstEmptyPosition(column1);

            if (firstEmptyPosition > HEIGHT_UPPER_LIMIT_FOR_VERTICAL_ORIENTATION || firstEmptyPosition < HEIGHT_LOWER_LIMIT_FOR_BOARD) {
                return false;
            }

            switch (nodeIndex) {
                case 0:
                    if (matchReverseVerticallyToRight(board, nodeIndex, firstCellColour, secondCellColour, firstEmptyPosition)) {
                        return true;
                    }
                    break;
                case 1:
                case 2:
                case 3:
                case 4:
                    if (matchReverseVerticallyToRight(board, nodeIndex, firstCellColour, secondCellColour, firstEmptyPosition)) {
                        return true;
                    }

                    if (matchReverseVerticallyToLeft(board, nodeIndex, firstCellColour, secondCellColour, firstEmptyPosition)) {
                        return true;
                    }
                    break;
                case 5:
                    if (matchReverseVerticallyToLeft(board, nodeIndex, firstCellColour, secondCellColour, firstEmptyPosition)) {
                        return true;
                    }
                    break;
            }
            return false;
        } else {
            return false;
        }
    }

    private boolean matchReverseVerticallyToRight(Board board, int nodeIndex, CellColour firstCellColour, CellColour secondCellColour, int firstEmptyPosition) {
        final Cell cellToRight1 = board.getCell(firstEmptyPosition, nodeIndex + 1);
        final Cell cellToRight2 = board.getCell(firstEmptyPosition + 1, nodeIndex + 1);

        if (cellToRight1.getCellColour() == firstCellColour || cellToRight2.getCellColour() == secondCellColour) {
            return true;
        }

        if (firstEmptyPosition > 0) {
            final Cell cellBelow = board.getCell(firstEmptyPosition - 1, nodeIndex);
            if (cellBelow.getCellColour() == firstCellColour) {
                return true;
            }
        }
        return false;
    }

    private boolean matchReverseVerticallyToLeft(Board board, int nodeIndex, CellColour firstCellColour, CellColour secondCellColour, int firstEmptyPosition) {
        final Cell cellToRight1 = board.getCell(firstEmptyPosition, nodeIndex - 1);
        final Cell cellToRight2 = board.getCell(firstEmptyPosition + 1, nodeIndex - 1);

        if (cellToRight1.getCellColour() == firstCellColour || cellToRight2.getCellColour() == secondCellColour) {
            return true;
        }
        return false;
    }
}
