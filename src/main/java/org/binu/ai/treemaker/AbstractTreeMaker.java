package org.binu.ai.treemaker;

import org.binu.data.ScoreNode;
import org.jetbrains.annotations.NotNull;

/**
 * Common implementation of TreeMaker
 */
public abstract class AbstractTreeMaker implements TreeMaker {
    protected @NotNull ScoreNode createRootNodeIfNull(ScoreNode rootNode) {
        final ScoreNode calculatedRootNode;
        if (rootNode == null) {
            calculatedRootNode = new ScoreNode();
        } else {
            calculatedRootNode = rootNode;
        }
        return calculatedRootNode;
    }
}
