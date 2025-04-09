package server;

import com.google.gson.Gson;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/ws")
public class WebSocketHandler {
    private static Map<Session, String> clients = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session) {
        clients.put(session, null);
        System.out.println("Client connected: " + session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
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

    @OnClose
    public void onClose(Session session) {
        clients.remove(session);
        System.out.println("Client disconnected: " + session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("Error for client " + session.getId() + ": " + throwable.getMessage());
    }

    private void handleConnect(UserGameCommand command, Session session) {
        System.out.println("Handling CONNECT for gameID: " + command.getGameID());

        // Send LOAD_GAME message to the client
        ServerMessage loadGameMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        session.getAsyncRemote().sendText(new Gson().toJson(loadGameMessage));

        // Notify other clients about the new connection
        ServerMessage notificationMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        for (Session client : clients.keySet()) {
            if (!client.equals(session)) {
                client.getAsyncRemote().sendText(new Gson().toJson(notificationMessage));
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