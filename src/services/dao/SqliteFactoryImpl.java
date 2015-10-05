package services.dao;

import org.sqlite.SQLiteConnection;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class SqliteFactoryImpl implements DatabaseFactory
{
    private static final String DATABASE_NAME = "project", TABLE_NAME_TWEET = "tweet";
    private static SqliteFactoryImpl sqliteFactoryImpl = null;

    private SqliteFactoryImpl()
    {
        try
        {
            SQLiteConnection dbConnection = this.getSQLiteConnection();
            Statement stmt = dbConnection.createStatement();
            stmt.executeUpdate("CREATE TABLE tweet" +
                    "(id INT PRIMARY KEY NOT NULL," +
                    " username TEXT NOT NULL," +
                    " tweet TEXT NOT NULL," +
                    " date DATETIME NOT NULL," +
                    " keyword TEXT NOT NULL)");
            stmt.close();
            dbConnection.close();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private SQLiteConnection getSQLiteConnection() throws SQLException
    {
        return new SQLiteConnection("/", DATABASE_NAME +".db");
    }

    public static SqliteFactoryImpl getInstance()
    {
        if (sqliteFactoryImpl == null)
            sqliteFactoryImpl = new SqliteFactoryImpl();

        return sqliteFactoryImpl;
    }

    @Override
    public boolean add(String username, String tweet, Date date, String keyword)
    {
        /*try
        {
            SQLiteConnection dbConnection = this.getSQLiteConnection();
            Statement stmt = dbConnection.createStatement();
            stmt.executeUpdate("INSERT INTO " + TABLE_NAME_TWEET + "()");
            stmt.close();
            dbConnection.close();
        } catch (SQLException e) { e.printStackTrace(); }*/
        return false;
    }
}