package boardgames.persistence;

import java.util.List;

import boardgames.persistence.data.*;
import boardgames.shared.dto.Account;
import boardgames.shared.dto.Game;
import boardgames.shared.dto.Match;
import boardgames.shared.dto.Participant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RunPersistence {
    public static void main(String[] args) {
        SpringApplication.run(RunPersistence.class, args);
    }
}
