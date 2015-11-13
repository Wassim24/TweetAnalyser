package services.dao;

import services.dao.sqlite.DaoSqliteFactory;

public class DictionaryDaoFactory
{
    private static final String DAO_ACCESS = DaoSqliteFactory.PACKAGE +".DictionarySqliteDaoImpl";
    private static DictionaryDao dictionaryDaoImpl = null;
    private DictionaryDaoFactory() {}

    public static DictionaryDao getInstance()
    {
        if (dictionaryDaoImpl == null)
            try
            {
                dictionaryDaoImpl = (DictionaryDao) Class.forName(DAO_ACCESS).newInstance();
            }
            catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {}

        return dictionaryDaoImpl;
    }
}
