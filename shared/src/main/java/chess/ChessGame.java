package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    ChessBoard gameBoard;
    TeamColor currentTurn;

    public ChessGame() {
        this.currentTurn = TeamColor.WHITE;
        this.gameBoard = new ChessBoard();
        this.gameBoard.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.currentTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.currentTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        //If there isn't a piece at the given start position then return null
        if (gameBoard.getPiece(startPosition) == null){
            return null;
        }


        Collection<ChessMove> candidateMoves;
        Collection<ChessMove> validMoves = new ArrayList<>();
        ChessPiece piece = gameBoard.getPiece(startPosition);
        candidateMoves = piece.pieceMoves(gameBoard, startPosition);

        //For each move the piece can make, make the move and check for validity
        for (ChessMove move : candidateMoves){
            ChessBoard testBoard = new ChessBoard(this.gameBoard);
            moveTestBoard(testBoard,move);

            //Debug
            testBoard.printBoard();
            System.out.println();


            ChessGame.TeamColor friendlyKing = gameBoard.getPiece(move.getStartPosition()).getTeamColor();
            ChessPosition kingPos;

            if (friendlyKing == TeamColor.WHITE){
                kingPos = testBoard.getWhiteKingPos();
            } else {
                kingPos = testBoard.getBlackKingPos();
            }

            boolean safe = true;


            //Select each piece on the board to test if the current move allows enemy pieces to take the king
            for (int r = 0; r < 8; r++) {
                for (int c = 0; c < 8; c++) {
                    ChessPosition pos = new ChessPosition(r + 1, c + 1);

                    if (testBoard.board[r][c] != null) {
                        ChessPiece testPiece = testBoard.board[r][c];

                        // If our currently selected piece is not on the same team
                        if (testPiece.getTeamColor() != piece.getTeamColor()) {
                            Collection<ChessMove> otherMoves = testPiece.pieceMoves(testBoard, pos);

                            // If our king is in range of enemy attack, mark the move as unsafe
                            for (ChessMove otherMove : otherMoves) {
                                if (otherMove.getEndPosition().equals(kingPos)) {
                                    safe = false;
                                    break;
                                }
                            }
                        }
                    }

                    if (!safe) {
                        break;
                    }
                }

                if (!safe) {
                    break;
                }
            }

            if (safe) {
                validMoves.add(move);
            }
        }


        return validMoves;
    }

    public Boolean isValidMove(ChessMove move, Collection<ChessMove> validMoves){
        for (ChessMove checkMove : validMoves){
            if (move == checkMove) {
                return true;
            }

        }
        return false;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = gameBoard.getPiece(move.getStartPosition());

        if (isValidMove(move, validMoves(move.getStartPosition()))){
            gameBoard.board[move.getStartPosition().getRow()-1][move.getStartPosition().getColumn()-1] = null;
            gameBoard.board[move.getEndPosition().getRow()-1][move.getEndPosition().getColumn()-1] = piece;
        } else {
            throw new InvalidMoveException();
        }

    }

    //Helper function to execute a move on the test board
    public void moveTestBoard(ChessBoard testBoard, ChessMove move){
        ChessPiece piece = testBoard.getPiece(move.getStartPosition());

        testBoard.board[move.getStartPosition().getRow()-1][move.getStartPosition().getColumn()-1] = null;
        testBoard.board[move.getEndPosition().getRow()-1][move.getEndPosition().getColumn()-1] = piece;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.gameBoard = new ChessBoard(board);
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.gameBoard;
    }
}
