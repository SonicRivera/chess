package server;

import com.google.gson.JsonObject;
import dataaccess.DataAccessException;
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
        JsonObject body = JsonUtil.fromJson(req.body(), JsonObject.class);
        String username = body.has("username") ? body.get("username").getAsString() : null;
        String password = body.has("password") ? body.get("password").getAsString() : null;
        String email = body.has("email") ? body.get("email").getAsString() : null;

        try {
            AuthData authData = userService.register(username, password, email);
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
        return "{}";
    };

    // Logout Handler
    public Route logout = (Request req, Response res) -> {
        return "{}";
    };

    // Error Response
    private record ErrorResponse(String message) {}

}
