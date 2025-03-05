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

    public Boolean isSpecialBlocked(ChessBoard board, ChessPosition endPos, ChessPiece piece){
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
        return new ArrayList<>();
    }

}
