global using Xunit;
global using static Shared.Data.Messages;
global using static Shared.Tets.TestData;

// NOTE(rune): Kør ikke test parallelt, da de alle bruger samme SQL database.
// https://stackoverflow.com/questions/1408175/execute-unit-tests-serially-rather-than-in-parallel
[assembly: CollectionBehavior(DisableTestParallelization = true)]
