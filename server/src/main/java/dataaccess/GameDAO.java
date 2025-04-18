package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
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
        if (game == null) {
            throw new DataAccessException("Game data cannot be null");
        }
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

    public GameData getGame(int gameID) throws DataAccessException {
        if (gameID <= 0) {
            throw new DataAccessException("Invalid gameID");
        }
        String sql = "SELECT game_id, white_username, black_username, game_name, game_state FROM games WHERE game_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, gameID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new GameData(rs.getInt("game_id"), rs.getString("white_username"), rs.getString("black_username"),
                            rs.getString("game_name"), deserializeGame(rs.getString("game_state")));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving game: " + e.getMessage());
        }
        return null;
    }

    public Map<Integer, GameData> listGames() throws DataAccessException {
        String sql = "SELECT game_id, white_username, black_username, game_name, game_state FROM games";
        Map<Integer, GameData> games = new HashMap<>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                GameData game = new GameData(rs.getInt("game_id"), rs.getString("white_username"), rs.getString("black_username"),
                        rs.getString("game_name"), deserializeGame(rs.getString("game_state")));
                games.put(game.gameID(), game);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error listing games: " + e.getMessage());
        }
        return games;
    }

    public void updateGame(GameData game) throws DataAccessException {
        if (getGame(game.gameID()) == null){
            throw new DataAccessException("Invalid GameID");
        }
        String sql = "UPDATE games SET white_username = ?, black_username = ?, game_name = ?, game_state = ? WHERE game_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, game.whiteUsername());
            stmt.setString(2, game.blackUsername());
            stmt.setString(3, game.gameName());
            stmt.setString(4, serializeGame(game.game()));
            stmt.setInt(5, game.gameID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error updating game: " + e.getMessage());
        }
    }

    public void clear() throws DataAccessException {
        String sql = "DELETE FROM games";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error clearing games: " + e.getMessage());
        }
    }


    private String serializeGame(ChessGame game) {
        return new Gson().toJson(game);
    }

    private ChessGame deserializeGame(String serializedGame) {
        return new Gson().fromJson(serializedGame, ChessGame.class);
    }
}