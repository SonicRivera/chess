package chess;

import java.util.ArrayList;

public class KnightMovesCalculator extends PieceMovesCalculator {


    private final ChessPiece piece;
    private final int row;
    private final int col;
    private final ChessBoard cBoard;

    public KnightMovesCalculator(ChessBoard board, ChessPosition startPosition, ChessPiece piece) {
        super(board, startPosition, piece);
        this.piece = piece;
        this.row = startPosition.getRow();
        this.col = startPosition.getColumn();
        this.cBoard = board;


        if (this.piece.getPieceType() != ChessPiece.PieceType.KNIGHT){
            throw new IllegalArgumentException("KnightMovesCalculator can only be used with Knight pieces.");
        }

    }

    public ArrayList<ChessMove> calculateMoves(){
        int[][] directions = {
                {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
                {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
        };

        return super.calculateLimitedMoves(directions, this.row, this.col, piece, cBoard);
    }

}
