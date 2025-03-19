import chess.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import server.Server;
import ui.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.util.Scanner;

public class Main {

    private static String sessionToken = null;


    public static void main(String[] args) {
        System.out.println("♕ Welcome to Chess. Type \"Help\" for options. ♕");
        Server server = new Server();
        server.run(832);
        PreLogin preLogin = new PreLogin();
        preLogin.run();

        System.out.println("Exiting...");
        System.exit(0);
    }
    // Everything below here should be in it's own class
}