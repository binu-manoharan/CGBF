package org.binu.scoring;

import org.binu.board.Board;
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

    @Before
    public void setUp() throws Exception {
        dataParser = new DataParser();
        board = new Board();
    }

    @Test
    public void should_score_board() throws Exception {
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

        final BoardScorer boardScorer = new BoardScorerImpl();
        final int score = boardScorer.scoreBoard(board);

        assertThat("Score is 40", score, is(40));
    }
}
