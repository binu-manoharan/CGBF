package org.binu.utils;

import org.binu.board.Block;
import org.binu.board.Board;
import org.binu.board.Cell;
import org.binu.data.CellColour;
import org.binu.data.Orientation;
import org.binu.data.OrientationAndIndex;
import org.binu.framework.CellArrayHelperImpl;

/**
 * Helper to find if drop to a column with certain orientation and index for
 * a block has matching colours close to it.
 */
public class MatchingColourHelper {

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
        }

        return false;
    }

    private boolean hasVerticalReversedMatchingColour(Board board, int nodeIndex, CellColour firstCellColour, CellColour secondCellColour) {
        if (nodeIndex >= 0 && nodeIndex < 6) {
            final Cell[] column1 = board.getColumn(nodeIndex);
            final int firstEmptyPosition = cellArrayHelper.getFirstEmptyPosition(column1);

            if (firstEmptyPosition > 10 && firstEmptyPosition < 0) {
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
