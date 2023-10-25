package boardgames.persistence;

import boardgames.shared.Account;
import boardgames.shared.Game;
import boardgames.shared.Session;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.*;

@SpringBootApplication
public class PersistenceApplication {

    public static void main(String[] args) {
        SqlDatabase database = new SqlDatabase();
        Account maja = database.getAccount("Maja123");


        Game game = new Game();
        game.setGameId(1);
        game.setName("TicTacToe");

        Session session = database.createSession(game);
    }
}
