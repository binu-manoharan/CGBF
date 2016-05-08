package org.binu.ai.boardscoring;

import java.util.ArrayList;
import java.util.List;

/**
 * Scoring Node.
 */
public class ScoreNode {
    int nodeIndex;
    int nodeScore;
    ScoreNode parent;

    List<ScoreNode> children;

    public ScoreNode() {
        this.nodeIndex = 0;
        this.nodeScore = 0;
        this.parent = null;
        children = new ArrayList<>();
    }

    public ScoreNode(int nodeIndex, int nodeScore) {
        this.nodeIndex = nodeIndex;
        this.nodeScore = nodeScore;
        children = new ArrayList<>();
    }

    public int getNodeIndex() {
        return nodeIndex;
    }

    public int getNodeScore() {
        return nodeScore;
    }

    public List<ScoreNode> getChildren() {
        return children;
    }

    public ScoreNode getParent() {
        return parent;
    }

    public void setParent(ScoreNode parent) {
        this.parent = parent;
    }

    public void setNodeIndex(int nodeIndex) {
        this.nodeIndex = nodeIndex;
    }

    public void setNodeScore(int nodeScore) {
        this.nodeScore = nodeScore;
    }

    public void addChild(ScoreNode childNode) {
        assert children.size() < 7;
        children.add(childNode);
    }

    public int getLevel() {
        ScoreNode node = getParent();
        int levelsToRoot = 0;
        while (node != null) {
            levelsToRoot++;
            node = node.getParent();
        }
        return levelsToRoot;
    }
}
