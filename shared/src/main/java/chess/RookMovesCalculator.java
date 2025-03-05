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

        ChessPosition start = new ChessPosition(this.row, this.col);
        ChessPosition end;

        ArrayList<ChessMove> moves = new ArrayList<>();
        int[][] directions = {
                {1, 0}, {0, 1}, {-1, 0}, {0, -1}
        };

       //Check all directions in one for loop
       for (int[] direction:directions){
           int newRow = start.getRow();
           int newCol = start.getColumn();
           piece.past = false;


           while (true){
               newRow += direction[0];
               newCol += direction[1];

               if (newRow < 1 || newRow > 8 || newCol < 1 || newCol > 8) {
                    break;
               }

               end = new ChessPosition(newRow, newCol);
               if (isSpecialBlocked(cBoard,end,piece)) {
                    break;
               }

               moves.add(new ChessMove(start, end, null));
           }

       }

        return moves;
    }

}
