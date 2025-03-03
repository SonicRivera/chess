package dataaccess;

import model.UserData;
import java.util.HashMap;
import java.util.Map;

public class UserDAO {
    private final Map<String, UserData> users = new HashMap<>();

    // Create a user
    public void createUser(UserData user) throws DataAccessException {
        if (users.containsKey(user.username())) {
            throw new DataAccessException("Error: Username already exists");
        }
        users.put(user.username(), user);
    }

    // Get a user
    public UserData getUser(String username) {
        return users.get(username);
    }

    // Clear
    public void clear() {
        users.clear();
    }
}
