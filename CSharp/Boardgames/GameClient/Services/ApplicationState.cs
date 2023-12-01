namespace GameClient.Services;

public class ApplicationState
{
    // Er det dumt at have en hel klasse til en property? Ja. Har vi tid til at ændre det? Nej.
    public bool IsDirty { get; set; }
}