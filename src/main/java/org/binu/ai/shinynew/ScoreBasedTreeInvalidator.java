package org.binu.ai.shinynew;

/**
 * Invalidates the tree based on myLastScore after identifying skull drops
 */
public class ScoreBasedTreeInvalidator {

    public static final int SCORE_TO_DROP_ONE_SKULL_ROW = 420;
    private int myLastScore;
    private int opponentLastScore;

    public boolean isMyTreeValid(int myScore) {
        return myScore / SCORE_TO_DROP_ONE_SKULL_ROW == myLastScore / SCORE_TO_DROP_ONE_SKULL_ROW;
    }

    public void setMyLastScore(int myLastScore) {
        this.myLastScore = myLastScore;
    }

    public void setOpponentLastScore(int opponentLastScore) {
        this.opponentLastScore = opponentLastScore;
    }

    public boolean isOpponentTreeValid(int opponentScore) {
        return opponentScore / SCORE_TO_DROP_ONE_SKULL_ROW == opponentLastScore / SCORE_TO_DROP_ONE_SKULL_ROW;
    }
}
