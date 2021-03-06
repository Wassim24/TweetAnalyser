package services.dao.sqlite;

import domain.Annotation;
import domain.Tweet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.sqlite.SQLiteConnection;
import services.dao.TweetDao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TweetSqliteDaoImpl implements TweetDao
{
    public static final String TABLE_NAME_TWEET = "tweet";
    public static final String COLUMN_ID = "id", COLUMN_USERNAME = "username", COLUMN_TWEET = "tweet", COLUMN_DATE = "date", COLUMN_KEYWORD = "keyword", COLUMN_ANNOTATION = "annotation";

    public TweetSqliteDaoImpl() {}

    @Override
    public boolean add(Tweet tweet)
    {
        try
        {
            SQLiteConnection dbConnection = DaoSqliteFactory.getSQLiteConnection();

            PreparedStatement stmt = dbConnection.prepareStatement("INSERT INTO " + TABLE_NAME_TWEET +"("+ COLUMN_USERNAME +", "+ COLUMN_TWEET +", "+ COLUMN_DATE +", "+ COLUMN_KEYWORD +", "+ COLUMN_ANNOTATION +") VALUES (?, ?, ?, ?, ?);");
            stmt.setString(1, tweet.getUsername());
            stmt.setString(2, tweet.getTweet());
            stmt.setDate(3, new java.sql.Date(tweet.getDate().getTime()));
            stmt.setString(4, tweet.getKeyword());
            stmt.setInt(5, tweet.getAnnotationValue());
            stmt.executeUpdate();

            stmt.close();
            dbConnection.close();
            return true;
        }
        catch (SQLException e) {}

        return false;
    }

    @Override
    public boolean update(Tweet tweet) {

        try
        {
            SQLiteConnection dbConnection = DaoSqliteFactory.getSQLiteConnection();

            PreparedStatement stmt = dbConnection.prepareStatement("UPDATE " + TABLE_NAME_TWEET +" SET " + COLUMN_ANNOTATION + " = " + tweet.getAnnotation().getValue() + " WHERE " + COLUMN_ID + " = " + tweet.getId());
            stmt.executeUpdate();

            stmt.close();
            dbConnection.close();
            return true;
        }
        catch (SQLException e) {}

        return false;
    }

    @Override
    public List<Tweet> getAll()
    {
        ObservableList<Tweet> response = FXCollections.observableArrayList();

        try
        {
            SQLiteConnection dbConnection = DaoSqliteFactory.getSQLiteConnection();
            Statement stmt = dbConnection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT "+ COLUMN_ID +", "+ COLUMN_USERNAME +", "+ COLUMN_TWEET +", "+ COLUMN_DATE +", "+ COLUMN_KEYWORD +", "+ COLUMN_ANNOTATION +" FROM "+ TABLE_NAME_TWEET);
            while (rs.next())
                response.add(new Tweet(rs.getInt(COLUMN_ID), rs.getString(COLUMN_USERNAME), rs.getString(COLUMN_TWEET), rs.getTimestamp(COLUMN_DATE), rs.getString(COLUMN_KEYWORD), Annotation.values()[(rs.getInt(COLUMN_ANNOTATION) < 0 ) ? (2 - Math.abs(rs.getInt(COLUMN_ANNOTATION))) : (rs.getInt(COLUMN_ANNOTATION) + 2)]));

            stmt.close();
            dbConnection.close();
        }
        catch (SQLException e) {}

        return response;
    }

    @Override
    public List<Tweet> getBetween(int start, int limit)
    {
        List<Tweet> response = new ArrayList<>();

        try
        {
            SQLiteConnection dbConnection = DaoSqliteFactory.getSQLiteConnection();
            Statement stmt = dbConnection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT "+ COLUMN_ID +", "+ COLUMN_USERNAME +", "+ COLUMN_TWEET +", "+ COLUMN_DATE +", "+ COLUMN_KEYWORD +", "+ COLUMN_ANNOTATION +" FROM "+ TABLE_NAME_TWEET + " LIMIT " + start + ", " + limit);
            while (rs.next())
                response.add(new Tweet(rs.getInt(COLUMN_ID), rs.getString(COLUMN_USERNAME), rs.getString(COLUMN_TWEET), rs.getTimestamp(COLUMN_DATE), rs.getString(COLUMN_KEYWORD), Annotation.values()[(rs.getInt(COLUMN_ANNOTATION) < 0 ) ? (2 - Math.abs(rs.getInt(COLUMN_ANNOTATION))) : (rs.getInt(COLUMN_ANNOTATION) + 2)]));

            stmt.close();
            dbConnection.close();
        }
        catch (SQLException e) {}

        return response;
    }

    @Override
    public List<Tweet> getAll(Annotation annotation)
    {
        ObservableList<Tweet> response = FXCollections.observableArrayList();

        try
        {
            SQLiteConnection dbConnection = DaoSqliteFactory.getSQLiteConnection();
            Statement stmt = dbConnection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM "+ TABLE_NAME_TWEET + " WHERE annotation = " + annotation.getValue());
            while (rs.next())
                response.add(new Tweet(rs.getInt(COLUMN_ID), rs.getString(COLUMN_USERNAME), rs.getString(COLUMN_TWEET), rs.getTimestamp(COLUMN_DATE), rs.getString(COLUMN_KEYWORD), Annotation.values()[(rs.getInt(COLUMN_ANNOTATION) < 0 ) ? (2 - Math.abs(rs.getInt(COLUMN_ANNOTATION))) : (rs.getInt(COLUMN_ANNOTATION) + 2)]));

            stmt.close();
            dbConnection.close();
        }
        catch (SQLException e) {}

        return response;
    }

    @Override
    public List<Tweet> getAll(Annotation annotation, String keyword)
    {
        ObservableList<Tweet> response = FXCollections.observableArrayList();

        try
        {
            SQLiteConnection dbConnection = DaoSqliteFactory.getSQLiteConnection();
            Statement stmt = dbConnection.createStatement();
            ResultSet rs = (keyword.isEmpty()) ? stmt.executeQuery("SELECT * FROM "+ TABLE_NAME_TWEET + " WHERE annotation = " + annotation.getValue()) : stmt.executeQuery("SELECT * FROM "+ TABLE_NAME_TWEET + " WHERE annotation = " + annotation.getValue() + " and keyword = '" + keyword +"'");
            while (rs.next())
                response.add(new Tweet(rs.getInt(COLUMN_ID), rs.getString(COLUMN_USERNAME), rs.getString(COLUMN_TWEET), rs.getTimestamp(COLUMN_DATE), rs.getString(COLUMN_KEYWORD), Annotation.values()[(rs.getInt(COLUMN_ANNOTATION) < 0 ) ? (2 - Math.abs(rs.getInt(COLUMN_ANNOTATION))) : (rs.getInt(COLUMN_ANNOTATION) + 2)]));

            stmt.close();
            dbConnection.close();
        }
        catch (SQLException e) {}

        return response;
    }
}