package org.binu.ai.shinynew;

import org.binu.board.Block;
import org.binu.board.BlockQueue;
import org.binu.board.Board;
import org.binu.board.Cell;
import org.binu.data.CellColour;
import org.binu.data.CellStatus;
import org.binu.data.Orientation;
import org.binu.data.ScoreNode;
import org.binu.integration.DataParser;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Test for {@link ShinyNewGameAI}
 */
public class ShinyNewGameAITest {

    private Board board;
    private DataParser dataParser;
    private BlockQueue blockQueue;
    private ShinyNewGameAI shinyNewGameAI;

    @Before
    public void setUp() throws Exception {
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
                "......",
                "......",
                "......"
        };
        dataParser = new DataParser();
        board = dataParser.createBoard(boardString);
        blockQueue = new BlockQueue();
        shinyNewGameAI = new ShinyNewGameAI(board, blockQueue);
    }

    @Test
    public void should_identify_node_scores() throws Exception {
        addBlockToBlockQueue(CellColour.GREEN, CellColour.GREEN);
        addBlockToBlockQueue(CellColour.GREEN, CellColour.GREEN);
        addBlockToBlockQueue(CellColour.RED, CellColour.RED);
        addBlockToBlockQueue(CellColour.RED, CellColour.RED);
        addBlockToBlockQueue(CellColour.RED, CellColour.RED);
        addBlockToBlockQueue(CellColour.GREEN, CellColour.GREEN);
        addBlockToBlockQueue(CellColour.RED, CellColour.RED);
        addBlockToBlockQueue(CellColour.RED, CellColour.RED);

        final List<ScoreNode> scoreNodes = shinyNewGameAI.calculateNextMove(null);
        assertThat("Score node is not null", scoreNodes, is(notNullValue()));
        assertThat("Score nodes two nodes - root & highest", scoreNodes.size(), is(2));
    }

    @Test
    public void should_return_third_node_as_the_highest_scoring_node() throws Exception {
        final ScoreNode fourthLevelNode = getScoreNode(null, 4, 150);
        final ScoreNode thirdLevelNode = getScoreNode(fourthLevelNode, 3, 250);
        final ScoreNode secondLevelNode = getScoreNode(thirdLevelNode, 2, 10);
        final ScoreNode firstLevelNode = getScoreNode(secondLevelNode, 0, 200);
        ScoreNode rootNode = getScoreNode(firstLevelNode, 0, 0);

        final ScoreNode highestScoreNode = shinyNewGameAI.getHighestScoreNode(rootNode);
        assertThat("Third level node is the highest scoring node", highestScoreNode, is(thirdLevelNode));

        ScoreNode nextMoveForHighestScoringNode = shinyNewGameAI.getNextMoveForHighestScoringNode(highestScoreNode);
        assertThat("Next move is the first level node", nextMoveForHighestScoringNode, is(firstLevelNode));
    }

    @Test
    public void should_return_the_first_node_as_highest_scoring_node() throws Exception {
        final ScoreNode fourthLevelNode = getScoreNode(null, 4, 150);
        final ScoreNode thirdLevelNode = getScoreNode(fourthLevelNode, 3, 250);
        final ScoreNode secondLevelNode = getScoreNode(thirdLevelNode, 2, 10);
        final ScoreNode firstLevelNode = getScoreNode(secondLevelNode, 0, 300);
        ScoreNode rootNode = getScoreNode(firstLevelNode, 0, 0);

        final ScoreNode highestScoreNode = shinyNewGameAI.getHighestScoreNode(rootNode);
        assertThat("Third level node is the highest scoring node", highestScoreNode, is(firstLevelNode));

        ScoreNode nextMoveForHighestScoringNode = shinyNewGameAI.getNextMoveForHighestScoringNode(highestScoreNode);
        assertThat("Next move is the first level node", nextMoveForHighestScoringNode, is(firstLevelNode));
    }

    @NotNull
    private ScoreNode getScoreNode(ScoreNode childNode, int currentNodeIndex, int currentNodeScore) {
        final ScoreNode scoreNode = new ScoreNode(currentNodeIndex, currentNodeScore, Orientation.VERTICAL);
        if (childNode != null) {
            scoreNode.addChild(childNode);
        }
        return scoreNode;
    }

    private void addBlockToBlockQueue(CellColour cell1Colour, CellColour cell2Colour) {
        final Cell[] cells = new Cell[2];
        cells[0] = new Cell(cell1Colour, CellStatus.OCCUPIED);
        cells[1] = new Cell(cell2Colour, CellStatus.OCCUPIED);
        final Block block = new Block(cells);
        blockQueue.add(block);
    }
}
