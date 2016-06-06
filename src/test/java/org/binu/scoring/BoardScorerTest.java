package org.binu.scoring;

import org.binu.board.Board;
import org.binu.data.CellColour;
import org.binu.data.CellStatus;
import org.binu.framework.*;
import org.binu.integration.DataParser;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Test for {@link BoardScorerImpl}
 */
public class BoardScorerTest {

    private DataParser dataParser;
    private Board board;
    private ChainClearer chainClearer;
    private CellArrayHelperImpl cellArrayHelper;
    private BoardCollapser boardCollapser;
    private BoardScorer boardScorer;

    @Before
    public void setUp() throws Exception {
        dataParser = new DataParser();
        board = new Board();
        cellArrayHelper = new CellArrayHelperImpl();
        chainClearer = new ChainClearerImpl(cellArrayHelper);
        boardCollapser = new BoardCollapserImpl(cellArrayHelper);
        boardScorer = new BoardScorerImpl(chainClearer, boardCollapser);
    }

    @Test
    public void should_score_40_to_the_board() throws Exception {
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
                "11....",
                "11...."
        };
        board = dataParser.createBoard(boardString);
        final int score = boardScorer.scoreBoardAndRecursivelyClearAndCollapse(board, false);

        assertThat("Score is 40", score, is(40));
    }

    @Test
    public void should_score_360_to_the_board() throws Exception {
        final String[] boardString = {
                "......",
                "......",
                "......",
                "......",
                "......",
                "......",
                "......",
                "..2...",
                "..2...",
                "..2...",
                "110...",
                "112..."
        };
        board = dataParser.createBoard(boardString);
        final int score = boardScorer.scoreBoardAndRecursivelyClearAndCollapse(board, true);

        assertThat("Score is 360", score, is(360));
        assertThat("Board [0][0] is cleared ", board.getCell(0, 0).getCellStatus(), is(CellStatus.EMPTY));
    }

    @Test
    public void should_score_360_to_the_board_and_update_board() throws Exception {
        final String[] boardString = {
                "......",
                "......",
                "......",
                "......",
                "......",
                "......",
                "......",
                "..2...",
                "..2...",
                "..2...",
                "110...",
                "112..."
        };
        board = dataParser.createBoard(boardString);
        final int score = boardScorer.scoreBoardAndRecursivelyClearAndCollapse(board, false);

        assertThat("Score is 360", score, is(360));
        assertThat("Board [0][0] is cleared ", board.getCell(0, 0).getCellStatus(), is(CellStatus.OCCUPIED));
        assertThat("Board [0][0] is cleared ", board.getCell(0, 0).getCellColour(), is(CellColour.BLUE));
    }

    @Test
    public void should_score_840_to_the_board() throws Exception {
        final String[] boardString = {
                "......",
                "......",
                "......",
                "......",
                "......",
                "......",
                "......",
                "..23..",
                "..23..",
                ".1111.",
                "..23..",
                "..23.."
        };
        board = dataParser.createBoard(boardString);
        final int score = boardScorer.scoreBoardAndRecursivelyClearAndCollapse(board, false);

        assertThat("Score is 840", score, is(840));
    }

    @Test
    public void should_score_1240_to_the_board() throws Exception {
        final String[] boardString = {
                "......",
                "......",
                "......",
                "......",
                "......",
                "......",
                "..23..",
                "..23..",
                "..23..",
                ".1111.",
                "..23..",
                "..23.."
        };
        board = dataParser.createBoard(boardString);
        final int score = boardScorer.scoreBoardAndRecursivelyClearAndCollapse(board, false);

        assertThat("Score is 1240", score, is(1240));
    }

    @Test
    public void should_score_2990_to_the_board() throws Exception {
        final String[] boardString = {
                "......",
                "......",
                "......",
                "......",
                "......",
                "...5..",
                "...2..",
                "...23.",
                "...34.",
                "..3444",
                "..2322",
                "155355"
        };
        board = dataParser.createBoard(boardString);
        final int score = boardScorer.scoreBoardAndRecursivelyClearAndCollapse(board, false);

        assertThat("Score is 2990", score, is(2990));
    }
}
