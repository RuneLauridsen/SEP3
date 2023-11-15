namespace GameClient.DTO;

// TODO(rune): Er det her Null-Object-Pattern???????????????
public static class Empty {
    public static Account Account() {
        return new Account {
            AccountId = 0,
            AccountStatus = 0,
            Username = "?",
            FirstName = "?",
            LastName = "?",
            Email = "?",
            CreatedOn = Empty.DateTime(),
            RegisterDateTime = Empty.DateTime(),
        };
    }

    public static Match Match() {
        return new Match {
            MatchId = 0,
            Status = 0,
            State = "?",
            OwnerId = 0,
            GameId = 0,
            CreatedOn = Empty.DateTime(),
        };
    }

    public static Game Game() {
        return new Game {
            GameId = 0,
            Name = "?",
        };
    }

    public static Participant Participant() {
        return new Participant {
            ParticipantId = 0,
            ParticipantStatus = 0,
            MatchId = 0,
            AccountId = 0,
            CreatedOn = Empty.DateTime(),
        };
    }

    public static DateTime DateTime() {
        return new DateTime(1, 1, 1, 1, 1, 1);
    }

    public static List<T> List<T>() {
        return new List<T>();
    }

    public static IEnumerable<T> Enumerable<T>() {
        return System.Linq.Enumerable.Empty<T>();
    }
}
