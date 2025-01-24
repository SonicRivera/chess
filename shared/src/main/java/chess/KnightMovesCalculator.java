package chess;

import java.util.ArrayList;

public class KnightMovesCalculator extends PieceMovesCalculator {


    private final ChessPosition startPosition;
    private final ChessPiece piece;
    private int row;
    private int col;
    private final ChessBoard Cboard;

    public KnightMovesCalculator(ChessBoard board, ChessPosition startPosition, ChessPiece piece) {
        super(board, startPosition, piece);
        this.startPosition = startPosition;
        this.piece = piece;
        this.row = startPosition.getRow();
        this.col = startPosition.getColumn();
        this.Cboard = board;


        if (this.piece.getPieceType() != ChessPiece.PieceType.KNIGHT){
            throw new IllegalArgumentException("KnightMovesCalculator can only be used with BISHOP pieces.");
        }

    }

    public void resetStart(){
        this.row = startPosition.getRow();
        this.col = startPosition.getColumn();
    }

    public ArrayList<ChessMove> calculateMoves(){
        Cboard.printBoard();

        ChessPosition start = new ChessPosition(this.row, this.col);
        ChessPosition end;

        ArrayList<ChessMove> moves = new ArrayList<>();
        int[][] directions = {
                {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
                {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
        };

       //Check for moving up
       resetStart();

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

//      Old Code
//
//       while (true){
//           this.row += 2;
//
//           //Check right
//           this.col += 1;
//           end = new ChessPosition(this.row, this.col);
//           if (row > 8 || col < 1){
//               break;
//           } else if (Blocked(Cboard, end, this.piece)) {
//               break;
//           } else  {
//            end = new ChessPosition(this.row, this.col);
//            moves.add(new ChessMove(start, end, null));
//           }
//
//       }

        return moves;
    }

}
