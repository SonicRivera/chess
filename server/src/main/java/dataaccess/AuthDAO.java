package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class AuthDAO {
    private final Map<String, AuthData> authTokens = new HashMap<>();

    // Create an auth token
    public void createAuth(AuthData auth) {
        authTokens.put(auth.authToken(), auth);
    }

    // Get an auth token
    public AuthData getAuth(String authToken) {
        return authTokens.get(authToken);
    }

    // Clear all authentication tokens
    public void clear() {
        authTokens.clear();
    }
}
