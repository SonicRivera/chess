import chess.*;
import client.ServerFacade;
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
//        Server server = new Server();
//        server.run(832);
        ServerFacade server = new ServerFacade();
        PreLogin preLogin = new PreLogin(server);
        preLogin.run();

        System.out.println("Exiting...");
        System.exit(0);
    }
}