package chess;

import java.util.ArrayList;

public class KingMovesCalculator extends PieceMovesCalculator {

    private final ChessPiece piece;
    private int row;
    private int col;
    private ChessBoard Cboard;

    public KingMovesCalculator(ChessBoard board, ChessPosition startPosition, ChessPiece piece) {
        super(board, startPosition, piece);
        this.piece = piece;
        this.row = startPosition.getRow();
        this.col = startPosition.getColumn();
        this.Cboard = board;

        if (this.piece.getPieceType() != ChessPiece.PieceType.KING) {
            throw new IllegalArgumentException("KingMovesCalculator can only be used with KING pieces.");
        }
    }

    public void addMove(ChessPosition start, ChessPosition end, ChessPiece piece, ArrayList<ChessMove> moves){
        if (!super.Blocked(Cboard, end, this.piece)) {
            moves.add(new ChessMove(start, end, null));
        } else {
            System.out.println("BLOCKED");
        }
    }

    public ArrayList<ChessMove> calculateMoves() {
        Cboard.printBoard();

        ChessPosition start = new ChessPosition(this.row, this.col);
        ChessPosition end;
        ArrayList<ChessMove> moves = new ArrayList<>();

        // Check for moving left
        if (this.col - 1 > 1) {
            end = new ChessPosition(this.row, this.col - 1);
            addMove(start, end, piece, moves);
        }

        // Check for moving up left
        if (this.col - 1 > 1 && this.row + 1 < 8) {
            end = new ChessPosition(this.row + 1, this.col - 1);
            addMove(start, end, piece, moves);

        }

        // Check moves for moving up
        if (this.row + 1 < 8) {
            end = new ChessPosition(this.row + 1, this.col);
            addMove(start, end, piece, moves);

        }

        // Check moves for moving up right
        if (this.col + 1 < 8 && this.row + 1 < 8) {
            end = new ChessPosition(this.row + 1, this.col + 1);
            addMove(start, end, piece, moves);

        }

        // Check for moving right
        if (this.col + 1 < 8) {
            end = new ChessPosition(this.row, this.col + 1);
            addMove(start, end, piece, moves);

        }

        // Check moves for moving down right
        if (this.col + 1 < 8 && this.row - 1 > 1) {
            end = new ChessPosition(this.row - 1, this.col + 1);
            addMove(start, end, piece, moves);

        }

        // Check for moving down
        if (this.row - 1 > 1) {
            end = new ChessPosition(this.row - 1, this.col);
            addMove(start, end, piece, moves);

        }

        // Check moves for moving down left
        if (this.col - 1 > 1 && this.row - 1 > 1) {
            end = new ChessPosition(this.row - 1, this.col - 1);
            addMove(start, end, piece, moves);

        }

        return moves;
    }
}
