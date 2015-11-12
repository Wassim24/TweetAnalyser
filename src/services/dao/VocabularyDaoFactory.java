package services.dao;

public class VocabularyDaoFactory
{
    private static final String DAO_ACCESS = "VocabularySqliteDaoImpl";
    private static VocabularyDao vocabularyDaoImpl = null;
    private VocabularyDaoFactory() {}

    public static VocabularyDao getInstance()
    {
        if (vocabularyDaoImpl == null)
            try
            {
                vocabularyDaoImpl = (VocabularyDao) Class.forName("services.dao." + DAO_ACCESS).newInstance();
            }
            catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {}

        return vocabularyDaoImpl;
    }

}
