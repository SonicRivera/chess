package server;

import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import com.google.gson.JsonParser;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import websocket.commands.*;
import websocket.messages.LoadGame;
import websocket.messages.Notification;
import websocket.messages.ServerMessage;
import websocket.messages.Error;

import java.io.IOException;
import java.util.Objects;

@WebSocket
public class WebSocketHandler {

    @OnWebSocketConnect
    public void onConnect(Session session) {
        Server.gameSessions.put(session, 0);
        System.out.println("Client connected: " + session.getRemoteAddress());
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception{
        System.out.println("Message received: " + message);

    // Parse the commandType field
    JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
    String commandType = jsonObject.get("commandType").getAsString();

    // Deserialize into the appropriate subclass based on commandType
    UserGameCommand command;
    switch (commandType) {
        case "CONNECT":
            command = new Gson().fromJson(message, Connect.class);
            Server.gameSessions.replace(session, command.getGameID());
            handleConnect((Connect) command, session);
            break;
        case "MAKE_MOVE":
            command = new Gson().fromJson(message, MakeMove.class);
            handleMakeMove((MakeMove) command, session);
            break;
        case "LEAVE":
            command = new Gson().fromJson(message, Leave.class);
            handleLeave((Leave) command, session);
            break;
        case "RESIGN":
            command = new Gson().fromJson(message, Resign.class);
            handleResign((Resign) command, session);
            break;
        default:
            System.err.println("Unknown commandType: " + commandType);
            sendError(session, new Error("Unknown commandType: " + commandType));
            break;
    }
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        Server.gameSessions.remove(session);
        System.out.println("Client disconnected: " + session.getRemoteAddress() + " with status code: " + statusCode
                + " and reason: " + reason);
    }

    @OnWebSocketError
    public void onError(Session session, Throwable throwable) {
        System.err.println("Error for client " + session.getRemoteAddress() + ": " + throwable.getMessage());
    }

    private void handleConnect(Connect command, Session session) throws IOException {
        System.out.println("Handling CONNECT for gameID: " + command.getGameID());

        try {
            AuthData auth = Server.userService.getAuth(command.getAuthToken());
            GameData game = Server.gameService.getGameData(command.getAuthToken(), command.getGameID());

            // Determine if the user is a player or observer
            boolean isWhitePlayer = Objects.equals(game.whiteUsername(), auth.username());
            boolean isBlackPlayer = Objects.equals(game.blackUsername(), auth.username());

            if (isWhitePlayer || isBlackPlayer) {
                String color = isWhitePlayer ? "WHITE" : "BLACK";
                Notification notif = new Notification("%s has joined the game as %s".formatted(auth.username(), color));
                broadcastMessage(session, notif);
            } else {
                Notification notif = new Notification("%s has joined the game as an OBSERVER".formatted(auth.username()));
                broadcastMessage(session, notif);
            }

            // Send the game state to the connecting user
            LoadGame load = new LoadGame(game.game());
            sendMessage(session, load);
        } catch (Exception e) {
            sendError(session, new Error("Error: Not authorized"));
        }
    }

    private void handleMakeMove(UserGameCommand command, Session session) {
        System.out.println("Handling MAKE_MOVE");
        // Validate move, update game state, and notify clients
    }

    private void handleLeave(UserGameCommand command, Session session) throws IOException {
        try {
            AuthData auth = Server.userService.getAuth(command.getAuthToken());
            GameData game = Server.gameService.getGameData(command.getAuthToken(), command.getGameID());
    
            // Update game state to free the player's spot
            if (game.whiteUsername().equals(auth.username())) {
                game = new GameData(game.gameID(), null, game.blackUsername(), game.gameName(),game.game());
            } else if (game.blackUsername().equals(auth.username())) {
                game = new GameData(game.gameID(), game.whiteUsername(), null, game.gameName(),game.game());
            }
    
            // Persist the updated game state to the database
            Server.gameService.updateGame(game);
    
            // Notify other players
            Notification notif = new Notification("%s has left the game".formatted(auth.username()));
            broadcastMessage(session, notif);
            session.close();
        } catch (Exception e) {
            sendError(session, new Error("Error: Not authorized"));
        }
    }

    private void handleResign(UserGameCommand command, Session session) {
        System.out.println("Handling RESIGN");
        // Notify all clients and mark game as over
    }

    // Send notification to all clients on current game except user
    public void broadcastMessage(Session user, ServerMessage message) throws IOException {
        broadcastMessage(user, message, false);
    }

    // Send the notification to all clients on the current game
    public void broadcastMessage(Session user, ServerMessage message, boolean toSelf) throws IOException {
        System.out.printf("Broadcasting (toSelf: %s): %s%n", toSelf, new Gson().toJson(message));
        for (Session session : Server.gameSessions.keySet()) {
            boolean inAGame = Server.gameSessions.get(session) != 0;
            boolean sameGame = Server.gameSessions.get(session).equals(Server.gameSessions.get(user));
            boolean isSelf = session == user;
            if ((toSelf || !isSelf) && inAGame && sameGame) {
                sendMessage(session, message);
            }
        }
    }

    public void sendMessage(Session session, ServerMessage message) throws IOException {
        session.getRemote().sendString(new Gson().toJson(message));
    }


    private void sendError(Session session, Error error) throws IOException {
        System.out.printf("Error: %s%n", new Gson().toJson(error));
        session.getRemote().sendString(new Gson().toJson(error));
    }

}