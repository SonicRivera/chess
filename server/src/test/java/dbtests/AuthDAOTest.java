package dbtests;

import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import dataaccess.*;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class AuthDAOTest {

    private AuthDAO authDAO;

    @BeforeEach
    public void setUp() throws DataAccessException, SQLException {
        authDAO = new AuthDAO();
        authDAO.clear();
    }

    @Test
    public void testCreateAuthSuccess() throws DataAccessException {
        AuthData authData = new AuthData("authToken1", "username1");
        authDAO.createAuth(authData);

        AuthData retrievedAuth = authDAO.getAuth("authToken1");
        assertNotNull(retrievedAuth);
        assertEquals("authToken1", retrievedAuth.authToken());
        assertEquals("username1", retrievedAuth.username());
    }

    @Test
    public void testGetAuthNotFound() throws DataAccessException {
        AuthData retrievedAuth = authDAO.getAuth("nonexistentToken");
        assertNull(retrievedAuth);
    }

    @Test
    public void testDeleteAuthSuccess() throws DataAccessException {
        AuthData authData = new AuthData("authToken1", "username1");
        authDAO.createAuth(authData);

        authDAO.deleteAuth("authToken1");

        AuthData retrievedAuth = authDAO.getAuth("authToken1");
        assertNull(retrievedAuth);
    }

    @Test
    public void testClearSuccess() throws DataAccessException {
        AuthData authData1 = new AuthData("authToken1", "username1");
        AuthData authData2 = new AuthData("authToken2", "username2");
        authDAO.createAuth(authData1);
        authDAO.createAuth(authData2);

        authDAO.clear();

        assertNull(authDAO.getAuth("authToken1"));
        assertNull(authDAO.getAuth("authToken2"));
    }
}