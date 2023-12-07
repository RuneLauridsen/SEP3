using GameClient.DTO;
using Shared.Tets;

namespace GameClient.Tests;

public class TestInvites  {
    private TestClient simon;
    private TestClient maja;
    private int matchId;
    private int participantId;

    public TestInvites() {
        TestUtil.ResetDatabase();
        simon = new TestClient(SIMON);
        maja = new TestClient(MAJA);

        matchId = simon.GameService.CreateMatchAsync(new(TICTACTOE_ID)).Result.match.MatchId;
        participantId = simon.GameService.AddParticipantAsync(new(matchId, MAJA_ID)).Result.participant.ParticipantId;
    }

    [Fact]
    private async Task Test_AcceptInvite() {
        {
            // Before accept, check pending.
            var res = await maja.GameService.GetPendingAsync(new());
            Assert.Equal(res.participants.Count, 1);
            Assert.Equal(res.participants[0].Status, Participant.STATUS_PENDING);
            Assert.Equal(res.participants[0].ParticipantId, participantId);
        }

        {
            // Before accept, check match.
            var res = await maja.GameService.GetMatchAsync(new(matchId));
            Assert.Equal(res.match.Status, Match.STATUS_PENDING);
        }

        {
            // Accept.
            var res = await maja.GameService.DecidePendingAsync(new(matchId, participantId, Participant.STATUS_ACCEPTED));
            Assert.Equal(res.errorReason, "");
        }

        {
            // After accept, check pending.
            var res = await maja.GameService.GetPendingAsync(new());
            Assert.Equal(res.participants.Count, 0);
        }

        {
            // After accept, check match.
            var res = await maja.GameService.GetMatchAsync(new(matchId));
            Assert.Equal(res.match.Status, Match.STATUS_ONGOING);
        }
    }
}
