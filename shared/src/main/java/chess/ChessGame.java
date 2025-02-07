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


        //For each move the piece can make, make the move and check if it's valid
        for (ChessMove move : candidateMoves) {
            ChessBoard testBoard = new ChessBoard(gameBoard);
            moveTestBoard(testBoard, move);
            ChessPosition kingPos = updateKingPos(testBoard, move);

            if (isMoveSafe(testBoard, piece.getTeamColor(), kingPos)) {
                validMoves.add(move);
            }
        }

        return validMoves;
    }

    private boolean isMoveSafe(ChessBoard testBoard, TeamColor teamColor, ChessPosition kingPos) {
        // Select each piece on the board to test if the current move allows enemy pieces to take the king
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                ChessPiece testPiece = testBoard.board[r][c];
                if (testPiece != null && testPiece.getTeamColor() != teamColor) {
                    Collection<ChessMove> otherMoves = testPiece.pieceMoves(testBoard, new ChessPosition(r + 1, c + 1));
                    for (ChessMove otherMove : otherMoves) {
                        if (otherMove.getEndPosition().equals(kingPos)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    //Returns the current position of friendly king
    public ChessPosition updateKingPos(ChessBoard board, ChessMove move){
        ChessGame.TeamColor friendlyKing = gameBoard.getPiece(move.getStartPosition()).getTeamColor();
        ChessPosition kingPos;

        if (friendlyKing == TeamColor.WHITE){
            kingPos = board.getWhiteKingPos();
        } else {
            kingPos = board.getBlackKingPos();
        }
        return kingPos;
    }

    public Boolean isValidMove(ChessMove move, Collection<ChessMove> validMoves){
        if (validMoves == null){
            return false;
        }
        for (ChessMove checkMove : validMoves){
            if (move.equals(checkMove)) {
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

        if (piece != null && piece.getTeamColor() == currentTurn){
            if (isValidMove(move, validMoves(move.getStartPosition()))) {
                gameBoard.board[move.getStartPosition().getRow() - 1][move.getStartPosition().getColumn() - 1] = null;
                gameBoard.board[move.getEndPosition().getRow() - 1][move.getEndPosition().getColumn() - 1] = piece;

                // Handle pawn promotion
                if (move.getPromotionPiece() != null && piece.getPieceType() == ChessPiece.PieceType.PAWN) {
                    gameBoard.board[move.getEndPosition().getRow() - 1][move.getEndPosition().getColumn() - 1] = new ChessPiece(piece.getTeamColor(), move.getPromotionPiece());
                }

                // Switch turns
                currentTurn = (currentTurn == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;

            } else {
                throw new InvalidMoveException();
            }

        }else {
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
        if (teamColor == TeamColor.WHITE) {
            return !isMoveSafe(gameBoard, teamColor, gameBoard.getWhiteKingPos());
        } else {
            return !isMoveSafe(gameBoard, teamColor, gameBoard.getBlackKingPos());

        }
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        boolean checkMated = true;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {

                if (gameBoard.board[row][col] != null && gameBoard.board[row][col].getTeamColor() == teamColor) {
                    gameBoard.printBoard();
                    Collection<ChessMove> moves = validMoves(new ChessPosition(row + 1, col + 1));
                    if (!moves.isEmpty()) {
                        checkMated = false;
                    }
                }
            }
        }

        return checkMated;
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
