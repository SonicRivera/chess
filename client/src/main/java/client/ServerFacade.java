package client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ui.Repl;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerFacade {

    private static String serverUrl;

    public ServerFacade(String url){
        serverUrl = url;
    }

    public static void Register(String[] info) {
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


    public static boolean Login(String[] info) {
        try {
            String url = "http://localhost:832/session";
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
                loggedIn = true;

                // Try getting the authToken from response
                try (InputStream is = connection.getInputStream()) {
                    String response = new String(is.readAllBytes());
                    JsonObject json = JsonParser.parseString(response).getAsJsonObject();
                    sessionToken = json.get("authToken").getAsString(); // Store the token
                    return true;
                }


            } else {
                try (InputStream is = connection.getErrorStream()) {
                    String error = new String(is.readAllBytes());
                    JsonObject json = JsonParser.parseString(error).getAsJsonObject();
                    System.out.println(json.get("message").getAsString());
                    return false;
                }

            }
        } catch (Exception e) {
            System.out.println("Error during login: " + e.getMessage());
        }

        return false;
    }

    public static void Logout(boolean quit) {
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
                Repl preLogin = new Repl();
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


    public static void createGame(String gameName) {
    }

    public static void joinGame(String gameId, String color) {
    }

    public static void listGames() {
    }

    public static void observeGame(String gameId) {
    }



}
