package org.binu.ai.shinynew;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Test for {@link ScoreBasedTreeInvalidator}
 */
public class ScoreBasedTreeInvalidatorTest {

    public static final int SCORE1 = 0;
    public static final int SCORE2 = 420;
    public static final int SCORE3 = 640;
    private ScoreBasedTreeInvalidator scoreBasedTreeInvalidator;

    @Before
    public void setUp() throws Exception {
        scoreBasedTreeInvalidator = new ScoreBasedTreeInvalidator();
    }

    @Test
    public void should_invalidate_my_tree_if_there_are_any_skull_drops() throws Exception {
        scoreBasedTreeInvalidator = new ScoreBasedTreeInvalidator();
        scoreBasedTreeInvalidator.setOpponentLastScore(SCORE1);
        final boolean isMyTreeValid1 = scoreBasedTreeInvalidator.isMyTreeValid(SCORE2);
        assertThat("The tree is invalid as a row of skulls have fallen", isMyTreeValid1, is(false));

        scoreBasedTreeInvalidator.setOpponentLastScore(SCORE2);
        final boolean isMyTreeValid2 = scoreBasedTreeInvalidator.isMyTreeValid(SCORE3);
        assertThat("The tree is invalid as a row of skulls have fallen", isMyTreeValid2, is(true));
    }


    @Test
    public void should_invalidate_opponent_tree_if_there_are_any_skull_drops() throws Exception {
        scoreBasedTreeInvalidator.setMyLastScore(SCORE1);
        final boolean isOpponentTreeValid1 = scoreBasedTreeInvalidator.isOpponentTreeValid(SCORE2);
        assertThat("The tree is invalid as a row of skulls have fallen", isOpponentTreeValid1, is(false));

        scoreBasedTreeInvalidator.setMyLastScore(SCORE2);
        final boolean isOpponentTreeValid2 = scoreBasedTreeInvalidator.isOpponentTreeValid(SCORE3);
        assertThat("The tree is invalid as a row of skulls have fallen", isOpponentTreeValid2, is(true));
    }
}
