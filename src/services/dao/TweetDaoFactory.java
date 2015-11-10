package services.dao;

public class TweetDaoFactory
{
    private static final String DAO_ACCESS = "TweetSqliteDaoImpl";
    private static TweetDao tweetDaoImpl = null;
    private TweetDaoFactory() {}

    public static TweetDao getInstance()
    {
        if (tweetDaoImpl == null)
            try
            {
                tweetDaoImpl = (TweetDao) Class.forName("services.dao." + DAO_ACCESS).newInstance();
            }
            catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {}

        return tweetDaoImpl;
    }
}
