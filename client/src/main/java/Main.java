import chess.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import server.Server;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("♕ Welcome to Chess. Type \"Help\" for options. ♕");
        Server server = new Server();
        server.run(832);
        PreLogin();
        System.exit(0);
    }

    // This should be in it's own class

    private static void PreLogin() {
        // Coloring
        final String RESET = "\u001B[0m";
        final String RED = "\u001B[31m";
        final String CYAN = "\u001B[36m";
        final String YELLOW = "\u001B[33m";

        Scanner scanner = new Scanner(System.in);
        String command = "";
        String prefix = RED + "[Logged Out] " + RESET + " >>> ";

        while (!command.equalsIgnoreCase("Quit") && !command.equalsIgnoreCase("Q")) {
            System.out.print(prefix);
            command = scanner.nextLine();

            // Help command
            if (command.equalsIgnoreCase("Help") || command.equalsIgnoreCase("H")) {
                System.out.println(CYAN + "register " + YELLOW + "<USERNAME> <PASSWORD> <EMAIL>" + RESET + " - account creation");
                System.out.println(CYAN + "login " + YELLOW + "<USERNAME> <PASSWORD>" + RESET + " - to play chess");
                System.out.println(CYAN + "quit" + RESET + " - quit playing chess");
                System.out.println(CYAN + "help" + RESET + " - Show possible commands");
            }

            // Register command
            else if (command.toLowerCase().startsWith("register")) {
                String[] info = command.split("\\s+");
                if (info.length < 4) {
                    System.out.println(RED + "Missing a username, password, and/or an email");
                } else if (info.length > 4) {
                    System.out.println(RED + "Please only input a username, password, and an email.");
                } else {
                    String[] registerInfo = new String[3];
                    System.arraycopy(info, 1, registerInfo, 0, 3);
                    Register(registerInfo);
                }
            }

            // Login command
            else if (command.toLowerCase().startsWith("login")) {
                String[] info = command.split("\\s+");
                if (info.length < 3) {
                    System.out.println(RED + "Missing either a username or a password.");
                } else if (info.length > 3) {
                    System.out.println(RED + "Please only input a username and password.");
                } else {
                    String[] loginInfo = new String[2];
                    System.arraycopy(info, 1, loginInfo, 0, 2);
                    Login(loginInfo);
                    break; // REMOVE THIS ONCE IMPLEMENTED
                }
            }

            else if (command.equalsIgnoreCase("Quit") || command.equalsIgnoreCase("Q")){
                break;
            }

            // Unknown Command Handling
            else {
                System.out.println(RED + "Unknown command. Type 'help' for a list of commands." + RESET);
            }

        }
    }

    private static void Register(String[] info) {
    try {
        String url = "http://localhost:832/user";
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        // Create JSON payload
        String payload = String.format("{\"username\":\"%s\",\"password\":\"%s\",\"email\":\"%s\"}",
                info[0], info[1], info[2]);

        // Send the request
        try (OutputStream os = connection.getOutputStream()) {
            os.write(payload.getBytes());
            os.flush();
        }

        // Read the response
        int responseCode = connection.getResponseCode();
        if (responseCode == 200) {
            System.out.println("Registration successful!");
            String[] loginInfo = new String[2];
            System.arraycopy(info, 0, loginInfo, 0, 2);
            Login(loginInfo);
            System.exit(0); // There might be a better way to do this...

        } else {
            try (InputStream is = connection.getErrorStream()) {
                String error = new String(is.readAllBytes());
                JsonObject json = JsonParser.parseString(error).getAsJsonObject();
                System.out.println(json.get("message").getAsString());
            }
        }
    } catch (Exception e) {
        System.out.println("Error during registration: " + e.getMessage());
    }
}

    private static void Login(String[] info) {
        for (String word : info) {
            System.out.println(word);
        }
        PostLogin();
    }


    // Everything below here should be in it's own class

    private static void PostLogin() {
        // Coloring
        final String RESET = "\u001B[0m";
        final String RED = "\u001B[31m";
        final String CYAN = "\u001B[36m";
        final String YELLOW = "\u001B[33m";

        Scanner scanner = new Scanner(System.in);
        String command = "";
        String prefix = RED + "[Logged In] " + RESET + " >>> ";

        while (true) {
            System.out.print(prefix);
            command = scanner.nextLine().trim();

            // Separate the base command and the arguments
            String[] parts = command.split("\\s+", 2);
            String baseCommand = parts[0].toLowerCase();
            String arguments = (parts.length > 1) ? parts[1] : "";

            // Help
            if (baseCommand.equals("help") || baseCommand.equals("h")) {
                System.out.println(CYAN + "help" + RESET + " - Show possible commands");
                System.out.println(CYAN + "logout" + RESET + " - Logs you out");
                System.out.println(CYAN + "create " + YELLOW + "<NAME>" + RESET + " - creates a game of chess");
                System.out.println(CYAN + "list" + RESET + " - lists games");
                System.out.println(CYAN + "join " + YELLOW + "<ID> [WHITE | BLACK]" + RESET + " - joins specified chess game");
                System.out.println(CYAN + "observe " + YELLOW + "<ID>" + RESET + " - watch a game of chess");
            }

            // Logout
            else if (baseCommand.equals("logout")) {
                System.out.println(YELLOW + "Logging out..." + RESET);
                Logout();
                break;
            }

            // Create
            else if (baseCommand.equals("create")) {
                if (arguments.isEmpty()) {
                    System.out.println(RED + "You must provide a game name." + RESET);
                } else {
                    createGame(arguments);
                }
            }

            // List
            else if (baseCommand.equals("list")) {
                listGames();
            }

            // Join
            else if (baseCommand.equals("join")) {
                String[] joinArgs = arguments.split("\\s+");
                if (joinArgs.length < 1 || joinArgs[0].isEmpty()) {
                    System.out.println(RED + "Please specify the game ID to join." + RESET);
                } else {
                    String gameId = joinArgs[0];
                    String color = (joinArgs.length >= 2) ? joinArgs[1].toUpperCase() : "ANY";
                    joinGame(gameId, color);
                }
            }

            // Observe
            else if (baseCommand.equals("observe")) {
                String[] observeArgs = arguments.split("\\s+");
                if (observeArgs.length != 1 || observeArgs[0].isEmpty()) {
                    System.out.println(RED + "Please specify the game ID to observe." + RESET);
                } else {
                    observeGame(observeArgs[0]);
                }
            } else if (command.equalsIgnoreCase("Quit") || command.equalsIgnoreCase("Q")) {
                Logout();
                break;
            }

            // Unknown Command Handling
            else {
                System.out.println(RED + "Unknown command. Type 'help' for a list of commands." + RESET);
            }
        }
    }

    private static void Logout() {

    }


    private static void createGame(String gameName) {
    }

    private static void joinGame(String gameId, String color) {
    }

    private static void listGames() {
    }

    private static void observeGame(String gameId) {
    }


}