package services.dao.sqlite;


import domain.Vocabulary;
import org.sqlite.SQLiteConnection;
import services.dao.VocabularyDao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class VocabularySqliteDaoImpl implements VocabularyDao {

    public static final String TABLE_NAME_VOCABULARY = "vocabulary";
    public static final String COLUMN_ID = "id", COLUMN_WORD = "word", COLUMN_POSOCC = "posocc", COLUMN_NEGOCC = "negocc", COLUMN_NEUOCC = "neuocc";

    public VocabularySqliteDaoImpl() {}

    @Override
    public boolean addAll(List<Vocabulary> vocabularies)
    {
        vocabularies.forEach(vocabulary -> this.add(vocabulary));
        return true;
    }

    @Override
    public boolean add(Vocabulary vocabulary)
    {
        try
        {
            SQLiteConnection dbConnection = DaoSqliteFactory.getSQLiteConnection();
            PreparedStatement stmt = dbConnection.prepareStatement("INSERT INTO " + TABLE_NAME_VOCABULARY +"("+ COLUMN_WORD +", "+ COLUMN_POSOCC + ", "+  COLUMN_NEGOCC +", "+ COLUMN_NEUOCC+ " ) VALUES (?, ?, ?, ?);");
            stmt.setString(1, vocabulary.getWord());
            stmt.setInt(2, vocabulary.getPosocc());
            stmt.setInt(3, vocabulary.getNegocc());
            stmt.setInt(3, vocabulary.getNeuocc());
            stmt.executeUpdate();
            stmt.close();
            dbConnection.close();

            return true;
        }
        catch (SQLException e) { e.printStackTrace(); }

        return false;
    }

    @Override
    public List<Vocabulary> get()
    {
        List<Vocabulary> response = new ArrayList<>();

        try
        {
            SQLiteConnection dbConnection = DaoSqliteFactory.getSQLiteConnection();
            Statement stmt = dbConnection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT word, posocc, negocc, neuocc FROM "+ TABLE_NAME_VOCABULARY);
            while (rs.next())
            {
                Vocabulary word = new Vocabulary();

                word.setWord(rs.getString(COLUMN_WORD));
                word.setPosocc(rs.getInt(COLUMN_POSOCC));
                word.setNegocc(rs.getInt(COLUMN_NEGOCC));
                word.setNeuocc(rs.getInt(COLUMN_NEGOCC));

                response.add(word);
            }

            stmt.close();
            dbConnection.close();
        }
        catch (SQLException e) { }

        return response;
    }
}
