package org.binu.board;

/**
 * Block representing the pieces that could fall on the {@link Board}
 */
public class Block {
    private Cell[] cells = new Cell[2];

    public Block(Cell[] cells) {
        this.cells = cells;
    }
}
