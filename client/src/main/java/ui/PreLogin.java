package ui;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class PreLogin {

    private static String sessionToken = null;
    public static boolean loggedIn;


    // Coloring
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String CYAN = "\u001B[36m";
    private static final String YELLOW = "\u001B[33m";

    public PreLogin(){
        loggedIn = false;
    }

    public void run() {


        Scanner scanner = new Scanner(System.in);
        String command;
        String prefix = RED + "[Logged Out] " + RESET + " >>> ";

        while (!loggedIn) {
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
                    if (Login(loginInfo)){
                        PostLogin postlogin = new PostLogin(sessionToken);
                        postlogin.run();
                        loggedIn = true;
                    }

                }
            }

            else if (command.equalsIgnoreCase("Quit") || command.equalsIgnoreCase("Q")){
                return;
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


    private static boolean Login(String[] info) {
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

}
