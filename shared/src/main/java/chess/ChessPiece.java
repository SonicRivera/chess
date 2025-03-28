package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;
    public boolean past;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
        this.past = false;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    public String getSymbol() {
        String whiteColor = "\u001b[31m"; // Red text for white
        String blackColor = "\u001b[34m"; // Blue text for black
        String resetColor = "\u001b[0m";  // Reset color
    
        String symbol;
        if (this.getPieceType() == PieceType.KING) {
            symbol = " K ";
        } else if (this.getPieceType() == PieceType.QUEEN) {
            symbol = " Q ";
        } else if (this.getPieceType() == PieceType.BISHOP) {
            symbol = " B ";
        } else if (this.getPieceType() == PieceType.KNIGHT) {
            symbol = " N ";
        } else if (this.getPieceType() == PieceType.ROOK) {
            symbol = " R ";
        } else if (this.getPieceType() == PieceType.PAWN) {
            symbol = " P ";
        } else {
            return null; // Invalid piece type
        }
    
        // Add color based on team
        if (this.getTeamColor() == ChessGame.TeamColor.BLACK) {
            return blackColor + symbol + resetColor;
        } else {
            return whiteColor + symbol + resetColor;
        }
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if (this.getPieceType() == PieceType.BISHOP){
            BishopMovesCalculator bishop = new BishopMovesCalculator(board, myPosition, this);
            return bishop.calculateMoves();

        } else if (this.getPieceType() == PieceType.KING) {

            KingMovesCalculator king = new KingMovesCalculator(board, myPosition, board.getPiece(myPosition));
            return king.calculateMoves();

        } else if (this.getPieceType() == PieceType.KNIGHT){

            KnightMovesCalculator knight = new KnightMovesCalculator(board,myPosition, board.getPiece(myPosition));
            return knight.calculateMoves();

        } else if (this.getPieceType() == PieceType.PAWN){

            PawnMovesCalculator pawn = new PawnMovesCalculator(board,myPosition, board.getPiece(myPosition));
            return pawn.calculateMoves();

        } else if (this.getPieceType() == PieceType.QUEEN){

            QueenMovesCalculator queen = new QueenMovesCalculator(board,myPosition, board.getPiece(myPosition));
            return queen.calculateMoves();

        } else if (this.getPieceType() == PieceType.ROOK){

            RookMovesCalculator rook = new RookMovesCalculator(board,myPosition, board.getPiece(myPosition));
            return rook.calculateMoves();

        }

        return new ArrayList<>();
    }
}
