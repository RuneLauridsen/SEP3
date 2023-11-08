﻿using static GameClient.Data.Messages;

namespace GameClient.Data;

public interface IGameService {
    public LoginResponse Login(LoginRequest request);
    public MoveResponse Move(MoveRequest request);
    public GetMatchesResponse GetMatches(GetMatchesRequest request);
    public CreateMatchResponse CreateMatch(CreateMatchRequest request);
    public GetGamesResponse GetGames(GetGamesRequest request);
}
