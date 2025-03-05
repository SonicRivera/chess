package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PawnMovesCalculator extends PieceMovesCalculator {


    private final ChessPiece piece;
    private final int row;
    private final int col;
    private final ChessBoard cBoard;

    public PawnMovesCalculator(ChessBoard board, ChessPosition startPosition, ChessPiece piece) {
        super(board, startPosition, piece);
        this.piece = piece;
        this.row = startPosition.getRow();
        this.col = startPosition.getColumn();
        this.cBoard = board;



        if (this.piece.getPieceType() != ChessPiece.PieceType.PAWN){
            throw new IllegalArgumentException("PawnMovesCalculator can only be used with Pawn pieces.");
        }

    }

    public ArrayList<ChessMove> calculateMoves() {
        ArrayList<ChessMove> moves = new ArrayList<>();

        ChessPosition start = new ChessPosition(this.row, this.col);
        int direction;
        int startRow;
        int promotionRow;

        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            direction = 1;
            startRow = 2;
            promotionRow = 8;
        } else {
            direction = -1;
            startRow = 7;
            promotionRow = 1;
        }

        ChessPosition end = new ChessPosition(this.row + direction, this.col);
        if (end.getRow() >= 1 && end.getRow() <= 8 && cBoard.getPiece(end) == null) {
            addMove(moves, start, end, end.getRow() == promotionRow);

            if (this.row == startRow) {
                end = new ChessPosition(this.row + 2 * direction, this.col);
                if (cBoard.getPiece(end) == null) {
                    addMove(moves, start, end, end.getRow() == promotionRow);
                }
            }
        }

        int[][] captureDirections = {{direction, 1}, {direction, -1}};
        for (int[] captureDirection : captureDirections) {
            end = new ChessPosition(this.row + captureDirection[0], this.col + captureDirection[1]);
            if (end.getRow() >= 1 && end.getRow() <= 8 && end.getColumn() >= 1 && end.getColumn() <= 8) {
                ChessPiece targetPiece = cBoard.getPiece(end);
                if (targetPiece != null && targetPiece.getTeamColor() != piece.getTeamColor()) {
                    addMove(moves, start, end, end.getRow() == promotionRow);
                }
            }
        }

        return moves;
    }

    private void addMove(List<ChessMove> moves, ChessPosition start, ChessPosition end, boolean isPromotion) {
        if (isPromotion) {
            Arrays.asList(ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.ROOK, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.KNIGHT)
                  .forEach(promotionPiece -> moves.add(new ChessMove(start, end, promotionPiece)));
        } else {
            moves.add(new ChessMove(start, end, null));
        }
    }
}
