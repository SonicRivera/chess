package chess;

import java.util.ArrayList;



public class PieceMovesCalculator {
    private int row;
    private int col;
    
    public PieceMovesCalculator(ChessBoard board, ChessPosition startPos, ChessPiece piece){
        this.row = startPos.getRow();
        this.col = startPos.getColumn();
        board.getPiece(startPos);
    }

    /**
     * Returns true if the piece is blocked.
     */
    public Boolean Blocked(ChessBoard board, ChessPosition endPos, ChessPiece piece){
        if (board.getPiece(endPos) != null) {
            return board.getPiece(endPos).getTeamColor() == piece.getTeamColor();
        }
        return false;
    }

    public ArrayList<ChessMove> calculateMoves(){
        return new ArrayList<>();
    }

}
