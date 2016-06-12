package org.binu.ai.boardscoring;

import org.binu.board.Block;
import org.binu.board.BlockQueue;
import org.binu.board.Board;
import org.binu.board.Cell;
import org.binu.data.CellColour;
import org.binu.data.CellStatus;
import org.binu.data.ScoreNode;
import org.binu.integration.DataParser;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Test for {@link ScoreNodeTreeParser}
 */
public class ScoreNodeTreeParserTest {
    private DataParser dataParser;
    private Board board;
    private ScoreNodeTreeFactory scoreNodeTreeFactory;
    private BlockQueue blockQueue;
    private ScoreNode rootNode;

    @Before
    public void setUp() throws Exception {
        dataParser = new DataParser();

        rootNode = new ScoreNode();
        scoreNodeTreeFactory = new ScoreNodeTreeFactory();
        blockQueue = new BlockQueue();
    }

    @Test
    public void should_compute_score_for_3_move_combo() throws Exception {
        final String[] boardString = {
                "......",
                "......",
                "......",
                "......",
                "......",
                "......",
                "1.....",
                "1.....",
                "2.....",
                "2.....",
                "3.1...",
                "3.1..."
        };

//        {
//            "......",
//            "......",
//            "......",
//            "......",
//            "......",
//            "......",
//            "1.....",
//            "1.....",
//            "2.....",
//            "2.....",
//            "3.1...",
//            "3.1..."
//        } ;

        board = dataParser.createBoard(boardString);

        blockQueue.add(getBlock(CellColour.GREEN, CellStatus.OCCUPIED, CellColour.GREEN, CellStatus.OCCUPIED));
        blockQueue.add(getBlock(CellColour.GREEN, CellStatus.OCCUPIED, CellColour.GREEN, CellStatus.OCCUPIED));
        blockQueue.add(getBlock(CellColour.YELLOW, CellStatus.OCCUPIED, CellColour.YELLOW, CellStatus.OCCUPIED));
        scoreNodeTreeFactory.populateRootNodeTree(board, blockQueue, rootNode, 3);

        final ScoreNodeTreeParser scoreNodeTreeParser = new ScoreNodeTreeParser();
        final int[] bestScoringPath = scoreNodeTreeParser.findBestScoringPath(rootNode, 3);

        assertThat("First move is", bestScoringPath[0], is(1));
        assertThat("First move is", bestScoringPath[1], is(1));
        assertThat("First move is", bestScoringPath[2], is(0));
    }

    @Test
    public void should_compute_score_normally() throws Exception {
        final String[] boardString = {
                "2.....",
                "2.....",
                "1.....",
                "1.....",
                "2.....",
                "2.....",
                "1.....",
                "1.....",
                "2.....",
                "2.....",
                "1.....",
                "1....."
        };

        board = dataParser.createBoard(boardString);

        blockQueue.add(getBlock(CellColour.PURPLE, CellStatus.OCCUPIED, CellColour.PURPLE, CellStatus.OCCUPIED));
        blockQueue.add(getBlock(CellColour.YELLOW, CellStatus.OCCUPIED, CellColour.YELLOW, CellStatus.OCCUPIED));
        blockQueue.add(getBlock(CellColour.RED, CellStatus.OCCUPIED, CellColour.RED, CellStatus.OCCUPIED));
        scoreNodeTreeFactory.populateRootNodeTree(board, blockQueue, rootNode, 3);

        final ScoreNodeTreeParser scoreNodeTreeParser = new ScoreNodeTreeParser();
        final int[] bestScoringPath = scoreNodeTreeParser.findBestScoringPath(rootNode, 3);

        assertThat("First move is", bestScoringPath[0], is(1));
        assertThat("First move is", bestScoringPath[1], is(1));
        assertThat("First move is", bestScoringPath[2], is(1));
    }

    @Test
    @Ignore
    public void should_compute_score_for_4_move_combo() throws Exception {
        final String[] boardString = {
                "......",
                "......",
                "......",
                "......",
                "......",
                "......",
                "......",
                "......",
                "......",
                "00....",
                "12....",
                "12...."
        };

        board = dataParser.createBoard(boardString);

        blockQueue.add(getBlock(CellColour.BLUE, CellStatus.OCCUPIED, CellColour.BLUE, CellStatus.OCCUPIED));
        blockQueue.add(getBlock(CellColour.GREEN, CellStatus.OCCUPIED, CellColour.GREEN, CellStatus.OCCUPIED));
        blockQueue.add(getBlock(CellColour.PURPLE, CellStatus.OCCUPIED, CellColour.PURPLE, CellStatus.OCCUPIED));
        blockQueue.add(getBlock(CellColour.PURPLE, CellStatus.OCCUPIED, CellColour.PURPLE, CellStatus.OCCUPIED));
        scoreNodeTreeFactory.populateRootNodeTree(board, blockQueue, rootNode, 4);

        final ScoreNodeTreeParser scoreNodeTreeParser = new ScoreNodeTreeParser();
        final int[] bestScoringPath = scoreNodeTreeParser.findBestScoringPath(rootNode, 4);

        assertThat("First move is", bestScoringPath[0], is(0));

//        assertChildrenScore(1, rootNode, 0, 0 , 0, 0, 0, 0);
//        assertChildrenScore(2, rootNode.getChildren().get(0), 0, 0, 35, 0, 0, 0);
//        assertChildrenScore(3, rootNode.getChildren().get(0).getChildren().get(1), 0, 0, 0, 0, 0, 0);
//        assertChildrenScore(4, rootNode.getChildren().get(0).getChildren().get(1). getChildren().get(2), 0, 0, 2875, 40, 0, 0);
    }

    private void assertChildrenScore(int childLevel, ScoreNode node, int... scores) {
        assertNode(childLevel, 0, node.getChildren().get(0).getNodeScore(), scores[0]);
        assertNode(childLevel, 1, node.getChildren().get(1).getNodeScore(), scores[1]);
        assertNode(childLevel, 2, node.getChildren().get(2).getNodeScore(), scores[2]);
        assertNode(childLevel, 3, node.getChildren().get(3).getNodeScore(), scores[3]);
        assertNode(childLevel, 4, node.getChildren().get(4).getNodeScore(), scores[4]);
        assertNode(childLevel, 5, node.getChildren().get(5).getNodeScore(), scores[5]);
    }

    private void assertNode(int childLevel, int childIndex, int node, int value) {
        assertThat("Expected Score for " + childLevel + "[" + childIndex + "]", node, is(value));
    }

    private Block getBlock(CellColour cell1Colour, CellStatus cell1Status, CellColour cell2Colour, CellStatus cell2Status) {
        final Cell[] blockCells = getCells(cell1Colour, cell1Status, cell2Colour, cell2Status);
        return new Block(blockCells);
    }

    private Cell[] getCells(CellColour cell1Colour, CellStatus cell1Status, CellColour cell2Colour, CellStatus cell2Status) {
        final Cell[] cells = new Cell[2];
        cells[0] = new Cell(cell1Colour, cell1Status);
        cells[1] = new Cell(cell2Colour, cell2Status);
        return cells;
    }
}
