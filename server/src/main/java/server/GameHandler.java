package server;

import com.google.gson.JsonObject;
import dataaccess.DataAccessException;
import service.GameService;
import model.GameData;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.List;

public class GameHandler {
    GameService gameService;

    public GameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    // List Games Handler
    public Route listGames = (Request req, Response res) -> {
        String authToken = req.headers("authorization");

        try {
            List<GameData> games = gameService.listGames(authToken);
            res.status(200);
            return JsonUtil.toJson(games);
        } catch (DataAccessException e) {
            res.status(401);
            return JsonUtil.toJson(new ErrorResponse(e.getMessage()));
        }
    };

    // Create Game Handler
    public Route createGame = (Request req, Response res) -> {
        String authToken = req.headers("authorization"); // Extract authToken

        JsonObject body = JsonUtil.fromJson(req.body(), JsonObject.class);
        String gameName = body.has("gameName") ? body.get("gameName").getAsString() : null;


        try {
            int gameID = gameService.createGame(authToken, gameName);
            res.status(200);
            return JsonUtil.toJson(new GameIDResponse(gameID));
        } catch (DataAccessException e) {
            String message = e.getMessage();
            if (message.startsWith("400")) {
                res.status(400);
            } else if (message.startsWith("401")) {
                res.status(401);
            } else {
                res.status(500);
            }
            return JsonUtil.toJson(new ErrorResponse(message.substring(4))); // Remove error code prefix
        }
    };

    // Join Game Handler
    public Route joinGame = (Request req, Response res) -> {
        String authToken = req.headers("authorization");
        JsonObject body = JsonUtil.fromJson(req.body(), JsonObject.class);
        String playerColor = body.get("playerColor").getAsString();
        int gameID = body.get("gameID").getAsInt();

        try {
            gameService.joinGame(authToken, gameID, playerColor);
            res.status(200);
            return "{}";
        } catch (DataAccessException e) {
            String message = e.getMessage();
            if (message.startsWith("400")) {
                res.status(400);
            } else if (message.startsWith("401")) {
                res.status(401);
            } else if (message.startsWith("403")) {
                res.status(403);
            } else {
                res.status(500);
            }
            return JsonUtil.toJson(new ErrorResponse(message.substring(4))); // Remove error code prefix
        }
    };

    private record GameIDResponse(int gameID) {}
    private record ErrorResponse(String message) {}

}
