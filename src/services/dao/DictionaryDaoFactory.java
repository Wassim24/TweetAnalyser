package services.dao;

public class DictionaryDaoFactory
{
    private static final String DAO_ACCESS = "DictionarySqliteDaoImpl";
    private static DictionaryDao dictionaryDaoImpl = null;
    private DictionaryDaoFactory() {}

    public static DictionaryDao getInstance()
    {
        if (dictionaryDaoImpl == null)
            try
            {
                dictionaryDaoImpl = (DictionaryDao) Class.forName("services.dao." + DAO_ACCESS).newInstance();
            }
            catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {}

        return dictionaryDaoImpl;
    }
}
