package chess;

import java.util.ArrayList;

public class BishopMovesCalculator extends PieceMovesCalculator {


    private final ChessBoard cBoard;
    private final ChessPiece piece;
    private final int row;
    private final int col;

    public BishopMovesCalculator(ChessBoard board, ChessPosition startPosition, ChessPiece piece) {
        super(board, startPosition, piece);
        this.piece = piece;
        this.row = startPosition.getRow();
        this.col = startPosition.getColumn();
        this.cBoard = board;


        if (this.piece.getPieceType() != ChessPiece.PieceType.BISHOP){
            throw new IllegalArgumentException("BishopMovesCalculator can only be used with Bishop pieces.");
        }

    }

    public ArrayList<ChessMove> calculateMoves(){
        int[][] directions = {
                {1, 1}, {-1, 1}, {-1, -1}, {1, -1}
        };
        return super.calculateMoves(directions, this.row, this.col, piece, cBoard);
    }

}
