package client;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.*;
import server.Server;

import java.util.Map;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("localhost:" + port);

    }

    @BeforeEach
    public void cleanSlate() {
        facade.clear();
        String[] args = {"Username", "SuperSecurePassword", "email@email.com"};
        String authToken = facade.register(args);
    }

    @AfterAll
    static void stopServer() {
        facade.clear();
        server.stop();
    }


    @Test
    public void negativeRegisterTest() {
        facade.clear();
        String[] args = {"Username", "email@email.com"};
        String authToken = facade.register(args);

        assert authToken.isEmpty();
    }

    @Test
    public void positiveRegisterTest() {
        facade.clear();
        String[] args = {"Username", "SuperSecurePassword", "email@email.com"};
        String authToken = facade.register(args);

        assertTrue(!authToken.isEmpty());

    }

    @Test
    public void negativeLoginTest() {
        facade.clear();
        String[] args = {"Username", "SuperSecurePassword", "email@email.com"};
        facade.register(args);
        args = new String[] {"Username", "WrongPassword"};
        String authToken = facade.login(args);

        assert authToken.isEmpty();
    }

    @Test
    public void positiveLoginTest() {
        String[] args = {"Username", "SuperSecurePassword"};
        String authToken = facade.login(args);

        assertTrue(!authToken.isEmpty());
        
    }
    
    @Test
    public void negativeLogoutTest() {
        // Try to log out without being logged in
        String authToken = "";
        assertTrue(!facade.logout(authToken));
    }

    @Test
    public void positiveLogoutTest() {
        String[] args = {"Username", "SuperSecurePassword"};
        String authToken = facade.login(args);

        assertTrue(facade.logout(authToken));
        
    }

    @Test
    public void negativeCreateGameTest() {
        // Try to create a game without being logged in.
        assertTrue(!facade.createGame("testGame", "badToken"));
    }

    @Test
    public void positiveCreateGameTest() {
        String[] args = {"Username", "SuperSecurePassword"};
        String authToken = facade.login(args);

        assertTrue(facade.createGame("testGame", authToken));

    }

    @Test
    public void negativeJoinGameTest() {
        // Try to join a game without being logged in.
        String[] args = {"Username", "SuperSecurePassword"};
        String authToken = facade.login(args);
        facade.createGame("testGame", authToken);
        facade.logout(authToken);

        Map<String, Object> result = facade.joinGame("1", "WHITE", authToken);
        Boolean success = (Boolean) result.get("bool");

        assertTrue(!success);
    }

    @Test
    public void positiveJoinGameTest() {
        String[] args = {"Username", "SuperSecurePassword"};
        String authToken = facade.login(args);
        facade.createGame("testGame", authToken);
        facade.listGames(authToken);

        Map<String, Object> result = facade.joinGame("1", "WHITE", authToken);
        Boolean success = (Boolean) result.get("bool");

        assertTrue(success);

    }

    @Test
    public void negativeListGameTest() {
        // Try to list games without being logged in.
        String[] args = {"Username", "SuperSecurePassword"};
        String authToken = facade.login(args);
        facade.createGame("testGame", authToken);
        facade.createGame("testGame2", authToken);
        facade.createGame("testGame3", authToken);
        facade.createGame("testGame4", authToken);
        facade.logout(authToken);

        assertTrue(!facade.listGames(authToken));
    }

    @Test
    public void positiveListGameTest() {
        String[] args = {"Username", "SuperSecurePassword"};
        String authToken = facade.login(args);
        facade.createGame("testGame", authToken);
        facade.createGame("testGame2", authToken);
        facade.createGame("testGame3", authToken);
        facade.createGame("testGame4", authToken);
        facade.listGames(authToken);

        assertTrue(facade.listGames(authToken));

    }





    





}
