package websocket.commands;

import chess.ChessGame;

public class Connect extends UserGameCommand {

    ChessGame.TeamColor color;

    public Connect(String authToken, int gameID) {
        super(CommandType.CONNECT, authToken, gameID);
    }
}