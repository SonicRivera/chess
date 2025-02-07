package chess;

import java.util.ArrayList;



public class PieceMovesCalculator {

    
    public PieceMovesCalculator(ChessBoard board, ChessPosition startPos, ChessPiece piece){

    }

    /**
     * Returns true if the piece is blocked.
     */
    public Boolean isBlocked(ChessBoard board, ChessPosition endPos, ChessPiece piece){
        if (board.getPiece(endPos) != null) {
            return board.getPiece(endPos).getTeamColor() == piece.getTeamColor();
        }
        return false;
    }

    public ArrayList<ChessMove> calculateMoves(){
        return new ArrayList<>();
    }

}
