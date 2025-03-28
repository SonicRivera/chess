package ui;


import client.ServerFacade;

public class ChessClient {

    private final ServerFacade server;
    public State state = State.SIGNEDOUT;
    private String sessionToken = null;

    // Colors
    String redText = EscapeSequences.SET_TEXT_COLOR_RED;
    String blueText = EscapeSequences.SET_TEXT_COLOR_BLUE;
    String yellowText = EscapeSequences.SET_TEXT_COLOR_YELLOW;
    String resetText = EscapeSequences.RESET_TEXT_COLOR;




    public ChessClient(String serverUrl){
        server = new ServerFacade(serverUrl);
    }

    public void eval(String command) {
        // Parse the command
        Command parsedCommand = parseCommand(command);
    
        // Dispatch commands based on state
        switch (state) {
            case SIGNEDOUT:
                handleSignedOutCommands(parsedCommand);
                break;
            case SIGNEDIN:
                handleSignedInCommands(parsedCommand);
                break;
            default:
                if (parsedCommand.baseCommand.equals("quit") || parsedCommand.baseCommand.equals("q")) {
                    handleQuitCommand();
                } else {
                    handleUnknownCommand();
                }
                break;
        }
    }
    
    // Helper class to represent a parsed command
    private static class Command {
        String baseCommand;
        String arguments;
    
        Command(String baseCommand, String arguments) {
            this.baseCommand = baseCommand;
            this.arguments = arguments;
        }
    }
    
    // Parse the command into baseCommand and arguments
    private Command parseCommand(String command) {
        String[] parts = command.split("\\s+", 2);
        String baseCommand = parts[0].toLowerCase();
        String arguments = (parts.length > 1) ? parts[1] : "";
        return new Command(baseCommand, arguments);
    }
    
    // Handle commands when the user is signed out
    private void handleSignedOutCommands(Command command) {
        String baseCommand = command.baseCommand;
        String arguments = command.arguments;
    
        switch (baseCommand) {
            case "help":
            case "h":
                printSignedOutHelp();
                break;
            case "register":
                handleRegisterCommand(arguments);
                break;
            case "login":
                handleLoginCommand(arguments);
                break;
            default:
                handleUnknownCommand();
        }
    }
    
    // Handle commands when the user is signed in
    private void handleSignedInCommands(Command command) {
        String baseCommand = command.baseCommand;
        String arguments = command.arguments;
    
        switch (baseCommand) {
            case "help":
            case "h":
                printSignedInHelp();
                break;
            case "logout":
                handleLogoutCommand();
                break;
            case "create":
                handleCreateCommand(arguments);
                break;
            case "list":
                server.listGames(sessionToken);
                break;
            case "join":
                handleJoinCommand(arguments);
                break;
            case "observe":
                handleObserveCommand(arguments);
                break;
            default:
                handleUnknownCommand();
        }
    }
    
    // Handle the quit command
    private void handleQuitCommand() {
        System.out.println("Goodbye!");
        System.exit(0);
    }
    
    // Handle unknown commands
    private void handleUnknownCommand() {
        System.out.println("Unknown command. Type 'help' for a list of commands.");
    }

    private void printSignedOutHelp(){
        System.out.println(blueText + "register " + yellowText + "<USERNAME> <PASSWORD> <EMAIL>" +
        EscapeSequences.RESET_TEXT_COLOR + " - account creation");
        System.out.println(blueText + "login " + yellowText + "<USERNAME> <PASSWORD>" +
        EscapeSequences.RESET_TEXT_COLOR + " - to play chess");
        System.out.println(blueText + "quit" + resetText + " - quit playing chess");
        System.out.println(blueText + "help" + resetText + " - Show possible commands");
    }

    private void printSignedInHelp(){
        System.out.println(blueText + "help" + resetText + " - Show possible commands");
        System.out.println(blueText + "logout" + resetText + " - Logs you out");
        System.out.println(blueText + "create " + yellowText + "<NAME>" + resetText + " - creates a game of chess");
        System.out.println(blueText + "list" + resetText + " - lists games");
        System.out.println(blueText + "join " + yellowText + "<ID> [WHITE | BLACK]" + resetText + " - joins specified chess game");
        System.out.println(blueText + "observe " + yellowText + "<ID>" + resetText + " - watch a game of chess");
    }

    private void handleRegisterCommand(String arguments){
        String[] regArgs = arguments.split("\\s+");
        if (regArgs.length < 3) {
            System.out.println(redText + "Missing a username, password, and/or an email");
        } else if (regArgs.length > 3) {
            System.out.println(redText + "Please only input a username, password, and an email.");
        } else {
            sessionToken = server.register(regArgs);
            if (!sessionToken.isEmpty()) {
                state = State.SIGNEDIN;
                server.listGames(sessionToken);
            }
        }
    }

    private void handleLoginCommand(String arguments){
        String[] loginArgs = arguments.split("\\s+");
        if (loginArgs.length < 2) {
            System.out.println(redText + "Missing either a username or a password.");
        } else if (loginArgs.length > 2) {
            System.out.println(redText + "Please only input a username and password.");
        } else {
            sessionToken = server.login(loginArgs);
            if (!sessionToken.isEmpty()){
                state = state.SIGNEDIN;
                server.listGames(sessionToken);
            }

        }
    }

    private void handleLogoutCommand(){
        System.out.println(yellowText + "Logging out..." + resetText);
        if(server.logout(sessionToken)){
            sessionToken = null;
            state = State.SIGNEDOUT;
        }
    }

    private void handleCreateCommand(String arguments){
        if (arguments.isEmpty()) {
            System.out.println(redText + "You must provide a game name." + resetText);
        } else {
            server.createGame(arguments, sessionToken);
            server.listGames(sessionToken);
        }
    }

    private void handleJoinCommand(String arguments){
        String[] joinArgs = arguments.split("\\s+");
        if (joinArgs.length < 2 || joinArgs[0].isEmpty()) {
            System.out.println(redText + "Please specify the game ID and a team color to join." + resetText);
            return;
        }

        String gameId = joinArgs[0];
        String color = (joinArgs.length >= 2) ? joinArgs[1].toUpperCase() : "ANY";
        if (!color.equals("WHITE") && !color.equals("BLACK")) {
            System.out.println(redText + "Invalid color. Please specify either 'WHITE' or 'BLACK'." + resetText);
            return;
        }

        try {
            int gameNum = Integer.parseInt(gameId);
        } catch (Exception e) {
            System.out.println("Please use a number from the list as a gameID");
            return;
        }

        if (server.joinGame(gameId, color, sessionToken)) {
            // state = State.PLAYING; // Use this later
        }
    }

    private void handleObserveCommand(String arguments){
        String[] observeArgs = arguments.split("\\s+");
        if (observeArgs.length != 1 || observeArgs[0].isEmpty()) {
            System.out.println(redText + "Please specify the game ID to observe." + resetText);
        } else {
            server.observeGame(observeArgs[0]);
            // state = State.OBSERVING; // Add this in later for functionality
        }
    }


}
