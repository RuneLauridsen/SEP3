using GameClient.DTO;
using GameClient.Services;
using Shared.AuthService;
using Shared.AuthState;
using Shared.Data;
using Shared.Tets;

namespace GameClient.Tests;

public class UnitTest1 {
    private TestClient client;

    public UnitTest1() {
        TestUtil.ResetDatabase();
        client = new TestClient(SIMON);
    }

    // TODO(rune): Test_Login
    // TODO(rune): Test_Register

    [Fact]
    public void Test_GetGames() {
        var res = client.GameService.GetGamesAsync(new()).Result;
        var games = res.games;
        Assert.Equal(2, games.Count);
    }

    [Fact]
    public void Test_CreateMatch_GetMatch() {
        int matchId;

        {
            var res = client.GameService.CreateMatchAsync(new(TICTACTOE_ID)).Result;

            Assert.Equal(res.errorReason, "");
            matchId = res.match.MatchId;
        }

        {
            var res = client.GameService.GetMatchAsync(new(matchId)).Result;

            Assert.Equal(res.match.Participants.Count, 1);
            Assert.Equal(res.match.Participants[0].AccountId, SIMON_ID);
            Assert.Equal(res.match.Participants[0].Status, Participant.STATUS_ACCEPTED);
        }

    }

    // TODO(rune): Test_GetMatch
    // TODO(rune): Test_GetMyMatches
    // TODO(rune): Test_CreateMatch
    // TODO(rune): Test_AddParticipant
    // TODO(rune): Test_GetParticipants
    // TODO(rune): Test_GetPending
    // TODO(rune): Test_DecidePending
    // TODO(rune): Test_GetAccount
    
    [Fact]
    public void Test_GetAccount()
    {
        var req = new GetAccountRequest(1);
        var res = gameService.GetAccountAsync(req).Result;
        var account = res.account;
        Assert.Equal(1, account.AccountId);
        
    }
    // TODO(rune): Test_GetAccounts
    // TODO(rune): Test_UpdateAccount
    // TODO(rune): Test_Move
    // TODO(rune): Test_ImpatientWin
    // TODO(rune): Test_GetScoreSums
    // TODO(rune): Test_GetMatchHistory
    // TODO(rune): Test_BeginLiveUpdate
    // TODO(rune): Test_UpdateUserStatus
}
