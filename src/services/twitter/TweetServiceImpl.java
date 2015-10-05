package services.twitter;

import services.dao.SqliteFactoryImpl;
import twitter4j.Query;
import twitter4j.RateLimitStatus;
import twitter4j.Status;
import twitter4j.TwitterException;

import java.util.List;

public class TweetServiceImpl implements TweetService
{
    private static TweetServiceImpl tweetServiceImpl = null;
    private TweetServiceImpl() {}

    public static TweetServiceImpl getInstance()
    {
        if (tweetServiceImpl == null)
            tweetServiceImpl = new TweetServiceImpl();

        return tweetServiceImpl;
    }

    @Override
    public List<Status> search(String keyword) throws TwitterException
    {
        return Twitter4JFactory.getInstance().getTwitterFactory().getInstance().search(new Query(keyword)).getTweets();
    }

    @Override
    public RateLimitStatus getRemainingSearchQueries() throws TwitterException
    {
        return Twitter4JFactory.getInstance().getTwitter().getRateLimitStatus("search").get("/search/tweets");
    }

    @Override
    public boolean add(Status status, String keyword)
    {
        return SqliteFactoryImpl.getInstance().add(status.getUser().getName(), status.getText(), status.getCreatedAt(), keyword);
    }
}