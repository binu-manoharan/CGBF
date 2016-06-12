package org.binu.data;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Test for {@link ScoreNode}.
 */
public class ScoreNodeTest {
    @Test
    public void should_set_parent_score_to_highest_of_the_children_score() throws Exception {
        final ScoreNode rootNode = new ScoreNode();
        final ScoreNode node1 = new ScoreNode(1, 20);
        final ScoreNode node2 = new ScoreNode();
        final ScoreNode grandChild = new ScoreNode();
        node1.setParent(rootNode);
        node2.setParent(rootNode);
        grandChild.setParent(node1);

        assertThat("Root node score is 0.", rootNode.getNodeScore(), is(0));
        assertThat("Root node level is 0", rootNode.getLevel(), is(0));
        assertThat("Parent level is 1", node1.getLevel(), is(1));
        assertThat("Grandchild node level is 2", grandChild.getLevel(), is(2));
    }

    @Test
    public void should_set_show_highest_scoring_child() throws Exception {
        final ScoreNode rootNode = new ScoreNode();
        final ScoreNode node1 = new ScoreNode(0, 20);
        final ScoreNode node2 = new ScoreNode(1, 30);
        final ScoreNode node3 = new ScoreNode(1, 30);
        node1.setParent(rootNode);
        node2.setParent(rootNode);
        node3.setParent(rootNode);
        rootNode.addChild(node1);
        rootNode.addChild(node2);
        rootNode.addChild(node3);

        assertThat("Root node score is 0.", rootNode.getChildren().size(), is(3));
    }
}
