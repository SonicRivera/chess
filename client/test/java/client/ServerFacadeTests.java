package client;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.*;
import server.Server;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);

    }

    @BeforeEach
    public void cleanSlate() {
        facade.clear();
        String[] args = {"Username", "SuperSecurePassword", "email@email.com"};
        String authToken = facade.Register(args);
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
        String authToken = facade.Register(args);

        assert authToken.isEmpty();
    }

    @Test
    public void positiveRegisterTest() {
        facade.clear();
        String[] args = {"Username", "SuperSecurePassword", "email@email.com"};
        String authToken = facade.Register(args);

        assertTrue(!authToken.isEmpty());

    }

    @Test
    public void negativeLoginTest() {
        facade.clear();
        String[] args = {"Username", "SuperSecurePassword", "email@email.com"};
        facade.Register(args);
        args = new String[] {"Username", "WrongPassword"};
        String authToken = facade.Login(args);

        assert authToken.isEmpty();
    }

    @Test
    public void positiveLoginTest() {
        String[] args = {"Username", "SuperSecurePassword"};
        String authToken = facade.Login(args);

        assertTrue(!authToken.isEmpty());
        
    }
    
    @Test
    public void negativeLogoutTest() {
        // Try to log out without being logged in
        String authToken = "";
        assertTrue(!facade.Logout(authToken));
    }

    @Test
    public void positiveLogoutTest() {
        String[] args = {"Username", "SuperSecurePassword"};
        String authToken = facade.Login(args);

        assertTrue(facade.Logout(authToken));
        
    }

    @Test
    public void negativeCreateGameTest() {
        // Try to create a game without being logged in.
        assertTrue(!facade.createGame("testGame", "badToken"));
    }

    @Test
    public void positiveCreateGameTest() {
        String[] args = {"Username", "SuperSecurePassword"};
        String authToken = facade.Login(args);

        assertTrue(facade.createGame("testGame", authToken));

    }

    @Test
    public void negativeJoinGameTest() {
        // Try to join a game without being logged in.
        String[] args = {"Username", "SuperSecurePassword"};
        String authToken = facade.Login(args);
        facade.createGame("testGame", authToken);
        facade.Logout(authToken);

        assertTrue(!facade.joinGame("1", "WHITE", authToken));
    }

    @Test
    public void positiveJoinGameTest() {
        String[] args = {"Username", "SuperSecurePassword"};
        String authToken = facade.Login(args);
        facade.createGame("testGame", authToken);
        facade.listGames(authToken);

        assertTrue(facade.joinGame("1", "WHITE", authToken));

    }

    @Test
    public void negativeListGameTest() {
        // Try to list games without being logged in.
        String[] args = {"Username", "SuperSecurePassword"};
        String authToken = facade.Login(args);
        facade.createGame("testGame", authToken);
        facade.createGame("testGame2", authToken);
        facade.createGame("testGame3", authToken);
        facade.createGame("testGame4", authToken);
        facade.Logout(authToken);

        assertTrue(!facade.listGames(authToken));
    }

    @Test
    public void positiveListGameTest() {
        String[] args = {"Username", "SuperSecurePassword"};
        String authToken = facade.Login(args);
        facade.createGame("testGame", authToken);
        facade.createGame("testGame2", authToken);
        facade.createGame("testGame3", authToken);
        facade.createGame("testGame4", authToken);
        facade.listGames(authToken);

        assertTrue(facade.listGames(authToken));

    }





    





}
