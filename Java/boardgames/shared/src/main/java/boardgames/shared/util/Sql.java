package boardgames.shared.util;

import org.intellij.lang.annotations.Language;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/*
    NOTE(rune):

    Wrapper rundt om java.sql.PreparedStatement og java.sql.ResultSet,
    med følgende forskelle:

    - Lukker automatisk efter execute(), querySingle() og queryAll().

    - Alle exceptions undervejs bliver spist, indtil man kalder execute(),
      querySingle() eller queryAll(), så bruger-koden kun skal tjekke efter
      fejl ved et failure point.

    - Hvis man giver null som argument til set() bliver det oversat til
      en jdbc setNull(), så bruger-koden ikke selv skal have if-sætninger
      rundt om hvert parameter som må være null.

    - Parameter index i setInt(), setString() osv. bliver automatisk,
      incremented hver gang man kalder en af set() metode.

    Eksempel:

        Sql sql = new Sql(connection, "SELECT col_a, col_b FROM foo WHERE col_c > ? AND col_d LIKE ?");
        sql.set(2); // col_c > 2
        sql.set("%bar%"); // col_d LIKE '%bar%'
        List<Foo> foos = sql.queryAll(x -> new Foo(x.readString("col_a"), x.readString("col_b")));
        return foos;
 */

public class Sql {
    private Connection connection;
    private ConnectionPool pool;
    private PreparedStatement statement;
    private ResultSet resultSet;
    private Exception caughtException;
    private int autoIndex;

    public Sql(ConnectionPool pool, @Language("SQL") String sql) {
        try {
            this.pool = pool;

            // Det er brugerens ansvar altid at kalde, execute(), querySingle(),
            // queryAll() eller close(), for at release connection igen.
            connection = pool.acquireConnection();
            if (connection == null) {
                throw new SQLException("Connection was null");
            }
            this.statement = connection.prepareStatement(sql);
        } catch (Exception e) {
            this.caughtException = e;
        }

        this.autoIndex = 1;
    }

    public Sql set(int v) { return setIndex(autoIndex++, v); }
    public Sql set(double v) { return setIndex(autoIndex++, v); }
    public Sql set(Integer v) { return setIndex(autoIndex++, v); }
    public Sql set(Double v) { return setIndex(autoIndex++, v); }
    public Sql set(String v) { return setIndex(autoIndex++, v); }
    public Sql set(LocalDate v) { return setIndex(autoIndex++, v); }
    public Sql set(LocalDateTime v) { return setIndex(autoIndex++, v); }
    public Sql set(byte[] v) { return setIndex(autoIndex++, v); }

    public Sql setIndex(int parameterIndex, int v) {
        if (caughtException == null) {
            try {
                statement.setInt(parameterIndex, v);
            } catch (Exception e) {
                caughtException = e;
            }
        }

        return this;
    }

    public Sql setIndex(int parameterIndex, double v) {
        if (caughtException == null) {
            try {
                statement.setDouble(parameterIndex, v);
            } catch (Exception e) {
                caughtException = e;
            }
        }

        return this;
    }

    public Sql setIndex(int parameterIndex, Integer v) {
        if (caughtException == null) {
            try {
                if (v == null) {
                    statement.setNull(parameterIndex, Types.INTEGER);
                } else {
                    statement.setInt(parameterIndex, v);
                }
            } catch (Exception e) {
                caughtException = e;
            }
        }

        return this;
    }

    public Sql setIndex(int parameterIndex, Double v) {
        if (caughtException == null) {
            try {
                if (v == null) {
                    statement.setNull(parameterIndex, Types.DOUBLE);
                } else {
                    statement.setDouble(parameterIndex, v);
                }
            } catch (Exception e) {
                caughtException = e;
            }
        }

        return this;
    }

    public Sql setIndex(int parameterIndex, String v) {
        if (caughtException == null) {
            try {
                if (v == null) {
                    statement.setNull(parameterIndex, Types.VARCHAR);
                } else {
                    statement.setString(parameterIndex, v);
                }
            } catch (Exception e) {
                caughtException = e;
            }
        }

        return this;
    }

    public Sql setIndex(int parameterIndex, LocalDate v) {
        if (caughtException == null) {
            try {
                if (v == null) {
                    statement.setNull(parameterIndex, Types.DATE);
                } else {
                    statement.setDate(parameterIndex, Date.valueOf(v));
                }
            } catch (Exception e) {
                caughtException = e;
            }
        }

        return this;
    }

    public Sql setIndex(int parameterIndex, LocalDateTime v) {
        if (caughtException == null) {
            try {
                if (v == null) {
                    statement.setNull(parameterIndex, Types.TIMESTAMP);
                } else {
                    statement.setTimestamp(parameterIndex, Timestamp.valueOf(v));
                }
            } catch (Exception e) {
                caughtException = e;
            }
        }

        return this;
    }

    public Sql setIndex(int parameterIndex, byte[] v) {
        if (caughtException == null) {
            try {
                if (v == null) {
                    statement.setNull(parameterIndex, Types.VARBINARY);
                } else {
                    statement.setBytes(parameterIndex, v);
                }
            } catch (Exception e) {
                caughtException = e;
            }
        }

        return this;
    }

    public boolean next() {
        if (caughtException == null) {
            try {
                // NOTE(rune): Først gange man kalder .next()
                if (resultSet == null) {
                    executeQuery();
                }

                if (resultSet != null) {
                    return resultSet.next();
                } else {
                    return false;
                }
            } catch (Exception e) {
                caughtException = e;
            }
        }

        return false;
    }

    public int readInt(String columnName) {
        if (caughtException == null) {
            try {
                return resultSet.getInt(columnName);
            } catch (Exception e) {
                caughtException = e;
            }
        }

        return 0;
    }

    public Integer readInteger(String columnName) {
        if (caughtException == null) {
            try {
                int i = resultSet.getInt(columnName);
                if (resultSet.wasNull()) {
                    return null;
                } else {
                    return i;
                }
            } catch (Exception e) {
                caughtException = e;
            }
        }

        return 0;
    }

    public double readDouble(String columnName) {
        if (caughtException == null) {
            try {
                return resultSet.getDouble(columnName);
            } catch (Exception e) {
                caughtException = e;
            }
        }

        return 0;
    }

    public String readString(String columnName) {
        if (caughtException == null) {
            try {
                return resultSet.getString(columnName);
            } catch (Exception e) {
                caughtException = e;
            }
        }

        return null;
    }

    public LocalDate readDate(String columnName) {
        if (caughtException == null) {
            try {
                Date v = resultSet.getDate(columnName);
                if (v == null) {
                    return null;
                } else {
                    return v.toLocalDate();
                }
            } catch (Exception e) {
                caughtException = e;
            }
        }

        return null;
    }

    public LocalDateTime readDateTime(String columnName) {
        if (caughtException == null) {
            try {
                Timestamp v = resultSet.getTimestamp(columnName);
                if (v == null) {
                    return null;
                } else {
                    return v.toLocalDateTime();
                }
            } catch (Exception e) {
                caughtException = e;
            }
        }

        return null;
    }

    public byte[] readBytes(String columnName) {
        if (caughtException == null) {
            try {
                byte[] bytes = resultSet.getBytes(columnName);
                return null;
            } catch (Exception e) {
                caughtException = e;
            }
        }

        return null;
    }

    public boolean readBoolean(String columnName) {
        if (caughtException == null) {
            try {
                boolean b = resultSet.getBoolean(columnName);
                return b;
            } catch (Exception e) {
                caughtException = e;
            }
        }

        return false;
    }

    private void executeQuery() {
        if (caughtException == null) {
            try {
                resultSet = statement.executeQuery();
            } catch (Exception e) {
                caughtException = e;
            }
        }
    }

    public int execute() {
        Timer.begin(1);

        int ret = 0;
        if (caughtException == null) {
            try {
                ret = statement.executeUpdate();
            } catch (Exception e) {
                caughtException = e;
                Log.error(e);
            }
        }
        close();

        Timer.endAndPrint();

        if (caughtException != null) {
            Log.error(caughtException);
        }

        return ret;
    }

    public <T> T querySingle(Function<Sql, T> readWith) {
        Timer.begin(1);

        T ret = null;
        if (next()) {
            ret = readWith.apply(this);
        }
        close();

        Timer.endAndPrint();

        if (caughtException != null) {
            Log.error(caughtException);
        }

        return ret;
    }

    public <T> List<T> queryAll(Function<Sql, T> readWith) {
        Timer.begin(1);

        List<T> ret = new ArrayList<>();
        while (next()) {
            ret.add(readWith.apply(this));
        }
        close();

        Timer.endAndPrint();

        if (caughtException != null) {
            Log.error(caughtException);
        }

        return ret;
    }

    public void throwIfFailed() throws Exception {
        if (caughtException != null) {
            throw caughtException;
        }
    }

    public void close() {
        SqlUtil.close(resultSet);
        SqlUtil.close(statement);

        resultSet = null;
        statement = null;

        pool.releaseConnection(connection);
    }
}
