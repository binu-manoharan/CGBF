package org.binu.integration;

import org.binu.board.Block;
import org.binu.board.BlockQueue;
import org.binu.board.Board;
import org.binu.board.Cell;
import org.binu.data.CellColour;
import org.binu.data.CellStatus;
import org.binu.data.ScoreNode;
import org.binu.framework.CellArrayHelperImpl;

import java.util.ArrayList;

/**
 * Data parser for game input
 */
public class DataParser {
    /**
     * Creates a block with two cells from the given 1-indexed values
     * @param topCellIndex 1-indexed enum value for top cell
     * @param bottomCellIndex 1-indexed enum value for bottom cell
     * @return block with both cells
     */
    public Block createColourBlock(int topCellIndex, int bottomCellIndex) {
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
            final Block colourBlock = createColourBlock(aBlockQueueData[1], aBlockQueueData[0]);
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

    public Board createBoard(String[] boardString) {
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

    public String[] createBoardString(Board board) {
        final String[] boardRows = new String[12];
        final char[] rowString = new char[6];
        for (int row = 0; row < Board.ROW_LENGTH; row++) {
            for (int col = 0; col < Board.COLUMN_LENGTH; col++) {
                final Cell cell = board.getCell(row, col);
                final CellStatus cellStatus = cell.getCellStatus();
                final CellColour cellColour = cell.getCellColour();
                char ch =' ';
                if (cellStatus == CellStatus.BLOCKED) {
                    ch = '0';
                } else if (cellStatus == CellStatus.EMPTY){
                    ch = '.';
                } else if (cellStatus == CellStatus.OCCUPIED){
                    switch (cellColour) {
                        case BLUE:
                            ch = '1';
                            break;
                        case GREEN:
                            ch = '2';
                            break;
                        case PURPLE:
                            ch = '3';
                            break;
                        case RED:
                            ch = '4';
                            break;
                        case YELLOW:
                            ch = '5';
                            break;
                    }
                } else {
                    throw new AssertionError("Invalid char in board row input");
                }
                rowString[col] = ch;
            }
            boardRows[Board.ROW_LENGTH - row - 1] = new String(rowString);
        }
        return boardRows;
    }

    public String[] prettifyBoardString(String[] outputBoardString) {
        for (int i = 0; i < outputBoardString.length; i++) {
            final String prettyBoardRow = outputBoardString[i].replaceAll("\\.", "  .");
            outputBoardString[i] = "|" + prettyBoardRow + "|";
        }
        return outputBoardString;
    }

    public String[] prettifyBoard(Board board) {
        final String[] boardString = createBoardString(board);
        final String[] prettifiedBoardString = prettifyBoardString(boardString);
        return prettifiedBoardString;
    }

    public Board followPath(Board board, BlockQueue blockQueue, ScoreNode scoreNode) {
        final CellArrayHelperImpl cellArrayHelper = new CellArrayHelperImpl();
        final Board boardWithDrops = new Board(board);
        final BlockQueue blockQueueToDrop = new BlockQueue(blockQueue);
        ScoreNode tempNode = scoreNode;
        final ArrayList<ScoreNode> scoringPath = new ArrayList<>();
        while (tempNode.getParent() != null) {
            scoringPath.add(tempNode);
            tempNode = tempNode.getParent();
        }

        for (int i = scoringPath.size() - 1; i >= 0; i--) {
            final Block nextBlock = blockQueueToDrop.getNextAndPop();
            assert nextBlock != null;

            final ScoreNode scoringPathNode = scoringPath.get(i);
            final boolean successfulDrop = cellArrayHelper.dropBlockIntoBoard(boardWithDrops, nextBlock,
                    scoringPathNode.getNodeIndex(), scoringPathNode.getOrientation());
            assert successfulDrop;
        }
        return boardWithDrops;
    }
}
