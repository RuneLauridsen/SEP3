﻿global using static GameClient.Data.Messages;
using GameClient.DTO;

namespace GameClient.Data;

// NOTE(rune): Syntax i Java og C# er tæt nok på hinanden til, at vi bare kan
// copy paste nedenstående kode mellem Message.java og Message.cs.
public class Messages {
    public record LoginRequest(String username, String password) { }
    public record LoginResponse(Account account, String jwt) { }

    public record RegisterRequest(String username, String firstName, String lastName, String email, String password);

   //Todo, lav response ordenlig
    public record RegisterResponse(String response);

    public record MoveRequest(int matchId, String gameState, String jwt) { }
    public record MoveResponse(int matchId, String gameState, String invalidMoveText) { }

    public record GetMatchesRequest(String jwt) { }
    public record GetMatchesResponse(List<Match> matches) { }
}