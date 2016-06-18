package org.binu.framework;

import org.binu.board.Block;
import org.binu.board.BlockQueue;
import org.binu.board.Board;
import org.binu.board.Cell;
import org.binu.data.CellColour;
import org.binu.data.CellStatus;
import org.binu.data.Orientation;
import org.binu.integration.DataParser;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * Test for {@link CellArrayHelper}.
 */
public class CellArrayHelperTest {

    private Cell[] cells;
    private CellArrayHelper cellArrayHelper;
    private Block block;
    private DataParser dataParser;
    private Board board;
    private BlockQueue blockQueue;

    @Before
    public void setUp() throws Exception {
        cells = new Cell[12];
        cellArrayHelper = new CellArrayHelperImpl();
        dataParser = new DataParser();
        initEmptyCells();
    }

    @Test
    public void should_return_top_of_column_as_2() throws Exception {
        board = new Board();
        cells[0] = getCell(CellColour.GREEN, CellStatus.OCCUPIED);
        cells[1] = getCell(CellColour.RED, CellStatus.OCCUPIED);

        final int firstEmptyPosition = cellArrayHelper.getFirstEmptyPosition(cells);

        assertThat("First empty position is 2 (zero indexed)", firstEmptyPosition, is(2));
        assertThat("Board is not clearable", cellArrayHelper.isClearable(board), is(false));
    }

    @Test
    public void should_return_top_of_column_as_3() throws Exception {
        cells[0] = getCell(CellColour.GREEN, CellStatus.OCCUPIED);
        cells[1] = getCell(CellColour.RED, CellStatus.OCCUPIED);
        cells[2] = getCell(CellColour.BLUE, CellStatus.OCCUPIED);

        final int firstEmptyPosition = cellArrayHelper.getFirstEmptyPosition(cells);

        assertThat("First empty position is 3 (zero indexed)", firstEmptyPosition, is(3));
    }

    @Test
    public void should_provide_the_indexes_of_the_elements_in_the_form_of_2x2_grid() throws Exception {
        final Cell[][] cellArray = new Cell[2][6];
        cellArray[0][0] = getCell(CellColour.RED, CellStatus.OCCUPIED);
        cellArray[0][1] = getCell(CellColour.RED, CellStatus.OCCUPIED);
        cellArray[0][2] = getCell(CellColour.RED, CellStatus.OCCUPIED);
        cellArray[0][3] = getCell(CellColour.GREEN, CellStatus.OCCUPIED);
        cellArray[0][4] = getCell(CellColour.RED, CellStatus.OCCUPIED);
        cellArray[0][5] = getCell(CellColour.RED, CellStatus.OCCUPIED);

        cellArray[1][0] = getCell(CellColour.RED, CellStatus.OCCUPIED);
        cellArray[1][1] = getCell(CellColour.RED, CellStatus.OCCUPIED);
        cellArray[1][2] = getCell(CellColour.RED, CellStatus.OCCUPIED);
        cellArray[1][3] = getCell(CellColour.RED, CellStatus.OCCUPIED);
        cellArray[1][4] = getCell(CellColour.RED, CellStatus.OCCUPIED);
        cellArray[1][5] = getCell(CellColour.RED, CellStatus.OCCUPIED);

        final List<int[]> indexOf4BlockGroup = cellArrayHelper.getIndexOf4BlockGroup(cellArray);

        assertThat("Index of block of 4 is", indexOf4BlockGroup.size(), is(2));
        assertBlockItems(indexOf4BlockGroup.get(0)[0], 0, indexOf4BlockGroup.get(0)[1], 0);
        assertBlockItems(indexOf4BlockGroup.get(1)[0], 0, indexOf4BlockGroup.get(1)[1], 4);

    }

    @Test
    public void should_provide_the_indexes_of_the_elements_in_the_form_of_2x2_grid_from_the_board() throws Exception {
        final String[] boardString = {
                "....33",
                "....33",
                "......",
                "......",
                "..44..",
                "..44..",
                ".11...",
                ".12...",
                ".22...",
                "022...",
                "111000",
                "111000"
        };
        board = dataParser.createBoard(boardString);

        final List<int[]> indexOf4BlockGroup = cellArrayHelper.getIndexOf4BlockGroup(board.getBoard());

        assertThat("Index of block of 4 is", indexOf4BlockGroup.size(), is(4));

        assertBlockItems(indexOf4BlockGroup.get(0)[0], 0, indexOf4BlockGroup.get(0)[1], 0);
        assertBlockItems(indexOf4BlockGroup.get(1)[0], 2, indexOf4BlockGroup.get(1)[1], 1);
        assertBlockItems(indexOf4BlockGroup.get(2)[0], 6, indexOf4BlockGroup.get(2)[1], 2);
        assertBlockItems(indexOf4BlockGroup.get(3)[0], 10, indexOf4BlockGroup.get(3)[1], 4);
    }

    @Test
    public void should_provide_the_indexes_of_the_elements_in_the_form_of_2x2_grid_from_the_board_including_consecutive_ones() throws Exception {
        final String[] boardString = {
                "....33",
                "....33",
                "......",
                "......",
                "..44..",
                "..44..",
                ".22...",
                ".22...",
                ".22...",
                "022...",
                "111000",
                "111000"
        };
        board = dataParser.createBoard(boardString);

        final List<int[]> indexOf4BlockGroup = cellArrayHelper.getIndexOf4BlockGroup(board.getBoard());

        //ideally it should be 5 but the extra point comes from the consecutive 2x2 blocks
        assertThat("Index of block of 4 is", indexOf4BlockGroup.size(), is(6));

        assertBlockItems(indexOf4BlockGroup.get(0)[0], 0, indexOf4BlockGroup.get(0)[1], 0);
        assertBlockItems(indexOf4BlockGroup.get(1)[0], 2, indexOf4BlockGroup.get(1)[1], 1);
        assertBlockItems(indexOf4BlockGroup.get(2)[0], 3, indexOf4BlockGroup.get(2)[1], 1);
        assertBlockItems(indexOf4BlockGroup.get(3)[0], 4, indexOf4BlockGroup.get(3)[1], 1);
        assertBlockItems(indexOf4BlockGroup.get(4)[0], 6, indexOf4BlockGroup.get(4)[1], 2);
        assertBlockItems(indexOf4BlockGroup.get(5)[0], 10, indexOf4BlockGroup.get(5)[1], 4);
    }

    @Test
    public void should_provide_the_indexes_of_the_elements_in_the_form_of_L_or_T() throws Exception {
        final String[] boardString = {
                "....3.",
                "55..33",
                ".5..3.",
                ".5.4..",
                "..44..",
                "...4..",
                ".2.5..",
                ".2555.",
                ".22...",
                "...3..",
                "111333",
                "..1..."
        };
        board = dataParser.createBoard(boardString);

        final List<int[]> indexOf4BlockGroup = cellArrayHelper.getIndexOfLAndT(board.getBoard());

        assertThat("Index of block of 4 is", indexOf4BlockGroup.size(), is(7));

        assertBlockItems(indexOf4BlockGroup.get(0)[0], 1, indexOf4BlockGroup.get(0)[1], 3);
        assertBlockItems(indexOf4BlockGroup.get(1)[0], 4, indexOf4BlockGroup.get(1)[1], 2);
        assertBlockItems(indexOf4BlockGroup.get(2)[0], 1, indexOf4BlockGroup.get(2)[1], 0);
        assertBlockItems(indexOf4BlockGroup.get(3)[0], 3, indexOf4BlockGroup.get(3)[1], 1);
        assertBlockItems(indexOf4BlockGroup.get(4)[0], 9, indexOf4BlockGroup.get(4)[1], 4);
        assertBlockItems(indexOf4BlockGroup.get(5)[0], 6, indexOf4BlockGroup.get(5)[1], 3);
        assertBlockItems(indexOf4BlockGroup.get(6)[0], 8, indexOf4BlockGroup.get(6)[1], 1);
    }

    @Test
    public void should_provide_the_indexes_of_the_elements_in_the_form_Z() throws Exception {
        final String[] boardString = {
                "......",
                "....5.",
                "...55.",
                "...5..",
                ".33...",
                "..33..",
                "......",
                "....2.",
                "....22",
                ".....2",
                ".11...",
                "11...."
        };
        board = dataParser.createBoard(boardString);

        final List<int[]> indexOf4BlockGroup = cellArrayHelper.getIndexOfZ(board.getBoard());

        assertThat("Index of block of 4 is", indexOf4BlockGroup.size(), is(4));

        assertBlockItems(indexOf4BlockGroup.get(0)[0], 3, indexOf4BlockGroup.get(0)[1], 4);
        assertBlockItems(indexOf4BlockGroup.get(1)[0], 9, indexOf4BlockGroup.get(1)[1], 3);
        assertBlockItems(indexOf4BlockGroup.get(2)[0], 0, indexOf4BlockGroup.get(2)[1], 1);
        assertBlockItems(indexOf4BlockGroup.get(3)[0], 6, indexOf4BlockGroup.get(3)[1], 2);
    }

    @Test
    public void should_provide_the_indexes_of_the_elements_in_the_form_4_block_line() throws Exception {
        final String[] boardString = {
                "..5...",
                "..5...",
                "..5...",
                "4.5.34",
                "4...34",
                "4...34",
                "4...34",
                "......",
                "1.2222",
                "1.....",
                "1.4444",
                "1....."
        };
        board = dataParser.createBoard(boardString);

        final List<int[]> indexOf4BlockGroup = cellArrayHelper.getIndexOfLines(board.getBoard());

        assertThat("Index of block of 4 is", indexOf4BlockGroup.size(), is(7));

        assertBlockItems(indexOf4BlockGroup.get(0)[0], 0, indexOf4BlockGroup.get(0)[1], 0);
        assertBlockItems(indexOf4BlockGroup.get(1)[0], 5, indexOf4BlockGroup.get(1)[1], 0);
        assertBlockItems(indexOf4BlockGroup.get(2)[0], 5, indexOf4BlockGroup.get(2)[1], 4);
        assertBlockItems(indexOf4BlockGroup.get(3)[0], 5, indexOf4BlockGroup.get(3)[1], 5);
        assertBlockItems(indexOf4BlockGroup.get(4)[0], 8, indexOf4BlockGroup.get(4)[1], 2);
        assertBlockItems(indexOf4BlockGroup.get(5)[0], 1, indexOf4BlockGroup.get(5)[1], 2);
        assertBlockItems(indexOf4BlockGroup.get(6)[0], 3, indexOf4BlockGroup.get(6)[1], 2);
        assertThat("Board is clearable", cellArrayHelper.isClearable(board), is(true));
    }

    @Test
    public void should_move_cells_to_empty_positions() throws Exception {
        final String[] boardString = {
                "......",
                "......",
                "......",
                "......",
                "......",
                "......",
                "......",
                "......",
                "......",
                "23....",
                ".0....",
                "1....."
        };
        board = dataParser.createBoard(boardString);

        firstColumnChecks();
        secondColumnChecks();
    }

    @Test
    public void should_drop_cells_on_the_column() throws Exception {
        final String[] boardString = {
                "......",
                "......",
                "......",
                "......",
                "......",
                "......",
                "......",
                "......",
                "......",
                "......",
                "1.....",
                "1....."
        };
        board = dataParser.createBoard(boardString);
        final Cell[] column = board.getColumn(0);
        final Block block = getBlock(CellColour.BLUE, CellStatus.OCCUPIED, CellColour.BLUE, CellStatus.OCCUPIED);
        blockQueue = new BlockQueue();
        blockQueue.add(block);
        final Cell[] cells = cellArrayHelper.dropBlockIntoColumn(column, block);

        assertThat("The 1st cell should be BLUE", cells[0].getCellColour(), is(CellColour.BLUE));
        assertThat("The 2nd cell should be BLUE", cells[1].getCellColour(), is(CellColour.BLUE));
        assertThat("The 3rd cell should be BLUE", cells[2].getCellColour(), is(CellColour.BLUE));
        assertThat("The 3rd cell should be BLUE", column[2].getCellColour(), is(nullValue()));
        assertThat("The 4th cell should be BLUE", cells[3].getCellColour(), is(CellColour.BLUE));
        assertThat("The 4th cell should be BLUE", column[3].getCellColour(), is(nullValue()));
    }

    @Test
    public void should_drop_cells_on_the_column_as_there_is_slots_left() throws Exception {
        final String[] boardString = {
                "......",
                "......",
                "1.....",
                "1.....",
                "2.....",
                "2.....",
                "1.....",
                "1.....",
                "2.....",
                "2.....",
                "1.....",
                "1....."
        };
        board = dataParser.createBoard(boardString);
        final Cell[] column = board.getColumn(0);
        final Block block = getBlock(CellColour.BLUE, CellStatus.OCCUPIED, CellColour.BLUE, CellStatus.OCCUPIED);
        blockQueue = new BlockQueue();
        blockQueue.add(block);
        final Cell[] cells = cellArrayHelper.dropBlockIntoColumn(column, block);

        assertThat("The 1st cell should be BLUE", cells[10].getCellColour(), is(CellColour.BLUE));
        assertThat("The 2nd cell should be BLUE", cells[11].getCellColour(), is(CellColour.BLUE));
    }

    @Test (expected = ArrayIndexOutOfBoundsException.class)
    public void should_not_allow_drop_cells_on_the_column_as_there_are_not_enough_slots_left() throws Exception {
        final String[] boardString = {
                "......",
                "2.....",
                "1.....",
                "1.....",
                "2.....",
                "2.....",
                "1.....",
                "1.....",
                "2.....",
                "2.....",
                "1.....",
                "1....."
        };
        board = dataParser.createBoard(boardString);
        final Cell[] column = board.getColumn(0);
        final Block block = getBlock(CellColour.BLUE, CellStatus.OCCUPIED, CellColour.BLUE, CellStatus.OCCUPIED);
        blockQueue = new BlockQueue();
        blockQueue.add(block);
        cellArrayHelper.dropBlockIntoColumn(column, block);
    }

    @Test
    public void should_return_true_to_the_block_is_droppable_on_the_board_column_0() throws Exception {
        final String[] boardString = {
                "......",
                "......",
                "1.....",
                "1.....",
                "2.....",
                "2.....",
                "1.....",
                "1.....",
                "2.....",
                "2.....",
                "1.....",
                "1....."
        };
        board = dataParser.createBoard(boardString);
        final Block block = getBlock(CellColour.BLUE, CellStatus.OCCUPIED, CellColour.BLUE, CellStatus.OCCUPIED);
        blockQueue = new BlockQueue();
        blockQueue.add(block);

        final boolean isDroppable = cellArrayHelper.blockIsDroppableOnColumn(board, block, 0, Orientation.VERTICAL);
        assertThat("Block is droppable on column 0: ", isDroppable, is(true));
    }

    @Test
    public void should_return_false_to_the_block_is_droppable_on_the_board_column_0() throws Exception {
        final String[] boardString = {
                "......",
                "5.....",
                "1.....",
                "1.....",
                "2.....",
                "2.....",
                "1.....",
                "1.....",
                "2.....",
                "2.....",
                "1.....",
                "1....."
        };
        board = dataParser.createBoard(boardString);
        final Block block = getBlock(CellColour.BLUE, CellStatus.OCCUPIED, CellColour.BLUE, CellStatus.OCCUPIED);
        blockQueue = new BlockQueue();
        blockQueue.add(block);

        final boolean isDroppable = cellArrayHelper.blockIsDroppableOnColumn(board, block, 0, Orientation.VERTICAL);
        assertThat("Block is droppable on column 0: ", isDroppable, is(false));
    }

    @Test
    public void should_drop_cells_vertically_on_the_board_column_1() throws Exception {
        final String[] boardString = {
                "......",
                "......",
                "1.....",
                "1.....",
                "2.....",
                "2.....",
                "1.....",
                "1.....",
                "2.....",
                "2.....",
                "1.....",
                "1....."
        };
        board = dataParser.createBoard(boardString);
        final Block block = getBlock(CellColour.RED, CellStatus.OCCUPIED, CellColour.BLUE, CellStatus.OCCUPIED);
        blockQueue = new BlockQueue();
        blockQueue.add(block);

        final boolean operationResult = cellArrayHelper.dropBlockIntoBoard(this.board, block, 0, Orientation.VERTICAL);

        assertThat("Drop was successful: ", operationResult, is(true));
        assertThat("The 1st cell should be BLUE", board.getCell(10, 0).getCellColour(), is(CellColour.BLUE));
        assertThat("The 2nd cell should be BLUE", board.getCell(11, 0).getCellColour(), is(CellColour.RED));
    }

    @Test
    public void should_drop_cells_reverse_vertically_on_the_board_column_1() throws Exception {
        final String[] boardString = {
                "......",
                "......",
                "1.....",
                "1.....",
                "2.....",
                "2.....",
                "1.....",
                "1.....",
                "2.....",
                "2.....",
                "1.....",
                "1....."
        };
        board = dataParser.createBoard(boardString);
        final Block block = getBlock(CellColour.RED, CellStatus.OCCUPIED, CellColour.BLUE, CellStatus.OCCUPIED);
        blockQueue = new BlockQueue();
        blockQueue.add(block);

        final boolean operationResult = cellArrayHelper.dropBlockIntoBoard(this.board, block, 0, Orientation.VERTICAL_REVERSED);

        assertThat("Drop was successful: ", operationResult, is(true));
        assertThat("The 1st cell should be BLUE", board.getCell(10, 0).getCellColour(), is(CellColour.RED));
        assertThat("The 2nd cell should be BLUE", board.getCell(11, 0).getCellColour(), is(CellColour.BLUE));
    }

    @Test
    public void should_drop_cells_on_the_board_first_column_as_there_is_slots_left_to_drop_horizontally() throws Exception {
        final String[] boardString = {
                "......",
                "4.....",
                "1.....",
                "1.....",
                "2.....",
                "2.....",
                "1.....",
                "1.....",
                "2.....",
                "2.....",
                "1.....",
                "1....."
        };
        board = dataParser.createBoard(boardString);
        final Block block = getBlock(CellColour.RED, CellStatus.OCCUPIED, CellColour.BLUE, CellStatus.OCCUPIED);
        blockQueue = new BlockQueue();
        blockQueue.add(block);

        final boolean operationResult = cellArrayHelper.dropBlockIntoBoard(this.board, block, 0, Orientation.HORIZONTAL);

        assertThat("Drop was successful: ", operationResult, is(true));
        assertThat("The 1st cell should be RED", board.getCell(11, 0).getCellColour(), is(CellColour.RED));
        assertThat("The 2nd cell should be BLUE", board.getCell(0, 1).getCellColour(), is(CellColour.BLUE));
    }

    @Test
    public void should_not_be_able_to_drop_cells_on_the_board_sixth_column_horizontally() throws Exception {
        final String[] boardString = {
                "......",
                "4.....",
                "1.....",
                "1.....",
                "2.....",
                "2.....",
                "1.....",
                "1.....",
                "2.....",
                "2.....",
                "1.....",
                "1....."
        };
        board = dataParser.createBoard(boardString);
        final Block block = getBlock(CellColour.RED, CellStatus.OCCUPIED, CellColour.BLUE, CellStatus.OCCUPIED);
        blockQueue = new BlockQueue();
        blockQueue.add(block);

        final boolean operationResult = cellArrayHelper.dropBlockIntoBoard(this.board, block, 5, Orientation.HORIZONTAL);

        assertThat("Drop was unsuccessful: ", operationResult, is(false));
    }

    @Test
    public void should_not_be_able_to_drop_cells_on_the_board_first_column_when_horizontal_reversed() throws Exception {
        final String[] boardString = {
                "......",
                "4.....",
                "1.....",
                "1.....",
                "2.....",
                "2.....",
                "1.....",
                "1.....",
                "2.....",
                "2.....",
                "1.....",
                "1....."
        };
        board = dataParser.createBoard(boardString);
        final Block block = getBlock(CellColour.BLUE, CellStatus.OCCUPIED, CellColour.RED, CellStatus.OCCUPIED);
        blockQueue = new BlockQueue();
        blockQueue.add(block);

        final boolean operationResult = cellArrayHelper.dropBlockIntoBoard(this.board, block, 0, Orientation.HORIZONTAL_REVERSED);

        assertThat("Drop was unsuccessful: ", operationResult, is(false));
    }

    @Test
    public void should_drop_cells_on_the_board_sixth_column_when_horizontal_reversed() throws Exception {
        final String[] boardString = {
                "......",
                "4.....",
                "1.....",
                "1.....",
                "2.....",
                "2.....",
                "1.....",
                "1.....",
                "2.....",
                "2.....",
                "1.....",
                "1...1."
        };
        board = dataParser.createBoard(boardString);
        final Block block = getBlock(CellColour.BLUE, CellStatus.OCCUPIED, CellColour.RED, CellStatus.OCCUPIED);
        blockQueue = new BlockQueue();
        blockQueue.add(block);

        final boolean operationResult = cellArrayHelper.dropBlockIntoBoard(this.board, block, 5, Orientation.HORIZONTAL_REVERSED);

        assertThat("Drop was successful: ", operationResult, is(true));
        assertThat("The 1st cell should be RED", board.getCell(1, 4).getCellColour(), is(CellColour.RED));
        assertThat("The 2nd cell should be BLUE", board.getCell(0, 5).getCellColour(), is(CellColour.BLUE));
    }

    @Test
    public void should_drop_cells_on_the_board_columns_with_horizontal_combinations_exactly_on_top_of_eachother() throws Exception {
        final String[] boardString = {
                "......",
                "......",
                "......",
                "......",
                "......",
                "......",
                "......",
                "......",
                "......",
                "......",
                "......",
                "......"
        };
        board = dataParser.createBoard(boardString);
        final Block block1 = getBlock(CellColour.BLUE, CellStatus.OCCUPIED, CellColour.RED, CellStatus.OCCUPIED);
        final Block block2 = getBlock(CellColour.GREEN, CellStatus.OCCUPIED, CellColour.PURPLE, CellStatus.OCCUPIED);

        final boolean operationResult1 = cellArrayHelper.dropBlockIntoBoard(this.board, block1, 5, Orientation.HORIZONTAL_REVERSED);

        assertThat("Drop was successful: ", operationResult1, is(true));
        assertThat("The 1st cell should be RED", board.getCell(0, 4).getCellColour(), is(CellColour.RED));
        assertThat("The 2nd cell should be BLUE", board.getCell(0, 5).getCellColour(), is(CellColour.BLUE));

        final boolean operationResult2 = cellArrayHelper.dropBlockIntoBoard(this.board, block2, 1, Orientation.HORIZONTAL_REVERSED);

        assertThat("Drop was successful: ", operationResult2, is(true));
        assertThat("The 1st cell should be PURPLE", board.getCell(0, 0).getCellColour(), is(CellColour.PURPLE));
        assertThat("The 2nd cell should be GREEN", board.getCell(0, 1).getCellColour(), is(CellColour.GREEN));

        final boolean operationResult3 = cellArrayHelper.dropBlockIntoBoard(this.board, block1, 0, Orientation.HORIZONTAL);

        assertThat("Drop was successful: ", operationResult3, is(true));
        assertThat("The 1st cell should be BLUE", board.getCell(1, 0).getCellColour(), is(CellColour.BLUE));
        assertThat("The 2nd cell should be RED", board.getCell(1, 1).getCellColour(), is(CellColour.RED));

        final boolean operationResult4 = cellArrayHelper.dropBlockIntoBoard(this.board, block2, 4, Orientation.HORIZONTAL);

        assertThat("Drop was successful: ", operationResult4, is(true));
        assertThat("The 1st cell should be GREEN", board.getCell(1, 4).getCellColour(), is(CellColour.GREEN));
        assertThat("The 2nd cell should be PURPLE", board.getCell(1, 5).getCellColour(), is(CellColour.PURPLE));
    }

    @Test
    public void should_drop_cells_on_the_board_columns_with_horizontal_combinations_partially_overlapping() throws Exception {
        final String[] boardString = {
                "......",
                "......",
                "......",
                "......",
                "......",
                "......",
                "......",
                "......",
                "......",
                "......",
                "......",
                "......"
        };
        board = dataParser.createBoard(boardString);
        final Block block1 = getBlock(CellColour.BLUE, CellStatus.OCCUPIED, CellColour.RED, CellStatus.OCCUPIED);
        final Block block2 = getBlock(CellColour.GREEN, CellStatus.OCCUPIED, CellColour.PURPLE, CellStatus.OCCUPIED);

        final boolean operationResult1 = cellArrayHelper.dropBlockIntoBoard(this.board, block1, 5, Orientation.HORIZONTAL_REVERSED);

        assertThat("Drop was successful: ", operationResult1, is(true));
        assertThat("The 1st cell should be RED", board.getCell(0, 4).getCellColour(), is(CellColour.RED));
        assertThat("The 2nd cell should be BLUE", board.getCell(0, 5).getCellColour(), is(CellColour.BLUE));

        final boolean operationResult2 = cellArrayHelper.dropBlockIntoBoard(this.board, block2, 1, Orientation.HORIZONTAL_REVERSED);

        assertThat("Drop was successful: ", operationResult2, is(true));
        assertThat("The 1st cell should be PURPLE", board.getCell(0, 0).getCellColour(), is(CellColour.PURPLE));
        assertThat("The 2nd cell should be GREEN", board.getCell(0, 1).getCellColour(), is(CellColour.GREEN));

        final boolean operationResult3 = cellArrayHelper.dropBlockIntoBoard(this.board, block1, 1, Orientation.HORIZONTAL);

        assertThat("Drop was successful: ", operationResult3, is(true));
        assertThat("The 1st cell should be BLUE", board.getCell(1, 1).getCellColour(), is(CellColour.BLUE));
        assertThat("The 2nd cell should be RED", board.getCell(0, 2).getCellColour(), is(CellColour.RED));

        final boolean operationResult4 = cellArrayHelper.dropBlockIntoBoard(this.board, block2, 3, Orientation.HORIZONTAL);

        assertThat("Drop was successful: ", operationResult4, is(true));
        assertThat("The 1st cell should be GREEN", board.getCell(0, 3).getCellColour(), is(CellColour.GREEN));
        assertThat("The 2nd cell should be PURPLE", board.getCell(1, 4).getCellColour(), is(CellColour.PURPLE));
    }

    private Block getBlock(CellColour cell1Colour, CellStatus cell1Status, CellColour cell2Colour, CellStatus cell2Status) {
        final Cell[] blockCells = getCells(cell1Colour, cell1Status, cell2Colour, cell2Status);
        return new Block(blockCells);
    }

    private Cell[] getCells(CellColour cell1Colour, CellStatus cell1Status, CellColour cell2Colour, CellStatus cell2Status) {
        final Cell[] cells = new Cell[2];
        cells[0] = new Cell(cell1Colour, cell1Status);
        cells[1] = new Cell(cell2Colour, cell2Status);
        return cells;
    }

    private void firstColumnChecks() {
        final Cell[] cells = getCollapsedCells(0);
        assertThat("First column [0] is BLUE", cells[0].getCellColour(), is(CellColour.BLUE));
        assertThat("First column [1] is BLUE", cells[1].getCellColour(), is(CellColour.GREEN));
        assertThat("First column [11] is EMPTY", cells[11].getCellStatus(), is(CellStatus.EMPTY));
    }

    private void secondColumnChecks() {
        final Cell[] cells = getCollapsedCells(1);

        assertThat("Second column [0] is BLOCKED", cells[0].getCellStatus(), is(CellStatus.BLOCKED));
        assertThat("Second column [1] is BLUE", cells[1].getCellColour(), is(CellColour.PURPLE));
        assertThat("Second column [2] is EMPTY", cells[2].getCellStatus(), is(CellStatus.EMPTY));
    }

    private Cell[] getCollapsedCells(int column) {
        final Cell[] cells = board.getColumn(column);
        return cellArrayHelper.collapseEmptyCells(cells);
    }

    private void assertBlockItems(int actual, int row, int actual2, int col) {
        assertThat("Index of block of block contains row cell index [0, 0]", actual, is(row));
        assertThat("Index of block of block contains row cell index [0, 0]", actual2, is(col));
    }

    private void initEmptyCells() {
        for (int i = 0; i < cells.length; i++) {
            cells[i] = new Cell(null, CellStatus.EMPTY);
        }
    }

    @NotNull
    private Cell getCell(CellColour cellColour, CellStatus cellStatus) {
        return new Cell(cellColour, cellStatus);
    }
}
