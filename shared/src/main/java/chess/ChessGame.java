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
        if (getBoard().getPiece(startPosition) == null){
            return null;
        }


        Collection<ChessMove> validMoves;
        ChessPiece piece = getBoard().getPiece(startPosition);
        validMoves = piece.pieceMoves(getBoard(), startPosition);

        //For each move the piece can make, make the move and check for validity
        for (ChessMove move : validMoves){
            ChessBoard testBoard = new ChessBoard(this.gameBoard);
            moveTestBoard(testBoard,move);


            //Select each piece on the board to test if the current move allows enemy pieces to take the king
            for (int r = 0; r < 8; r++){
                for (int c= 0; c < 8; c++){

                    //If our currently selected piece is not the piece we're testing for valid moves and is not on the same team
                    if (testBoard.board[r][c] != piece && testBoard.board[r][c].getTeamColor() != piece.getTeamColor()){

                        //For each piece on the board, make the move and check if it's valid. If it's not, reset and try the next move.

                        ChessPosition pos = new ChessPosition(r, c);
                        if (testBoard.getPiece(pos) != null){


                            Collection<ChessMove> otherMoves = testBoard.getPiece(pos).pieceMoves(testBoard, pos);



                        }

                    }
                }
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
        throw new RuntimeException("Not implemented");

    }

    //Helper function to execute a move on the test board
    public void moveTestBoard(ChessBoard testBoard, ChessMove move){
        ChessPiece piece = testBoard.getPiece(move.getStartPosition());

        testBoard.board[move.getStartPosition().getRow()][move.getStartPosition().getColumn()] = null;
        testBoard.board[move.getEndPosition().getRow()][move.getEndPosition().getColumn()] = piece;
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
