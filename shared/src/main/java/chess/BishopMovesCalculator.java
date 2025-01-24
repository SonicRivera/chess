package chess;

import java.sql.Array;
import java.util.ArrayList;

public class BishopMovesCalculator extends PieceMovesCalculator {


    private final ChessPosition startPosition;
    private final ChessPiece piece;
    private int row;
    private int col;
    private ChessBoard Cboard;

    public BishopMovesCalculator(ChessBoard board, ChessPosition startPosition, ChessPiece piece) {
        super(board, startPosition, piece);
        this.startPosition = startPosition;
        this.piece = piece;
        this.row = startPosition.getRow();
        this.col = startPosition.getColumn();
        this.Cboard = board;


        if (this.piece.getPieceType() != ChessPiece.PieceType.BISHOP){
            throw new IllegalArgumentException("BishopMovesCalculator can only be used with BISHOP pieces.");
        }

    }

    public void resetStart(){
        this.row = startPosition.getRow();
        this.col = startPosition.getColumn();
    }

    public ArrayList<ChessMove> calculateMoves(){
        Cboard.printBoard();

        ChessPosition start = new ChessPosition(this.row, this.col);
        ChessPosition end = new ChessPosition(0, 0);

        ArrayList<ChessMove> moves = new ArrayList<>();
       //Check for moving up left
        resetStart();
       while (true){
           this.row += 1;
           this.col -= 1;
           ChessPosition pos = new ChessPosition(row, col);
           if (row > 7 || col < 1){
               end = new ChessPosition(this.row+1, this.col-1);
               if (super.Blocked(Cboard, end, this.piece)) {
                   break;
               }
               break;
           } else {
            end = new ChessPosition(this.row, this.col);
            moves.add(new ChessMove(start, end, null));
           }

       }



        //Check moves for moving up right
        resetStart();
        while (true){
            this.row += 1;
            this.col += 1;
            ChessPosition pos = new ChessPosition(row, col);
            if (row > 7 || col > 7){
                end = new ChessPosition(this.row+1, this.col-1);
                if (super.Blocked(Cboard, end, this.piece)) {
                    break;
                }
                break;
            } else {
                end = new ChessPosition(this.row, this.col);
                moves.add(new ChessMove(start, end, null));
            }

        }

        //check for moving down left
        resetStart();
        while (true){
            this.row -= 1;
            this.col -= 1;
            ChessPosition pos = new ChessPosition(row, col);
            if (row < 1 || col < 1){
                end = new ChessPosition(this.row+1, this.col-1);
                if (super.Blocked(Cboard, end, this.piece)) {
                    break;
                }
                break;
            } else {
                end = new ChessPosition(this.row, this.col);
                moves.add(new ChessMove(start, end, null));
            }

        }

        //check for moving down right
        resetStart();
        while (true){
            this.row -= 1;
            this.col += 1;
            ChessPosition pos = new ChessPosition(row, col);
            if (row < 1 ||  col > 7){
                end = new ChessPosition(this.row+1, this.col-1);
                if (super.Blocked(Cboard, end, this.piece)) {
                    break;
                }
                break;
            } else {
                end = new ChessPosition(this.row, this.col);
                moves.add(new ChessMove(start, end, null));
            }

        }

        return moves;
    }

}
