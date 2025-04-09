package client;

import com.google.gson.Gson;

import chess.ChessGame;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGame;
import websocket.messages.Notification;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketClient extends Endpoint {
    private Session session;

    public WebSocketClient(String serverDomain) throws Exception {
        try {
            URI uri = new URI("ws://" + serverDomain + "/ws");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, uri);

            // Set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    handleMessage(message);
                }
            });

        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new Exception("Failed to connect to WebSocket server", ex);
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig config) {
        this.session = session;
        System.out.println("WebSocket connection opened.");  // Remove for production
    }

    private void handleMessage(String message) {
        if (message.contains("\"serverMessageType\":\"NOTIFICATION\"")) {
            Notification notif = new Gson().fromJson(message, Notification.class);
            printNotification(notif.getMessage());
        } else if (message.contains("\"serverMessageType\":\"ERROR\"")) {
            Error error = new Gson().fromJson(message, Error.class);
            printNotification(error.getMessage());
        } else if (message.contains("\"serverMessageType\":\"LOAD_GAME\"")) {
            LoadGame loadGame = new Gson().fromJson(message, LoadGame.class);
            printLoadedGame(loadGame.getGame());
        }
    }

    private void printNotification(String message) {
        System.out.println("\n[NOTIFICATION] " + message);
    }

    private void printLoadedGame(ChessGame game) {
        System.out.println("\n[LOAD_GAME] Game state updated:");
        ServerFacade.printGame(game.getBoard(), true);
    }

    public void sendMessage(String message) {
        if (this.session != null && this.session.isOpen()) {
            this.session.getAsyncRemote().sendText(message);
        } else {
            System.err.println("WebSocket session is not open. Cannot send message.");
        }
    }

    public void sendConnectCommand(String authToken, int gameID) {
        UserGameCommand connectCommand = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
        String jsonCommand = new Gson().toJson(connectCommand);
        sendMessage(jsonCommand);
    }

    public void close() {
        try {
            if (this.session != null) {
                this.session.close();
            }
        } catch (IOException e) {
            System.out.println("Something went wrong when closing.");;
        }
    }
}