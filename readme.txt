Database setup: 

    Kør database setup script på PostgreSQL serveren:   
        .\Java\boardgames\persistence\src\main\resources\database.sql
    
    Tilpas connection string i: 
        .\Java\boardgames\persistence\src\main\resources\ConnectionString.txt
        .\CSharp\Boardgames\Shared.Tests\ConnectionString.txt
       
Compile:
    
    Kør .\setup.bat (kræver maven og dotnet CLI er installeret)
    
    ELLER
    
    Åbn mappe .\Java\boardgames i IntellJ.
    Compile shared, persistence og logic med IntelliJ.
    
    Åbn solution .\CSharp\Boardgames\Boardgames.sln i Rider.
    Compile GameClient og AdminClient med Rider.
    
Start:

    .\run-adminclient.bat
    .\run-gameclient.bat
    .\run-logic.bat
    .\run-persistence.bat
    
    ELLER
    
    Åbn mappe .\Java\boardgames i IntellJkør med IntelliJ.
    Kør persistence.RunPersistence og logic.RunLogicServer med IntelliJ.
    
    Åbn solution .\CSharp\Boardgames\Boardgames.sln i Rider.
    Kør GameClient og AdminClient med Rider.
        
    persistence kører i debug configuration på port 8080.
    GameClient kører i debug configuration på port 5215.
    AdminClient kører i debug configuration på port 5165.
    
Test:
    
    Start persistence og logic.
    Kør xUnit test i:
        .\CSharp\Boardgames\GameClient.Tests
        .\CSharp\Boardgames\AdminClient.Tests


    
    