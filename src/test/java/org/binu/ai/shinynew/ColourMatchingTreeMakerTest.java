package org.binu.ai.shinynew;

import org.binu.board.Block;
import org.binu.board.BlockQueue;
import org.binu.board.Board;
import org.binu.board.Cell;
import org.binu.data.CellColour;
import org.binu.data.CellStatus;
import org.binu.data.ScoreNode;
import org.binu.integration.DataParser;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

/**
 * Test for {@link ColourMatchingTreeMaker}
 */
public class ColourMatchingTreeMakerTest {
    private Board board;
    private DataParser dataParser;
    private BlockQueue blockQueue;
    private ColourMatchingTreeMaker colourMatchingTreeMaker;

    @Before
    public void setUp() throws Exception {
        colourMatchingTreeMaker = new ColourMatchingTreeMaker();
    }

    @Test
    public void should_create_tree() throws Exception {
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
        dataParser = new DataParser();
        board = dataParser.createBoard(boardString);
        blockQueue = new BlockQueue();
        addBlockToBlockQueue(CellColour.GREEN, CellColour.GREEN);
        addBlockToBlockQueue(CellColour.RED, CellColour.RED);
        addBlockToBlockQueue(CellColour.GREEN, CellColour.GREEN);
        addBlockToBlockQueue(CellColour.RED, CellColour.RED);
        addBlockToBlockQueue(CellColour.RED, CellColour.RED);
        addBlockToBlockQueue(CellColour.GREEN, CellColour.GREEN);
        addBlockToBlockQueue(CellColour.RED, CellColour.RED);
        addBlockToBlockQueue(CellColour.RED, CellColour.RED);

        final ScoreNode rootNode = colourMatchingTreeMaker.makeScoreTree(board, blockQueue, null, 50);
        assertThat("Should create a non-null root node", rootNode, is(notNullValue()));
    }

    @Test
    public void should_add_colours_to_map() throws Exception {
        final HashMap<CellColour, Integer> cellColourIntegerHashMap = new HashMap<>();
        colourMatchingTreeMaker.putCellColourInMap(cellColourIntegerHashMap, CellColour.GREEN);
        colourMatchingTreeMaker.putCellColourInMap(cellColourIntegerHashMap, CellColour.GREEN);
        colourMatchingTreeMaker.putCellColourInMap(cellColourIntegerHashMap, CellColour.RED);

        assertThat("Map has size 2", cellColourIntegerHashMap.size(), is(2));
        assertThat("There are two occurrences of green", cellColourIntegerHashMap.get(CellColour.GREEN), is(2));
        assertThat("There one occurrence of red", cellColourIntegerHashMap.get(CellColour.RED), is(1));
    }

    @Test
    public void should_remove_from_map_cell_colour_that_occur_less_than_4_times() throws Exception {
        final HashMap<CellColour, Integer> cellColourOccurrenceHashMap = new HashMap<>();
        cellColourOccurrenceHashMap.put(CellColour.GREEN, 4);
        cellColourOccurrenceHashMap.put(CellColour.RED, 5);
        cellColourOccurrenceHashMap.put(CellColour.BLUE, 3);
        cellColourOccurrenceHashMap.put(CellColour.YELLOW, 2);
        cellColourOccurrenceHashMap.put(CellColour.PURPLE, 1);

        final ArrayList<CellColour> cellColours = colourMatchingTreeMaker.getChainableColours(cellColourOccurrenceHashMap);
        assertThat("Map has size 2", cellColours.size(), is(2));
        assertThat("There are two occurrences of green", cellColours, hasItem(CellColour.GREEN));
        assertThat("There one occurrence of red", cellColours, hasItem(CellColour.RED));
    }

    private void addBlockToBlockQueue(CellColour cell1Colour, CellColour cell2Colour) {
        final Cell[] cells = new Cell[2];
        cells[0] = new Cell(cell1Colour, CellStatus.OCCUPIED);
        cells[1] = new Cell(cell2Colour, CellStatus.OCCUPIED);
        final Block block = new Block(cells);
        blockQueue.add(block);
    }
}
