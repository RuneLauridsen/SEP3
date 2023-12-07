namespace Shared.Tets;

public class TestData {
   public const int JULIE_ID = 1;
   public const int MAJA_ID = 2;
   public const int SIMON_ID = 3;
   public const int RUNE_ID = 4;
   public const int BOB_ID = 5;

   public static readonly TestUser JULIE = new(JULIE_ID, "BenDover", "julie", false);
   public static readonly TestUser MAJA = new(MAJA_ID, "Maja123", "maja", false);
   public static readonly TestUser SIMON = new(SIMON_ID, "Minii", "simon", false);
   public static readonly TestUser RUNE = new(RUNE_ID, "rune", "runerune", false);
   public static readonly TestUser BOB = new(BOB_ID, "Bob", "bobersej", true);


   public const int TICTACTOE_ID = 1;
   public const int STRATEGO_ID = 2;
}

public class TestUser {
    public int AccountId { get; }
    public string Username { get; }
    public string Password { get; }
    public bool IsAdmin { get; }

    public TestUser(int accountId, string username, string password, bool isAdmin) {
        AccountId = accountId;
        Username = username;
        Password = password;
        this.IsAdmin = isAdmin;
    }
}
