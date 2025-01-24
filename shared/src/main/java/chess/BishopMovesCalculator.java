package chess;

import java.sql.Array;
import java.util.ArrayList;

public class BishopMovesCalculator {


    private final ChessPosition startPosition;
    private final ChessPiece.PieceType piece;
    private int row;
    private int col;
    private ChessBoard Cboard;

    public BishopMovesCalculator(ChessBoard board, ChessPosition startPosition, ChessPiece.PieceType piece) {
        this.startPosition = startPosition;
        this.piece = piece;
        this.row = startPosition.getRow();
        this.col = startPosition.getColumn();
        this.Cboard = board;


        if (piece != ChessPiece.PieceType.BISHOP){
            throw new IllegalArgumentException("BishopMovesCalculator can only be used with BISHOP pieces.");
        }

    }

    public ArrayList<ChessMove> calculateMoves(){
        ChessPosition start = new ChessPosition(this.row, this.col);
        ChessPosition end = new ChessPosition(0, 0);

        ArrayList<ChessMove> moves = new ArrayList<>();
       //Check for moving up left
       while (true){
           this.row += 1;
           this.col -= 1;
           ChessPosition pos = new ChessPosition(row, col);
           if (row > 8 || col < 1 || Cboard.getPiece(pos) != null) {
               break;
           } else {
            end = new ChessPosition(this.row, this.col);
            ChessMove move = new ChessMove(start, end, null);
            moves.add(move);
           }

       }



        //Check moves for moving up right
        while (true){
            this.row += 1;
            this.col += 1;
            ChessPosition pos = new ChessPosition(row, col);
            if (row > 8 || col > 8 || Cboard.getPiece(pos) != null) {
                break;
            } else {
                end = new ChessPosition(this.row, this.col);
                ChessMove move = new ChessMove(start, end, null);
                moves.add(move);
            }

        }

        //check for moving down left
        while (true){
            this.row -= 1;
            this.col -= 1;
            ChessPosition pos = new ChessPosition(row, col);
            if (row < 1 || col < 1 || Cboard.getPiece(pos) != null) {
                break;
            } else {
                end = new ChessPosition(this.row, this.col);
                ChessMove move = new ChessMove(start, end, null);
                moves.add(move);
            }

        }

        //check for moving down right
        while (true){
            this.row -= 1;
            this.col += 1;
            ChessPosition pos = new ChessPosition(row, col);
            if (row < 1 ||  col > 8 || Cboard.getPiece(pos) != null) {
                break;
            } else {
                end = new ChessPosition(this.row, this.col);
                ChessMove move = new ChessMove(start, end, null);
                moves.add(move);
            }

        }

        return moves;
    }

}
