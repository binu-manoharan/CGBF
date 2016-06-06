package org.binu.scoring;

import org.binu.board.Board;
import org.binu.framework.CellArrayHelperImpl;
import org.binu.framework.ChainClearer;
import org.binu.framework.ChainClearerImpl;
import org.binu.integration.DataParser;
import org.junit.Before;
import org.junit.Ignore;
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

    @Before
    public void setUp() throws Exception {
        dataParser = new DataParser();
        board = new Board();
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
        chainClearer = new ChainClearerImpl(new CellArrayHelperImpl());
        final BoardScorer boardScorer = new BoardScorerImpl(chainClearer);
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
        chainClearer = new ChainClearerImpl(new CellArrayHelperImpl());
        final BoardScorer boardScorer = new BoardScorerImpl(chainClearer);
        final int score = boardScorer.scoreBoard(board);

        assertThat("Score is 360", score, is(360));
    }
}
