package dataaccess;

import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTest {

    private UserDAO userDAO;

    @BeforeEach
    public void setUp() throws DataAccessException, SQLException {
        userDAO = new UserDAO();
        userDAO.clear();
    }

    @Test
    public void testCreateUserSuccess() throws DataAccessException {
        UserData user = new UserData("username1", "password1", "email1@example.com");
        userDAO.createUser(user);

        UserData retrievedUser = userDAO.getUser("username1");
        assertNotNull(retrievedUser);
        assertEquals("username1", retrievedUser.username());
        assertTrue(BCrypt.checkpw("password1", retrievedUser.password()));
        assertEquals("email1@example.com", retrievedUser.email());
    }

    @Test
    public void testCreateUserFailureDuplicateUsername() throws DataAccessException {
        UserData user1 = new UserData("username1", "password1", "email1@example.com");
        userDAO.createUser(user1);

        UserData user2 = new UserData("username1", "password2", "email2@example.com");
        assertThrows(DataAccessException.class, () -> {
            userDAO.createUser(user2);
        });
    }

    @Test
    public void testGetUserSuccess() throws DataAccessException {
        UserData user = new UserData("username1", "password1", "email1@example.com");
        userDAO.createUser(user);

        verifyUser(user, "password1");
    }

    @Test
    public void testGetUserNotFound() throws DataAccessException {
        UserData retrievedUser = userDAO.getUser("nonexistentUser");
        assertNull(retrievedUser);
    }

    @Test
    public void testClearSuccess() throws DataAccessException {
        UserData user1 = new UserData("username1", "password1", "email1@example.com");
        UserData user2 = new UserData("username2", "password2", "email2@example.com");
        userDAO.createUser(user1);
        userDAO.createUser(user2);

        userDAO.clear();

        assertNull(userDAO.getUser("username1"));
        assertNull(userDAO.getUser("username2"));
    }

    private void verifyUser(UserData user, String password) throws DataAccessException {
        UserData retrievedUser = userDAO.getUser(user.username());
        assertNotNull(retrievedUser);
        assertEquals(user.username(), retrievedUser.username());
        assertTrue(BCrypt.checkpw(password, retrievedUser.password()));
        assertEquals(user.email(), retrievedUser.email());
    }
}