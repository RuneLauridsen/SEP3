namespace GameClient.DTO;

public class Game {
    public required int GameId { get; set; }
    public required string Name { get; set; }

    public static Game Empty() {
        return new Game {
            GameId = 0,
            Name = "?"
        };
    }
}
