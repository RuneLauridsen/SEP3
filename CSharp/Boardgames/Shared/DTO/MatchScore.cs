namespace GameClient.DTO;

public class MatchScore {
    public required int GameId { get; set; }
    public required string GameName { get; set; }
    public required int MatchId { get; set; }
    public required DateTime MatchFinishedOn { get; set; }
    public required int Score { get; set; }
}
