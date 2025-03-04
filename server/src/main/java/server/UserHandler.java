package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataaccess.DataAccessException;
import model.UserData;
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

        UserData userData = new Gson().fromJson(req.body(), UserData.class);

        try {
            AuthData authData = userService.register(userData.username(), userData.password(), userData.email());
            res.status(200);
            return JsonUtil.toJson(authData);
        } catch (DataAccessException e) {
            String message = e.getMessage();
            if (message.startsWith("400")) {
                res.status(400);
            } else if (message.startsWith("403")) {
                res.status(403);
            } else {
                res.status(500);
            }
            return JsonUtil.toJson(new ErrorResponse(message.substring(4))); // Remove error code prefix
        }
    };

    // Login Handler
    public Route login = (Request req, Response res) -> {
        UserData userData = new Gson().fromJson(req.body(), UserData.class);
        String username = userData.username();
        String password = userData.password();

        try {
            AuthData authData = userService.login(username, password);
            res.status(200);

            return new Gson().toJson(authData);
        } catch (DataAccessException e) {
            String message = e.getMessage();
            if (message.startsWith("401")) {
                res.status(401);
            } else {
                res.status(500);
            }
            return new Gson().toJson(new ErrorResponse(message.substring(4)));
        }
    };

    // Logout Handler
    public Route logout = (Request req, Response res) -> {
        String authToken = req.headers("authorization");

        try {
            userService.logout(authToken);
            res.status(200);
            return "{}";
        } catch (DataAccessException e) {
            String message = e.getMessage();
            if (message.startsWith("401")) {
                res.status(401);
            } else {
                res.status(500);
            }
            return JsonUtil.toJson(new ErrorResponse(message.substring(4)));
        }
    };

    // Error Response
    private record ErrorResponse(String message) {}

}
