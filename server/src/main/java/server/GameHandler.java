package server;

import service.GameService;
import model.GameData;
import spark.Request;
import spark.Response;
import spark.Route;

public class GameHandler {
    private final GameService gameService;

    public GameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    // List Games
    public Route listGames = (Request req, Response res) -> {
        return "{}";
    };

    // Create Game
    public Route createGame = (Request req, Response res) -> {
        return "{}";
    };

    // Join Game
    public Route joinGame = (Request req, Response res) -> {
        return "{}";
    };

}
