package ui;

import java.util.Scanner;

public class Repl {

    private final ChessClient client;

    // Colors
    private String RED = EscapeSequences.SET_TEXT_COLOR_RED;
    private String RESET = EscapeSequences.RESET_TEXT_COLOR;

    public Repl(String serverUrl){
        client = new ChessClient(serverUrl, this);
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        String command = "";
        String prefix = RED + "[Logged Out] " + RESET + " >>> ";



        while (!command.equalsIgnoreCase("Quit") && !command.equalsIgnoreCase("Q")) {
            System.out.print(prefix);
            command = scanner.nextLine();

            client.eval(command);

            if (command.equalsIgnoreCase("Quit") || command.equalsIgnoreCase("Q")){
                return;
            }

        }


    }

}
