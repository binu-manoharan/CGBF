package org.binu.framework;

import org.binu.data.ScoreNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Finds best scores for each level.
 */
public class ScoreNodeHelper {

    public ScoreNode getBestScoreNodeForLevel(ScoreNode currentNode, int level) {

        final List<ScoreNode> scoreNodes = getScoreNodes(currentNode, level);
        int highestNodeScore = 0;

        ScoreNode highestScoringNode = null;
        for (ScoreNode scoreNode : scoreNodes) {
            final int nodeScore = scoreNode.getNodeScore();
            if (nodeScore > highestNodeScore) {
                highestScoringNode = scoreNode;
                highestNodeScore = nodeScore;
            }
        }
        return highestScoringNode;
    }

    private List<ScoreNode> getScoreNodes(ScoreNode currentNode, int level) {
        final int treeLevel = currentNode.getTreeLevel();
        final List<ScoreNode> children = currentNode.getChildren();
        final ArrayList<ScoreNode> allChildren = new ArrayList<>();
        if (treeLevel + 1 == level) {
            allChildren.addAll(children);
        } else {
            for (ScoreNode child : children) {
                allChildren.addAll(getScoreNodes(child, level));
            }
        }
        return allChildren;
    }
}
