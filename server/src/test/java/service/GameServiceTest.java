package service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {

    private UserDAO userDAO;
    private AuthDAO authDAO;
    private UserService userService;
    private GameDAO gameDAO;
    private GameService gameService;

    @BeforeEach
    public void setUp() {
        userDAO = new UserDAO();
        authDAO = new AuthDAO();
        userService = new UserService(userDAO, authDAO);
        gameDAO = new GameDAO();
        gameService = new GameService(gameDAO, authDAO);
    }

    @Test
    public void testListGames() throws DataAccessException {
        String authToken = "validToken";
        authDAO.createAuth(new AuthData(authToken, "user1"));
        gameDAO.createGame(new GameData(1, "user1", "user2", "game1", null));

        String gamesJson = gameService.listGames(authToken);

        JsonObject jsonObject = JsonParser.parseString(gamesJson).getAsJsonObject();
        JsonArray gamesArray = jsonObject.getAsJsonArray("games");

        assertEquals(1, gamesArray.size());
        JsonObject gameObject = gamesArray.get(0).getAsJsonObject();
        assertEquals("game1", gameObject.get("gameName").getAsString());
    }

    @Test
    public void testListGamesUnauthorized() {
        String invalidAuthToken = "invalidToken";

        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            gameService.listGames(invalidAuthToken);
        });

        assertEquals("401 Error: unauthorized", exception.getMessage());
    }

    @Test
    public void testCreateGame() throws DataAccessException {
        String authToken = "validToken";
        String gameName = "newGame";
        authDAO.createAuth(new AuthData(authToken, "user1"));

        int gameId = gameService.createGame(authToken, gameName);

        assertEquals(1, gameId);
        GameData createdGame = gameDAO.getGame(gameId);
        assertNotNull(createdGame);
        assertEquals(gameName, createdGame.gameName());
    }

    @Test
    public void testCreateGameUnauthorized() {
        String invalidAuthToken = "invalidToken";
        String gameName = "newGame";

        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            gameService.createGame(invalidAuthToken, gameName);
        });

        assertEquals("401: Error: unauthorized", exception.getMessage());
    }

    @Test
    public void testCreateGameBadRequest() throws DataAccessException {
        String authToken = "validToken";
        authDAO.createAuth(new AuthData(authToken, "user1"));

        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            gameService.createGame(authToken, "");
        });

        assertEquals("400: Error: bad request", exception.getMessage());
    }

    @Test
    public void testJoinGame() throws DataAccessException {
        String authToken = "validToken";
        int gameID = 1;
        String playerColor = "WHITE";
        authDAO.createAuth(new AuthData(authToken, "user1"));
        gameDAO.createGame(new GameData(gameID, null, "user2", "game1", null));

        gameService.joinGame(authToken, gameID, playerColor);

        GameData updatedGame = gameDAO.getGame(gameID);
        assertNotNull(updatedGame);
        assertEquals("user1", updatedGame.whiteUsername());
    }

    @Test
    public void testJoinGameUnauthorized() {
        String invalidAuthToken = "invalidToken";
        int gameID = 1;
        String playerColor = "WHITE";

        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            gameService.joinGame(invalidAuthToken, gameID, playerColor);
        });

        assertEquals("401 Error: unauthorized", exception.getMessage());
    }

    @Test
    public void testJoinGameBadRequest() throws DataAccessException {
        String authToken = "validToken";
        int invalidGameID = 999;
        String playerColor = "WHITE";
        authDAO.createAuth(new AuthData(authToken, "user1"));

        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            gameService.joinGame(authToken, invalidGameID, playerColor);
        });

        assertEquals("400 Error: bad request", exception.getMessage());
    }

    @Test
    public void testJoinGameAlreadyTaken() throws DataAccessException {
        String authToken = "validToken";
        int gameID = 1;
        String playerColor = "WHITE";
        authDAO.createAuth(new AuthData(authToken, "user1"));
        gameDAO.createGame(new GameData(gameID, "user2", "user3", "game1", null));

        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            gameService.joinGame(authToken, gameID, playerColor);
        });

        assertEquals("403 Error: already taken", exception.getMessage());
    }
}