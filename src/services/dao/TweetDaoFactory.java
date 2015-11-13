package services.dao;

import services.dao.sqlite.DaoSqliteFactory;

public class TweetDaoFactory
{
    private static final String DAO_ACCESS = DaoSqliteFactory.PACKAGE +".TweetSqliteDaoImpl";
    private static TweetDao tweetDaoImpl = null;
    private TweetDaoFactory() {}

    public static TweetDao getInstance()
    {
        if (tweetDaoImpl == null)
            try
            {
                tweetDaoImpl = (TweetDao) Class.forName(DAO_ACCESS).newInstance();
            }
            catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {}

        return tweetDaoImpl;
    }
}
