package ui;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class PostLogin {

    private static String sessionToken;
    private static boolean loggedIn;

    // Coloring
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String CYAN = "\u001B[36m";
    private static final String YELLOW = "\u001B[33m";

    public PostLogin(String sessionToken){
        PostLogin.sessionToken = sessionToken;
        loggedIn = true;
    }




    public void run() {


        Scanner scanner = new Scanner(System.in);
        String command = "";
        String prefix = RED + "[Logged In] " + RESET + " >>> ";

        while (loggedIn) {
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
                Logout(false);
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
                Logout(true);
                return;
            }

            // Unknown Command Handling
            else {
                System.out.println(RED + "Unknown command. Type 'help' for a list of commands." + RESET);
            }
        }

        PreLogin preLogin = new PreLogin();
        preLogin.run();

    }

    private static void Logout(boolean quit) {
        try {
            if (sessionToken == null) {
                System.out.println(RED + "No session token, please log in.");
                return;
            }



            String url = "http://localhost:832/session";
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Authorization", sessionToken); // Include the token

            // Read the response
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                System.out.println("Logout successful!");
                sessionToken = null;
                PreLogin preLogin = new PreLogin();
                loggedIn = false;
                if (quit){
                    return;
                }
                preLogin.run();

            } else {
                try (InputStream is = connection.getErrorStream()) {
                    String error = new String(is.readAllBytes());
                    JsonObject json = JsonParser.parseString(error).getAsJsonObject();
                    System.out.println("Error during logout: " + json.get("message").getAsString());
                }
            }
        } catch (Exception e) {
            System.out.println("Error during logout: " + e.getMessage());
        }
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
