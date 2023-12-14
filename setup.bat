@echo off

:: Java
pushd . 
cd .\Java\boardgames
call mvn -f shared\pom.xml      clean install
call mvn -f persistence\pom.xml clean install 
call mvn -f logic\pom.xml       clean install
popd

:: C#
@echo off
pushd .
cd CSharp\boardgames
dotnet build GameClient
dotnet build AdminClient
popd