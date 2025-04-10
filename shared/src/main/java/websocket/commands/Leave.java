package websocket.commands;

import chess.ChessGame;

public class Leave extends UserGameCommand {

    public Leave(String authToken, int gameID) {
        super(CommandType.LEAVE, authToken, gameID);
    }
}