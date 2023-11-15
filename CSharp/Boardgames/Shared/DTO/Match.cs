namespace GameClient.DTO;

public class Match {
    public required int MatchId { get; set; }
    public required int Status { get; set; }
    public required String State { get; set; }
    public required int OwnerId { get; set; }
    public required int GameId { get; set; }
    public required DateTime CreatedOn { get; set; }

    public List<Participant> Participants { get; set; } = Empty.List<Participant>();
    public Account Owner { get; set; } = Empty.Account();
    public Game Game { get; set; } = Empty.Game();

    public const int STATUS_NONE = 0;
    public const int STATUS_PENDING = 1;
    public const int STATUS_ONGOING = 2;
    public const int STATUS_FINISHED = 3;
}
