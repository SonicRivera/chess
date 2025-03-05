package chess;

import java.util.ArrayList;

public class RookMovesCalculator extends PieceMovesCalculator {


    private final ChessBoard cBoard;
    private final ChessPiece piece;
    private final int row;
    private final int col;

    public RookMovesCalculator(ChessBoard board, ChessPosition startPosition, ChessPiece piece) {
        super(board, startPosition, piece);
        this.piece = piece;
        this.row = startPosition.getRow();
        this.col = startPosition.getColumn();
        this.cBoard = board;


        if (this.piece.getPieceType() != ChessPiece.PieceType.ROOK){
            throw new IllegalArgumentException("RookMovesCalculator can only be used with Rook pieces.");
        }

    }

    public ArrayList<ChessMove> calculateMoves(){

        int[][] directions = {
                {1, 0}, {0, 1}, {-1, 0}, {0, -1}
        };

        return super.calculateMoves(directions, this.row, this.col, piece, cBoard);
    }

}
