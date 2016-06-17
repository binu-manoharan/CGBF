package org.binu.framework;

import org.binu.board.Block;
import org.binu.board.Board;
import org.binu.board.Cell;
import org.binu.data.CellColour;
import org.binu.data.CellStatus;
import org.binu.data.Orientation;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of CellArrayHelper
 */
public class CellArrayHelperImpl implements CellArrayHelper {
    @Override
    public int getFirstEmptyPosition(Cell[] cells) {
        final int length = cells.length;
        for (int i = length - 1; i >= 0; i--) {
            final CellStatus cellStatus = cells[i].getCellStatus();
            if (cellStatus != CellStatus.EMPTY) {
                return i + 1;
            }
        }
        return 0;
    }

    @Override
    public List<int[]> getIndexOf4BlockGroup(Cell[][] cellArray) {
        final int rowLength = cellArray.length;
        final int colLength = cellArray[0].length;

        final List<int[]> resultList = new ArrayList<>();

        matchingPointsWith4Block(cellArray, rowLength, colLength, resultList);
        return resultList;
    }

    private void matchingPointsWith4Block(Cell[][] cellArray, int rowLength, int colLength, List<int[]> resultList) {
        for (int row = 0; row < rowLength - 1; row++) {
            for (int col = 0; col < colLength - 1; col++) {
                final CellColour cellBotLeft = cellArray[row][col].getCellColour();
                final CellColour cellTopLeft = cellArray[row + 1][col].getCellColour();
                final CellColour cellTopRight = cellArray[row + 1][col + 1].getCellColour();
                final CellColour cellBotRight = cellArray[row][col + 1].getCellColour();

                if (cellBotLeft != null && cellBotLeft == cellBotRight && cellTopLeft == cellTopRight && cellBotLeft == cellTopRight) {
                    addMatchingResult(resultList, row, col);
                    col++;
                }
            }
        }
    }

    @Override
    public Cell[] collapseEmptyCells(Cell[] cells) {
        final int firstEmptyPosition = getFirstEmptyPosition(cells);
        final int cellLength = cells.length;
        final Cell[] collapsedCells = new Cell[cellLength];
        int collapsedCellIndex = 0;
        for (int cellIndex = 0; cellIndex < cellLength; cellIndex++) {
            if (cellIndex < firstEmptyPosition) {
                if (cells[cellIndex].getCellStatus() != CellStatus.EMPTY) {
                    collapsedCells[collapsedCellIndex++] = cells[cellIndex];
                }
            }
        }

        for (int cellIndex = collapsedCellIndex; cellIndex < cellLength; cellIndex++) {
            collapsedCells[cellIndex] = new Cell(null, CellStatus.EMPTY);
        }
        return collapsedCells;
    }

    @Override
    public void collapseEmptyCells(Board board) {
        for (int col = 0; col < Board.COLUMN_LENGTH; col++) {
            final Cell[] cells = collapseEmptyCells(board.getColumn(col));
            board.setColumn(col, cells);
        }
    }

    @Override
    public Cell[] dropBlockIntoColumn(Cell[] cells, Block block) {
        final int firstEmptyPosition = getFirstEmptyPosition(cells);
        if (firstEmptyPosition + block.getCells().length > cells.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        final int length = cells.length;
        final Cell[] droppedCells = new Cell[length];

        System.arraycopy(cells, 0, droppedCells, 0, cells.length);

        if (firstEmptyPosition != length) {
            final Cell[] blockCells = block.getCells();
            droppedCells[firstEmptyPosition] = blockCells[0];
            droppedCells[firstEmptyPosition + 1] = blockCells[1];
        } else {
            assert true : "Cell is too full!";
        }

        return droppedCells;
    }

    @Override
    public boolean blockIsDroppableOnColumn(Board board, Block block, int columnIndex, Orientation orientation) {
        final Cell[] column = board.getColumn(columnIndex);
        final int firstEmptyPosition = getFirstEmptyPosition(column);
        return firstEmptyPosition + block.getCells().length <= column.length;
    }

    @Override
    public boolean dropBlockIntoBoard(Board board, Block block, int columnIndex, Orientation orientation) {
        //TODO see if block is droppable on column can be merged here to not call firstEmptyPosition twice
        final boolean isDroppable = blockIsDroppableOnColumn(board, block, columnIndex, Orientation.VERTICAL);
        if (isDroppable) {
            final Cell[] column = board.getColumn(columnIndex);
            final int firstEmptyPosition = getFirstEmptyPosition(column);
            int offset = 0;
            for (Cell cell: block.getCells()) {
                board.setCell(firstEmptyPosition + offset, columnIndex, cell.getCellStatus(), cell.getCellColour());
                offset++;
            }
        }
        return isDroppable;
    }

    @Override
    public List<int[]> getIndexOfLAndT(Cell[][] cellArray) {
        final int rowLength = cellArray.length;
        final int colLength = cellArray[0].length;

        final List<int[]> resultList = new ArrayList<>();

        matchingPointsWithLOrTToTheTop(cellArray, rowLength, colLength, resultList);
        matchingPointsWithLOrTToTheBottom(cellArray, rowLength, colLength, resultList);
        matchingPointsWithLOrTToTheRight(cellArray, rowLength, colLength, resultList);
        matchingPointsWithLOrTToTheLeft(cellArray, rowLength, colLength, resultList);
        return resultList;
    }

    @Override
    public List<int[]> getIndexOfZ(Cell[][] cellArray) {
        final int rowLength = cellArray.length;
        final int colLength = cellArray[0].length;

        final List<int[]> resultList = new ArrayList<>();

        matchingPointsWithZToTheRight(cellArray, rowLength, colLength, resultList);
        matchingPointsWithZToTheLeft(cellArray, rowLength, colLength, resultList);
        return resultList;
    }

    @Override
    public List<int[]> getIndexOfLines(Cell[][] cellArray) {
        final int rowLength = cellArray.length;
        final int colLength = cellArray[0].length;

        final List<int[]> resultList = new ArrayList<>();

        matchingPointsWithLinesToTheTop(cellArray, rowLength, colLength, resultList);
        matchingPointsWithLinesToTheRight(cellArray, rowLength, colLength, resultList);
        return resultList;
    }

    @Override
    public List<int[]> getIndexesOfAllShapes(Cell[][] cellArray) {
        final ArrayList<int[]> cellList = new ArrayList<>();
        cellList.addAll(getIndexOfLines(cellArray));
        cellList.addAll(getIndexOf4BlockGroup(cellArray));
        cellList.addAll(getIndexOfLAndT(cellArray));
        cellList.addAll(getIndexOfZ(cellArray));

        return cellList;
    }

    @Override
    public boolean isClearable(Board board) {
        final Cell[][] cellArray = board.getBoard();
        if (!getIndexOfLines(cellArray).isEmpty()) return true;
        if (!getIndexOf4BlockGroup(cellArray).isEmpty()) return true;
        if (!getIndexOfLAndT(cellArray).isEmpty()) return true;
        if (!getIndexOfZ(cellArray).isEmpty()) return true;
        return false;
    }

    private void matchingPointsWithLinesToTheRight(Cell[][] cellArray, int rowLength, int colLength, List<int[]> resultList) {
        for (int row = 0; row < rowLength; row++) {
            for (int col = 0; col < colLength - 3; col++) {
                final CellColour cellLeft = cellArray[row][col].getCellColour();
                final CellColour cellMidLeft = cellArray[row][col + 1].getCellColour();
                final CellColour cellMidRight = cellArray[row][col + 2].getCellColour();
                final CellColour cellTop = cellArray[row][col + 3].getCellColour();

                final boolean makesLine = cellLeft != null && cellLeft == cellMidLeft && cellMidRight == cellMidLeft && cellTop == cellMidRight;

                if (makesLine) {
                    addMatchingResult(resultList, row, col);
                }
            }
        }
    }

    private void matchingPointsWithLinesToTheTop(Cell[][] cellArray, int rowLength, int colLength, List<int[]> resultList) {
        for (int row = 0; row < rowLength - 3; row++) {
            for (int col = 0; col < colLength; col++) {
                final CellColour cellBot = cellArray[row][col].getCellColour();
                final CellColour cellMidBot = cellArray[row + 1][col].getCellColour();
                final CellColour cellMidTop = cellArray[row + 2][col].getCellColour();
                final CellColour cellTop = cellArray[row + 3][col].getCellColour();

                final boolean makesLine = cellBot != null && cellBot == cellMidBot && cellMidTop == cellMidBot && cellTop == cellMidTop;

                if (makesLine) {
                    addMatchingResult(resultList, row, col);
                }
            }
        }
    }

    private void matchingPointsWithZToTheLeft(Cell[][] cellArray, int rowLength, int colLength, List<int[]> resultList) {
        for (int row = 0; row < rowLength - 1; row++) {
            for (int col = 1; col < colLength - 1; col++) {
                final CellColour cellLeftBot = cellArray[row][col - 1].getCellColour();
                final CellColour cellLeftMid = cellArray[row][col].getCellColour();
                final CellColour cellLeftTop = cellArray[row][col + 1].getCellColour();
                final CellColour cellRightBot = cellArray[row + 1][col - 1].getCellColour();
                final CellColour cellRightMid = cellArray[row + 1][col].getCellColour();
                final CellColour cellRightTop = cellArray[row + 1][col + 1].getCellColour();

                final boolean midCellsAreSame = cellLeftMid != null && cellLeftMid == cellRightMid;
                final boolean makesZ = (cellLeftMid == cellLeftTop && cellLeftMid == cellRightBot) || (cellLeftMid == cellLeftBot && cellLeftMid == cellRightTop);

                if (midCellsAreSame && makesZ) {
                    addMatchingResult(resultList, row, col);
                }
            }
        }
    }

    private void matchingPointsWithZToTheRight(Cell[][] cellArray, int rowLength, int colLength, List<int[]> resultList) {
        for (int row = 1; row < rowLength - 1; row++) {
            for (int col = 0; col < colLength - 1; col++) {
                final CellColour cellLeftBot = cellArray[row - 1][col].getCellColour();
                final CellColour cellLeftMid = cellArray[row][col].getCellColour();
                final CellColour cellLeftTop = cellArray[row + 1][col].getCellColour();
                final CellColour cellRightBot = cellArray[row - 1][col + 1].getCellColour();
                final CellColour cellRightMid = cellArray[row][col + 1].getCellColour();
                final CellColour cellRightTop = cellArray[row + 1][col + 1].getCellColour();

                final boolean midCellsAreSame = cellLeftMid != null && cellLeftMid == cellRightMid;
                final boolean makesZ = (cellLeftMid == cellLeftTop && cellLeftMid == cellRightBot) || (cellLeftMid == cellLeftBot && cellLeftMid == cellRightTop);

                if (midCellsAreSame && makesZ) {
                    addMatchingResult(resultList, row, col);
                }
            }
        }
    }

    private void matchingPointsWithLOrTToTheRight(Cell[][] cellArray, int rowLength, int colLength, List<int[]> resultList) {
        for (int row = 0; row < rowLength - 2; row++) {
            for (int col = 0; col < colLength - 1; col++) {
                final CellColour cellLeftBot = cellArray[row][col].getCellColour();
                final CellColour cellLeftMid = cellArray[row + 1][col].getCellColour();
                final CellColour cellLeftTop = cellArray[row + 2][col].getCellColour();
                final CellColour cellRightBot = cellArray[row][col + 1].getCellColour();
                final CellColour cellRightMid = cellArray[row + 1][col + 1].getCellColour();
                final CellColour cellRightTop = cellArray[row + 2][col + 1].getCellColour();

                final boolean isAtleastOneMatchingOnRight = cellLeftBot != null && (cellLeftBot == cellRightBot ||
                        cellLeftBot == cellRightMid || cellLeftBot == cellRightTop);

                if (isAtleastOneMatchingOnRight && cellLeftBot == cellLeftMid && cellLeftMid == cellLeftTop) {
                    addMatchingResult(resultList, row, col);
                }
            }
        }
    }

    private void matchingPointsWithLOrTToTheLeft(Cell[][] cellArray, int rowLength, int colLength, List<int[]> resultList) {
        for (int row = 0; row < rowLength - 2; row++) {
            for (int col = 1; col < colLength; col++) {
                final CellColour cellLeftBot = cellArray[row][col].getCellColour();
                final CellColour cellLeftMid = cellArray[row + 1][col].getCellColour();
                final CellColour cellLeftTop = cellArray[row + 2][col].getCellColour();
                final CellColour cellRightBot = cellArray[row][col - 1].getCellColour();
                final CellColour cellRightMid = cellArray[row + 1][col - 1].getCellColour();
                final CellColour cellRightTop = cellArray[row + 2][col - 1].getCellColour();

                final boolean isAtleastOneMatchingOnRight = cellLeftBot != null && (cellLeftBot == cellRightBot ||
                        cellLeftBot == cellRightMid || cellLeftBot == cellRightTop);

                if (isAtleastOneMatchingOnRight && cellLeftBot == cellLeftMid && cellLeftMid == cellLeftTop) {
                    addMatchingResult(resultList, row, col);
                }
            }
        }
    }

    private void matchingPointsWithLOrTToTheBottom(Cell[][] cellArray, int rowLength, int colLength, List<int[]> resultList) {
        for (int row = 1; row < rowLength; row++) {
            for (int col = 0; col < colLength - 2; col++) {
                final CellColour cellRightBot = cellArray[row][col].getCellColour();
                final CellColour cellRightMid = cellArray[row][col + 1].getCellColour();
                final CellColour cellRightTop = cellArray[row][col + 2].getCellColour();
                final CellColour cellLeftBot = cellArray[row - 1][col].getCellColour();
                final CellColour cellLeftMid = cellArray[row - 1][col + 1].getCellColour();
                final CellColour cellLeftTop = cellArray[row - 1][col + 2].getCellColour();

                final boolean isAtleastOneMatchingOnRight = cellRightBot != null && (cellRightBot == cellLeftBot ||
                        cellRightBot == cellLeftMid || cellRightBot == cellLeftTop);

                if (isAtleastOneMatchingOnRight && cellRightBot == cellRightMid && cellRightMid == cellRightTop) {
                    addMatchingResult(resultList, row, col);
                }
            }
        }
    }

    private void matchingPointsWithLOrTToTheTop(Cell[][] cellArray, int rowLength, int colLength, List<int[]> resultList) {
        for (int row = 0; row < rowLength - 1; row++) {
            for (int col = 0; col < colLength - 2; col++) {
                final CellColour cellLeftBot = cellArray[row][col].getCellColour();
                final CellColour cellLeftMid = cellArray[row][col + 1].getCellColour();
                final CellColour cellLeftTop = cellArray[row][col + 2].getCellColour();
                final CellColour cellRightBot = cellArray[row + 1][col].getCellColour();
                final CellColour cellRightMid = cellArray[row + 1][col + 1].getCellColour();
                final CellColour cellRightTop = cellArray[row + 1][col + 2].getCellColour();

                final boolean isAtleastOneMatchingOnRight = cellLeftBot != null && (cellLeftBot == cellRightBot ||
                        cellLeftBot == cellRightMid || cellLeftBot == cellRightTop);

                if (isAtleastOneMatchingOnRight && cellLeftBot == cellLeftMid && cellLeftMid == cellLeftTop) {
                    addMatchingResult(resultList, row, col);
                }
            }
        }
    }

    private void addMatchingResult(List<int[]> resultList, int row, int col) {
        final int[] matchingResult = new int[2];
        matchingResult[0] = row;
        matchingResult[1] = col;
        resultList.add(matchingResult);
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
