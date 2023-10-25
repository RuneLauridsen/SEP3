package boardgames.persistence;

import java.util.List;

import boardgames.shared.Account;
import boardgames.shared.Game;
import boardgames.shared.Session;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.*;

@SpringBootApplication
public class PersistenceApplication {

    public static void main(String[] args) {
        SqlDatabase db = new SqlDatabase();
        Account maja = db.getAccount("Maja123");
        Account julie = db.getAccount("BenDover");

        Game game = new Game();
        game.setGameId(1);
        game.setName("TicTacToe");

        Session session = db.createSession(game);

        db.addAccountToSession(session, maja);
        db.addAccountToSession(session, julie);

        List<Account> accs = db.getAccountsInSession(session);

        db.updateSessionState(session, "Donatello");
    }
}
