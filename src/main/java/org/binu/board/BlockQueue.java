package org.binu.board;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * BlockQueue with Blocks pieces that are falling on the board.
 */
public class BlockQueue {
    private List<Block> blocks;

    public BlockQueue() {
        this.blocks = new ArrayList<>(8);
    }

    public void add(Block block) {
        assert blocks.size() <= 8;
        blocks.add(block);
    }

    @Nullable
    public Block getNext() {
        return blocks.size() > 0 ? blocks.get(0) : null;
    }

    Block getNextAndPop() {
        Block next = getNext();
        if (next != null) {
            blocks.remove(0);
        }
        return next;
    }
}
