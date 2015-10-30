package services.dao;

import domain.Annotation;
import domain.TweetEntityBeans;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.sqlite.SQLiteConnection;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class SqliteFactoryImpl implements DataAccessObject
{
    private static final String DATABASE_NAME = "project", TABLE_NAME_TWEET = "tweet";
    private static final String COLUMN_ID = "id", COLUMN_USERNAME = "username", COLUMN_TWEET = "tweet", COLUMN_DATE = "date", COLUMN_KEYWORD = "keyword", COLUMN_ANNOTATION = "annotation", COLUMN_WORDSCOUNT = "wordsCount";

    public SqliteFactoryImpl()
    {
        if (!Files.exists(Paths.get(DATABASE_NAME +".db")))
            try
            {
                SQLiteConnection dbConnection = this.getSQLiteConnection();
                Statement stmt = dbConnection.createStatement();
                stmt.executeUpdate("CREATE TABLE "+ TABLE_NAME_TWEET +
                    "("+ COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " "+ COLUMN_USERNAME +" TEXT NOT NULL," +
                    " "+ COLUMN_TWEET +" TEXT NOT NULL," +
                    " "+ COLUMN_DATE +" DATETIME NOT NULL," +
                    " "+ COLUMN_KEYWORD +" TEXT NOT NULL," +
                    " "+ COLUMN_ANNOTATION +" INTEGER CHECK("+ COLUMN_ANNOTATION +" IN("+ Annotation.NONANNOTE.getValue() +","+ Annotation.NEGATIF.getValue() +","+ Annotation.NEUTRE.getValue() +","+ Annotation.POSITIF.getValue() +")) NOT NULL DEFAULT(-2)," +
                    " "+ COLUMN_WORDSCOUNT + " INTEGER NOT NULL DEFAULT(0))");

                stmt.close();
                dbConnection.close();
            }
            catch (SQLException e) { e.printStackTrace(); }
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
            PreparedStatement stmt = dbConnection.prepareStatement("INSERT INTO " + TABLE_NAME_TWEET +"("+ COLUMN_USERNAME +", "+ COLUMN_TWEET +", "+ COLUMN_DATE +", "+ COLUMN_KEYWORD +", "+ COLUMN_ANNOTATION +", "+ COLUMN_WORDSCOUNT +") VALUES (?, ?, ?, ?, ?, ?);");
            stmt.setString(1, tweetEntityBeans.getUsername());
            stmt.setString(2, tweetEntityBeans.getTweet());
            stmt.setDate(3, new java.sql.Date(tweetEntityBeans.getDate().getTime()));
            stmt.setString(4, tweetEntityBeans.getKeyword());
            stmt.setInt(5, tweetEntityBeans.getAnnotation());
            stmt.setInt(6, tweetEntityBeans.getWordsCount());
            stmt.executeUpdate();
            stmt.close();
            dbConnection.close();

            return true;
        }
        catch (SQLException e) { e.printStackTrace(); }

        return false;
    }

    @Override
    public List<TweetEntityBeans> all()
    {
        ObservableList<TweetEntityBeans> response = FXCollections.observableArrayList();

        try
        {
            SQLiteConnection dbConnection = this.getSQLiteConnection();
            Statement stmt = dbConnection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, username, tweet, date, keyword, annotation, wordsCount FROM "+ TABLE_NAME_TWEET);
            while (rs.next())
                response.add(new TweetEntityBeans(rs.getInt(COLUMN_ID), rs.getString(COLUMN_USERNAME), rs.getString(COLUMN_TWEET), rs.getTimestamp(COLUMN_DATE), rs.getString(COLUMN_KEYWORD), Annotation.values()[(rs.getInt(COLUMN_ANNOTATION) < 0 ) ? (2 - Math.abs(rs.getInt(COLUMN_ANNOTATION))) : (rs.getInt(COLUMN_ANNOTATION) + 2) ]));

            stmt.close();
            dbConnection.close();
        }
        catch (SQLException e) { e.printStackTrace(); }

        return response;
    }
}