namespace Shared;

public class DummyProgram {
    public static void Main() {
        // NOTE:
        // Vi har brug for
        //      <FrameworkReference Include="Microsoft.AspNetCore.App" />
        // for at bruge ProtectedSessionStorage, men med med
        //      <FrameworkReference Include="Microsoft.AspNetCore.App" />
        // kan vi ikke compile uden et entry point.
        //
        // IDK hvorfor Microsoft ikke længere distribuerer ProtectedSessionStorage
        // i en almindelig nuget package...

        throw new Exception("This is not a standalone executable.");
    }
}
