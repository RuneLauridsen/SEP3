package boardgames.persistence.data;

import boardgames.shared.dto.Game;

import java.util.List;

public interface GameData {
    public Game get(int gameId);
    public List<Game> getAll();
    // TODO(rune): Behøver vel ikke nogen create osv. da vi kun har et fast katalog?
}
