package org.binu.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Scoring Node.
 */
public class ScoreNode {
    private int nodeIndex;
    private int nodeScore;
    private ScoreNode parent;
    private Orientation orientation;
    private List<ScoreNode> children;
    private int level;

    public ScoreNode() {
        this.nodeIndex = 0;
        this.nodeScore = 0;
        this.level = 0;
        this.parent = null;
        children = new ArrayList<>();
        orientation = Orientation.VERTICAL;
    }

    @Deprecated
    public ScoreNode(int nodeIndex, int nodeScore) {
        this.nodeIndex = nodeIndex;
        this.nodeScore = nodeScore;
        children = new ArrayList<>();
    }

    public ScoreNode(int nodeIndex, int nodeScore, Orientation orientation, int level) {
        this.nodeIndex = nodeIndex;
        this.nodeScore = nodeScore;
        children = new ArrayList<>();
        this.orientation = orientation;
        this.level = level;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
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
        childNode.setParent(this);
    }

    @Deprecated
    public int getLevel() {
        ScoreNode node = getParent();
        int levelsToRoot = 0;
        while (node != null) {
            levelsToRoot++;
            node = node.getParent();
        }
        return levelsToRoot;
    }

    public int getTreeLevel() {
        int calculatedLevel = 0;
        ScoreNode scoreNode = this;
        while (scoreNode.getParent() != null) {
            calculatedLevel++;
            scoreNode = scoreNode.getParent();
        }
        return calculatedLevel;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ScoreNode scoreNode = (ScoreNode) o;

        if (nodeIndex != scoreNode.nodeIndex) return false;
        return orientation == scoreNode.orientation;
    }

    @Override
    public int hashCode() {
        int result = nodeIndex;
        result = 31 * result + orientation.hashCode();
        return result;
    }
}
