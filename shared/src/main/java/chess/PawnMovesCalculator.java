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

        ArrayList<ChessMove> moves = new ArrayList<>();

        ChessPosition start = new ChessPosition(this.row, this.col);
        int direction;
        int startRow;
        int promotionRow;

        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE){
            direction = 1;
            startRow = 2;
            promotionRow = 8;
        } else {
            direction = -1;
            startRow = 7;
            promotionRow = 1;
        }

        ChessPosition end = new ChessPosition(this.row + direction, this.col);
        if (end.getRow() >= 1 && end.getRow() <= 8 && Cboard.getPiece(end) == null){
            if (end.getRow() == promotionRow){
                moves.add(new ChessMove(start, end, ChessPiece.PieceType.QUEEN));
                moves.add(new ChessMove(start, end, ChessPiece.PieceType.ROOK));
                moves.add(new ChessMove(start, end, ChessPiece.PieceType.BISHOP));
                moves.add(new ChessMove(start, end, ChessPiece.PieceType.KNIGHT));

            } else {
                moves.add(new ChessMove(start, end, null));
            }
            if (this.row == startRow){
                end = new ChessPosition(this.row + 2 * direction, this.col);
                if(Cboard.getPiece(end) == null) {
                    if (end.getRow() == promotionRow){
                        moves.add(new ChessMove(start, end, ChessPiece.PieceType.QUEEN));
                        moves.add(new ChessMove(start, end, ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(start, end, ChessPiece.PieceType.BISHOP));
                        moves.add(new ChessMove(start, end, ChessPiece.PieceType.KNIGHT));
                    } else {
                        moves.add(new ChessMove(start,end, null));
                    }
                }
            }
        }

        return moves;
    }

}
