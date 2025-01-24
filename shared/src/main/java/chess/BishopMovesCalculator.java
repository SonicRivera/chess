package chess;

import java.sql.Array;
import java.util.ArrayList;

public class BishopMovesCalculator extends PieceMovesCalculator {

    public BishopMovesCalculator(ChessPosition startPosition, ChessBoard board) {
        super(startPosition, board);

    }

    @Override
    public ArrayList<ChessMove> calculateMoves(){

        ArrayList<?> moves = new ArrayList<>();
        //Check for moving up left
        while (true){
            this.row += 1;
            this.col -= 1;
            ChessPosition pos = new ChessPosition(row, col);
            if (row > 8 || col < 1 || CBoard.getPiece(pos) != null) {
                break;
            }

        }



        //Check moves for moving up right

        //check for moving down left

        //check for moving down right

        return moves;
    }

}
