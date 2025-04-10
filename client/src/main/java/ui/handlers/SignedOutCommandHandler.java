package ui.handlers;

import ui.ChessClient;

public class SignedOutCommandHandler {
    private final ChessClient client;

    public SignedOutCommandHandler(ChessClient client) {
        this.client = client;
    }

    public void handleCommand(String baseCommand, String arguments) {
        switch (baseCommand) {
            case "help":
            case "h":
                client.printSignedOutHelp();
                break;
            case "register":
                client.handleRegisterCommand(arguments);
                break;
            case "login":
                client.handleLoginCommand(arguments);
                break;
            default:
                client.handleUnknownCommand();
        }
    }
}