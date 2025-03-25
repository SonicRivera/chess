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

    // Colors
    private static String RED = EscapeSequences.SET_TEXT_COLOR_RED;
    private String BLUE = EscapeSequences.SET_TEXT_COLOR_BLUE;
    private String YELLOW = EscapeSequences.SET_TEXT_COLOR_YELLOW;
    private String RESET = EscapeSequences.RESET_TEXT_COLOR;

    public PostLogin(String sessionToken){
        PostLogin.sessionToken = sessionToken;
        loggedIn = true;
    }




    public void run() {


        Scanner scanner = new Scanner(System.in);
        String command;
        String prefix = RED + "[Logged In] " + RESET + " >>> ";

        while (loggedIn) {
            System.out.print(prefix);
            command = scanner.nextLine().trim();

            // Separate the base command and the arguments
            String[] parts = command.split("\\s+", 2);
            String baseCommand = parts[0].toLowerCase();
            String arguments = (parts.length > 1) ? parts[1] : "";


        }

    }


}
