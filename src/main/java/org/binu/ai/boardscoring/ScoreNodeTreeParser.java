package org.binu.ai.boardscoring;

import org.binu.data.ScoreNode;

import java.util.List;

/**
 * Parser for tree created by {@link ScoreNodeTreeFactory}
 */
public class ScoreNodeTreeParser {

    public int[] findBestScoringPath(ScoreNode rootNode, int level) {
        final List<ScoreNode> children = rootNode.getChildren();
        int totalScore = -20000;
        int[] bestMoves = new int[level];
        for (final ScoreNode childNode : children) {
            final int childNodeScore = childNode.getNodeScore();
            final List<ScoreNode> grandChildren = childNode.getChildren();
            final int numberOfGrandChildren = grandChildren.size();
            if (numberOfGrandChildren == 0) {
                final int totalScoreTillLeaf = childNode.getNodeScore();
                totalScore = getTotalScore(totalScore, bestMoves, childNode, null, null, totalScoreTillLeaf);
            }
            for (final ScoreNode greatGrandChild : grandChildren) {
                final List<ScoreNode> greatGrandChildren = greatGrandChild.getChildren();
                final int grandChildNodeScore = greatGrandChild.getNodeScore();
                final int numberOfGreatGrandChildren = greatGrandChildren.size();
                if (numberOfGreatGrandChildren == 0) {
                    final int totalScoreTillLeaf = childNode.getNodeScore() + greatGrandChild.getNodeScore();
                    totalScore = getTotalScore(totalScore, bestMoves, childNode, greatGrandChild, null, totalScoreTillLeaf);
                }
                for (final ScoreNode greatGreatGrandChild : greatGrandChildren) {
                    final List<ScoreNode> greatGreatGrandChildChildren = greatGreatGrandChild.getChildren();
                    final int greatGreatGrandChildNodeScore = greatGreatGrandChild.getNodeScore();
                    final int totalScoreTillLeafNode = childNodeScore + grandChildNodeScore + greatGreatGrandChildNodeScore;
                    totalScore = getTotalScore(totalScore, bestMoves, childNode, greatGrandChild, greatGreatGrandChild, totalScoreTillLeafNode);
                }
            }
        }
        System.err.println("Best score: " + totalScore);
        System.err.println("1: " + bestMoves[0]);
        System.err.println("2: " + bestMoves[1]);
        System.err.println("3: " + bestMoves[2]);
        return bestMoves;
    }

    private int getTotalScore(int totalScore, int[] bestMoves, ScoreNode childNode, ScoreNode greatGrandChild, ScoreNode greatGreatGrandChild, int currentNodeScore) {
        if (currentNodeScore > totalScore) {
            totalScore = currentNodeScore;
            bestMoves[0] = childNode.getNodeIndex();
            bestMoves[1] = greatGrandChild == null? 0: greatGrandChild.getNodeIndex() ;
            bestMoves[2] = greatGreatGrandChild == null? 0: greatGreatGrandChild.getNodeIndex() ;
        }
        return totalScore;
    }
}
