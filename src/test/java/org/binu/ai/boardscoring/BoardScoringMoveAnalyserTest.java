package org.binu.ai.boardscoring;

import org.binu.board.Block;
import org.binu.board.BlockQueue;
import org.binu.board.Board;
import org.binu.board.Cell;
import org.binu.data.CellColour;
import org.binu.data.CellStatus;
import org.binu.integration.DataParser;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Test for {@link BoardScoringMoveAnalyser}
 */
public class BoardScoringMoveAnalyserTest {
    private DataParser dataParser;
    private BlockQueue blockQueue;

    @Before
    public void setUp() throws Exception {
        dataParser = new DataParser();
        blockQueue = new BlockQueue();
    }

    //TODO identify another chain
    @Ignore
    @Test
    public void should_compute_score_based_on_number_of_disappeared_cells_and_give_more_points_to_chains() throws Exception {
        final String[] boardString = {
                "......",
                "......",
                "......",
                "......",
                "......",
                "......",
                "......",
                "2.....",
                "2.....",
                "0.....",
                "21....",
                "21...."
        };

        final Board board = dataParser.createBoard(boardString);

        final Block block = getBlock(CellColour.BLUE, CellStatus.OCCUPIED, CellColour.BLUE, CellStatus.OCCUPIED);
        blockQueue.add(block);

        final BoardScoringMoveAnalyser boardScoringMoveAnalyser = new BoardScoringMoveAnalyser(board, blockQueue);
        boardScoringMoveAnalyser.setLevel(1);
        final int bestMove = boardScoringMoveAnalyser.findBestMove();
        assertThat("Best move is on column 2", bestMove, is(1));
    }

    @Test
    public void should_compile_blah() throws Exception {
        //blockQueue [1,1]@col1 [2,2]@col2 [2,2]@col2
        final String[] boardString = {
                "......",
                "......",
                "......",
                "......",
                "......",
                "......",
                "3.....",
                "3.....",
                "2.....",
                "2.....",
                "1.....",
                "1....."
        };

        final Board board = dataParser.createBoard(boardString);

        final Block block1 = getBlock(CellColour.PURPLE, CellStatus.OCCUPIED, CellColour.PURPLE, CellStatus.OCCUPIED);
        final Block block2 = getBlock(CellColour.GREEN, CellStatus.OCCUPIED, CellColour.GREEN, CellStatus.OCCUPIED);
        final Block block3 = getBlock(CellColour.GREEN, CellStatus.OCCUPIED, CellColour.GREEN, CellStatus.OCCUPIED);
        blockQueue.add(block1);
        blockQueue.add(block2);
        blockQueue.add(block3);

        final BoardScoringMoveAnalyser boardScoringMoveAnalyser = new BoardScoringMoveAnalyser(board, blockQueue);
        final int bestMove = boardScoringMoveAnalyser.findBestMove();
        assertThat("Best move is on column 1", bestMove, is(0));
    }

    @Test
    public void should_make_a_3_step_chain() throws Exception {
        //blockQueue [1,1]@col1 [2,2]@col2 [2,2]@col2
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
                "0.....",
                "1.....",
                "1....."
        };

        final Board board = dataParser.createBoard(boardString);

        final Block block1 = getBlock(CellColour.BLUE, CellStatus.OCCUPIED, CellColour.BLUE, CellStatus.OCCUPIED);
        final Block block2 = getBlock(CellColour.GREEN, CellStatus.OCCUPIED, CellColour.GREEN, CellStatus.OCCUPIED);
        final Block block3 = getBlock(CellColour.GREEN, CellStatus.OCCUPIED, CellColour.GREEN, CellStatus.OCCUPIED);
        blockQueue.add(block1);
        blockQueue.add(block2);
        blockQueue.add(block3);

        final BoardScoringMoveAnalyser boardScoringMoveAnalyser = new BoardScoringMoveAnalyser(board, blockQueue);
        final int bestMove = boardScoringMoveAnalyser.findBestMove();
        assertThat("Best move is on column 1", bestMove, is(0));
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
