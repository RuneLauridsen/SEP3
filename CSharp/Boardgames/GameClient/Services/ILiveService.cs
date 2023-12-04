namespace GameClient.Services;

public interface ILiveService {
    public event EventHandler<int>? MatchUpdated;
}
