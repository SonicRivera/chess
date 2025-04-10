package server;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

@WebSocket
public class WebSocketHandler {

    @OnWebSocketConnect
    public void onConnect(Session session) {
        Server.gameSessions.put(session, 0);
        System.out.println("Client connected: " + session.getRemoteAddress());
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        System.out.println("Message received: " + message);
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);

        switch (command.getCommandType()) {
            case CONNECT:
                handleConnect(command, session);
                break;
            case MAKE_MOVE:
                handleMakeMove(command, session);
                break;
            case LEAVE:
                handleLeave(command, session);
                break;
            case RESIGN:
                handleResign(command, session);
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

    private void handleConnect(UserGameCommand command, Session session) {
        System.out.println("Handling CONNECT for gameID: " + command.getGameID());

        // Send LOAD_GAME message to the client
        ServerMessage loadGameMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        session.getRemote().sendStringByFuture(new Gson().toJson(loadGameMessage));

        // Notify other clients about the new connection
        ServerMessage notificationMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        for (Session client : Server.gameSessions.keySet()) {
            if (!client.equals(session)) {
                client.getRemote().sendStringByFuture(new Gson().toJson(notificationMessage));
            }
        }
    }

    private void handleMakeMove(UserGameCommand command, Session session) {
        System.out.println("Handling MAKE_MOVE");
        // Validate move, update game state, and notify clients
    }

    private void handleLeave(UserGameCommand command, Session session) {
        System.out.println("Handling LEAVE");
        // Notify other clients
    }

    private void handleResign(UserGameCommand command, Session session) {
        System.out.println("Handling RESIGN");
        // Notify all clients and mark game as over
    }
}