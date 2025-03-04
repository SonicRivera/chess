package service;

import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.AuthDAO;
import model.GameData;
import model.AuthData;

import java.util.ArrayList;
import java.util.List;

public class GameService {
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public GameService(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    // List all games
    public List<GameData> listGames(String authToken) throws DataAccessException {
        if (authDAO.getAuth(authToken) == null) {
            throw new DataAccessException("401 Error: unauthorized");
        }
        return new ArrayList<>(gameDAO.listGames().values());
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

        GameData newGame = new GameData(0, null, null, gameName, null);
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

        if ("WHITE".equals(playerColor) && game.whiteUsername() == null) {
            gameDAO.createGame(new GameData(gameID, auth.username(), game.blackUsername(), game.gameName(), game.game()));
        } else if ("BLACK".equals(playerColor) && game.blackUsername() == null) {
            gameDAO.createGame(new GameData(gameID, game.whiteUsername(), auth.username(), game.gameName(), game.game()));
        } else {
            throw new DataAccessException("403 Error: already taken");
        }
    }

    public void clear() {
        gameDAO.clear();
    }
}