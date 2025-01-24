package chess;

import java.util.ArrayList;

public class PawnMovesCalculator extends PieceMovesCalculator {


    private final ChessPiece piece;
    private final int row;
    private final int col;
    private final ChessBoard Cboard;
    private boolean firstMove;
    private int color;

    public PawnMovesCalculator(ChessBoard board, ChessPosition startPosition, ChessPiece piece) {
        super(board, startPosition, piece);
        this.piece = piece;
        this.row = startPosition.getRow();
        this.col = startPosition.getColumn();
        this.Cboard = board;
        this.firstMove = true;



        if (this.piece.getPieceType() != ChessPiece.PieceType.PAWN){
            throw new IllegalArgumentException("PawnMovesCalculator can only be used with Pawn pieces.");
        }

    }

    public ArrayList<ChessMove> calculateMoves(){
        Cboard.printBoard();

        ChessPosition start = new ChessPosition(this.row, this.col);
        ChessPosition end;

        ArrayList<ChessMove> moves = new ArrayList<>();
        int[][] directions = {
                {1, 0}, {1, 1}, {1, -1}
        };

        //We may have to set the values manually if each pawn doesn't create it's own instance of calculating moves. ie if white =1, if black =-1;

        //If this piece is black then check to see if it can double move as well as only move down.
        if (piece.getTeamColor() == ChessGame.TeamColor.BLACK){
            this.firstMove = start.getRow() == 7;
            for (int[] direction: directions) {
                direction[0] *= -1;
            }
            end = new ChessPosition(this.row - 2, this.col);

        } else { //Otherwise, check if white can double move up.
            this.firstMove = start.getRow() == 2;
            end = new ChessPosition(this.row + 2, this.col);

        }

        //If it is our pawns first move, add the double move if it is not blocked
        if (firstMove) {

            if (!Blocked(Cboard,end, piece)){
                moves.add(new ChessMove(start,end, null));
            }
        }



        //Check all directions in one for loop
        for (int i = 0; i < directions.length; i++){

            int newRow = start.getRow() + directions[i][0];
            int newCol = start.getColumn() + directions[i][1];
            end = new ChessPosition(newRow, newCol);

            if (i == 0){
                if (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8) {
                    if(Cboard.getPiece(end) == null) {
                        moves.add(new ChessMove(start, end, null));
                    }
                }
            } else {
                if (Cboard.getPiece(end) == null){
                    continue;
                }
                if (Cboard.getPiece(end).getTeamColor() != piece.getTeamColor()){
                    moves.add(new ChessMove(start, end, null));
                }
            }





        }



       
        return moves;
    }

}
