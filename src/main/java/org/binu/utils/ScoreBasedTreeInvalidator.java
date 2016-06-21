package org.binu.utils;

/**
 * Invalidates the tree based on myLastScore after identifying skull drops
 */
public class ScoreBasedTreeInvalidator {

    public static final int SCORE_TO_DROP_ONE_SKULL_ROW = 420;
    private int myLastScore;
    private int opponentLastScore;

    /**
     * My tree is valid based on previous score of the opponent. If his score has gone up enough to drop another
     * row of skulls then my tree needs to be invalidated.
     * @param opponentScore opponent score
     * @return true if skulls are dropped
     */
    public boolean isMyTreeValid(int opponentScore) {
        return opponentScore / SCORE_TO_DROP_ONE_SKULL_ROW == opponentLastScore / SCORE_TO_DROP_ONE_SKULL_ROW;
    }

    /**
     * Opponent tree is valid based on my previous score. If I have dropped a row of skulls then my tree for the
     * opponent needs to be invalidated.
     * @param myScore opponent score
     * @return true if skulls are dropped
     */
    public boolean isOpponentTreeValid(int myScore) {
        return myScore / SCORE_TO_DROP_ONE_SKULL_ROW == myLastScore / SCORE_TO_DROP_ONE_SKULL_ROW;
    }

    public void setMyLastScore(int myLastScore) {
        this.myLastScore = myLastScore;
    }

    public void setOpponentLastScore(int opponentLastScore) {
        this.opponentLastScore = opponentLastScore;
    }
}
