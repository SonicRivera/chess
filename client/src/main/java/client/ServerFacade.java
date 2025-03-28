package client;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import ui.EscapeSequences;

public class ServerFacade {

    private static String serverUrl;
    private static ArrayList<String> gameList = new ArrayList<>(); // To store game IDs for selection


    public ServerFacade(String url){
        serverUrl = url;
    }

    public static String register(String[] info) {
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


    public static String login(String[] info) {
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

    public static boolean logout(String sessionToken) {
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


    public static boolean createGame(String gameName, String sessionToken) {
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
                return true;

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

        return false;

    }

    public static boolean joinGame(String gameId, String color, String sessionToken) {
        int game = Integer.parseInt(gameId);

        if (game < 1 || (game > gameList.size())) {
            System.out.println("Please enter an ID from the list.");
            listGames(sessionToken);
            return false;
        }



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

    public static boolean listGames(String sessionToken) {
        gameList.clear();
    
        try {
            String url = serverUrl + "/game";
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", sessionToken); // Include the token
    
            // Read the response code
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                handleErrorResponse(connection);
                return false;
            }
    
            // Parse the response
            try (InputStream is = connection.getInputStream()) {
                String response = new String(is.readAllBytes());
                JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
    
                // Check if the "games" key exists
                if (!jsonResponse.has("games")) {
                    System.out.println("No games available.");
                    return false;
                }
    
                // Process the games array
                jsonResponse.getAsJsonArray("games").forEach(gameElement -> {
                    JsonObject game = gameElement.getAsJsonObject();
                    String gameId = game.get("gameID").getAsString();
                    String gameName = game.get("gameName").getAsString();
                    String whitePlayer = game.has("whiteUsername") ? game.get("whiteUsername").getAsString() : "None";
                    String blackPlayer = game.has("blackUsername") ? game.get("blackUsername").getAsString() : "None";
    
                    // Add the game ID to the list
                    gameList.add(gameId);
    
                    // Print the game details
                    System.out.printf("%d. %s (White: %s, Black: %s)%n", gameList.size(), gameName, whitePlayer, blackPlayer);
                });
            }

            if (gameList.isEmpty()) {
                System.out.println("No active games... You should create one!");
            }
    
            return true;
    
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }
    
    private static void handleErrorResponse(HttpURLConnection connection) {
        try (InputStream is = connection.getErrorStream()) {
            if (is != null) {
                String error = new String(is.readAllBytes());
                JsonObject json = JsonParser.parseString(error).getAsJsonObject();
                System.out.println("Error: " + json.get("message").getAsString());
            }
        } catch (Exception e) {
            System.out.println("Error reading error response: " + e.getMessage());
        }
    }

    public static void observeGame(String gameId) {
        printGame(new ChessGame().getBoard(), true);
    }

    private static void printGame(ChessBoard board, boolean white){
        String color;
        String columnLabels = white ? "\u001b[100m a  b  c  d  e  f  g  h    \u001b[0m" : "\u001b[100m h  g  f  e  d  c  b  a    \u001b[0m";
        System.out.println(columnLabels);

        for (int i = (white ? 7 : 0); (white ? i >= 0 : i <= 7); i += (white ? -1 : 1)) {
                for (int j = (white ? 0 : 7); (white ? j < 8 : j >= 0); j+= (white ? 1 : -1)) {
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



    public void clear(){
        try {
            String url = serverUrl + "/db";
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("DELETE");

            // Get the response code
            int responseCode = connection.getResponseCode();

            // Check if the response code is 200
            if (responseCode == 200) {
                System.out.println("Request was successful!");
            }
        } catch (Exception e) {
            System.out.println("Ruh Roh Raggy...");
        }
    }
}
