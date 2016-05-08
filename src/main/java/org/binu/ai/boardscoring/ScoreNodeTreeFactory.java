package org.binu.ai.boardscoring;

import org.binu.ai.simple.CellArrayHelper;
import org.binu.ai.simple.CellArrayHelperImpl;
import org.binu.board.BlockQueue;
import org.binu.board.Board;
import org.binu.board.Cell;
import org.binu.data.CellStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Create a Score Node Tree
 */
public class ScoreNodeTreeFactory {

    private final BoardClearerImpl boardClearer;
    private final ScoreNode rootNode;
    private final CellArrayHelper cellArrayHelper;

    public ScoreNodeTreeFactory(ScoreNode rootNode) {
        this.rootNode = rootNode;
        cellArrayHelper = new CellArrayHelperImpl();
        boardClearer = new BoardClearerImpl(cellArrayHelper);
    }

    public ScoreNode populateRootNodeTree(Board board, BlockQueue blockQueue, int depth) {
        addToScoreNode(rootNode, board, blockQueue, depth);
        return rootNode;
    }

    private void addToScoreNode(ScoreNode scoreNode, Board board, BlockQueue blockQueue, int depth) {

        final int level = scoreNode.getLevel();
        if (level >= depth) {
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

    @NotNull
    private Board getBoardWithDroppedColumn(Board board, BlockQueue blockQueue, int col) {
        final Cell[] column = board.getColumn(col);
        //TODO check for assertion error from dropBlockIntoColumn
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

    @NotNull
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
                //TODO: Don't agressively remove blocks might be helpful for combos.
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
