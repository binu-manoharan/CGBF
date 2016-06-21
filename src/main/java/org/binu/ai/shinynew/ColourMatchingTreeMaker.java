package org.binu.ai.shinynew;

import org.binu.board.Block;
import org.binu.board.BlockQueue;
import org.binu.board.Board;
import org.binu.board.Cell;
import org.binu.data.CellColour;
import org.binu.data.ScoreNode;
import org.binu.utils.TimeHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Makes move based on possible colour matches
 */
public class ColourMatchingTreeMaker extends AbstractTreeMaker {


    private List<CellColour> chainableColours;

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
        System.err.println("Number colour matching searches: " + count);
        return calculatedRootNode;
    }

    private void makeColourMatchingMoves(Board board, BlockQueue blockQueue, ScoreNode calculatedRootNode) {

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
