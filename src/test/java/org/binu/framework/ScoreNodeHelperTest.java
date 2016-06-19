package org.binu.framework;

import org.binu.data.Orientation;
import org.binu.data.ScoreNode;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Test for {@link ScoreNodeHelper}
 */
public class ScoreNodeHelperTest {

    private ScoreNodeHelper scoreNodeHelper;
    private ScoreNode rootNode;

    @Test
    public void should_find_best_score_for_first_level() throws Exception {
        scoreNodeHelper = new ScoreNodeHelper();
        rootNode = new ScoreNode();
        final ScoreNode firstChild = new ScoreNode(0, 50, Orientation.VERTICAL);
        final ScoreNode secondChild = new ScoreNode(1, 20, Orientation.VERTICAL);
        final ScoreNode thirdChild = new ScoreNode(2, 0, Orientation.VERTICAL);
        final ScoreNode fourthChild = new ScoreNode(3, 70, Orientation.VERTICAL);
        final ScoreNode fifthChild = new ScoreNode(4, 90, Orientation.VERTICAL);
        final ScoreNode sixthChild = new ScoreNode(5, 75, Orientation.VERTICAL);

        rootNode.addChild(firstChild);
        rootNode.addChild(secondChild);
        rootNode.addChild(thirdChild);
        rootNode.addChild(fourthChild);
        rootNode.addChild(fifthChild);
        rootNode.addChild(sixthChild);

        final ScoreNode nodeWithBestScore = scoreNodeHelper.getBestScoreNodeForLevel(rootNode, 1);
        final int nodeScore = nodeWithBestScore.getNodeScore();
        assertThat("Node score is 90", nodeScore, is(90));
        assertThat("Node with score 90 is the fifth node", nodeWithBestScore, is(fifthChild));
    }

    @Test
    public void should_find_best_score_for_second_level() throws Exception {
        scoreNodeHelper = new ScoreNodeHelper();
        rootNode = new ScoreNode();
        final ScoreNode firstChild = new ScoreNode(0, 50, Orientation.VERTICAL);
        final ScoreNode secondChild = new ScoreNode(4, 90, Orientation.VERTICAL);
        final ScoreNode secondLevelChild1 = new ScoreNode(5, 55, Orientation.VERTICAL);
        final ScoreNode secondLevelChild2 = new ScoreNode(4, 75, Orientation.VERTICAL);
        final ScoreNode secondLevelChild3 = new ScoreNode(4, 25, Orientation.VERTICAL);

        rootNode.addChild(firstChild);
        rootNode.addChild(secondChild);
        firstChild.addChild(secondLevelChild1);
        firstChild.addChild(secondLevelChild2);
        secondChild.addChild(secondLevelChild3);

        final ScoreNode nodeWithBestScore = scoreNodeHelper.getBestScoreNodeForLevel(rootNode, 2);
        final int nodeScore = nodeWithBestScore.getNodeScore();
        assertThat("Node score is 75", nodeScore, is(75));
        assertThat("Node with score 75 is the secondLevelChild2 node", nodeWithBestScore, is(secondLevelChild2));
    }
}
