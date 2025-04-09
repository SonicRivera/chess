import ui.*;

public class Main {


    public static void main(String[] args) {
        System.out.println("♕ Welcome to Chess. Type \"Help\" for options. ♕");
        var serverUrl = "localhost:8080";
        if (args.length == 1){
            serverUrl = args[0];
        }

        new Repl(serverUrl).run();

        System.out.println("Exiting...");
        System.exit(0);
    }
    // Everything below here should be in it's own class
}