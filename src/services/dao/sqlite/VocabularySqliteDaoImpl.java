package services.dao.sqlite;

import domain.Vocabulary;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.sqlite.SQLiteConnection;
import services.dao.VocabularyDao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class VocabularySqliteDaoImpl implements VocabularyDao
{
    public static final String TABLE_NAME_VOCABULARY = "vocabulary";
    public static final String COLUMN_ID = "id", COLUMN_WORD = "word", COLUMN_POSOCC = "posocc", COLUMN_NEGOCC = "negocc", COLUMN_NEUOCC = "neuocc", COLUMN_GRAMME = "gramme";

    public VocabularySqliteDaoImpl() {}

    @Override
    public boolean addAll(List<Vocabulary> vocabularies)
    {
        for (Vocabulary vocabulary : vocabularies)
            if (this.add(vocabulary) == false)
                return false;

        return true;
    }

    @Override
    public boolean add(Vocabulary vocabulary)
    {
        try
        {
            SQLiteConnection dbConnection = DaoSqliteFactory.getSQLiteConnection();
            PreparedStatement stmt = dbConnection.prepareStatement("INSERT OR REPLACE INTO " + TABLE_NAME_VOCABULARY +"("+ COLUMN_WORD +", "+ COLUMN_POSOCC + ", "+  COLUMN_NEGOCC +", "+ COLUMN_NEUOCC+ ", "+ COLUMN_GRAMME + ") VALUES (?, ?, ?, ?, ?);");
            stmt.setString(1, vocabulary.getWord());
            stmt.setInt(2, vocabulary.getPosocc());
            stmt.setInt(3, vocabulary.getNegocc());
            stmt.setInt(4, vocabulary.getNeuocc());
            stmt.setInt(5, vocabulary.getNgramme());

            stmt.executeUpdate();
            stmt.close();
            dbConnection.close();

            return true;
        }
        catch (SQLException e) { e.printStackTrace(); }

        return false;
    }

    @Override
    public Vocabulary get(String word)
    {
        Vocabulary response = null;

        try
        {
            SQLiteConnection dbConnection = DaoSqliteFactory.getSQLiteConnection();
            Statement stmt = dbConnection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT "+ COLUMN_ID +", "+ COLUMN_WORD +", "+ COLUMN_POSOCC +", "+ COLUMN_NEGOCC +", "+ COLUMN_NEGOCC +", "+ COLUMN_GRAMME +" FROM "+ TABLE_NAME_VOCABULARY +" WHERE "+ COLUMN_WORD +" = '"+ word +"'");
            response = new Vocabulary(rs.getInt(COLUMN_ID), rs.getString(COLUMN_WORD), rs.getInt(COLUMN_POSOCC), rs.getInt(COLUMN_NEGOCC), rs.getInt(COLUMN_NEGOCC), rs.getInt(COLUMN_GRAMME));

            stmt.close();
            dbConnection.close();
        }
        catch (SQLException e) { }

        return response;
    }

    @Override
    public List<Vocabulary> getAll()
    {
        ObservableList<Vocabulary> response = FXCollections.observableArrayList();

        try
        {
            SQLiteConnection dbConnection = DaoSqliteFactory.getSQLiteConnection();
            Statement stmt = dbConnection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT "+ COLUMN_ID +", "+ COLUMN_WORD +", "+ COLUMN_POSOCC +", "+ COLUMN_NEGOCC +", "+ COLUMN_NEUOCC +", "+ COLUMN_GRAMME +" FROM "+ TABLE_NAME_VOCABULARY);
            while (rs.next())
                response.add(new Vocabulary(rs.getInt(COLUMN_ID), rs.getString(COLUMN_WORD), rs.getInt(COLUMN_POSOCC), rs.getInt(COLUMN_NEGOCC), rs.getInt(COLUMN_NEUOCC), rs.getInt(COLUMN_GRAMME)));

            stmt.close();
            dbConnection.close();
        }
        catch (SQLException e) { }

        return response;
    }

    @Override
    public List<Vocabulary> getAll(int ngramme)
    {
        ObservableList<Vocabulary> response = FXCollections.observableArrayList();

        try
        {
            SQLiteConnection dbConnection = DaoSqliteFactory.getSQLiteConnection();
            Statement stmt = dbConnection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT "+ COLUMN_ID +", "+ COLUMN_WORD +", "+ COLUMN_POSOCC +", "+ COLUMN_NEGOCC +", "+ COLUMN_NEUOCC +", "+ COLUMN_GRAMME +" FROM "+ TABLE_NAME_VOCABULARY +" WHERE "+ COLUMN_GRAMME +" = "+ ngramme);
            while (rs.next())
                response.add(new Vocabulary(rs.getInt(COLUMN_ID), rs.getString(COLUMN_WORD), rs.getInt(COLUMN_POSOCC), rs.getInt(COLUMN_NEGOCC), rs.getInt(COLUMN_NEUOCC), rs.getInt(COLUMN_GRAMME)));

            stmt.close();
            dbConnection.close();
        }
        catch (SQLException e) { }

        return response;
    }
}
