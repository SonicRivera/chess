package ui;


import chess.*;
import client.ServerFacade;
import client.WebSocketClient;
import com.google.gson.Gson;
import websocket.commands.UserGameCommand;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


import websocket.commands.*;

public class ChessClient {

    private final ServerFacade server;
    public State state = State.SIGNEDOUT;
    private String authToken = null;
    private String serverURL;
    ChessGame chessGame;
    WebSocketClient ws;
    int dbGameID;
    boolean playerColor;



    // Colors
    String redText = EscapeSequences.SET_TEXT_COLOR_RED;
    String blueText = EscapeSequences.SET_TEXT_COLOR_BLUE;
    String yellowText = EscapeSequences.SET_TEXT_COLOR_YELLOW;
    String resetText = EscapeSequences.RESET_TEXT_COLOR;




    public ChessClient(String serverUrl){
        this.serverURL = serverUrl;
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
            case PLAYING:
                handlePlayingCommands(parsedCommand);
                break;
            case OBSERVING:
                handleObservingCommands(parsedCommand);
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
                server.listGames(authToken);
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

    private void handlePlayingCommands(Command command) {
        String baseCommand = command.baseCommand;
        String arguments = command.arguments;

        switch (baseCommand) {
            case "help":
            case "h":
                printPlayingHelp();
                break;
            case "redraw":
                handleRedrawCommand();
                break;
            case "leave":
                handleLeaveCommand();
                break;
            case "move":
                handleMoveCommand(arguments);
                break;
            case "resign":
                handleResignCommand();
                break;
            case "moves":
                handleLegalMovesCommand();
                break;
            default:
                handleUnknownCommand();
        }
    }

    private void handleObservingCommands(Command command) {
        String baseCommand = command.baseCommand;
        String arguments = command.arguments;

        switch (baseCommand) {
            case "help":
            case "h":
                printObservingHelp();
                break;
            case "redraw":
                handleRedrawCommand();
                break;
            case "leave":
                handleLeaveCommand();
                break;
            case "moves":
                handleLegalMovesCommand();
                break;
            default:
                handleUnknownCommand();
        }
    }

    private void printObservingHelp() {
        System.out.println(blueText + "help" + resetText + " - Show possible commands");
        System.out.println(blueText + "redraw" + resetText + " - Redraws the chess board");
        System.out.println(blueText + "leave" + resetText + " - Stops observing the current game");
        System.out.println(blueText + "moves " + yellowText + "<PIECE>" + resetText + " - Highlights legal moves for a piece");
    }

    private void handleLegalMovesCommand() {
    }

    private void handleResignCommand() {
        System.out.println("Are you sure you want to resign? (Y/N)");
        Scanner scanner = new Scanner(System.in);
        String answer = scanner.nextLine();
        while (!(answer.equalsIgnoreCase("Y") || answer.equalsIgnoreCase("N"))) {
            System.out.println("Please enter a Y for yes or a N for no.");
            answer = scanner.nextLine();
        }
        if (answer.equalsIgnoreCase("Y")){
            resign(dbGameID);
        } else {
            System.out.println("Did not resign");
        }


    }

    private void handleMoveCommand(String arguments) {
        String[] movArgs = arguments.split("\\s+");
        if (movArgs.length >= 2 && movArgs[0].matches("[a-h][1-8]") && movArgs[1].matches("[a-h][1-8]")) {
            ChessPosition from = new ChessPosition(movArgs[0].charAt(1) - '0', movArgs[0].charAt(0) - ('a'-1));
            ChessPosition to = new ChessPosition(movArgs[1].charAt(1) - '0',movArgs[1].charAt(0) - ('a'-1));

            ChessPiece.PieceType promotion = null;
            if (movArgs.length == 3) {
                promotion = getPieceType(movArgs[2]);
                if (promotion == null) {
                    System.out.println("Invalid promotion piece name (ex: 'knight')");
                    System.out.println(blueText + "move " + yellowText + "<FROM> <TO> [PROMOTION PIECE]" + resetText +
                            " - Makes a move (e.g., move e2 e4) and selects promotion piece (if necessary)");
                }
            }

            makeMove(dbGameID, new ChessMove(from, to, promotion));
        }
        else {
            System.out.println("Please provide a to and from coordinate (ex: 'c3 d5')");
            System.out.println(blueText + "move " + yellowText + "<FROM> <TO> [PROMOTION PIECE]" + resetText +
                    " - Makes a move (e.g., move e2 e4) and selects promotion piece (if necessary)");
        }
    }

    private ChessPiece.PieceType getPieceType(String name) {
        return switch (name.toUpperCase()) {
            case "QUEEN" -> ChessPiece.PieceType.QUEEN;
            case "BISHOP" -> ChessPiece.PieceType.BISHOP;
            case "KNIGHT" -> ChessPiece.PieceType.KNIGHT;
            case "ROOK" -> ChessPiece.PieceType.ROOK;
            case "PAWN" -> ChessPiece.PieceType.PAWN;
            default -> null;
        };
    }

    private void handleLeaveCommand() {
        state = State.SIGNEDIN;
        leave(dbGameID);
    }

    private void handleRedrawCommand() {
        //server.printGame();
    }

    private void printPlayingHelp() {
        System.out.println(blueText + "help" + resetText + " - Show possible commands");
        System.out.println(blueText + "redraw" + resetText + " - Redraws the chess board");
        System.out.println(blueText + "leave" + resetText + " - Leaves the current game");
        System.out.println(blueText + "move " + yellowText + "<FROM> <TO> [PROMOTION PIECE]" + resetText +
                " - Makes a move (e.g., move e2 e4) and selects promotion piece (if necessary)");

        System.out.println(blueText + "resign" + resetText + " - Resigns from the game");
        System.out.println(blueText + "moves " + yellowText + "<PIECE>" + resetText + " - Highlights legal moves for a piece");
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
        resetText + " - account creation");
        System.out.println(blueText + "login " + yellowText + "<USERNAME> <PASSWORD>" +
        resetText + " - to play chess");
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
            authToken = server.register(regArgs);
            if (!authToken.isEmpty()) {
                state = State.SIGNEDIN;
                server.listGames(authToken);
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
            authToken = server.login(loginArgs);
            if (!authToken.isEmpty()){
                state = state.SIGNEDIN;
                server.listGames(authToken);
            }

        }
    }

    private void handleLogoutCommand(){
        System.out.println(yellowText + "Logging out..." + resetText);
        if(server.logout(authToken)){
            authToken = null;
            state = State.SIGNEDOUT;
        }
    }

    private void handleCreateCommand(String arguments){
        if (arguments.isEmpty()) {
            System.out.println(redText + "You must provide a game name." + resetText);
        } else {
            server.createGame(arguments, authToken);
            server.listGames(authToken);
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

        Map<String, Object> data = server.joinGame(gameId, color, authToken);
        Boolean success = (Boolean) data.get("bool");


        if (success) {
            state = State.PLAYING;
            chessGame = (ChessGame) data.get("chessGame");
            dbGameID = Integer.parseInt(data.get("gameID").toString());
            playerColor = getPlayerColor(data);
            connectWS();
            joinPlayer(dbGameID);
        }
    }

    private boolean getPlayerColor(Map<String, Object> data){
        return (Boolean) data.get("color");
    }

    private void handleObserveCommand(String arguments){
        String[] observeArgs = arguments.split("\\s+");
        if (observeArgs.length != 1 || observeArgs[0].isEmpty()) {
            System.out.println(redText + "Please specify the game ID to observe." + resetText);
        } else {
            state = State.OBSERVING;
            connectWS();
            Map<String, Object> data = server.observeGame(observeArgs[0]);
            dbGameID = Integer.parseInt(data.get("gameID").toString());
            chessGame = (ChessGame) data.get("chessGame");
            playerColor = true;
            printGame(chessGame.getBoard());
            joinObserver(dbGameID);


        }
    }

    public void connectWS() {
        try {
            ws = new WebSocketClient(serverURL, this);
        }
        catch (Exception e) {
            System.out.println("Failed to make connection with server");
        }
    }

    public void sendCommand(UserGameCommand command) {
        String message = new Gson().toJson(command);
        ws.sendMessage(message);
    }

    public void joinPlayer(int gameID) {
        sendCommand(new Connect(authToken, gameID));
    }

    public void joinObserver(int gameID) {
        sendCommand(new Connect(authToken, gameID));
    }

    public void makeMove(int gameID, ChessMove move) {
        sendCommand(new MakeMove(authToken, gameID, move));
    }

    public void leave(int gameID) {
        sendCommand(new Leave(authToken, gameID));
    }

    public void resign(int gameID) {
        sendCommand(new Resign(authToken, gameID));
    }

    public void printGame(ChessBoard board){
        
        
        String color;
        String columnLabels = playerColor ? "\u001b[100m a  b  c  d  e  f  g  h    \u001b[0m" : "\u001b[100m h  g  f  e  d  c  b  a    \u001b[0m";
        System.out.println(columnLabels);

        for (int i = (playerColor ? 7 : 0); (playerColor ? i >= 0 : i <= 7); i += (playerColor ? -1 : 1)) {
            for (int j = (playerColor ? 0 : 7); (playerColor ? j < 8 : j >= 0); j+= (playerColor ? 1 : -1)) {
                if ((i + j) % 2 == 0){
                    color = "\u001b[100m";
                } else {
                    color = "\u001b[47m";
                }


                ChessPiece piece = board.board[i][j];
                if (piece == null) {
                    System.out.print(color + "   " + "\u001b[0m");
                } else {
                    System.out.print(color + piece.getSymbol() + "\u001b[0m");
                }
            }
            System.out.println("\u001b[100m " + (i + 1) + " \u001b[0m");
        }

        System.out.println(columnLabels);
        System.out.println();
    }


}
