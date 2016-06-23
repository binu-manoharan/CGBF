package org.binu.ai.treemaker;

import org.binu.board.Block;
import org.binu.board.BlockQueue;
import org.binu.board.Board;
import org.binu.board.Cell;
import org.binu.data.CellColour;
import org.binu.data.Orientation;
import org.binu.data.OrientationAndIndex;
import org.binu.data.ScoreNode;
import org.binu.framework.BoardCollapserImpl;
import org.binu.framework.CellArrayHelper;
import org.binu.framework.CellArrayHelperImpl;
import org.binu.framework.ChainClearerImpl;
import org.binu.scoring.BoardScorerImpl;
import org.binu.utils.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Makes move based on possible colour matches
 */
public class ColourMatchingTreeMaker extends AbstractTreeMaker {


    private final MatchingColourHelper matchingColourHelper;
    private List<CellColour> chainableColours;
    private CellArrayHelper cellArrayHelper;
    private BoardScorerImpl boardScorer;
    private RandomOrientationHelper randomOrientationHelper;
    private OrientationAndIndexHelper orientationAndIndexHelper;
    private final RandomValueGenerator randomValueGenerator;

    public ColourMatchingTreeMaker() {
        randomOrientationHelper = new RandomOrientationHelper();
        cellArrayHelper = new CellArrayHelperImpl();
        boardScorer = new BoardScorerImpl(new ChainClearerImpl(cellArrayHelper), new BoardCollapserImpl(cellArrayHelper));
        matchingColourHelper = new MatchingColourHelper((CellArrayHelperImpl) cellArrayHelper);
        orientationAndIndexHelper = new OrientationAndIndexHelper();
        randomValueGenerator = new RandomValueGenerator();
    }

    @Override
    public ScoreNode makeScoreTree(Board board, BlockQueue blockQueue, ScoreNode rootNode, int timeLimitInMS) {
        final HashMap<CellColour, Integer> colourOccurrenceMap;colourOccurrenceMap = new HashMap<>();

        for (Block block : blockQueue.getBlocks()) {
            final Cell[] cells = block.getCells();
            final CellColour firstCellColour = cells[0].getCellColour();
            final CellColour secondCellColour = cells[1].getCellColour();
            putCellColourInMap(colourOccurrenceMap, firstCellColour);
            putCellColourInMap(colourOccurrenceMap, secondCellColour);
        }

        chainableColours = getChainableColours(colourOccurrenceMap);

        final ScoreNode calculatedRootNode;
        calculatedRootNode = createRootNodeIfNull(rootNode);

        final TimeHelper timeHelper = new TimeHelper();
        int count = 0;
        while (timeHelper.getTimeSinceStartInMills() < timeLimitInMS) {
            makeColourMatchingMoves(new Board(board), new BlockQueue(blockQueue), calculatedRootNode);
            count++;
        }
        System.err.println("Number of colour matching searches: " + count);
        return calculatedRootNode;
    }

    private void makeColourMatchingMoves(Board board, BlockQueue blockQueue, ScoreNode scoreNode) {

        final Block block = blockQueue.getNextAndPop();

        if (block == null) {
            return;
        }

        final List<OrientationAndIndex> allOrientationAndIndexCombinations = orientationAndIndexHelper.getAllOrientationAndIndexCombinations();
        final ArrayList<OrientationAndIndex> avaliableOrientations = new ArrayList<>();
        for (OrientationAndIndex orientationAndIndexCombination : allOrientationAndIndexCombinations) {
            final boolean hasMatchingColour = matchingColourHelper.columnHasMatchingColour(board, block, orientationAndIndexCombination);
            if (hasMatchingColour) {
                avaliableOrientations.add(orientationAndIndexCombination);
            }
        }

        final int availableSize = avaliableOrientations.size();
        final int availableIndex;

        final OrientationAndIndex currentMove;
        if (availableSize > 0) {
            availableIndex = randomValueGenerator.getRandomValue(0, availableSize - 1);
            currentMove = avaliableOrientations.get(availableIndex);
        } else {
            currentMove = randomOrientationHelper.getRandomOrientationWithDropIndex();
        }

        final int nodeIndex = currentMove.getNodeIndex();
        final Orientation orientation = currentMove.getOrientation();

        if (block == null) {
            return;
        }

        final boolean droppedSuccessfully = cellArrayHelper.dropBlockIntoBoard(board, block, nodeIndex, orientation);
        if (droppedSuccessfully) {
            final int score = boardScorer.scoreBoardAndRecursivelyClearAndCollapse(board, true);
            ScoreNode currentNode = new ScoreNode(nodeIndex, score, orientation);
            final List<ScoreNode> children = scoreNode.getChildren();
            if (!children.contains(currentNode)) {
                scoreNode.addChild(currentNode);
                makeColourMatchingMoves(board, blockQueue, currentNode);
            } else {
                final int currentNodeIndex = children.indexOf(currentNode);
                currentNode = children.get(currentNodeIndex);
                makeColourMatchingMoves(board, blockQueue, currentNode);
            }
        }
    }

    public ArrayList<CellColour> getChainableColours(HashMap<CellColour, Integer> colourOccurrenceMap) {
        final ArrayList<CellColour> cellColoursWithLessThanFourOccurrence = new ArrayList<>();
        for (Map.Entry<CellColour, Integer> cellColourIntegerEntry : colourOccurrenceMap.entrySet()) {
            if (cellColourIntegerEntry.getValue() >= 4) {
                cellColoursWithLessThanFourOccurrence.add(cellColourIntegerEntry.getKey());
            }
        }
        return cellColoursWithLessThanFourOccurrence;
    }

    public void putCellColourInMap(HashMap<CellColour, Integer> colourOccurrenceMap, CellColour cellColour) {
        if (!colourOccurrenceMap.containsKey(cellColour)) {
            colourOccurrenceMap.put(cellColour, 1);
        } else {
            final Integer integer = colourOccurrenceMap.get(cellColour);
            colourOccurrenceMap.put(cellColour, integer + 1);
        }
    }
}
