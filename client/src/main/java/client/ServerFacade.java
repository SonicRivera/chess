package client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ui.EscapeSequences;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerFacade {

    private static String serverUrl;

    public ServerFacade(String url){
        serverUrl = url;
    }

    public static boolean Register(String[] info) {
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
                Login(loginInfo);
                return true;

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

        return false;
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

    public static void joinGame(String gameId, String color) {
    }

    public static void listGames() {
    }

    public static void observeGame(String gameId) {
    }



}
