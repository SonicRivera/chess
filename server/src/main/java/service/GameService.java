package service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.AuthDAO;
import model.GameData;
import model.AuthData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import chess.ChessGame;

import com.google.gson.JsonObject;

public class GameService {
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;
    private final Gson gson = new Gson();

    public GameService(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

        // List all games
    public String listGames(String authToken) throws DataAccessException {
        if (authDAO.getAuth(authToken) == null) {
            throw new DataAccessException("401 Error: unauthorized");
        }
        List<GameData> gamesList = new ArrayList<>(gameDAO.listGames().values());

        JsonArray gamesArray = new JsonArray();
        for (GameData game : gamesList) {
            JsonObject gameObject = new JsonObject();
            gameObject.addProperty("gameID", game.gameID());
            gameObject.addProperty("whiteUsername", game.whiteUsername());
            gameObject.addProperty("blackUsername", game.blackUsername());
            gameObject.addProperty("gameName", game.gameName());
            gameObject.addProperty("chessGame", gson.toJson(game.game()));
            gamesArray.add(gameObject);
        }

        JsonObject result = new JsonObject();
        result.add("games", gamesArray);

        return gson.toJson(result);
    }

    // Create a new game
    public int createGame(String authToken, String gameName) throws DataAccessException {
        if (authToken == null || authToken.isEmpty()) {
            throw new DataAccessException("401: Error: unauthorized");
        }

        AuthData auth = authDAO.getAuth(authToken);

        if (auth == null) {
            throw new DataAccessException("401: Error: unauthorized");
        }

        if (gameName == null || gameName.isEmpty()) {
            throw new DataAccessException("400: Error: bad request");
        }

        GameData newGame = new GameData(0, null, null, gameName, new ChessGame());
        return gameDAO.createGame(newGame);
    }

    // Join a game
    public void joinGame(String authToken, int gameID, String playerColor) throws DataAccessException {
        AuthData auth = authDAO.getAuth(authToken);
        if (auth == null) {
            throw new DataAccessException("401 Error: unauthorized");
        }

        GameData game = gameDAO.getGame(gameID);
        if (game == null) {
            throw new DataAccessException("400 Error: bad request");
        }

        if (!Objects.equals(playerColor, "WHITE") && !Objects.equals(playerColor, "BLACK")){
            throw new DataAccessException("400 Error: bad request");
        } else if ("WHITE".equals(playerColor) && game.whiteUsername() == null) {
            GameData newData = new GameData(gameID, auth.username(), game.blackUsername(), game.gameName(), game.game());
            gameDAO.updateGame(newData);
        } else if ("BLACK".equals(playerColor) && game.blackUsername() == null) {
            GameData newData = new GameData(gameID, game.whiteUsername(), auth.username(), game.gameName(), game.game());
            gameDAO.updateGame(newData);
        } else {
            throw new DataAccessException("403 Error: already taken");
        }
    }

    public void clear() throws DataAccessException {
        gameDAO.clear();
    }

    public GameData getGameData(String authToken, int gameID) throws DataAccessException {
        try {
            authDAO.getAuth(authToken);
        } catch (DataAccessException e) {
            throw new DataAccessException("401: Unauthorized");
        }

        try {
            return gameDAO.getGame(gameID);
        } catch (DataAccessException e) {
            throw new DataAccessException("400: Bad Request");
        }
    }

    public void updateGame(GameData data) throws DataAccessException {
        gameDAO.updateGame(data);

    }
}