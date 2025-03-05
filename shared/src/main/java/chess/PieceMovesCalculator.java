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

    public ArrayList<ChessMove> calculateMoves(int[][] directions, int row, int col, ChessPiece piece, ChessBoard cBoard){

        ChessPosition start = new ChessPosition(row, col);
        ChessPosition end;

        ArrayList<ChessMove> moves = new ArrayList<>();


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

    public ArrayList<ChessMove> calculateLimitedMoves(int[][] directions, int row, int col, ChessPiece piece, ChessBoard cBoard){

        ChessPosition start = new ChessPosition(row, col);
        ChessPosition end;

        ArrayList<ChessMove> moves = new ArrayList<>();

        //Check all directions in one for loop
        for (int[] direction:directions){
            int newRow = start.getRow() + direction[0];
            int newCol = start.getColumn() + direction[1];

            if (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8) {
                end = new ChessPosition(newRow, newCol);
                if(!isBlocked(cBoard,end,piece)) {
                    moves.add(new ChessMove(start, end, null));
                }
            }
        }

        return moves;
    }

}
