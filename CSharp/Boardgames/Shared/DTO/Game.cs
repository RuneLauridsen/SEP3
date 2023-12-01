namespace GameClient.DTO;

// WARNING(rune): Navne skal matche mellem Java og C#.
public class Game {
    public required int GameId { get; set; }
    public required string Name { get; set; }
    public required string GamePicture { get; set; }
    public required string GamePictureType { get; set; }
}
