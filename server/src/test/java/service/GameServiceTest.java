package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

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
    public void testClear() throws DataAccessException {
        // Create a new user
        String username = "testuser";
        String password = "password";
        String email = "test@example.com";
        UserData newUser = new UserData(username, password, email);
        userDAO.createUser(newUser);

        // Clear the data
        userService.clear();

        // Verify clear
        assertNull(userDAO.getUser(username));
    }

    @Test
    public void testListGames() throws DataAccessException {
        String authToken = "validToken";
        authDAO.createAuth(new AuthData(authToken, "user1"));
        gameDAO.createGame(new GameData(1, "user1", "user2", "game1", null));

        List<GameData> games = gameService.listGames(authToken);

        assertEquals(1, games.size());
        assertEquals("game1", games.get(0).gameName());
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
}