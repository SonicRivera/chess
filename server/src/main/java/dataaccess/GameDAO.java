package dataaccess;

import model.GameData;
import java.util.HashMap;
import java.util.Map;

public class GameDAO {
    private final Map<Integer, GameData> games = new HashMap<>();
    private int nextGameID = 1;

    // Create a new game
    public int createGame(GameData game) {
        int gameID = nextGameID++;

        games.put(gameID, new GameData(gameID, game.whiteUsername(), game.blackUsername(), game.gameName(), game.game()));
        return gameID;
    }

    // Retrieve a game by ID
    public GameData getGame(int gameID) {
        return games.get(gameID);
    }

    // List all games
    public Map<Integer, GameData> listGames() {
        return games;
    }

    // Update an existing game
    public void updateGame(int gameID, GameData updatedGame) {
        if (games.containsKey(gameID)) {
            games.put(gameID, updatedGame);
        }
    }

    // Clear all games
    public void clear() {
        games.clear();
        nextGameID = 1;
    }
}
