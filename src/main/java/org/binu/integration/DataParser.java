package org.binu.integration;

import org.binu.board.Block;
import org.binu.board.BlockQueue;
import org.binu.board.Board;
import org.binu.board.Cell;
import org.binu.data.CellColour;
import org.binu.data.CellStatus;

/**
 * Data parser for game input
 */
public class DataParser {
    /**
     * Creates a block with two cells from the given 1-indexed values
     * @param bottomCellIndex 1-indexed enum value for bottom cell
     * @param topCellIndex 1-indexed enum value for top cell
     * @return block with both cells
     */
    public Block createColourBlock(int bottomCellIndex, int topCellIndex) {
        final CellColour bottomCellColour = CellColour.values()[bottomCellIndex - 1];
        final CellColour topCellColour = CellColour.values()[topCellIndex - 1];

        final Cell bottomCell = new Cell(bottomCellColour, CellStatus.OCCUPIED);
        final Cell topCell = new Cell(topCellColour, CellStatus.OCCUPIED);

        final Cell[] cells = new Cell[2];
        cells[0] = bottomCell;
        cells[1] = topCell;
        return new Block(cells);
    }

    public BlockQueue createBlockQueue(int[][] blockQueueData) {
        final BlockQueue blockQueue = new BlockQueue();
        for (int[] aBlockQueueData : blockQueueData) {
            final Block colourBlock = createColourBlock(aBlockQueueData[0], aBlockQueueData[1]);
            blockQueue.add(colourBlock);
        }
        return blockQueue;
    }

    public Cell[] createBoardRow(String rowString) {
        assert rowString != null && !rowString.equals("");
        final char[] chars = rowString.toCharArray();
        final Cell[] cells = new Cell[6];

        for(int i = 0; i < chars.length; i++) {
            final char aChar = chars[i];
            switch (aChar) {
                case '.':
                    cells[i] = new Cell(null, CellStatus.EMPTY);
                    break;
                case '0':
                    cells[i] = new Cell(null, CellStatus.BLOCKED);
                    break;
                case '1':
                    cells[i] = new Cell(CellColour.BLUE, CellStatus.OCCUPIED);
                    break;
                case '2':
                    cells[i] = new Cell(CellColour.GREEN, CellStatus.OCCUPIED);
                    break;
                case '3':
                    cells[i] = new Cell(CellColour.PURPLE, CellStatus.OCCUPIED);
                    break;
                case '4':
                    cells[i] = new Cell(CellColour.RED, CellStatus.OCCUPIED);
                    break;
                case '5':
                    cells[i] = new Cell(CellColour.YELLOW, CellStatus.OCCUPIED);
                    break;
                default:
                    throw new AssertionError("Invalid char in board row input");
            }
        }

        return cells;
    }

    Board createBoard(String[] boardString) {
        final Board board = new Board();

        assert boardString.length == 12;

        final int lengthIndex = boardString.length - 1;
        for (int i = lengthIndex; i >= 0 ; i--) {
            final String rowString = boardString[i];
            final Cell[] boardRow = createBoardRow(rowString);

            final int rowIndex = lengthIndex - i;
            board.setRow(rowIndex, boardRow);
        }

        return board;
    }
}