package chess;

import java.util.ArrayList;



public class PieceMovesCalculator {
    private int row;
    private int col;
    
    public PieceMovesCalculator(ChessBoard board, ChessPosition startPos){
        this.row = startPos.getRow();
        this.col = startPos.getColumn();
        board.getPiece(startPos);
    }

    public ArrayList<ChessMove> calculateMoves(){
        return new ArrayList<>();
    }

}
