package chess;

import java.util.ArrayList;

public class QueenMovesCalculator extends PieceMovesCalculator {


    private final ChessBoard Cboard;
    private final ChessPiece piece;
    private final int row;
    private final int col;

    public QueenMovesCalculator(ChessBoard board, ChessPosition startPosition, ChessPiece piece) {
        super(board, startPosition, piece);
        this.piece = piece;
        this.row = startPosition.getRow();
        this.col = startPosition.getColumn();
        this.Cboard = board;


        if (this.piece.getPieceType() != ChessPiece.PieceType.QUEEN){
            throw new IllegalArgumentException("QueenMovesCalculator can only be used with Queen pieces.");
        }

    }

    @Override
    public Boolean isBlocked(ChessBoard board, ChessPosition endPos, ChessPiece piece){
        if (!piece.past){
            if (board.getPiece(endPos) != null) {
                if(board.getPiece(endPos).getTeamColor() == piece.getTeamColor()){
                    return true;
                } else {
                    piece.past = true;
                    return false;
                }
            }
        }
        return piece.past;
    }

    public ArrayList<ChessMove> calculateMoves(){

        ChessPosition start = new ChessPosition(this.row, this.col);
        ChessPosition end;

        ArrayList<ChessMove> moves = new ArrayList<>();
        int[][] directions = {
                {1, 0}, {1, 1}, {0, 1}, {-1, 1},
                {-1, 0}, {-1, -1}, {0, -1}, {1, -1}
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
               if (isBlocked(Cboard,end,piece)) {
                    break;
               }

               moves.add(new ChessMove(start, end, null));
           }

       }

        return moves;
    }

}
