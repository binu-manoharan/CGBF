package org.binu.board;

import org.binu.data.CellColour;
import org.binu.data.CellStatus;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

/**
 * Test for {@link BlockQueue}
 */
public class BlockQueueTest {

    private BlockQueue blockQueue;
    private Block firstBlock;
    private Block secondBlock;

    @Before
    public void setUp() throws Exception {
        this.blockQueue = new BlockQueue();
    }

    @Test
    public void should_add_one_element_to_the_queue() throws Exception {
        firstBlock = getBlock(CellColour.GREEN, CellStatus.OCCUPIED, CellColour.GREEN, CellStatus.OCCUPIED);
        blockQueue.add(firstBlock);

        assertThat("BlockQueue contains the block we just added", blockQueue.getNext(), is(firstBlock));
    }

    @Test
    public void should_add_two_element_to_the_queue() throws Exception {
        firstBlock = getBlock(CellColour.GREEN, CellStatus.OCCUPIED, CellColour.GREEN, CellStatus.OCCUPIED);
        secondBlock = getBlock(CellColour.RED, CellStatus.OCCUPIED, CellColour.RED, CellStatus.OCCUPIED);

        blockQueue.add(firstBlock);
        blockQueue.add(secondBlock);

        assertThat("BlockQueue contains the block we just added", blockQueue.getNext(), is(firstBlock));
        assertThat("BlockQueue contains the block we just added", blockQueue.getNextAndPop(), is(firstBlock));
        assertThat("BlockQueue contains the block we just added", blockQueue.getNext(), is(secondBlock));
        assertThat("BlockQueue contains the block we just added", blockQueue.getNextAndPop(), is(secondBlock));
        assertThat("BlockQueue contains the block we just added", blockQueue.getNextAndPop(), is(nullValue()));
    }

    @Test
    public void should_get_second_element_from_the_block() throws Exception {
        firstBlock = getBlock(CellColour.GREEN, CellStatus.OCCUPIED, CellColour.GREEN, CellStatus.OCCUPIED);
        secondBlock = getBlock(CellColour.RED, CellStatus.OCCUPIED, CellColour.RED, CellStatus.OCCUPIED);

        blockQueue.add(firstBlock);
        blockQueue.add(secondBlock);

        final Block block = blockQueue.getBlock(1);
        assertThat("Second element was provided by getBlock(1)", block, is(secondBlock));
    }

    @Test (expected = AssertionError.class)
    public void should_not_be_able_to_add_more_than_max_elements_to_the_queue() throws Exception {
        firstBlock = getBlock(CellColour.GREEN, CellStatus.OCCUPIED, CellColour.GREEN, CellStatus.OCCUPIED);

        blockQueue.add(firstBlock);
        blockQueue.add(firstBlock);
        blockQueue.add(firstBlock);
        blockQueue.add(firstBlock);
        blockQueue.add(firstBlock);
        blockQueue.add(firstBlock);
        blockQueue.add(firstBlock);
        blockQueue.add(firstBlock);
        blockQueue.add(firstBlock);
        blockQueue.add(firstBlock);
        blockQueue.add(firstBlock);
        blockQueue.add(firstBlock);

        assertThat("BlockQueue contains the block we just added", blockQueue.getNext(), is(firstBlock));
    }

    @Test
    public void should_return_second_element_that_was_added_to_the_queue_when_first_is_deleted() throws Exception {
        firstBlock = getBlock(CellColour.GREEN, CellStatus.OCCUPIED, CellColour.GREEN, CellStatus.OCCUPIED);
        secondBlock = getBlock(CellColour.RED, CellStatus.OCCUPIED, CellColour.RED, CellStatus.OCCUPIED);

        blockQueue.add(firstBlock);
        blockQueue.add(secondBlock);
        blockQueue.getNextAndPop();
        blockQueue.add(firstBlock);

        assertThat("BlockQueue contains the block we just added", blockQueue.getNext(), is(secondBlock));
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
}