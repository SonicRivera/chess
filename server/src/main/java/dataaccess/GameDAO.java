package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import jdk.jshell.spi.ExecutionControl;
import model.GameData;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class GameDAO {

    public GameDAO() {

        try {
            DatabaseManager.createDatabase();
            DatabaseManager.createTables();

        } catch (DataAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

    public int createGame(GameData game) throws DataAccessException {
        String sql = "INSERT INTO games (white_username, black_username, game_name, game_state) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, game.whiteUsername());
            stmt.setString(2, game.blackUsername());
            stmt.setString(3, game.gameName());
            stmt.setString(4, serializeGame(game.game()));
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error creating game: " + e.getMessage());
        }
        return -1;
    }

    public GameData getGame(int gameID) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Not implemented");
    }

    public Map<Integer, GameData> listGames() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Not implemented");
    }

    public void updateGame(GameData game) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Not implemented");
    }

    public void clear() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Not implemented");
    }


    private String serializeGame(ChessGame game) {
        return new Gson().toJson(game);
    }
}