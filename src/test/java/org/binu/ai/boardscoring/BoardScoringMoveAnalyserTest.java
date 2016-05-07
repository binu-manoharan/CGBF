package org.binu.ai.boardscoring;

import org.binu.board.Block;
import org.binu.board.BlockQueue;
import org.binu.board.Board;
import org.binu.board.Cell;
import org.binu.data.CellColour;
import org.binu.data.CellStatus;
import org.binu.integration.DataParser;
import org.junit.Before;
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
        final int bestMove = boardScoringMoveAnalyser.findBestMove();
        assertThat("Best move is on column 2", bestMove, is(1));
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
