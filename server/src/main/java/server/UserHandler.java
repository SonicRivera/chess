package server;

import service.UserService;
import model.AuthData;
import spark.Request;
import spark.Response;
import spark.Route;

public class UserHandler {
    UserService userService;

    public UserHandler(UserService userService) {
        this.userService = userService;
    }

    // Register Handler
    public Route register = (Request req, Response res) -> {

        return "{}";
    };

    // Register Handler
    public Route login = (Request req, Response res) -> {
        return "{}";
    };

    // Register Handler
    public Route logout = (Request req, Response res) -> {
        return "{}";
    };


}
