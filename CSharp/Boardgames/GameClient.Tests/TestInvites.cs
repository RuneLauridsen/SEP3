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
        await Test_CheckBeforeAfterDecide(Participant.STATUS_ACCEPTED, 0, Match.STATUS_ONGOING);
    }

    [Fact]
    private async Task Test_RejectInvite() {
        await Test_CheckBeforeAfterDecide(Participant.STATUS_REJECTED, 0, Match.STATUS_PENDING);
    }


    private async Task Test_CheckBeforeAfterDecide(
        int decideStatus,
        int expectPendingCountAfter,
        int expectMatchStatusAfter
        ) {
        {
            // Before decide, check pending.
            var res = await maja.GameService.GetPendingAsync(new());
            Assert.Equal(res.participants.Count, 1);
            Assert.Equal(res.participants[0].Status, Participant.STATUS_PENDING);
            Assert.Equal(res.participants[0].ParticipantId, participantId);
        }

        {
            // Before decide, check match.
            var res = await maja.GameService.GetMatchAsync(new(matchId));
            Assert.Equal(res.match.Status, Match.STATUS_PENDING);
        }

        {
            // Decide.
            var res = await maja.GameService.DecidePendingAsync(new(matchId, participantId, decideStatus));
            Assert.Equal(res.errorReason, "");
        }

        {
            // After decide, check pending.
            var res = await maja.GameService.GetPendingAsync(new());
            Assert.Equal(res.participants.Count, expectPendingCountAfter);
        }

        {
            // After decide, check match.
            var res = await maja.GameService.GetMatchAsync(new(matchId));
            Assert.Equal(res.match.Status, expectMatchStatusAfter);
        }
    }
}
