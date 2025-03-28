package client;

import chess.ChessGame;
import chess.ChessPiece;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import chess.ChessBoard;
import ui.EscapeSequences;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ServerFacade {

    private static String serverUrl;
    private static ArrayList<String> gameList = new ArrayList<>(); // To store game IDs for selection


    public ServerFacade(String url){
        serverUrl = url;
    }

    public static String Register(String[] info) {
        try {
            String url = serverUrl + "/user";
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

                // Try getting the authToken from response
                try (InputStream is = connection.getInputStream()) {
                    String response = new String(is.readAllBytes());
                    JsonObject json = JsonParser.parseString(response).getAsJsonObject();
                    return json.get("authToken").getAsString();
                }

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

        return "";
    }


    public static String Login(String[] info) {
        try {
            String url = serverUrl + "/session";
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Create JSON payload
            String payload = String.format("{\"username\":\"%s\",\"password\":\"%s\"}",
                    info[0], info[1]);

            // Send the request
            try (OutputStream os = connection.getOutputStream()) {
                os.write(payload.getBytes());
                os.flush();
            }

            // Read the response
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                System.out.println("Login successful!");

                // Try getting the authToken from response
                try (InputStream is = connection.getInputStream()) {
                    String response = new String(is.readAllBytes());
                    JsonObject json = JsonParser.parseString(response).getAsJsonObject();
                    return json.get("authToken").getAsString(); // Return the session token
                }


            } else {
                try (InputStream is = connection.getErrorStream()) {
                    String error = new String(is.readAllBytes());
                    JsonObject json = JsonParser.parseString(error).getAsJsonObject();
                    System.out.println(json.get("message").getAsString());
                    return "";
                }

            }
        } catch (Exception e) {
            System.out.println("Error during login: " + e.getMessage());
        }

        return "";
    }

    public static boolean Logout(String sessionToken) {
        try {
            if (sessionToken == null) {
                System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + "No session token, please log in.");
                return false;
            }



            String url = serverUrl + "/session";
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Authorization", sessionToken); // Include the token

            // Read the response
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                System.out.println("Logout successful!");
                return true;

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

        return false;
    }


    public static void createGame(String gameName, String sessionToken) {
        try {

            String url = serverUrl + "/game";
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", sessionToken); // Include the token
            connection.setDoOutput(true);

            // Create JSON payload
            String payload = String.format("{\"gameName\":\"%s\"}", gameName);

            // Send the request
            try (OutputStream os = connection.getOutputStream()) {
                os.write(payload.getBytes());
                os.flush();
            }

            // Read the response
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                System.out.println("Successfully created game " + gameName);

            } else {
                try (InputStream is = connection.getErrorStream()) {
                    String error = new String(is.readAllBytes());
                    JsonObject json = JsonParser.parseString(error).getAsJsonObject();
                    System.out.println("Error during creation: " + json.get("message").getAsString());
                }
            }
        } catch (Exception e) {
            System.out.println("Error during creation: " + e.getMessage());
        }

    }

    public static boolean joinGame(String gameId, String color, String sessionToken) {
        int game = Integer.parseInt(gameId);

        try {
            if (sessionToken == null || sessionToken.isEmpty()) {
                System.out.println("Error: No session token found. Please log in first.");
                return false;
            }

            String url = serverUrl + "/game";
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", sessionToken); // Include the token
            connection.setDoOutput(true);

            // Create JSON payload

            String payload = String.format("{\"gameID\":\"%s\",\"playerColor\":\"%s\"}", gameList.get(game - 1), color);
    
            // Send the request
            try (OutputStream os = connection.getOutputStream()) {
                os.write(payload.getBytes());
                os.flush();
            }
    
            // Read the response
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                System.out.println("COLOR IS: " + color);
                if (color.equals("WHITE")){
                    printGame(new ChessGame().getBoard(), true);
                } else {
                    printGame(new ChessGame().getBoard(), false);
                }

                return true;
            } else {
                try (InputStream is = connection.getErrorStream()) {
                    String error = new String(is.readAllBytes());
                    JsonObject json = JsonParser.parseString(error).getAsJsonObject();
                    System.out.println("Error during joining game: " + json.get("message").getAsString());
                }
            }
        } catch (Exception e) {
            System.out.println("Error during joining game: " + e.getMessage());
        }

        return false;
    }

    public static void listGames(String sessionToken) {
        gameList.clear();
        try {
            String url = serverUrl + "/game";
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", sessionToken); // Include the token
    
            // Read the response
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                try (InputStream is = connection.getInputStream()) {
                    String response = new String(is.readAllBytes());
                    JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
    
                    // Assuming the server returns a JSON array of games under the key "games"
                    if (jsonResponse.has("games")) {

                    for (int i = 0; i < jsonResponse.getAsJsonArray("games").size(); i++) {
                        JsonObject game = jsonResponse.getAsJsonArray("games").get(i).getAsJsonObject();
                        String gameId = game.get("gameID").getAsString(); // Store the game ID
                        String gameName = game.get("gameName").getAsString();
                        String whitePlayer = game.has("whiteUsername") ? game.get("whiteUsername").getAsString() : "None";
                        String blackPlayer = game.has("blackUsername") ? game.get("blackUsername").getAsString() : "None";

                        // Add the game ID to the list
                        gameList.add(gameId);

                        // Print the game details in a numbered list
                        System.out.printf("%d. %s (White: %s, Black: %s)%n", i + 1, gameName, whitePlayer, blackPlayer);
                    }
                    } else {
                        System.out.println("No games available.");
                    }
                }
            } else {
                try (InputStream is = connection.getErrorStream()) {
                    String error = new String(is.readAllBytes());
                    JsonObject json = JsonParser.parseString(error).getAsJsonObject();
                    System.out.println("Error: " + json.get("message").getAsString());
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void observeGame(String gameId) {
        new ChessGame().getBoard().printBoard();
    }

    private static void printGame(ChessBoard board, boolean white){
        String color;


        // White Board

        if (white){
            System.out.println("Printing white board");
            for (int i = 7; i >= 0; i--) {
                for (int j = 0; j < 8; j++) {
                    if ((i + j) % 2 == 0){
                        color = "\u001b[100m";
                    } else {
                        color = "\u001b[47m";
                    }


                    ChessPiece piece = board.board[i][j];
                    if (piece == null) {
                        System.out.print(color + " \u2003 " + "\u001b[0m");
                    } else {
                        System.out.print(color + piece.getSymbol() + "\u001b[0m");
                    }
                }
                System.out.println();
            }
            System.out.println();
        } else {
            // Black Board
            System.out.println("Printing black board");

            for (int i = 0; i <= 7; i++) {
                for (int j = 0; j < 8; j++) {
                    if ((i + j) % 2 == 0){
                        color = "\u001b[100m";
                    } else {
                        color = "\u001b[47m";
                    }

                    ChessPiece piece = board.board[i][j];
                    if (piece == null) {
                        System.out.print(color + " \u2003 " + "\u001b[0m");
                    } else {
                        System.out.print(color + piece.getSymbol() + "\u001b[0m");
                    }
                }
                System.out.println();
            }
            System.out.println();
            }
        }
}
