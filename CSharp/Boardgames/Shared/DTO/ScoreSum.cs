namespace GameClient.DTO;

public class ScoreSum {
    public required int GameId { get; set; }
    public required int AccountId { get; set; }
    public required string AccountName { get; set; }
    public required int Score { get; set; }
    public required int Count { get; set; }
}
