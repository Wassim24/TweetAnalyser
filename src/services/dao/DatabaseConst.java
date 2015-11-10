package services.dao;

import domain.Annotation;
import org.sqlite.SQLiteConnection;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConst
{
    public static final String DATABASE_NAME = "project";

    public DatabaseConst()  {}

    private static SQLiteConnection getSubSQLiteConnection() throws SQLException
    {
        return new SQLiteConnection("/", DATABASE_NAME +".db");
    }

    public static SQLiteConnection getSQLiteConnection() throws SQLException
    {

        if (!Files.exists(Paths.get(DatabaseConst.DATABASE_NAME +".db")))
        {
            try {
                SQLiteConnection dbConnection = getSubSQLiteConnection();
                Statement stmt = dbConnection.createStatement();
                stmt.executeUpdate("CREATE TABLE " + TweetSqliteDaoImpl.TABLE_NAME_TWEET +
                        "(" + TweetSqliteDaoImpl.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " " + TweetSqliteDaoImpl.COLUMN_USERNAME + " TEXT NOT NULL," +
                        " " + TweetSqliteDaoImpl.COLUMN_TWEET + " TEXT NOT NULL," +
                        " " + TweetSqliteDaoImpl.COLUMN_DATE + " DATETIME NOT NULL," +
                        " " + TweetSqliteDaoImpl.COLUMN_KEYWORD + " TEXT NOT NULL," +
                        " " + TweetSqliteDaoImpl.COLUMN_ANNOTATION + " INTEGER CHECK(" + TweetSqliteDaoImpl.COLUMN_ANNOTATION + " IN(" + Annotation.NONANNOTE.getValue() + "," + Annotation.NEGATIF.getValue() + "," + Annotation.NEUTRE.getValue() + "," + Annotation.POSITIF.getValue() + ")) NOT NULL DEFAULT(-2)," +
                        " " + TweetSqliteDaoImpl.COLUMN_WORDSCOUNT + " INTEGER NOT NULL DEFAULT(0))");

                stmt.executeUpdate("CREATE TABLE " + DictionarySqliteDaoImpl.TABLE_NAME_DICTIONARY +
                        "(" + DictionarySqliteDaoImpl.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " " + DictionarySqliteDaoImpl.COLUMN_WORD + " TEXT NOT NULL," +
                        " " + DictionarySqliteDaoImpl.COLUMN_ANNOTATION + " INTEGER CHECK(" + TweetSqliteDaoImpl.COLUMN_ANNOTATION + " IN(" + Annotation.NONANNOTE.getValue() + "," + Annotation.NEGATIF.getValue() + "," + Annotation.NEUTRE.getValue() + "," + Annotation.POSITIF.getValue() + ")) NOT NULL DEFAULT(-2))");

                stmt.close();
                dbConnection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return getSubSQLiteConnection();
    }
}
