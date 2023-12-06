package boardgames.persistence.data;

import boardgames.shared.dto.Game;

import java.util.List;

public interface GameData {
    public Game get(int gameId);
    public Game getWithPicture(int gameId);
    public List<Game> getAll();
}
