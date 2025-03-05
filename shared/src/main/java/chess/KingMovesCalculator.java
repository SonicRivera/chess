package chess;

import java.util.ArrayList;

public class KingMovesCalculator extends PieceMovesCalculator {


    private final ChessBoard cBoard;
    private final ChessPiece piece;
    private final int row;
    private final int col;

    public KingMovesCalculator(ChessBoard board, ChessPosition startPosition, ChessPiece piece) {
        super(board, startPosition, piece);
        this.piece = piece;
        this.row = startPosition.getRow();
        this.col = startPosition.getColumn();
        this.cBoard = board;


        if (this.piece.getPieceType() != ChessPiece.PieceType.KING){
            throw new IllegalArgumentException("KingMovesCalculator can only be used with King pieces.");
        }

    }

    public ArrayList<ChessMove> calculateMoves(){
        int[][] directions = {
                {1, 0}, {1, 1}, {0, 1}, {-1, 1},
                {-1, 0}, {-1, -1}, {0, -1}, {1, -1}
        };

        return super.calculateLimitedMoves(directions, this.row, this.col, piece, cBoard);
    }

}
