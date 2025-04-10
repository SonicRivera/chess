package ui.handlers;

import ui.ChessClient;

public class SignedInCommandHandler {
    private final ChessClient client;

    public SignedInCommandHandler(ChessClient client) {
        this.client = client;
    }

    public void handleCommand(String baseCommand, String arguments) {
        switch (baseCommand) {
            case "help":
            case "h":
                client.printSignedInHelp();
                break;
            case "logout":
                client.handleLogoutCommand();
                break;
            case "create":
                client.handleCreateCommand(arguments);
                break;
            case "list":
                client.server.listGames(client.authToken);
                break;
            case "join":
                client.handleJoinCommand(arguments);
                break;
            case "observe":
                client.handleObserveCommand(arguments);
                break;
            default:
                client.handleUnknownCommand();
        }
    }
}