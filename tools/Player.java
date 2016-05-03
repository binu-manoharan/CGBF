import java.util.*;
import java.io.*;
import java.math.*;

class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);

        // game loop
        while (true) {
            DataParser dataParser = new DataParser();
            BlockQueue blockQueue = new BlockQueue();
            for (int i = 0; i < 8; i++) {
                int colorA = in.nextInt(); // color of the first block
                int colorB = in.nextInt(); // color of the attached block
                Block colourBlock = dataParser.createColourBlock(colorA, colorB);
                blockQueue.add(colourBlock);
            }

            for (int i = 0; i < 12; i++) {
                String row = in.next();
            }

            Board myBoard = new Board();
            for (int i = 0; i < 12; i++) {
                String row = in.next(); // One line of the map ('.' = empty, '0' = skull block, '1' to '5' = colored block)
                Cell[] boardRow = dataParser.createBoardRow(row);
                myBoard.setRow(Board.ROW_LENGTH - i - 1, boardRow);
            }

            GameAI gameAI = new SimpleGameAI(myBoard, blockQueue);
            int nextMove = gameAI.calculateNextMove();
            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");

            System.out.println(nextMove); // "x": the column in which to drop your blocks
        }
    }
}

/**
 * Enum indicating a cell status
 */
enum CellStatus {
    EMPTY,
    BLOCKED,
    OCCUPIED
}

/**
 * Cell Colours for occupied rows.
 */
enum CellColour {
    BLUE,
    GREEN,
    PURPLE,
    RED,
    YELLOW
}


/**
 * Common AI features.
 */
abstract class GameAI implements IGameAI {
    Board board;
    BlockQueue blockQueue;
    CellArrayParser cellArrayParser;

    public GameAI(Board board, BlockQueue blockQueue) {
        this.board = board;
        this.blockQueue = blockQueue;

        cellArrayParser = new CellArrayParserImpl();
    }
}


/**
 * Simple AI
 */
class SimpleGameAI extends GameAI {
    SimpleGameAI(Board board, BlockQueue blockQueue) {
        super(board, blockQueue);
    }

    /**
     * {@inheritDoc}
     */
    public int calculateNextMove() {
        int columnMatch = bestColumnMatch();
        return columnMatch;
    }

    private int bestColumnMatch() {
        int highestColoumnScore = -1;
        int highestScoreIndex = -1;

        for (int i = 0; i < Board.COLUMN_LENGTH; i++) {
            Cell[] column = board.getColumn(i);
            int columnScore = cellArrayParser.getCellArrayScore(column, blockQueue.getNext());

            if (columnScore > highestColoumnScore) {
                highestColoumnScore = columnScore;
                highestScoreIndex = i;
            }
        }
        return highestScoreIndex;
    }
}


/**
 * Logic for parsing a cell array.
 */
interface CellArrayParser {
    int getFirstEmptyPosition(Cell[] cells);

    int getCellArrayScore(Cell[] column, Block block);
}


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
    public int getCellArrayScore(Cell[] cells, Block block) {
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

/**
 * Game AI class.
 */
interface IGameAI {

    /**
     * Calculate the next move to make
     *
     * @return index of the column to play in
     */
    int calculateNextMove();
}


/**
 * Data parser for game input
 */
class DataParser {
    /**
     * Creates a block with two cells from the given 1-indexed values
     *
     * @param bottomCellIndex 1-indexed enum value for bottom cell
     * @param topCellIndex    1-indexed enum value for top cell
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

        for (int i = 0; i < chars.length; i++) {
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
        for (int i = lengthIndex; i >= 0; i--) {
            final String rowString = boardString[i];
            final Cell[] boardRow = createBoardRow(rowString);

            final int rowIndex = lengthIndex - i;
            board.setRow(rowIndex, boardRow);
        }

        return board;
    }
}


/**
 * Factory to create blocks
 */
class BlockFactory {

    /**
     * Creates a falling 2x1 block (assumed to be vertical with first block at the bottom)
     *
     * @param cellColour cellColour of the block
     * @return 2x1 block
     */
    public static Block create2x1SameColourBlock(CellColour cellColour) {
        Cell[] cells = new Cell[2];
        cells[0] = new Cell(cellColour, CellStatus.OCCUPIED);
        cells[1] = new Cell(cellColour, CellStatus.OCCUPIED);
        return new Block(cells);
    }
}

/**
 * Block representing the pieces that could fall on the {@link Board}
 */
class Block {

    private Cell[] cells = new Cell[2];

    public Block(Cell[] cells) {
        this.cells = cells;
    }

    public Cell[] getCells() {
        return cells;
    }
}


/**
 * Contains board and corresponding data.
 */
class Board {

    public static final int ROW_LENGTH = 12;
    public static final int COLUMN_LENGTH = 6;
    private final Cell[][] cells;

    public Board() {
        cells = new Cell[ROW_LENGTH][COLUMN_LENGTH];
        initEmptyBoard();
    }

    private void initEmptyBoard() {
        for (int i = 0; i < Board.ROW_LENGTH; i++) {
            for (int j = 0; j < Board.COLUMN_LENGTH; j++) {
                cells[i][j] = new Cell(null, CellStatus.EMPTY);
            }
        }
    }

    /**
     * returns the current board as a 2d array
     *
     * @return entire board data
     */
    public Cell[][] getBoard() {
        return cells;
    }

    /**
     * Get cell data from the board
     *
     * @param row    row index
     * @param column column index
     * @return Cell corresponding to the indexes on the board
     */
    public Cell getCell(int row, int column) {
        return cells[row][column];
    }

    /**
     * Set colour and status
     *
     * @param row        row index
     * @param column     column index
     * @param cellStatus new cell status
     * @param cellColour new cell colour
     */
    public void setCell(int row, int column, CellStatus cellStatus, CellColour cellColour) {
        cells[row][column].setCellColour(cellColour);
        cells[row][column].setCellStatus(cellStatus);
    }

    /**
     * Get the column for the given column index
     *
     * @param column column index
     * @return cell array for the column
     */
    public Cell[] getColumn(int column) {
        final Cell[] columns = new Cell[ROW_LENGTH];
        for (int i = 0; i < ROW_LENGTH; i++) {
            columns[i] = cells[i][column];
        }
        return columns;
    }

    /**
     * Get the row for the given row index
     *
     * @param row row index
     * @return cell array for the row
     */
    Cell[] getRow(int row) {
        return cells[row];
    }

    /**
     * Set the row for the given row index
     *
     * @param row row index
     */
    public void setRow(int row, Cell[] rowCells) {
        cells[row] = rowCells;
    }
}


/**
 * BlockQueue with Blocks pieces that are falling on the board.
 */
class BlockQueue {
    private final List<Block> blocks;

    public BlockQueue() {
        this.blocks = new ArrayList<>(8);
    }

    /**
     * Add element to the block queue
     *
     * @param block element to add
     */
    public void add(Block block) {
        assert blocks.size() <= 8;
        blocks.add(block);
    }

    /**
     * Get the next element in the block.
     *
     * @return the next block
     */

    public Block getNext() {
        return blocks.size() > 0 ? blocks.get(0) : null;
    }

    /**
     * Pops the block and retuns the popped block.
     *
     * @return the popped block
     */
    Block getNextAndPop() {
        final Block next = getNext();
        if (next != null) {
            blocks.remove(0);
        }
        return next;
    }

    /**
     * Get block at an index
     *
     * @param blockIndex index to get the block at
     * @return Block on that index
     */
    public Block getBlock(int blockIndex) {
        return blocks.get(blockIndex);
    }
}


/**
 * Represents cell on the board.
 */
class Cell {

    private CellColour cellColour;
    private CellStatus cellStatus;

    public Cell(CellColour cellColour, CellStatus cellStatus) {
        this.cellColour = cellColour;
        this.cellStatus = cellStatus;
    }

    public CellStatus getCellStatus() {
        return cellStatus;
    }

    public CellColour getCellColour() {
        return cellColour;
    }

    public void setCellColour(CellColour cellColour) {
        this.cellColour = cellColour;
    }

    public void setCellStatus(CellStatus cellStatus) {
        this.cellStatus = cellStatus;
    }
}
