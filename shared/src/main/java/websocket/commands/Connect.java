package websocket.commands;

import chess.ChessGame;

public class Connect extends UserGameCommand {

    ChessGame.TeamColor color;

    public Connect(String authToken, int gameID, boolean color) {
        super(CommandType.CONNECT, authToken, gameID);
        if (color) {
            this.color = ChessGame.TeamColor.WHITE;
        } else {
            this.color = ChessGame.TeamColor.BLACK;
        }
    }

    public ChessGame.TeamColor getColor() {
        return color;
    }
}