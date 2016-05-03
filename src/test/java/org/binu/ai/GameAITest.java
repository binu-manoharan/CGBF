package org.binu.ai;

import org.binu.board.Block;
import org.binu.board.BlockFactory;
import org.binu.board.BlockQueue;
import org.binu.board.Board;
import org.binu.data.CellColour;
import org.binu.data.CellStatus;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Test for {@link IGameAI}.
 */
public class GameAITest {

    private Board board;
    private IGameAI gameAI;
    private BlockQueue blockQueue;

    @Before
    public void setUp() throws Exception {
        board = new Board();
        blockQueue = new BlockQueue();
        gameAI = new SimpleGameAI(board, blockQueue);
    }

    @Test
    public void should_find_next_move_to_make_4() throws Exception {
        board.setCell(0,0, CellStatus.OCCUPIED, CellColour.RED);
        board.setCell(1,0, CellStatus.OCCUPIED, CellColour.RED);

        Block block = BlockFactory.create2x1SameColourBlock(CellColour.RED);
        blockQueue.add(block);

        int nextMove = gameAI.calculateNextMove();
        assertThat("The AI should make a 4 by calculating the next move to be first column",
                nextMove, is(0));
    }

    @Test
    public void should_find_next_better_move_to_make_5() throws Exception {
        board.setCell(0, 0, CellStatus.OCCUPIED, CellColour.RED);
        board.setCell(1, 0, CellStatus.OCCUPIED, CellColour.RED);

        board.setCell(0, 1, CellStatus.OCCUPIED, CellColour.RED);
        board.setCell(1, 1, CellStatus.OCCUPIED, CellColour.RED);
        board.setCell(2, 1, CellStatus.OCCUPIED, CellColour.RED);

        Block block = BlockFactory.create2x1SameColourBlock(CellColour.RED);
        blockQueue.add(block);

        int nextMove = gameAI.calculateNextMove();
        assertThat("The AI should make a 5 by calculating the next move to be second column",
                nextMove, is(1));
    }

    @Test
    public void should_make_4_instead_of_making_5_with_other_colours() throws Exception {
        board.setCell(0, 0, CellStatus.OCCUPIED, CellColour.RED);
        board.setCell(1, 0, CellStatus.OCCUPIED, CellColour.RED);

        board.setCell(0, 1, CellStatus.OCCUPIED, CellColour.RED);
        board.setCell(1, 1, CellStatus.OCCUPIED, CellColour.GREEN);
        board.setCell(2, 1, CellStatus.OCCUPIED, CellColour.BLUE);

        Block block = BlockFactory.create2x1SameColourBlock(CellColour.RED);
        blockQueue.add(block);

        int nextMove = gameAI.calculateNextMove();
        assertThat("The AI should make 4 by calculating the next move to be first column",
                nextMove, is(0));
    }

    @Test
    public void should_add_to_an_empty_row_if_there_is_no_match_on_others() throws Exception {
        board.setCell(0, 0, CellStatus.OCCUPIED, CellColour.RED);
        board.setCell(1, 0, CellStatus.OCCUPIED, CellColour.RED);

        board.setCell(0, 1, CellStatus.OCCUPIED, CellColour.GREEN);
        board.setCell(1, 1, CellStatus.OCCUPIED, CellColour.GREEN);
        board.setCell(2, 1, CellStatus.OCCUPIED, CellColour.GREEN);

        Block block = BlockFactory.create2x1SameColourBlock(CellColour.BLUE);
        blockQueue.add(block);

        int nextMove = gameAI.calculateNextMove();
        assertThat("The AI should make 2 as there are no matches on the first two columns",
                nextMove, is(2));
    }
}