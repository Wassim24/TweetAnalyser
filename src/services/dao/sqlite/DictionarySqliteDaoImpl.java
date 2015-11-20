package services.dao.sqlite;

import domain.Annotation;
import domain.Dictionary;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.sqlite.SQLiteConnection;
import services.dao.DictionaryDao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DictionarySqliteDaoImpl implements DictionaryDao
{
    public static final String TABLE_NAME_DICTIONARY = "dictionary";
    public static final String COLUMN_ID = "id", COLUMN_WORD = "word", COLUMN_ANNOTATION = "annotation";

    public DictionarySqliteDaoImpl() {}

    @Override
    public boolean addAll(Dictionary dictionary)
    {
        for (String word : dictionary.getWords())
            if (!this.add(word, dictionary.getAnnotation()))
                return false;

        return true;
    }

    @Override
    public boolean add(String word, Annotation annotation)
    {
        try
        {
            SQLiteConnection dbConnection = DaoSqliteFactory.getSQLiteConnection();
            PreparedStatement stmt = dbConnection.prepareStatement("INSERT INTO " + TABLE_NAME_DICTIONARY +"("+ COLUMN_WORD +", "+ COLUMN_ANNOTATION +") VALUES (?, ?);");
            stmt.setString(1, word);
            stmt.setInt(2, annotation.getValue());
            stmt.executeUpdate();
            stmt.close();
            dbConnection.close();

            return true;
        }
        catch (SQLException e) {}

        return false;
    }

    @Override
    public Dictionary get(Annotation annotation)
    {
        Dictionary response = new Dictionary(annotation);

        try
        {
            SQLiteConnection dbConnection = DaoSqliteFactory.getSQLiteConnection();
            Statement stmt = dbConnection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT "+ COLUMN_WORD +" FROM "+ TABLE_NAME_DICTIONARY +" WHERE "+ COLUMN_ANNOTATION +" = "+ annotation.getValue());
            while (rs.next())
                response.getWords().add(rs.getString(COLUMN_WORD));

            stmt.close();
            dbConnection.close();
        }
        catch (SQLException e) {}

        return response;
    }

    @Override
    public List<Dictionary> getAll()
    {
        ObservableList<Dictionary> response = FXCollections.observableArrayList();

        try
        {
            SQLiteConnection dbConnection = DaoSqliteFactory.getSQLiteConnection();
            Statement stmt = dbConnection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT "+ COLUMN_ID +", "+ COLUMN_WORD +", "+ COLUMN_ANNOTATION +" FROM "+ TABLE_NAME_DICTIONARY);
            while (rs.next())
                response.add(new Dictionary(rs.getInt(COLUMN_ID), rs.getString(COLUMN_WORD), Annotation.values()[(rs.getInt(COLUMN_ANNOTATION) < 0 ) ? (2 - Math.abs(rs.getInt(COLUMN_ANNOTATION))) : (rs.getInt(COLUMN_ANNOTATION) + 2) ]));

            stmt.close();
            dbConnection.close();
        }
        catch (SQLException e) { e.printStackTrace(); }

        return response;
    }

}