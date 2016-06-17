package org.binu.framework;

import org.binu.data.Orientation;

/**
 * Container class to hold Orientation and node information
 * 0 -> horizontal (0 <= col index < 5)
 * 1 -> vertical reversed
 * 2 -> horizontal reversed (1 <= col index < 6)
 * 3 -> vertical
 */
public class OrientationAndIndex {
    private final Orientation orientation;
    private final int nodeIndex;

    public OrientationAndIndex(Orientation orientation, int nodeIndex) {
        this.orientation = orientation;
        this.nodeIndex = nodeIndex;
    }

    public int getNodeIndex() {
        return nodeIndex;
    }

    public Orientation getOrientation() {
        return orientation;
    }
}
