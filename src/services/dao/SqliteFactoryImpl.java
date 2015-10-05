package services.dao;

import domain.TweetEntityBeans;
import org.sqlite.SQLiteConnection;
import twitter4j.Status;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SqliteFactoryImpl implements DataAccessObject
{
    private static final String DATABASE_NAME = "project", TABLE_NAME_TWEET = "tweet";

    public SqliteFactoryImpl()
    {
        if (!Files.exists(Paths.get(DATABASE_NAME +".db")))
            try
            {
                SQLiteConnection dbConnection = this.getSQLiteConnection();
                Statement stmt = dbConnection.createStatement();
                stmt.executeUpdate("CREATE TABLE tweet" +
                        "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
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

    @Override
    public boolean add(TweetEntityBeans tweetEntityBeans)
    {
        try
        {
            SQLiteConnection dbConnection = this.getSQLiteConnection();
            PreparedStatement stmt = dbConnection.prepareStatement("INSERT INTO " + TABLE_NAME_TWEET +"(username, tweet, date, keyword) VALUES (?, ?, ?, ?);");
            stmt.setString(1, tweetEntityBeans.getUsername());
            stmt.setString(2, tweetEntityBeans.getTweet());
            stmt.setDate(3, new java.sql.Date(tweetEntityBeans.getDate().getTime()));
            stmt.setString(4, tweetEntityBeans.getKeyword());
            stmt.executeUpdate();
            stmt.close();
            dbConnection.close();

            return true;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    @Override
    public List<TweetEntityBeans> all()
    {
        List<TweetEntityBeans> response = new ArrayList<TweetEntityBeans>();

        try
        {
            SQLiteConnection dbConnection = this.getSQLiteConnection();
            Statement stmt = dbConnection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, username, tweet, date, keyword FROM "+ TABLE_NAME_TWEET);
            while (rs.next())
                response.add(new TweetEntityBeans(rs.getInt("id"), rs.getString("username"), rs.getString("tweet"), rs.getTimestamp("date"), rs.getString("keyword")));

            stmt.close();
            dbConnection.close();
        } catch (SQLException e) { e.printStackTrace(); }

        return response;
    }
}