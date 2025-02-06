package chess;

import java.util.ArrayList;

public class KnightMovesCalculator extends PieceMovesCalculator {


    private final ChessPiece piece;
    private final int row;
    private final int col;
    private final ChessBoard Cboard;

    public KnightMovesCalculator(ChessBoard board, ChessPosition startPosition, ChessPiece piece) {
        super(board, startPosition, piece);
        this.piece = piece;
        this.row = startPosition.getRow();
        this.col = startPosition.getColumn();
        this.Cboard = board;


        if (this.piece.getPieceType() != ChessPiece.PieceType.KNIGHT){
            throw new IllegalArgumentException("KnightMovesCalculator can only be used with Knight pieces.");
        }

    }

    public ArrayList<ChessMove> calculateMoves(){

        ChessPosition start = new ChessPosition(this.row, this.col);
        ChessPosition end;

        ArrayList<ChessMove> moves = new ArrayList<>();
        int[][] directions = {
                {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
                {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
        };

       //Check all directions in one for loop
       for (int[] direction:directions){
           int newRow = start.getRow() + direction[0];
           int newCol = start.getColumn() + direction[1];

           if (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8) {
               end = new ChessPosition(newRow, newCol);
               if(!Blocked(Cboard,end,piece)) {
                    moves.add(new ChessMove(start, end, null));
               }
           }
       }

        return moves;
    }

}
