package services.dao.sqlite;

import domain.Annotation;
import org.sqlite.SQLiteConnection;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.Statement;

public class DaoSqliteFactory
{
    public static final String PACKAGE = "services.dao.sqlite";
    private static final String DATABASE_NAME = "project.sqlite";
    private DaoSqliteFactory() {}

    private static SQLiteConnection getSubSQLiteConnection() throws SQLException
    {
        return new SQLiteConnection("/", DATABASE_NAME);
    }

    public static SQLiteConnection getSQLiteConnection() throws SQLException
    {
        if (!Files.exists(Paths.get(DATABASE_NAME)))
        {
            try
            {
                SQLiteConnection dbConnection = getSubSQLiteConnection();
                Statement stmt = dbConnection.createStatement();

                stmt.executeUpdate("CREATE TABLE " + TweetSqliteDaoImpl.TABLE_NAME_TWEET +
                        "(" + TweetSqliteDaoImpl.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " " + TweetSqliteDaoImpl.COLUMN_USERNAME + " TEXT NOT NULL," +
                        " " + TweetSqliteDaoImpl.COLUMN_TWEET + " TEXT NOT NULL," +
                        " " + TweetSqliteDaoImpl.COLUMN_DATE + " DATETIME NOT NULL," +
                        " " + TweetSqliteDaoImpl.COLUMN_KEYWORD + " TEXT NOT NULL," +
                        " " + TweetSqliteDaoImpl.COLUMN_ANNOTATION + " INTEGER CHECK(" + TweetSqliteDaoImpl.COLUMN_ANNOTATION + " IN(" + Annotation.NONANNOTE.getValue() + "," + Annotation.NEGATIF.getValue() + "," + Annotation.NEUTRE.getValue() + "," + Annotation.POSITIF.getValue() + ")) NOT NULL DEFAULT(-2))");

                stmt.executeUpdate("CREATE TABLE " + DictionarySqliteDaoImpl.TABLE_NAME_DICTIONARY +
                        "(" + DictionarySqliteDaoImpl.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " " + DictionarySqliteDaoImpl.COLUMN_WORD + " TEXT NOT NULL," +
                        " " + DictionarySqliteDaoImpl.COLUMN_ANNOTATION + " INTEGER CHECK(" + TweetSqliteDaoImpl.COLUMN_ANNOTATION + " IN(" + Annotation.NONANNOTE.getValue() + "," + Annotation.NEGATIF.getValue() + "," + Annotation.NEUTRE.getValue() + "," + Annotation.POSITIF.getValue() + ")) NOT NULL DEFAULT(-2))");

                stmt.executeUpdate("CREATE TABLE " + VocabularySqliteDaoImpl.TABLE_NAME_VOCABULARY +
                        "(" + VocabularySqliteDaoImpl.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " " + VocabularySqliteDaoImpl.COLUMN_WORD + " TEXT NOT NULL," +
                        " " + VocabularySqliteDaoImpl.COLUMN_POSOCC + " INTEGER NOT NULL," +
                        " " + VocabularySqliteDaoImpl.COLUMN_NEGOCC + " INTEGER NOT NULL," +
                        " " + VocabularySqliteDaoImpl.COLUMN_NEUOCC + " INTEGER NOT NULL," +
                        " " + VocabularySqliteDaoImpl.COLUMN_GRAMME + " INTEGER NOT NULL);");
                stmt.executeUpdate("CREATE UNIQUE INDEX "+ VocabularySqliteDaoImpl.TABLE_NAME_VOCABULARY +"_idx ON "+ VocabularySqliteDaoImpl.TABLE_NAME_VOCABULARY +"("+ VocabularySqliteDaoImpl.COLUMN_WORD +");");

                stmt.close();
                dbConnection.close();
            }
            catch (SQLException e) {}
        }

        return getSubSQLiteConnection();
    }
}
