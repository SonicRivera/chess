package dataaccess;

import chess.ChessGame;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

import static org.junit.jupiter.api.Assertions.*;

public class GameDAOTest {

    private GameDAO gameDAO;

    @BeforeEach
    public void setUp() throws DataAccessException {
        gameDAO = new GameDAO();
        gameDAO.clear();
    }

    @Test
    public void testCreateGameSuccess() throws DataAccessException {
        ChessGame actualGame = new ChessGame();
        GameData game = new GameData(0, "whitePlayer", "blackPlayer", "Test Game", actualGame);
        int gameID = gameDAO.createGame(game);

        GameData retrievedGame = gameDAO.getGame(gameID);
        assertNotNull(retrievedGame);
        assertEquals(gameID, retrievedGame.gameID());
        assertEquals("whitePlayer", retrievedGame.whiteUsername());
        assertEquals("blackPlayer", retrievedGame.blackUsername());
        assertEquals("Test Game", retrievedGame.gameName());
        assertEquals(serializeGame(actualGame), serializeGame(retrievedGame.game()));
    }

    @Test
    public void testCreateGameFailureNullGameData() {
        assertThrows(DataAccessException.class, () -> {
            gameDAO.createGame(null);
        });
    }

    @Test
    public void testGetGameNotFound() throws DataAccessException {
        GameData retrievedGame = gameDAO.getGame(999);
        assertNull(retrievedGame);
    }

    @Test
    public void testGetGameFailureInvalidGameID() {
        assertThrows(DataAccessException.class, () -> {
            gameDAO.getGame(-1);
        });
    }

    @Test
    public void testUpdateGameSuccess() throws DataAccessException {
        GameData game = new GameData(0, "whitePlayer", "blackPlayer",
         "Test Game", new ChessGame());
        int gameID = gameDAO.createGame(game);

        GameData updatedGame = new GameData(gameID, "whitePlayerName", "blackPlayerName",
         "Updated Game", new ChessGame());
        gameDAO.updateGame(updatedGame);

        GameData retrievedGame = gameDAO.getGame(gameID);
        assertNotNull(retrievedGame);
        assertEquals(gameID, retrievedGame.gameID());
        assertEquals("whitePlayerName", retrievedGame.whiteUsername());
        assertEquals("blackPlayerName", retrievedGame.blackUsername());
        assertEquals("Updated Game", retrievedGame.gameName());
        // Not entirely sure how to test for game states...
    }

    @Test
    public void testUpdateGameFailureNonexistentGameID() throws DataAccessException {
        GameData updatedGame = new GameData(999, "whitePlayerName", "blackPlayerName",
         "Updated Game", new ChessGame());
        assertThrows(DataAccessException.class, () -> {
            gameDAO.updateGame(updatedGame);
        });
    }

    @Test
    public void testClearSuccess() throws DataAccessException {
        GameData game1 = new GameData(1, "whitePlayer1", "blackPlayer1", "Test Game 1", new ChessGame());
        GameData game2 = new GameData(2, "whitePlayer2", "blackPlayer2", "Test Game 2", new ChessGame());
        gameDAO.createGame(game1);
        gameDAO.createGame(game2);

        gameDAO.clear();

        assertNull(gameDAO.getGame(game1.gameID()));
        assertNull(gameDAO.getGame(game2.gameID()));
    }

    private String serializeGame(ChessGame game) {
        return new Gson().toJson(game);
    }

    private ChessGame deserializeGame(String serializedGame) {
        return new Gson().fromJson(serializedGame, ChessGame.class);
    }
}