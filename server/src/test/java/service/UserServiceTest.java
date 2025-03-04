package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    private UserDAO userDAO;
    private AuthDAO authDAO;
    private UserService userService;

    @BeforeEach
    public void setUp() {
        userDAO = new UserDAO();
        authDAO = new AuthDAO();
        userService = new UserService(userDAO, authDAO);
    }

    @Test
    public void testRegisterSuccess() throws DataAccessException {
        String username = "testuser";
        String password = "password";
        String email = "test@example.com";

        // Register the new user
        AuthData authData = userService.register(username, password, email);

        // Verify auth
        assertNotNull(authData);
        assertEquals(username, authData.username());

        // Verify the user was created
        UserData createdUser = userDAO.getUser(username);
        assertNotNull(createdUser);
        assertEquals(username, createdUser.username());
        assertEquals(password, createdUser.password());
        assertEquals(email, createdUser.email());

        // Verify auth token creation
        AuthData createdAuth = authDAO.getAuth(authData.authToken());
        assertNotNull(createdAuth);
        assertEquals(username, createdAuth.username());
    }

    @Test
    public void testRegisterUsernameTaken() throws DataAccessException {
        String username = "testuser";
        String password = "password";
        String email = "test@example.com";

        // Create an existing user
        UserData existingUser = new UserData(username, password, email);
        userDAO.createUser(existingUser);

        // Attempt to register a new user with the same username
        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            userService.register(username, password, email);
        });
        assertEquals("403 Error: username already taken", exception.getMessage());
    }

    @Test
    public void testRegisterFailureBadRequest() throws DataAccessException {
        String username = null;
        String password = "password";
        String email = "test@example.com";

        // Attempt to register a new user with missing username
        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            userService.register(username, password, email);
        });

        assertEquals("400 Error: bad request", exception.getMessage());
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
}