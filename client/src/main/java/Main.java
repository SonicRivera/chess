import chess.*;
import server.Server;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("♕ Welcome to Chess. Type \"Help\" for options. ♕");
        PreLogin();
        Server server = new Server();
        server.run(832);
    }

    private static void PreLogin() {
        // Coloring
        final String RESET = "\u001B[0m";
        final String RED = "\u001B[31m";
        final String CYAN = "\u001B[36m";
        final String YELLOW = "\u001B[33m";

        Scanner scanner = new Scanner(System.in);
        String command = "";
        String prefix = RED + "[Logged Out] " + RESET + " >>> ";

        while (!command.equalsIgnoreCase("Quit") && !command.equalsIgnoreCase("Q")) {
            System.out.print(prefix);
            command = scanner.nextLine();

            if (command.equalsIgnoreCase("Help") || command.equalsIgnoreCase("H")) {
                System.out.println(CYAN + "register " + YELLOW  + "<USERNAME> <PASSWORD> <EMAIL>" + RESET + " - account creation" );
                System.out.println(CYAN + "login " + YELLOW  + "<USERNAME> <PASSWORD>" + RESET + " - to play chess");
                System.out.println(CYAN + "quit" + RESET + " - quit playing chess");
                System.out.println(CYAN + "help" + RESET + " - Show possible commands");
            }

            if (command.toLowerCase().startsWith("register")){
                String[] info = command.split("\\s+");
                if (info.length < 4) {
                    System.out.println("Hey you need to have a username, password, and an email");
                } else if (info.length > 4){
                    System.out.println("Hey you just need to put a username, password, and an email.");
                } else {
                    String[] registerInfo = new String[3];
                    System.arraycopy(info, 1, registerInfo, 0, 3);
                    Register(registerInfo);
                }
            }

        }
    }

    private static void Register(String[] info){

    }

    private static void Login(String[] info){

    }

}