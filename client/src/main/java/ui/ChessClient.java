package ui;


import client.ServerFacade;

public class ChessClient {

    private final ServerFacade server;
    public State state = State.SIGNEDOUT;
    private String sessionToken = null;




    public ChessClient(String serverUrl){
        server = new ServerFacade(serverUrl);
    }

    public void eval(String command){

        // Colors
        String RED = EscapeSequences.SET_TEXT_COLOR_RED;
        String BLUE = EscapeSequences.SET_TEXT_COLOR_BLUE;
        String YELLOW = EscapeSequences.SET_TEXT_COLOR_YELLOW;
        String RESET = EscapeSequences.RESET_TEXT_COLOR;

        // Separate the base command and the arguments
        String[] parts = command.split("\\s+", 2);
        String baseCommand = parts[0].toLowerCase();
        String arguments = (parts.length > 1) ? parts[1] : "";


        if (state == State.SIGNEDOUT){
            // Help command
            if (baseCommand.equals("help") || baseCommand.equals("h")) {
                System.out.println(BLUE + "register " + YELLOW + "<USERNAME> <PASSWORD> <EMAIL>" + EscapeSequences.RESET_TEXT_COLOR + " - account creation");
                System.out.println(BLUE + "login " + YELLOW + "<USERNAME> <PASSWORD>" + EscapeSequences.RESET_TEXT_COLOR + " - to play chess");
                System.out.println(BLUE + "quit" + RESET + " - quit playing chess");
                System.out.println(BLUE + "help" + RESET + " - Show possible commands");
            }

            // Register command
            else if (baseCommand.equals("register")) {
                String[] regArgs = arguments.split("\\s+");
                if (regArgs.length < 3) {
                    System.out.println(RED + "Missing a username, password, and/or an email");
                } else if (regArgs.length > 3) {
                    System.out.println(RED + "Please only input a username, password, and an email.");
                } else {
                    sessionToken = server.Register(regArgs);
                    if (!sessionToken.isEmpty()) {
                        state = State.SIGNEDIN;
                        server.listGames(sessionToken);
                    }
                }
            }

            // Login command
            else if (baseCommand.equals("login")) {
                String[] loginArgs = arguments.split("\\s+");
                if (loginArgs.length < 2) {
                    System.out.println(RED + "Missing either a username or a password.");
                } else if (loginArgs.length > 2) {
                    System.out.println(RED + "Please only input a username and password.");
                } else {
                    sessionToken = server.Login(loginArgs);
                    if (!sessionToken.isEmpty()){
                        state = state.SIGNEDIN;
                        server.listGames(sessionToken);
                    }

                }
            }

        } else if (state == State.SIGNEDIN){
            // Help
            if (baseCommand.equals("help") || baseCommand.equals("h")) {
                System.out.println(BLUE + "help" + RESET + " - Show possible commands");
                System.out.println(BLUE + "logout" + RESET + " - Logs you out");
                System.out.println(BLUE + "create " + YELLOW + "<NAME>" + RESET + " - creates a game of chess");
                System.out.println(BLUE + "list" + RESET + " - lists games");
                System.out.println(BLUE + "join " + YELLOW + "<ID> [WHITE | BLACK]" + RESET + " - joins specified chess game");
                System.out.println(BLUE + "observe " + YELLOW + "<ID>" + RESET + " - watch a game of chess");
            }

            // Logout
            else if (baseCommand.equals("logout")) {
                System.out.println(YELLOW + "Logging out..." + RESET);
                if(server.Logout(sessionToken)){
                    sessionToken = null;
                    state = State.SIGNEDOUT;
                }

            }

            // Create
            else if (baseCommand.equals("create")) {
                if (arguments.isEmpty()) {
                    System.out.println(RED + "You must provide a game name." + RESET);
                } else {
                    server.createGame(arguments, sessionToken);
                    server.listGames(sessionToken);
                }
            }

            // List
            else if (baseCommand.equals("list")) {
                server.listGames(sessionToken);
            }

            // Join
            else if (baseCommand.equals("join")) {
                String[] joinArgs = arguments.split("\\s+");
                if (joinArgs.length < 2 || joinArgs[0].isEmpty()) {
                    System.out.println(RED + "Please specify the game ID and a team color to join." + RESET);
                    return;
                }

                String gameId = joinArgs[0];
                String color = (joinArgs.length >= 2) ? joinArgs[1].toUpperCase() : "ANY";
                if (!color.equals("WHITE") && !color.equals("BLACK")) {
                    System.out.println(RED + "Invalid color. Please specify either 'WHITE' or 'BLACK'." + RESET);
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

            // Observe
            else if (baseCommand.equals("observe")) {
                String[] observeArgs = arguments.split("\\s+");
                if (observeArgs.length != 1 || observeArgs[0].isEmpty()) {
                    System.out.println(RED + "Please specify the game ID to observe." + RESET);
                } else {
                    server.observeGame(observeArgs[0]);
                    // state = State.OBSERVING; // Add this in later for functionality
                }
            }

        }

        else if (baseCommand.equals("quit") || baseCommand.equals("q")){
            if (state == State.SIGNEDIN){
                server.Logout(sessionToken);
            }

            return;
        }
        // Unknown Command Handling
        else {
            System.out.println(RED + "Unknown command. Type 'help' for a list of commands." + RESET);
        }

    }


}
