package services.dao;

import services.dao.sqlite.DaoSqliteFactory;

public class VocabularyDaoFactory
{
    private static final String DAO_ACCESS = DaoSqliteFactory.PACKAGE +".VocabularySqliteDaoImpl";
    private static VocabularyDao vocabularyDaoImpl = null;
    private VocabularyDaoFactory() {}

    public static VocabularyDao getInstance()
    {
        if (vocabularyDaoImpl == null)
            try
            {
                vocabularyDaoImpl = (VocabularyDao) Class.forName(DAO_ACCESS).newInstance();
            }
            catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {}

        return vocabularyDaoImpl;
    }

}
