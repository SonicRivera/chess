package ui;

import java.util.Scanner;

public class Repl {

    private final ChessClient client;

    public Repl(String serverUrl){
        client = new ChessClient(serverUrl);
    }

    public void run() {

        // Colors
        String RED = EscapeSequences.SET_TEXT_COLOR_RED;
        String RESET = EscapeSequences.RESET_TEXT_COLOR;

        Scanner scanner = new Scanner(System.in);
        String command = "";
        String prefix;


        while (!command.equalsIgnoreCase("Quit") && !command.equalsIgnoreCase("Q")) {

            if (client.state == State.SIGNEDOUT){
                prefix = RED + "[Logged Out] " + RESET + " >>> ";
            } else {
                prefix = RED + "[Logged In] " + RESET + " >>> ";
            }
            System.out.print(prefix);
            command = scanner.nextLine();

            client.eval(command);

            if (command.equalsIgnoreCase("Quit") || command.equalsIgnoreCase("Q")){
                return;
            }
        }
    }
}
