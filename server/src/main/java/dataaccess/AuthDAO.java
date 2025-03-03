package dataaccess;

import model.AuthData;
import java.util.HashMap;
import java.util.Map;

public class AuthDAO {
    private final Map<String, AuthData> authTokens = new HashMap<>();

    // Create an authentication token
    public void createAuth(AuthData auth) {
        authTokens.put(auth.authToken(), auth);
    }

    // Clear all authentication tokens
    public void clear() {
        authTokens.clear();
    }
}
