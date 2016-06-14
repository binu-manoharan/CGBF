import java.util.*;
import java.io.*;
import java.math.*;

class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);

        // game loop
        int mylastscore = 0;
        int opponentLastScore = 0;
        Board oldBoard = new Board();
        ScoreNode rootNode = null;
        while (true) {

            long startTime = System.currentTimeMillis();

            DataParser dataParser = new DataParser();
            BlockQueue blockQueue = new BlockQueue();
            for (int i = 0; i < 8; i++) {
                int colorA = in.nextInt(); // color of the first block
                int colorB = in.nextInt(); // color of the attached block
                Block colourBlock = dataParser.createColourBlock(colorA, colorB);
                blockQueue.add(colourBlock);
            }
            int score1 = in.nextInt();
            Board myBoard = new Board();
            for (int i = 0; i < 12; i++) {
                String row = in.next();
                Cell[] boardRow = dataParser.createBoardRow(row);
                myBoard.setRow(Board.ROW_LENGTH - i - 1, boardRow);
                System.err.println(row);
            }

            int score2 = in.nextInt();

            for (int i = 0; i < 12; i++) {
                String row = in.next(); // One line of the map ('.' = empty, '0' = skull block, '1' to '5' = colored block)
            }

            boolean invalidRootNode = false;
            if (opponentLastScore % 420 != score2 % 420) {
                invalidRootNode = true;
            }
            opponentLastScore = score2;

            if (invalidRootNode) {
                rootNode = null;
                System.err.println("Board has dropped skulls!");
            }
            ShinyNewGameAI gameAI = new ShinyNewGameAI(myBoard, blockQueue);
            ScoreNode nextMove = gameAI.calculateNextMove(rootNode);
            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");

            final int nodeIndex = nextMove.getNodeIndex();
            System.err.println("Current play: " + nodeIndex);

            new CellArrayHelperImpl().dropBlockIntoBoard(oldBoard, blockQueue.getNext(), nodeIndex);
            System.out.println(nodeIndex); // "x": the column in which to drop your blocks
            rootNode = nextMove;
            rootNode.setParent(null);
            long endTime = System.currentTimeMillis();
            System.err.print("Exec time: " + ((endTime - startTime)));

        }
    }
}


/**
 * Scoring Node.
 */
class ScoreNode {
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

/**
 * Enum indicating a cell status
 */
enum CellStatus {
    EMPTY,
    BLOCKED,
    OCCUPIED
}

/**
 * Enum for Rotation of the block
 * <ul>
 * <li>HORIZONTAL 1 -> 4</li>
 * <li>VERTCIAL 1 below 4</li>
 * <li>HORIZONTAL_REVERSED 4 -> 1</li>
 * <li>VERTICAL_REVERSED 4 below 1</li>
 * </ul>
 */
enum Orientation {
    HORIZONTAL,
    VERTICAL,
    HORIZONTAL_REVERSED,
    VERTICAL_REVERSED
}

/**
 * Cell Colours for occupied rows.
 */
enum CellColour {
    BLUE,
    GREEN,
    PURPLE,
    RED,
    YELLOW
}


/**
 * Common AI features.
 */
abstract class GameAI implements IGameAI {
    protected Board board;
    protected BlockQueue blockQueue;


    public GameAI(Board board, BlockQueue blockQueue) {
        this.board = board;
        this.blockQueue = blockQueue;


    }
}


/**
 * Basic board scoring.
 */
class BoardScoreCalculatorImpl implements BoardScoreCalculator {
    private final Board board;
    private final BlockQueue blockQueue;
    private final CellArrayHelper cellArrayHelper;
    private final ChainClearerImpl boardClearer;
    private final BoardCollapserImpl boardCollapser;

    public BoardScoreCalculatorImpl(Board board, BlockQueue blockQueue) {
        this.board = board;
        this.blockQueue = blockQueue;
        cellArrayHelper = new CellArrayHelperImpl();
        boardCollapser = new BoardCollapserImpl(cellArrayHelper);
        boardClearer = new ChainClearerImpl(cellArrayHelper);
    }

    @Override
    public int calculateColumnScore(int column) {
        final int score = getScore(column);
        return score;
    }

    private int getScore(int column) {
        final Board tempBoardBeforeClear;

        try {
            tempBoardBeforeClear = getBoardWithDroppedQueue(column);
        } catch (ArrayIndexOutOfBoundsException e) {
            //never take this path
            return -10000;
        }

        final Board tempBoardAfterClear = new Board(tempBoardBeforeClear);
        boardClearer.clearBoard(tempBoardAfterClear);

        final int score = getScore(tempBoardBeforeClear, tempBoardAfterClear);

        boardCollapser.collapseBoard(tempBoardAfterClear);
        final Board tempBoardAfterCollapse = new Board(tempBoardAfterClear);
        boardClearer.clearBoard(tempBoardAfterCollapse);

        final int comboScore = getScore(tempBoardAfterClear, tempBoardAfterCollapse) * 8;
        final int totalScore = comboScore + score;
        return totalScore;
    }

    private int getScore(Board tempBoardBefore, Board tempBoardAfter) {
        int score = 0;
        for (int i = 0; i < Board.ROW_LENGTH; i++) {
            for (int j = 0; j < Board.COLUMN_LENGTH; j++) {
                final CellStatus beforeCellStatus = tempBoardBefore.getCell(i, j).getCellStatus();
                if (tempBoardAfter.getCell(i, j).getCellStatus() != beforeCellStatus) {
                    if (beforeCellStatus != CellStatus.BLOCKED) {
                        score += 10;
                    } else {
                        score -= 5;
                    }
                }
            }
        }
        return score;
    }

    private Board getBoardWithDroppedQueue(int column) throws ArrayIndexOutOfBoundsException{
        final Board tempBoardBefore = new Board(board);
        final Cell[] columnToDropInto = tempBoardBefore.getColumn(column);

        final Cell[] cells = cellArrayHelper.dropBlockIntoColumn(columnToDropInto, blockQueue.getNext());
        tempBoardBefore.setColumn(column, cells);
        return tempBoardBefore;
    }
}


/**
 * GameAI capable of scoring the board.
 */
class BoardScoringGameAI extends GameAI {
    public BoardScoringGameAI(Board board, BlockQueue blockQueue) {
        super(board, blockQueue);
    }

    @Override
    public int calculateNextMove() {
        final BoardScoringMoveAnalyser moveAnalyser = new BoardScoringMoveAnalyser(board, blockQueue);
        return moveAnalyser.findBestMove();
    }
}


/**
 * Create a Score Node Tree
 */
class ScoreNodeTreeFactory {

    private final ChainClearerImpl boardClearer;
    private final CellArrayHelper cellArrayHelper;

    public ScoreNodeTreeFactory() {
        cellArrayHelper = new CellArrayHelperImpl();
        boardClearer = new ChainClearerImpl(cellArrayHelper);
    }

    public void populateRootNodeTree(Board board, BlockQueue blockQueue, ScoreNode rootNode, int depth) {
        addToScoreNode(rootNode, board, blockQueue, depth);
    }

    private void addToScoreNode(ScoreNode scoreNode, Board board, BlockQueue blockQueue, int depth) {

        final int level = scoreNode.getLevel();
        if (level >= depth) {
            return;
        }

        if (cellArrayHelper.isClearable(board)) {
            return;
        }

        for (int col = 0; col < Board.COLUMN_LENGTH; col++) {
            try {
                final Board tempBoard = getBoardWithDroppedColumn(board, blockQueue, col);
                final int score = getScore(tempBoard);

                final ScoreNode childNode = new ScoreNode(col, score);
                childNode.setParent(scoreNode);
                scoreNode.addChild(childNode);

                final BlockQueue tempBlockQueue = new BlockQueue(blockQueue);
                tempBlockQueue.getNextAndPop();

                addToScoreNode(childNode, tempBoard, tempBlockQueue, depth);
            } catch (ArrayIndexOutOfBoundsException e) {
                final ScoreNode childNode = new ScoreNode(col, -10000);
                childNode.setParent(scoreNode);
                scoreNode.addChild(childNode);
            }
        }
    }

    
    private Board getBoardWithDroppedColumn(Board board, BlockQueue blockQueue, int col) {
        final Cell[] column = board.getColumn(col);
        final Cell[] droppedColumn = cellArrayHelper.dropBlockIntoColumn(column, blockQueue.getNext());
        final Board tempBoard = new Board(board);
        tempBoard.setColumn(col, droppedColumn);
        return tempBoard;
    }


    private int getScore(Board board) {
        return scoreBoard(board, 0, 1);
    }

    private int scoreBoard(Board tempBoardBeforeClear, int initialScore, int multiplier) {
        final Board tempBoardAfterClear = getClearedBoardCopy(tempBoardBeforeClear);

        if (!cellArrayHelper.isClearable(tempBoardBeforeClear)) {
            return initialScore;
        }

        final int boardDifferentialScore = initialScore + getBoardDifferentialScore(tempBoardBeforeClear, tempBoardAfterClear) * multiplier;
        cellArrayHelper.collapseEmptyCells(tempBoardAfterClear);
        multiplier *= 8;
        return scoreBoard(tempBoardAfterClear, boardDifferentialScore, multiplier) ;
    }

    private Board getClearedBoardCopy(Board tempBoardBeforeClear) {
        final Board tempBoardAfterClear = new Board(tempBoardBeforeClear);
        boardClearer.clearBoard(tempBoardAfterClear);
        return tempBoardAfterClear;
    }

    private int getBoardDifferentialScore(Board tempBoardBefore, Board tempBoardAfter) {
        int score = 0;
        for (int i = 0; i < Board.ROW_LENGTH; i++) {
            for (int j = 0; j < Board.COLUMN_LENGTH; j++) {
                final CellStatus beforeCellStatus = tempBoardBefore.getCell(i, j).getCellStatus();
                if (tempBoardAfter.getCell(i, j).getCellStatus() != beforeCellStatus) {
                    if (beforeCellStatus != CellStatus.BLOCKED) {
                        score += 10;
                    } else {
                        score -= 5;
                    }
                }
            }
        }
        return score;
    }
}

/**
 * Score calculator for possible moves based on current board position.
 */
interface BoardScoreCalculator {

    int calculateColumnScore(int column);
}



/**
 * Move analyser
 */
class BoardScoringMoveAnalyser extends AbstractMoveAnalyser {
    private Board board;
    private BlockQueue blockQueue;
    private ScoreNodeTreeFactory scoreNodeTreeFactory;
    private ScoreNode rootNode;
    private ScoreNodeTreeParser scoreNodeTreeParser;

    private int level;

    public BoardScoringMoveAnalyser(Board board, BlockQueue blockQueue) {
        this.board = board;
        this.blockQueue = blockQueue;
        rootNode = new ScoreNode();
        level = 4;
        scoreNodeTreeParser = new ScoreNodeTreeParser();
        scoreNodeTreeFactory = new ScoreNodeTreeFactory();
    }

    @Override
    public int findBestMove() {
        scoreNodeTreeFactory.populateRootNodeTree(board, blockQueue, rootNode, level);
        final int[] bestScoringPath = scoreNodeTreeParser.findBestScoringPath(rootNode, level);
        System.err.println("1: " + bestScoringPath[0]);
        System.err.println("2: " + bestScoringPath[1]);
        System.err.println("3: " + bestScoringPath[2]);
        return bestScoringPath[0];
    }

    
    private ScoreNode getScoreNode(BoardScoreCalculatorImpl boardScoreCalculator, int columnIndex, ScoreNode parent) {
        final ScoreNode child = new ScoreNode();
        child.setNodeIndex(columnIndex);
        child.setNodeScore(boardScoreCalculator.calculateColumnScore(columnIndex));
        child.setParent(parent);
        return child;
    }

    private Integer getWinningIndex(ArrayList<Integer> highestColumnIndex) {
        final double randomMultiplier = Math.random();
        final double randomVal = randomMultiplier * highestColumnIndex.size();
        return highestColumnIndex.get((int) randomVal);
    }

    
    private ArrayList<Integer> getHighestScoringIndexes(int[][] columnScores, int highestColumnScore) {
        final ArrayList<Integer> highestColumnIndex = new ArrayList<>();
        for (int col = 0; col < Board.COLUMN_LENGTH; col++) {
            if (columnScores[col][1] == highestColumnScore) {
                highestColumnIndex.add(columnScores[col][0]);
            }
        }
        return highestColumnIndex;
    }

    private int getHighestColumnScore(BoardScoreCalculatorImpl boardScoreCalculator, int[][] columnScores) {
        int highestColumnScore = 0;
        for (int col = 0; col < Board.COLUMN_LENGTH; col++) {
            final int columnScore = boardScoreCalculator.calculateColumnScore(col);
            columnScores[col][0] = col;
            columnScores[col][1] = columnScore;
            if (columnScore > highestColumnScore) {
                highestColumnScore = columnScore;
            }
        }
        return highestColumnScore;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}



/**
 * Parser for tree created by {@link ScoreNodeTreeFactory}
 */
class ScoreNodeTreeParser {

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


/**
 * Shiny!!! move analysis.
 */
class ShinyNewMoveAnalyser {
    public ScoreNode makeScoreTree(Board board, BlockQueue blockQueue, ScoreNode rootNode) {
        final ShinyNewRandomMoveMaker shinyNewRandomMoveMaker = new ShinyNewRandomMoveMaker();
        final ScoreNode calculatedRootNode;
        if (rootNode == null) {
            calculatedRootNode = new ScoreNode();
        } else {
            calculatedRootNode = rootNode;
        }
        final long startTime = System.currentTimeMillis();
        long currentTime = System.currentTimeMillis();
        int count = 0;
        while (currentTime - startTime < 50) {
            shinyNewRandomMoveMaker.makeRandomMove(new Board(board), new BlockQueue(blockQueue), calculatedRootNode, 0);
            currentTime = System.currentTimeMillis();
            count++;
        }
        System.err.println("Number of searches: " + count);

        return calculatedRootNode;
    }
}



/**
 * Make random moves
 */
class ShinyNewRandomMoveMaker {
    private static final int MIN_VALUE = 0;
    private static final int MAX_VALUE = 5;
    private RandomValueGenerator randomValueGenerator;
    private CellArrayHelper cellArrayHelper;
    private BoardScorerImpl boardScorer;

    public ShinyNewRandomMoveMaker() {
        randomValueGenerator = new RandomValueGenerator();
        cellArrayHelper = new CellArrayHelperImpl();
        boardScorer = new BoardScorerImpl(new ChainClearerImpl(cellArrayHelper), new BoardCollapserImpl(cellArrayHelper));
    }

    public void makeRandomMove(Board board, BlockQueue blockQueue, ScoreNode scoreNode, int level) {

        final int randomValue = randomValueGenerator.getRandomValue(MIN_VALUE, MAX_VALUE);
        final Block block = blockQueue.getNextAndPop();

        if (block == null) {
            return;
        }

        final boolean droppedSuccessfully = cellArrayHelper.dropBlockIntoBoard(board, block, randomValue);
        if (droppedSuccessfully) {
            final int score = boardScorer.scoreBoardAndRecursivelyClearAndCollapse(board, true);
            ScoreNode currentNode = new ScoreNode(randomValue, score, Orientation.VERTICAL, level);
            final List<ScoreNode> children = scoreNode.getChildren();
            if (!children.contains(currentNode)) {
                scoreNode.addChild(currentNode);
                makeRandomMove(board, blockQueue, currentNode, ++level);
            } else {
                final int currentNodeIndex = children.indexOf(currentNode);
                currentNode = children.get(currentNodeIndex);
                makeRandomMove(board, blockQueue, currentNode, ++level);
            }
        }
    }
}



/**
 * Shiny!!!
 */
class ShinyNewGameAI {

    private ScoreNodeHelper scoreNodeHelper;
    private Board board;
    private BlockQueue blockQueue;

    public ShinyNewGameAI(Board board, BlockQueue blockQueue) {
        this.board = board;
        this.blockQueue = blockQueue;
        scoreNodeHelper = new ScoreNodeHelper();
    }

    public ScoreNode calculateNextMove(ScoreNode rootNode) {
        final ShinyNewMoveAnalyser shinyNewMoveAnalyser = new ShinyNewMoveAnalyser();
        final ScoreNode rootNode1 = shinyNewMoveAnalyser.makeScoreTree(board, blockQueue, rootNode);

        ScoreNode highestScoreNode = getHighestScoreNode(rootNode1);
        highestScoreNode = getNextMoveForHighestScoringNode(highestScoreNode);
        return highestScoreNode;
    }

    public ScoreNode getNextMoveForHighestScoringNode(ScoreNode highestScoreNode) {
        while (highestScoreNode.getTreeLevel() > 1) {
            System.err.print("Path: " + highestScoreNode.getNodeIndex() + " <- ");
            highestScoreNode = highestScoreNode.getParent();
        }
        System.err.println("Path: " + highestScoreNode.getNodeIndex());
        return highestScoreNode;
    }

    public ScoreNode getHighestScoreNode(ScoreNode rootNode) {
        final ArrayList<ScoreNode> scoreNodes = new ArrayList<>();
        int highestScore = 0;
        int highestIndex = 0;
        for (int i = 1; i < 9; i++) {
            final ScoreNode bestScoreNodeForLevel = scoreNodeHelper.getBestScoreNodeForLevel(rootNode, i);


            if (bestScoreNodeForLevel != null) {
                final int nodeScore = bestScoreNodeForLevel.getNodeScore();
                if (nodeScore > highestScore) {
                    highestScore = nodeScore;
                    highestIndex = scoreNodes.size();
                }
                scoreNodes.add(bestScoreNodeForLevel);

                System.err.println(i + "  " + nodeScore);
            }
        }

        ScoreNode scoreNode = scoreNodes.get(highestIndex);
        System.err.println("Highest index: " + highestIndex);
        return scoreNode;
    }
}

/**
 * Game AI class.
 */
interface IGameAI {

    /**
     * Calculate the next move to make
     * @return index of the column to play in
     */
    int calculateNextMove();
}

/**
 * Helper to get random values
 */
class RandomValueGenerator {

    private static final int OFFSET_VALUE_TO_ROUND_UP = 1;

    public int getRandomValue(int minValue, int maxValue) {
        final double random = Math.random();
        final double randomValue = random * (maxValue - minValue + OFFSET_VALUE_TO_ROUND_UP);
        return (int) (minValue + randomValue);
    }
}


/**
 * Simple board collapser implementation.
 */
class BoardCollapserImpl implements BoardCollapser {

    private CellArrayHelper cellArrayHelper;

    public BoardCollapserImpl(CellArrayHelper cellArrayHelper) {
        this.cellArrayHelper = cellArrayHelper;
    }

    /** {@inheritDoc} */
    @Override
    public Board collapseBoard(Board board) {
        cellArrayHelper.collapseEmptyCells(board);
        return board;
    }
}



/**
 * Logic for parsing a cell array.
 */
interface CellArrayHelper {
    /**
     * Returns index of the first non empty position in the cell array where new block can sit
     *
     * @param cells cell array to check empty position for
     * @return index position
     */
    int getFirstEmptyPosition(Cell[] cells);

    /**
     * Collapses any empty cells that exists between other types of cells
     *
     * @param cells cell array to parse
     * @return collapsed cell array
     */
    Cell[] collapseEmptyCells(Cell[] cells);

    /**
     * Collapse empty cells on a board
     *
     * @param board 2d cell array to collapse empty cells on
     */
    void collapseEmptyCells(Board board);

    /**
     * Drop block into cell[]
     *
     * @param cells cells to drop into
     * @param block blocks to drop into the cell
     * @return dropped cell array
     */
    Cell[] dropBlockIntoColumn(Cell[] cells, Block block);

    /**
     * Is there space to fit in the block
     *
     * @param board       board against which is droppable is checked
     * @param block       block that needs to be placed on the board
     * @param columnIndex index of the column to drop on  @return true if there is space to drop
     */
    boolean blockIsDroppableOnColumn(Board board, Block block, int columnIndex);

    /**
     * Drop the block into the board at a certain index
     *
     * @param board       board on which the block is dropped
     * @param block       block to be dropped
     * @param columnIndex column index where the block is dropped
     * @return true if the column was dropped successful, false when there is no space to drop it
     */
    boolean dropBlockIntoBoard(Board board, Block block, int columnIndex);

    /**
     * Returns int [] containing two values which is the x and y within the 2d array for an element with the first
     * repeated group block
     *
     * @param cellArray 2d array
     * @return x and y containing the repeated groups
     */
    List<int[]> getIndexOf4BlockGroup(Cell[][] cellArray);

    /**
     * Get cell indexes of L and T formations
     *
     * @param cellArray cellArray to parse
     * @return list of x and y co-ordinates of at least a cell within the shape
     */
    List<int[]> getIndexOfLAndT(Cell[][] cellArray);

    /**
     * Get cell indexes of Z formations
     *
     * @param cellArray cellArray to parse
     * @return list of x and y co-ordinates of at least a cell within the shape
     */
    List<int[]> getIndexOfZ(Cell[][] cellArray);

    /**
     * Get cell indexes of line formations
     *
     * @param cellArray cellArray to parse
     * @return list of x and y co-ordinates of at least a cell within the shape
     */
    List<int[]> getIndexOfLines(Cell[][] cellArray);

    /**
     * Get cell indexes of all formations
     *
     * @param cellArray cellArray to parse
     * @return list of x and y co-ordinates of at least a cell within the shape
     */
    List<int[]> getIndexesOfAllShapes(Cell[][] cellArray);

    /**
     * does the board contain any desirable shape
     *
     * @param board board to check the status for
     * @return true if the board contains any of the shapes
     */
    boolean isClearable(Board board);
}


/**
 * Clears board pieces that make 4 and ones surrounding it.
 */
interface ChainClearer {
    /**
     * Clear the board based on the 4 together rule.
     *
     * @param board board to parse
     */
    void clearBoard(Board board);

    /**
     * Empty out cells of the board checking for 4 match in column vertically
     *
     * @param board board to parse
     */
    void clearBoardByLine(Board board);

    /**
     * Empty out cells of a board checking for 4 square match
     *
     * @param board board to parse
     */
    void clearBoardBySquare(Board board);

    /**
     * Empty out cells making a 4 with a T or L shape
     *
     * @param board board to parse
     */
    void clearBoardByTAndL(Board board);

    /**
     * Empty out cells making a 4 with a Z shape
     *
     * @param board board to parse
     */
    void clearBoardByZ(Board board);

    /**
     * Board has at least one step to clear
     *
     * @param board board to check against
     * @return true if board has at least one item to clear
     */
    boolean isClearable(Board board);
}


/**
 * Collapses a board where there is a block of 4 colours together.
 * This formation can be either vertical or horizontal or a square of 4.
 */
interface BoardCollapser {
    //TODO add isCollapsable
    /**
     * Collapse the board based on the 4 together rule.
     * @param board board to parse
     * @return parsedBoard
     */
    Board collapseBoard(Board board);
}



/**
 * Finds best scores for each level.
 */
class ScoreNodeHelper {

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
        if (treeLevel == level - 1) {
            allChildren.addAll(children);
        } else {
            for (ScoreNode child : children) {
                allChildren.addAll(getScoreNodes(child, level));
            }
        }
        return allChildren;
    }
}

/**
 * Abstract move analyser.
 */
abstract class AbstractMoveAnalyser implements MoveAnalyser {
}

/**
 * Calculate the better of the move.
 */
interface MoveAnalyser {
    int findBestMove();
}



/**
 * Implementation of CellArrayHelper
 */
class CellArrayHelperImpl implements CellArrayHelper {
    @Override
    public int getFirstEmptyPosition(Cell[] cells) {
        final int length = cells.length;
        for (int i = length - 1; i >= 0; i--) {
            final CellStatus cellStatus = cells[i].getCellStatus();
            if (cellStatus != CellStatus.EMPTY) {
                return i + 1;
            }
        }
        return 0;
    }

    @Override
    public List<int[]> getIndexOf4BlockGroup(Cell[][] cellArray) {
        final int rowLength = cellArray.length;
        final int colLength = cellArray[0].length;

        final List<int[]> resultList = new ArrayList<>();

        matchingPointsWith4Block(cellArray, rowLength, colLength, resultList);
        return resultList;
    }

    private void matchingPointsWith4Block(Cell[][] cellArray, int rowLength, int colLength, List<int[]> resultList) {
        for (int row = 0; row < rowLength - 1; row++) {
            for (int col = 0; col < colLength - 1; col++) {
                final CellColour cellBotLeft = cellArray[row][col].getCellColour();
                final CellColour cellTopLeft = cellArray[row + 1][col].getCellColour();
                final CellColour cellTopRight = cellArray[row + 1][col + 1].getCellColour();
                final CellColour cellBotRight = cellArray[row][col + 1].getCellColour();

                if (cellBotLeft != null && cellBotLeft == cellBotRight && cellTopLeft == cellTopRight && cellBotLeft == cellTopRight) {
                    addMatchingResult(resultList, row, col);
                    col++;
                }
            }
        }
    }

    @Override
    public Cell[] collapseEmptyCells(Cell[] cells) {
        final int firstEmptyPosition = getFirstEmptyPosition(cells);
        final int cellLength = cells.length;
        final Cell[] collapsedCells = new Cell[cellLength];
        int collapsedCellIndex = 0;
        for (int cellIndex = 0; cellIndex < cellLength; cellIndex++) {
            if (cellIndex < firstEmptyPosition) {
                if (cells[cellIndex].getCellStatus() != CellStatus.EMPTY) {
                    collapsedCells[collapsedCellIndex++] = cells[cellIndex];
                }
            }
        }

        for (int cellIndex = collapsedCellIndex; cellIndex < cellLength; cellIndex++) {
            collapsedCells[cellIndex] = new Cell(null, CellStatus.EMPTY);
        }
        return collapsedCells;
    }

    @Override
    public void collapseEmptyCells(Board board) {
        for (int col = 0; col < Board.COLUMN_LENGTH; col++) {
            final Cell[] cells = collapseEmptyCells(board.getColumn(col));
            board.setColumn(col, cells);
        }
    }

    @Override
    public Cell[] dropBlockIntoColumn(Cell[] cells, Block block) {
        final int firstEmptyPosition = getFirstEmptyPosition(cells);
        if (firstEmptyPosition + block.getCells().length > cells.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        final int length = cells.length;
        final Cell[] droppedCells = new Cell[length];

        System.arraycopy(cells, 0, droppedCells, 0, cells.length);

        if (firstEmptyPosition != length) {
            final Cell[] blockCells = block.getCells();
            droppedCells[firstEmptyPosition] = blockCells[0];
            droppedCells[firstEmptyPosition + 1] = blockCells[1];
        } else {
            assert true : "Cell is too full!";
        }

        return droppedCells;
    }

    @Override
    public boolean blockIsDroppableOnColumn(Board board, Block block, int columnIndex) {
        final Cell[] column = board.getColumn(columnIndex);
        final int firstEmptyPosition = getFirstEmptyPosition(column);
        return firstEmptyPosition + block.getCells().length <= column.length;
    }

    @Override
    public boolean dropBlockIntoBoard(Board board, Block block, int columnIndex) {
        //TODO see if block is droppable on column can be merged here to not call firstEmptyPosition twice
        final boolean isDroppable = blockIsDroppableOnColumn(board, block, columnIndex);
        if (isDroppable) {
            final Cell[] column = board.getColumn(columnIndex);
            final int firstEmptyPosition = getFirstEmptyPosition(column);
            int offset = 0;
            for (Cell cell: block.getCells()) {
                board.setCell(firstEmptyPosition + offset, columnIndex, cell.getCellStatus(), cell.getCellColour());
                offset++;
            }
        }
        return isDroppable;
    }

    @Override
    public List<int[]> getIndexOfLAndT(Cell[][] cellArray) {
        final int rowLength = cellArray.length;
        final int colLength = cellArray[0].length;

        final List<int[]> resultList = new ArrayList<>();

        matchingPointsWithLOrTToTheTop(cellArray, rowLength, colLength, resultList);
        matchingPointsWithLOrTToTheBottom(cellArray, rowLength, colLength, resultList);
        matchingPointsWithLOrTToTheRight(cellArray, rowLength, colLength, resultList);
        matchingPointsWithLOrTToTheLeft(cellArray, rowLength, colLength, resultList);
        return resultList;
    }

    @Override
    public List<int[]> getIndexOfZ(Cell[][] cellArray) {
        final int rowLength = cellArray.length;
        final int colLength = cellArray[0].length;

        final List<int[]> resultList = new ArrayList<>();

        matchingPointsWithZToTheRight(cellArray, rowLength, colLength, resultList);
        matchingPointsWithZToTheLeft(cellArray, rowLength, colLength, resultList);
        return resultList;
    }

    @Override
    public List<int[]> getIndexOfLines(Cell[][] cellArray) {
        final int rowLength = cellArray.length;
        final int colLength = cellArray[0].length;

        final List<int[]> resultList = new ArrayList<>();

        matchingPointsWithLinesToTheTop(cellArray, rowLength, colLength, resultList);
        matchingPointsWithLinesToTheRight(cellArray, rowLength, colLength, resultList);
        return resultList;
    }

    @Override
    public List<int[]> getIndexesOfAllShapes(Cell[][] cellArray) {
        final ArrayList<int[]> cellList = new ArrayList<>();
        cellList.addAll(getIndexOfLines(cellArray));
        cellList.addAll(getIndexOf4BlockGroup(cellArray));
        cellList.addAll(getIndexOfLAndT(cellArray));
        cellList.addAll(getIndexOfZ(cellArray));

        return cellList;
    }

    @Override
    public boolean isClearable(Board board) {
        final Cell[][] cellArray = board.getBoard();
        if (!getIndexOfLines(cellArray).isEmpty()) return true;
        if (!getIndexOf4BlockGroup(cellArray).isEmpty()) return true;
        if (!getIndexOfLAndT(cellArray).isEmpty()) return true;
        if (!getIndexOfZ(cellArray).isEmpty()) return true;
        return false;
    }

    private void matchingPointsWithLinesToTheRight(Cell[][] cellArray, int rowLength, int colLength, List<int[]> resultList) {
        for (int row = 0; row < rowLength; row++) {
            for (int col = 0; col < colLength - 3; col++) {
                final CellColour cellLeft = cellArray[row][col].getCellColour();
                final CellColour cellMidLeft = cellArray[row][col + 1].getCellColour();
                final CellColour cellMidRight = cellArray[row][col + 2].getCellColour();
                final CellColour cellTop = cellArray[row][col + 3].getCellColour();

                final boolean makesLine = cellLeft != null && cellLeft == cellMidLeft && cellMidRight == cellMidLeft && cellTop == cellMidRight;

                if (makesLine) {
                    addMatchingResult(resultList, row, col);
                }
            }
        }
    }

    private void matchingPointsWithLinesToTheTop(Cell[][] cellArray, int rowLength, int colLength, List<int[]> resultList) {
        for (int row = 0; row < rowLength - 3; row++) {
            for (int col = 0; col < colLength; col++) {
                final CellColour cellBot = cellArray[row][col].getCellColour();
                final CellColour cellMidBot = cellArray[row + 1][col].getCellColour();
                final CellColour cellMidTop = cellArray[row + 2][col].getCellColour();
                final CellColour cellTop = cellArray[row + 3][col].getCellColour();

                final boolean makesLine = cellBot != null && cellBot == cellMidBot && cellMidTop == cellMidBot && cellTop == cellMidTop;

                if (makesLine) {
                    addMatchingResult(resultList, row, col);
                }
            }
        }
    }

    private void matchingPointsWithZToTheLeft(Cell[][] cellArray, int rowLength, int colLength, List<int[]> resultList) {
        for (int row = 0; row < rowLength - 1; row++) {
            for (int col = 1; col < colLength - 1; col++) {
                final CellColour cellLeftBot = cellArray[row][col - 1].getCellColour();
                final CellColour cellLeftMid = cellArray[row][col].getCellColour();
                final CellColour cellLeftTop = cellArray[row][col + 1].getCellColour();
                final CellColour cellRightBot = cellArray[row + 1][col - 1].getCellColour();
                final CellColour cellRightMid = cellArray[row + 1][col].getCellColour();
                final CellColour cellRightTop = cellArray[row + 1][col + 1].getCellColour();

                final boolean midCellsAreSame = cellLeftMid != null && cellLeftMid == cellRightMid;
                final boolean makesZ = (cellLeftMid == cellLeftTop && cellLeftMid == cellRightBot) || (cellLeftMid == cellLeftBot && cellLeftMid == cellRightTop);

                if (midCellsAreSame && makesZ) {
                    addMatchingResult(resultList, row, col);
                }
            }
        }
    }

    private void matchingPointsWithZToTheRight(Cell[][] cellArray, int rowLength, int colLength, List<int[]> resultList) {
        for (int row = 1; row < rowLength - 1; row++) {
            for (int col = 0; col < colLength - 1; col++) {
                final CellColour cellLeftBot = cellArray[row - 1][col].getCellColour();
                final CellColour cellLeftMid = cellArray[row][col].getCellColour();
                final CellColour cellLeftTop = cellArray[row + 1][col].getCellColour();
                final CellColour cellRightBot = cellArray[row - 1][col + 1].getCellColour();
                final CellColour cellRightMid = cellArray[row][col + 1].getCellColour();
                final CellColour cellRightTop = cellArray[row + 1][col + 1].getCellColour();

                final boolean midCellsAreSame = cellLeftMid != null && cellLeftMid == cellRightMid;
                final boolean makesZ = (cellLeftMid == cellLeftTop && cellLeftMid == cellRightBot) || (cellLeftMid == cellLeftBot && cellLeftMid == cellRightTop);

                if (midCellsAreSame && makesZ) {
                    addMatchingResult(resultList, row, col);
                }
            }
        }
    }

    private void matchingPointsWithLOrTToTheRight(Cell[][] cellArray, int rowLength, int colLength, List<int[]> resultList) {
        for (int row = 0; row < rowLength - 2; row++) {
            for (int col = 0; col < colLength - 1; col++) {
                final CellColour cellLeftBot = cellArray[row][col].getCellColour();
                final CellColour cellLeftMid = cellArray[row + 1][col].getCellColour();
                final CellColour cellLeftTop = cellArray[row + 2][col].getCellColour();
                final CellColour cellRightBot = cellArray[row][col + 1].getCellColour();
                final CellColour cellRightMid = cellArray[row + 1][col + 1].getCellColour();
                final CellColour cellRightTop = cellArray[row + 2][col + 1].getCellColour();

                final boolean isAtleastOneMatchingOnRight = cellLeftBot != null && (cellLeftBot == cellRightBot ||
                        cellLeftBot == cellRightMid || cellLeftBot == cellRightTop);

                if (isAtleastOneMatchingOnRight && cellLeftBot == cellLeftMid && cellLeftMid == cellLeftTop) {
                    addMatchingResult(resultList, row, col);
                }
            }
        }
    }

    private void matchingPointsWithLOrTToTheLeft(Cell[][] cellArray, int rowLength, int colLength, List<int[]> resultList) {
        for (int row = 0; row < rowLength - 2; row++) {
            for (int col = 1; col < colLength; col++) {
                final CellColour cellLeftBot = cellArray[row][col].getCellColour();
                final CellColour cellLeftMid = cellArray[row + 1][col].getCellColour();
                final CellColour cellLeftTop = cellArray[row + 2][col].getCellColour();
                final CellColour cellRightBot = cellArray[row][col - 1].getCellColour();
                final CellColour cellRightMid = cellArray[row + 1][col - 1].getCellColour();
                final CellColour cellRightTop = cellArray[row + 2][col - 1].getCellColour();

                final boolean isAtleastOneMatchingOnRight = cellLeftBot != null && (cellLeftBot == cellRightBot ||
                        cellLeftBot == cellRightMid || cellLeftBot == cellRightTop);

                if (isAtleastOneMatchingOnRight && cellLeftBot == cellLeftMid && cellLeftMid == cellLeftTop) {
                    addMatchingResult(resultList, row, col);
                }
            }
        }
    }

    private void matchingPointsWithLOrTToTheBottom(Cell[][] cellArray, int rowLength, int colLength, List<int[]> resultList) {
        for (int row = 1; row < rowLength; row++) {
            for (int col = 0; col < colLength - 2; col++) {
                final CellColour cellRightBot = cellArray[row][col].getCellColour();
                final CellColour cellRightMid = cellArray[row][col + 1].getCellColour();
                final CellColour cellRightTop = cellArray[row][col + 2].getCellColour();
                final CellColour cellLeftBot = cellArray[row - 1][col].getCellColour();
                final CellColour cellLeftMid = cellArray[row - 1][col + 1].getCellColour();
                final CellColour cellLeftTop = cellArray[row - 1][col + 2].getCellColour();

                final boolean isAtleastOneMatchingOnRight = cellRightBot != null && (cellRightBot == cellLeftBot ||
                        cellRightBot == cellLeftMid || cellRightBot == cellLeftTop);

                if (isAtleastOneMatchingOnRight && cellRightBot == cellRightMid && cellRightMid == cellRightTop) {
                    addMatchingResult(resultList, row, col);
                }
            }
        }
    }

    private void matchingPointsWithLOrTToTheTop(Cell[][] cellArray, int rowLength, int colLength, List<int[]> resultList) {
        for (int row = 0; row < rowLength - 1; row++) {
            for (int col = 0; col < colLength - 2; col++) {
                final CellColour cellLeftBot = cellArray[row][col].getCellColour();
                final CellColour cellLeftMid = cellArray[row][col + 1].getCellColour();
                final CellColour cellLeftTop = cellArray[row][col + 2].getCellColour();
                final CellColour cellRightBot = cellArray[row + 1][col].getCellColour();
                final CellColour cellRightMid = cellArray[row + 1][col + 1].getCellColour();
                final CellColour cellRightTop = cellArray[row + 1][col + 2].getCellColour();

                final boolean isAtleastOneMatchingOnRight = cellLeftBot != null && (cellLeftBot == cellRightBot ||
                        cellLeftBot == cellRightMid || cellLeftBot == cellRightTop);

                if (isAtleastOneMatchingOnRight && cellLeftBot == cellLeftMid && cellLeftMid == cellLeftTop) {
                    addMatchingResult(resultList, row, col);
                }
            }
        }
    }

    private void addMatchingResult(List<int[]> resultList, int row, int col) {
        final int[] matchingResult = new int[2];
        matchingResult[0] = row;
        matchingResult[1] = col;
        resultList.add(matchingResult);
    }

    private int getNumberOfElementsWithSameColourAsTopElement(Cell[] cells, int topElementPosition, CellColour topElementColour, int numberOfElementsWithSameColourAsTopElement) {
        for (int i = topElementPosition - 1; i >= 0; i--) {
            if (topElementColour == cells[i].getCellColour()) {
                numberOfElementsWithSameColourAsTopElement++;
            } else {
                break;
            }
        }
        return numberOfElementsWithSameColourAsTopElement;
    }
}



/**
 * Simple board clearer implementation.
 */
class ChainClearerImpl implements ChainClearer {

    private CellArrayHelper cellArrayHelper;

    public ChainClearerImpl(CellArrayHelper cellArrayHelper) {
        this.cellArrayHelper = cellArrayHelper;
    }

    @Override
    public void clearBoard(Board board) {
        clearBoardForAllShapes(board);
    }

    private void clearBoardForAllShapes(Board board) {
        final ArrayList<int[]> cellList = new ArrayList<>();
        cellList.addAll(cellArrayHelper.getIndexesOfAllShapes(board.getBoard()));
        for (int[] cell : cellList) {
            clearFromCell(cell[0], cell[1], board);
        }
    }

    @Override
    public void clearBoardByLine(Board board) {
        final List<int[]> cellList = cellArrayHelper.getIndexOfLines(board.getBoard());
        for (int[] cell : cellList) {
            clearFromCell(cell[0], cell[1], board);
        }
    }

    @Override
    public void clearBoardBySquare(Board board) {
        final List<int[]> cellList = cellArrayHelper.getIndexOf4BlockGroup(board.getBoard());
        for (int[] cell : cellList) {
            clearFromCell(cell[0], cell[1], board);
        }
    }

    @Override
    public void clearBoardByTAndL(Board board) {
        final List<int[]> cellList = cellArrayHelper.getIndexOfLAndT(board.getBoard());
        for (int[] cell : cellList) {
            clearFromCell(cell[0], cell[1], board);
        }
    }

    @Override
    public void clearBoardByZ(Board board) {
        final List<int[]> cellList = cellArrayHelper.getIndexOfZ(board.getBoard());
        for (int[] cell : cellList) {
            clearFromCell(cell[0], cell[1], board);
        }
    }

    @Override
    public boolean isClearable(Board board) {
        return cellArrayHelper.isClearable(board);
    }

    private void clearFromCell(int rowId, int columnId, Board board) {
        final Cell collapsingCell = board.getCell(rowId, columnId);
        final CellColour cellColour = collapsingCell.getCellColour();

        clearCellAndAround(rowId, columnId, board, cellColour);
    }

    private void clearCellAndAround(int rowId, int columnId, Board board, CellColour cellColour) {
        if (rowId >= 0 && rowId < Board.ROW_LENGTH && columnId >= 0 && columnId < Board.COLUMN_LENGTH && cellColour != null) {
            final Cell cell = board.getCell(rowId, columnId);
            final CellStatus currentCellStatus = cell.getCellStatus();
            final CellColour currentCellColour = cell.getCellColour();
            if (cellColour == currentCellColour || currentCellStatus == CellStatus.BLOCKED) {
                board.setCell(rowId, columnId, CellStatus.EMPTY, null);
                if (currentCellColour == cellColour) {
                    clearSurroundingCell(rowId, columnId, board, cellColour);
                }
            }
        }
    }

    private void clearSurroundingCell(int rowId, int columnId, Board board, CellColour cellColour) {
        clearCellAndAround(rowId - 1, columnId, board, cellColour);
        clearCellAndAround(rowId, columnId - 1, board, cellColour);
        clearCellAndAround(rowId, columnId + 1, board, cellColour);
        clearCellAndAround(rowId + 1, columnId, board, cellColour);
    }
}


/**
 * Data parser for game input
 */
class DataParser {
    /**
     * Creates a block with two cells from the given 1-indexed values
     * @param bottomCellIndex 1-indexed enum value for bottom cell
     * @param topCellIndex 1-indexed enum value for top cell
     * @return block with both cells
     */
    public Block createColourBlock(int bottomCellIndex, int topCellIndex) {
        final CellColour bottomCellColour = CellColour.values()[bottomCellIndex - 1];
        final CellColour topCellColour = CellColour.values()[topCellIndex - 1];

        final Cell bottomCell = new Cell(bottomCellColour, CellStatus.OCCUPIED);
        final Cell topCell = new Cell(topCellColour, CellStatus.OCCUPIED);

        final Cell[] cells = new Cell[2];
        cells[0] = bottomCell;
        cells[1] = topCell;
        return new Block(cells);
    }

    public BlockQueue createBlockQueue(int[][] blockQueueData) {
        final BlockQueue blockQueue = new BlockQueue();
        for (int[] aBlockQueueData : blockQueueData) {
            final Block colourBlock = createColourBlock(aBlockQueueData[0], aBlockQueueData[1]);
            blockQueue.add(colourBlock);
        }
        return blockQueue;
    }

    public Cell[] createBoardRow(String rowString) {
        assert rowString != null && !rowString.equals("");
        final char[] chars = rowString.toCharArray();
        final Cell[] cells = new Cell[6];

        for(int i = 0; i < chars.length; i++) {
            final char aChar = chars[i];
            switch (aChar) {
                case '.':
                    cells[i] = new Cell(null, CellStatus.EMPTY);
                    break;
                case '0':
                    cells[i] = new Cell(null, CellStatus.BLOCKED);
                    break;
                case '1':
                    cells[i] = new Cell(CellColour.BLUE, CellStatus.OCCUPIED);
                    break;
                case '2':
                    cells[i] = new Cell(CellColour.GREEN, CellStatus.OCCUPIED);
                    break;
                case '3':
                    cells[i] = new Cell(CellColour.PURPLE, CellStatus.OCCUPIED);
                    break;
                case '4':
                    cells[i] = new Cell(CellColour.RED, CellStatus.OCCUPIED);
                    break;
                case '5':
                    cells[i] = new Cell(CellColour.YELLOW, CellStatus.OCCUPIED);
                    break;
                default:
                    throw new AssertionError("Invalid char in board row input");
            }
        }

        return cells;
    }

    public Board createBoard(String[] boardString) {
        final Board board = new Board();

        assert boardString.length == 12;

        final int lengthIndex = boardString.length - 1;
        for (int i = lengthIndex; i >= 0 ; i--) {
            final String rowString = boardString[i];
            final Cell[] boardRow = createBoardRow(rowString);

            final int rowIndex = lengthIndex - i;
            board.setRow(rowIndex, boardRow);
        }

        return board;
    }

    public String[] createBoardString(Board board) {
        final String[] boardRows = new String[12];
        final char[] rowString = new char[6];
        for (int row = 0; row < Board.ROW_LENGTH; row++) {
            for (int col = 0; col < Board.COLUMN_LENGTH; col++) {
                final Cell cell = board.getCell(row, col);
                final CellStatus cellStatus = cell.getCellStatus();
                final CellColour cellColour = cell.getCellColour();
                char ch =' ';
                if (cellStatus == CellStatus.BLOCKED) {
                    ch = '0';
                } else if (cellStatus == CellStatus.EMPTY){
                    ch = '.';
                } else if (cellStatus == CellStatus.OCCUPIED){
                    switch (cellColour) {
                        case BLUE:
                            ch = '1';
                            break;
                        case GREEN:
                            ch = '2';
                            break;
                        case PURPLE:
                            ch = '3';
                            break;
                        case RED:
                            ch = '4';
                            break;
                        case YELLOW:
                            ch = '5';
                            break;
                    }
                } else {
                    throw new AssertionError("Invalid char in board row input");
                }
                rowString[col] = ch;
            }
            boardRows[Board.ROW_LENGTH - row - 1] = new String(rowString);
        }
        return boardRows;
    }

    public String[] prettifyBoardString(String[] outputBoardString) {
        for (int i = 0; i < outputBoardString.length; i++) {
            final String prettyBoardRow = outputBoardString[i].replaceAll("\\.", "  .");
            outputBoardString[i] = "|" + prettyBoardRow + "|";
        }
        return outputBoardString;
    }

    public String[] prettifyBoard(Board board) {
        final String[] boardString = createBoardString(board);
        final String[] prettifiedBoardString = prettifyBoardString(boardString);
        return prettifiedBoardString;
    }
}


/**
 * Factory to create blocks
 */
class BlockFactory {

    /**
     * Creates a falling 2x1 block (assumed to be vertical with first block at the bottom)
     * @param cellColour cellColour of the block
     * @return 2x1 block
     */
    public static Block create2x1SameColourBlock(CellColour cellColour) {
        Cell[] cells = new Cell[2];
        cells[0] = new Cell(cellColour, CellStatus.OCCUPIED);
        cells[1] = new Cell(cellColour, CellStatus.OCCUPIED);
        return new Block(cells);
    }
}

/**
 * Block representing the pieces that could fall on the {@link Board}
 */
class Block {

    private Cell[] cells = new Cell[2];

    public Block(Cell[] cells) {
        this.cells = cells;
    }

    public Cell[] getCells() {
        return cells;
    }
}


/**
 * Contains board and corresponding data.
 */
class Board {

    public static final int ROW_LENGTH = 12;
    public static final int COLUMN_LENGTH = 6;
    private final Cell[][] cells;

    public Board() {
        cells = new Cell[ROW_LENGTH][COLUMN_LENGTH];
        initEmptyBoard();
    }

    public Board(Board board) {
        this.cells = new Cell[ROW_LENGTH][COLUMN_LENGTH];

        for (int row = 0; row < ROW_LENGTH; row++) {
            for (int col = 0; col < COLUMN_LENGTH; col++) {
                final Cell cell = board.getCell(row, col);
                cells[row][col] = new Cell(cell.getCellColour(), cell.getCellStatus());
            }
        }
    }

    private void initEmptyBoard() {
        for (int i = 0; i < Board.ROW_LENGTH; i++) {
            for (int j = 0; j < Board.COLUMN_LENGTH; j++) {
                cells[i][j] = new Cell(null, CellStatus.EMPTY);
            }
        }
    }

    /**
     * returns the current board as a 2d array
     *
     * @return entire board data
     */
    public Cell[][] getBoard() {
        return cells;
    }

    /**
     * Get cell data from the board
     *
     * @param row    row index
     * @param column column index
     * @return Cell corresponding to the indexes on the board
     */
    public Cell getCell(int row, int column) {
        return cells[row][column];
    }

    /**
     * Set colour and status
     *
     * @param row        row index
     * @param column     column index
     * @param cellStatus new cell status
     * @param cellColour new cell colour
     */
    public void setCell(int row, int column, CellStatus cellStatus, CellColour cellColour) {
        cells[row][column].setCellColour(cellColour);
        cells[row][column].setCellStatus(cellStatus);
    }

    /**
     * Get the column for the given column index
     *
     * @param column column index
     * @return cell array for the column
     */
    public Cell[] getColumn(int column) {
        final Cell[] columns = new Cell[ROW_LENGTH];
        for (int i = 0; i < ROW_LENGTH; i++) {
            columns[i] = cells[i][column];
        }
        return columns;
    }

    /**
     * Set the column for the given column index
     *
     * @param column column index
     * @param cells column data
     */
    public void setColumn(int column, Cell[] cells) {
        for (int row = 0; row < ROW_LENGTH; row++) {
            setCell(row, column, cells[row].getCellStatus(), cells[row].getCellColour());
        }
    }

    /**
     * Get the row for the given row index
     *
     * @param row row index
     * @return cell array for the row
     */
    public Cell[] getRow(int row) {
        return cells[row];
    }

    /**
     * Set the row for the given row index
     *
     * @param row row index
     */
    public void setRow(int row, Cell[] rowCells) {
        cells[row] = rowCells;
    }
}



/**
 * BlockQueue with Blocks pieces that are falling on the board.
 */
class BlockQueue {
    private final List<Block> blocks;

    public BlockQueue() {
        this.blocks = new ArrayList<>(8);
    }

    /**
     * Copy constructor
     * @param blockQueue block list
     */
    public BlockQueue(BlockQueue blockQueue) {
        this.blocks = new ArrayList<>(blockQueue.getBlocks());
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    /**
     * Add element to the block queue
     * @param block element to add
     */
    public void add(Block block) {
        assert blocks.size() <= 8;
        blocks.add(block);
    }

    /**
     * Get the next element in the block.
     * @return the next block
     */
    
    public Block getNext() {
        return blocks.size() > 0 ? blocks.get(0) : null;
    }

    /**
     * Pops the block and retuns the popped block.
     * @return the popped block
     */
    public Block getNextAndPop() {
        final Block next = getNext();
        if (next != null) {
            blocks.remove(0);
        }
        return next;
    }

    /**
     * Get block at an index
     * @param blockIndex index to get the block at
     * @return Block on that index
     */
    public Block getBlock(int blockIndex) {
        return blocks.get(blockIndex);
    }

    /**
     * Get current size of block queue
     * @return size
     */
    public int getSize() {
        return blocks.size();
    }
}


/**
 * Represents cell on the board.
 */
class Cell {

    private CellColour cellColour;
    private CellStatus cellStatus;

    public Cell(CellColour cellColour, CellStatus cellStatus) {
        this.cellColour = cellColour;
        this.cellStatus = cellStatus;
    }

    public CellStatus getCellStatus() {
        return cellStatus;
    }

    public CellColour getCellColour() {
        return cellColour;
    }

    public void setCellColour(CellColour cellColour) {
        this.cellColour = cellColour;
    }

    public void setCellStatus(CellStatus cellStatus) {
        this.cellStatus = cellStatus;
    }
}


/**
 * Scores the board
 */
interface BoardScorer {

    /**
     * Calculate score for the given board
     *
     * @param board board to compute score for
     * @param updateBoard if to work on a copy or update the board which passed by ref
     * @return score value
     */
    int scoreBoardAndRecursivelyClearAndCollapse(Board board, boolean updateBoard);
}



/**
 * Board scoring implementation
 */
class BoardScorerImpl implements BoardScorer {

    private final ChainClearer chainClearer;
    private final BoardCollapser boardCollapser;

    public BoardScorerImpl(ChainClearer chainClearer, BoardCollapser boardCollapser) {
        this.chainClearer = chainClearer;
        this.boardCollapser = boardCollapser;
    }

    @Override
    public int scoreBoardAndRecursivelyClearAndCollapse(Board board, boolean updateBoard) {
        final int score;
        if (updateBoard) {
            score = calculateScore(board, 0);
        } else {
            final Board tempBoard = new Board(board);
            score = calculateScore(tempBoard, 0);
        }
        return score;
    }

    private int calculateScore(Board board, int chainPower) {
        final boolean clearable = chainClearer.isClearable(board);
        if (clearable) {
            final Board currentBoardState = new Board(board);
            chainClearer.clearBoard(board);

            int score = 0;

            final HashMap<CellColour, Integer> colourSizeMap = new HashMap<>();

            for (int row = 0; row < Board.ROW_LENGTH; row++) {
                for (int col = 0; col < Board.COLUMN_LENGTH; col++) {
                    final Cell newCell = board.getCell(row, col);
                    final Cell oldCell = currentBoardState.getCell(row, col);
                    if (oldCell.getCellStatus() != CellStatus.EMPTY && oldCell.getCellStatus() != CellStatus.BLOCKED && newCell.getCellStatus() == CellStatus.EMPTY) {

                        final CellColour cellColour = oldCell.getCellColour();
                        if (!colourSizeMap.containsKey(cellColour)) {
                            colourSizeMap.put(cellColour, 1);
                        } else {
                            final Integer integer = colourSizeMap.get(cellColour);
                            colourSizeMap.put(cellColour, integer + 1);
                        }
                    }
                }
            }

            final int numColours = colourSizeMap.size();

            if (numColours == 0) {
                return 0;
            }

            boardCollapser.collapseBoard(board);

            final int colourBonus = getColourBonus(numColours);
            int groupBonus = 0;
            for (Integer integer : colourSizeMap.values()) {
                if (integer > 4) {
                    groupBonus += integer - 4;
                }
            }

            for (Map.Entry<CellColour, Integer> cellColourIntegerEntry : colourSizeMap.entrySet()) {
                final Integer numberOfBlock = cellColourIntegerEntry.getValue();
                final int comboTotal = chainPower + colourBonus + groupBonus;
                score += (numberOfBlock * 10) * (comboTotal == 0 ? 1: comboTotal);
            }

            final int updateChainPower = chainPower == 0 ? 8 : chainPower * 2;
            return score + calculateScore(board, updateChainPower);
        }
        else {
            return 0;
        }
    }

    private int getColourBonus(int numColours) {
        switch (numColours) {
            case 1:
                return 0;
            case 2:
                return 2;
            case 3:
                return 4;
            case 4:
                return 8;
            case 5:
                return 16;
            default:
                assert false;
                return 0;
        }
    }
}
