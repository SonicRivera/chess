package service;

import dataaccess.GameDAO;
import dataaccess.AuthDAO;


public class GameService {
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public GameService(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public void clear() {
        gameDAO.clear();
    }


}
