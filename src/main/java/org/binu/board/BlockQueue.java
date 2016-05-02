package org.binu.board;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * BlockQueue with Blocks pieces that are falling on the board.
 */
public class BlockQueue {
    private final List<Block> blocks;

    public BlockQueue() {
        this.blocks = new ArrayList<>(8);
    }

    /**
     * Add element to the block queue
     * @param block element to add
     */
    public void add(Block block) {
        assert blocks.size() <= 8;
        blocks.add(block);
    }

    /**
     * Get the next element in the block.
     * @return the next block
     */
    @Nullable
    public Block getNext() {
        return blocks.size() > 0 ? blocks.get(0) : null;
    }

    /**
     * Pops the block and retuns the popped block.
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
     * @param blockIndex index to get the block at
     * @return Block on that index
     */
    public Block getBlock(int blockIndex) {
        return blocks.get(blockIndex);
    }
}
