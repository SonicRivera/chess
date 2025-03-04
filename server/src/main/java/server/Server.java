package server;

import spark.*;
import service.UserService;
import service.GameService;
import dataaccess.*;

public class Server {

    UserDAO userDAO;
    AuthDAO authDAO;
    GameDAO gameDAO;

    UserService userService;
    GameService gameService;

    UserHandler userHandler;
    GameHandler gameHandler;

    public Server() {


    }

    public int run(int desiredPort) {

        userDAO = new UserDAO();
        authDAO = new AuthDAO();
        gameDAO = new GameDAO();

        userService = new UserService(userDAO, authDAO);
        gameService = new GameService(gameDAO, authDAO);

        userHandler = new UserHandler(userService);
        gameHandler = new GameHandler(gameService);

        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        UserService userService = new UserService(new UserDAO(), new AuthDAO());

        UserHandler userHandler = new UserHandler(userService);


        // User Endpoints
        Spark.delete("/db", this::clear);
        Spark.post("/user", userHandler.register);
        Spark.post("/session", userHandler.login);
        Spark.delete("/session", userHandler.logout);

        // Game Endpoints
        Spark.get("/game", gameHandler.listGames);
        Spark.post("/game", gameHandler.createGame);
        Spark.put("/game", gameHandler.joinGame);

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object clear(Request req, Response resp) {

        userService.clear();
        gameService.clear();

        resp.status(200);
        return "{}";
    }


}
