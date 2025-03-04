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

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {

    private GameDAO gameDAO;
    private AuthDAO authDAO;
    private UserDAO userDAO;
    private GameService gameService;
    private UserService userService;

    @BeforeEach
    public void setUp() {
        gameDAO = new GameDAO();
        userDAO = new UserDAO();
        authDAO = new AuthDAO();
        gameService = new GameService(gameDAO, authDAO);
        userService = new UserService(userDAO, authDAO);
        
    }

    @Test
    public void testCreateGame_Success() throws DataAccessException {
        String username = "testuser";
        String password = "password";
        String email = "test@example.com";

        // Create a new user
        UserData newUser = new UserData(username, password, email);
        userDAO.createUser(newUser);

        // Login with the created user
        AuthData authData = userService.login(username, password);

        // Verify auth
        assertNotNull(authData);
        assertEquals(username, authData.username());

        // Verify auth token creation
        AuthData createdAuth = authDAO.getAuth(authData.authToken());
        assertNotNull(createdAuth);
        assertEquals(username, createdAuth.username());

        String gameName = "Test Game";
        
        // Create the game
        int gameID = gameService.createGame(createdAuth.authToken(), gameName);

        // Verify the game was created
        GameData createdGame = gameDAO.getGame(gameID);
        assertNotNull(createdGame);
        assertEquals(gameName, createdGame.gameName());
    }

    @Test
    public void testCreateGame_Failure_Unauthorized() {
        String authToken = "invalidAuthToken";
        String gameName = "Test Game";

        // Attempt to create a game with an invalid auth token
        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            gameService.createGame(authToken, gameName);
        });

        // Verify the exception message
        assertEquals("401 Error: unauthorized", exception.getMessage());
    }

    @Test
    public void testCreateGame_Failure_BadRequest() throws DataAccessException {
        String authToken = "validAuthToken";
        String gameName = "";

        // Mock the auth token
        AuthData authData = new AuthData(authToken, "testuser");
        authDAO.createAuth(authData);

        // Attempt to create a game with an empty game name
        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            gameService.createGame(authToken, gameName);
        });

        // Verify the exception message
        assertEquals("400 Error: bad request", exception.getMessage());
    }

    @Test
    public void testClear() throws DataAccessException {
        String authToken = "validAuthToken";
        String gameName = "Test Game";

        // Mock the auth token
        AuthData authData = new AuthData(authToken, "testuser");
        authDAO.createAuth(authData);

        // Create a game
        int gameID = gameService.createGame(authToken, gameName);

        // Verify the game was created
        assertNotNull(gameDAO.getGame(gameID));

        // Clear the data
        gameService.clear();

        // Verify the game was cleared
        assertNull(gameDAO.getGame(gameID));
    }
}