﻿namespace GameClient.DTO;

// WARNING(rune): Navne skal matche mellem Java og C#.
public class Match {
    public required int MatchId { get; set; }
    public required int Status { get; set; }
    public required String Data { get; set; }
    public required int OwnerId { get; set; }
    public required int GameId { get; set; }
    public required DateTime CreatedOn { get; set; }
    public DateTime? StartedOn { get; set; }
    public DateTime? LastMoveOn { get; set; }
    public int? NextAccountId { get; set; }

    public List<Participant> Participants { get; set; } = Empty.List<Participant>();
    public Account Owner { get; set; } = Empty.Account();
    public Game Game { get; set; } = Empty.Game();

    public const int STATUS_NONE = 0;
    public const int STATUS_PENDING = 1;
    public const int STATUS_ONGOING = 2;
    public const int STATUS_FINISHED = 3;

    public string StatusName() {
        return Status switch {
            STATUS_NONE => "STATUS_NONE",
            STATUS_PENDING => "STATUS_PENDING",
            STATUS_ONGOING => "STATUS_ONGOING",
            STATUS_FINISHED => "STATUS_FINISHED",
            _ => ""
        };
    }

    public string StatusDisplayName() {
        return Status switch {
            STATUS_NONE => "None",
            STATUS_PENDING => "Pending",
            STATUS_ONGOING => "Ongoing",
            STATUS_FINISHED => "Finished",
            _ => ""
        };
    }

    public override string ToString() {
        return "Match#" + MatchId;
    }
}
