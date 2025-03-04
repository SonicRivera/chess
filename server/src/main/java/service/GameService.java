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

    public void clear() {
        gameDAO.clear();
    }
}