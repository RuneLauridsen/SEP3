﻿using System.Data.SqlClient;
using Npgsql;

namespace Shared.Tets;

public class TestUtil {
    public static void ResetDatabase() {
        string sql = File.ReadAllText("database.sql");
        ExecuteSql(sql);
        Console.WriteLine("Database reset.");
    }

    public static void ExecuteSql(string sql) {
        string connString = File.ReadAllText("ConnectionString.txt");

        using var conn = NpgsqlDataSource.Create(connString);
        using var cmd = conn.CreateCommand(sql);
        cmd.ExecuteNonQuery();
    }
}
