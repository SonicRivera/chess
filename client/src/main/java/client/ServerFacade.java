package client;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import ui.EscapeSequences;


public class ServerFacade {

    private String serverUrl;
    private LinkedHashMap<String, ChessGame> gameList = new LinkedHashMap<>(); // To store gameID as key and ChessGame as value


    public ServerFacade(String url){
        serverUrl = url;
    }

    public String register(String[] info) {
        try {
            String url = "http://" + serverUrl + "/user";
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


    public String login(String[] info) {
        try {
            String url = "http://" + serverUrl + "/session";
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

    public boolean logout(String sessionToken) {
        try {
            if (sessionToken == null) {
                System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + "No session token, please log in.");
                return false;
            }



            String url = "http://" + serverUrl + "/session";
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


    public boolean createGame(String gameName, String sessionToken) {
        try {

            String url = "http://" + serverUrl + "/game";
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

    public Map<String, Object> joinGame(String gameId, String color, String sessionToken) {
        Map<String, Object> data = new HashMap<>();

        int game = Integer.parseInt(gameId);

        if (game < 1 || (game > gameList.size())) {
            System.out.println("Please enter an ID from the list.");
            listGames(sessionToken);
            data.put("bool", false);
            data.put("chessGame", null);
            data.put("gameID", null);
            return data;
        }



        try {
            if (sessionToken == null || sessionToken.isEmpty()) {
                System.out.println("Error: No session token found. Please log in first.");
                data.put("bool", false);
                data.put("chessGame", null);
                data.put("gameID", null);
                return data;
            }

            String url = "http://" + serverUrl + "/game";
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", sessionToken); // Include the token
            connection.setDoOutput(true);

            // Create JSON payload

            String gameKey = new ArrayList<>(gameList.keySet()).get(game - 1);
            String payload = String.format("{\"gameID\":\"%s\",\"playerColor\":\"%s\"}", gameKey, color);
    
            // Send the request
            try (OutputStream os = connection.getOutputStream()) {
                os.write(payload.getBytes());
                os.flush();
            }
    
            // Read the response
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                if (color.equals("WHITE")){
                    ChessGame chessGame = gameList.get(gameKey);
                    data.put("bool", true);
                    data.put("chessGame", chessGame);
                    data.put("gameID", gameKey);
                    data.put("color", true);
                    return data;
                } else {
                    ChessGame chessGame = gameList.get(gameKey);
                    data.put("bool", true);
                    data.put("chessGame", chessGame);
                    data.put("gameID", gameKey);
                    data.put("color", false);
                    return data;
                }

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

        data.put("bool", false);
        data.put("chessGame", null);
        data.put("gameID", null);
        return data;
    }

    public boolean listGames(String sessionToken) {
        gameList.clear();
    
        try {
            String url = "http://" + serverUrl + "/game";
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
                    ChessGame gameData = new Gson().fromJson(game.get("chessGame").getAsString(), ChessGame.class);
    
                    // Add the game ID to the list
                    gameList.put(gameId, gameData);
    
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
    
    private void handleErrorResponse(HttpURLConnection connection) {
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

    public Map<String, Object> observeGame(String gameId) {
        Map<String, Object> data = new HashMap<>();
        int gameIdInt = Integer.parseInt(gameId);
        String gameKey = new ArrayList<>(gameList.keySet()).get(gameIdInt - 1);
        ChessGame chessGame = gameList.get(gameKey);
        data.put("chessGame", chessGame);
        data.put("gameID", gameKey);
        return data;
    }





    public void clear(){
        try {
            String url ="http://" +  serverUrl + "/db";
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
