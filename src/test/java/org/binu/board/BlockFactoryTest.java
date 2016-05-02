package org.binu.board;

import org.binu.data.CellColour;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Test for {@link BlockFactory}
 */
public class BlockFactoryTest {

    @Test
    public void should_create_a_2x1_block() throws Exception {
        Block block = BlockFactory.create2x1SameColourBlock(CellColour.GREEN);

        assertThat("Block has 2 elements", block.getCells().length, is(2));
        assertThat("Block has 2 elements", block.getCells()[0].getCellColour(), is(CellColour.GREEN));
        assertThat("Block has 2 elements", block.getCells()[1].getCellColour(), is(CellColour.GREEN));
    }
}