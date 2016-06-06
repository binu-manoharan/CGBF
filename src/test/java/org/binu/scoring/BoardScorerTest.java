package org.binu.scoring;

import org.binu.board.Board;
import org.binu.framework.*;
import org.binu.integration.DataParser;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Created by binu on 05/06/16.
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
        final int score = boardScorer.scoreBoard(board);

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
        final int score = boardScorer.scoreBoard(board);

        assertThat("Score is 360", score, is(360));
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
        final int score = boardScorer.scoreBoard(board);

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
        final int score = boardScorer.scoreBoard(board);

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
        final int score = boardScorer.scoreBoard(board);

        assertThat("Score is 2990", score, is(2990));
    }
}