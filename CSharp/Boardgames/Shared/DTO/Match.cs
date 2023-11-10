namespace GameClient.DTO;

public class Match {
    public required int MatchId { get; set; }
    public required String State { get; set; }
    public required int OwnerId { get; set; }
    public required int GameId { get; set; }

    public static Match Empty() {
        return new Match {
            MatchId = 0,
            State = "?",
            OwnerId = 0,
            GameId = 0,
        };
    }
}
