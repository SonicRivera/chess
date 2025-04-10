package client;

import com.google.gson.Gson;

import chess.ChessGame;
import ui.ChessClient;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGame;
import websocket.messages.Notification;
import websocket.messages.Error;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketClient extends Endpoint {
    private Session session;
    private ChessClient chessClient;

    public WebSocketClient(String serverDomain, ChessClient client) throws Exception {
        this.chessClient = client;
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
        System.out.println();
        chessClient.chessGame = game;
        chessClient.printGame(game.getBoard());
    }

    public void sendMessage(String message) {
            this.session.getAsyncRemote().sendText(message);
    }
}