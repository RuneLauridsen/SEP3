using GameClient.DTO;
using GameClient.Games;
using GameClient.Services;
using Shared.Tets;

namespace GameClient.Tests;

public class PlayGameTest {
    private TestClient rune;
    private TestClient maja;
    private int matchId;
    private int participantId;

    public PlayGameTest() {
        TestUtil.ResetDatabase();
        rune = new TestClient(RUNE);
        maja = new TestClient(MAJA);
    }

    [Fact]
    private async Task Test_CreateMatch_Sunny() {

        // 1. Tryk “Create match”
        // N/A

        // 2. Trykker på “Create match” under TicTacToe
        {
            CreateMatchRequest req = new(TICTACTOE_ID);
            CreateMatchResponse res = await rune.GameService.CreateMatchAsync(req);
            Assert.Equal(res.errorReason, "");
            matchId = res.match.MatchId;
        }

        // 3. Tryk “Invite” på den ved siden af Maja123
        {
            AddParticipantRequest req = new(matchId, MAJA_ID);
            AddParticipantResponse res = await rune.GameService.AddParticipantAsync(req);
            Assert.Equal(res.errorReason, "");
            participantId = res.participant.ParticipantId;
        }

        // 4. Bruger går tilbage til forsiden hvor en Match vil være
        {
            GetMyMatchesRequest req = new();
            GetMyMatchesResponse res = await rune.GameService.GetMyMatchesAsync(req);
            Assert.Equal(res.matches.Count, 1);
            Assert.Equal(res.matches[0].MatchId, matchId);
        }
    }

    [Fact]
    private async Task Test_PlayMatch() {

        // Rune: 1. Brug “Create Match” sunny case
        await Test_CreateMatch_Sunny();

        // Maja: 1. Tryk “Accept” på invitation
        await maja.GameService.DecidePendingAsync(new(matchId, participantId, Participant.STATUS_ACCEPTED));

        // Rune: 2. Tryk på  “Tictactoe” med participents rune og Maja123
        // N/A

        // Maja: 2. Tryk på “Tictactoe” med participents rune og Maja123
        // N/A

        // Rune: 3. Tryk “Play”
        // N/A

        // Maja: 3. Tryk “Play”
        // N/A

        // Rune: 4. Tryk i feltet i første række og kolonne
        await Move(rune, 1, 1);

        // Maja: 4. Tryk i feltet anden række og kolonne
        await Move(maja, 2, 2);

        // Rune: 5. Tryk i feltet i tredje række og første kolonne
        await Move(rune, 3, 1);

        // Maja: 5. Tryk i feltet i anden række, tredje kolonne
        await Move(maja, 2, 3);

        // Rune: 6. Tryk i feltet i anden række og første kolonne
        await Move(rune, 2, 1);

        // Rune: 7. Systemmet sender en besked “Match is finished”
        {
            GetMatchRequest req = new(matchId);
            GetMatchResponse res = await rune.GameService.GetMatchAsync(req);
            Assert.Equal(res.match.Status, Match.STATUS_FINISHED);
        }

        // Maja: 6. Systemmet sender en besked “Match is finished”
        {
            GetMatchRequest req = new(matchId);
            GetMatchResponse res = await maja.GameService.GetMatchAsync(req);
            Assert.Equal(res.match.Status, Match.STATUS_FINISHED);
        }
    }

    private async Task Move(TestClient client, int row, int column) {
        int bit = TicTacToeIdx(row, column);
        MoveRequest req = new(matchId, JsonUtil.ToJson(new TicTacToeMove {
            PlaceOnIndex = bit,
            TakeFromIndex = -1,
        }));

        MoveResponse res = await client.GameService.MoveAsync(req);
        Assert.Equal(res.result.InvalidMoveText, "");
    }

    private static int TicTacToeIdx(int row, int column) {
        int idx = (row - 1) + (column - 1) * 3;
        Assert.True(idx >= 0 && idx < 9);
        return idx;
    }
}
