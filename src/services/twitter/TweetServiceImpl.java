package services.twitter;

import domain.TweetEntityBeans;
import services.dao.DaoFactory;
import twitter4j.Query;
import twitter4j.RateLimitStatus;
import twitter4j.TwitterException;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<TweetEntityBeans> search(String keyword) throws TwitterException
    {
        return Twitter4JFactory.getInstance().getTwitterFactory().getInstance().search(new Query(keyword)).getTweets().stream().map(s -> new TweetEntityBeans(s.getUser().getScreenName(), s.getText(), s.getCreatedAt(), keyword)).collect(Collectors.toList());
    }

    @Override
    public RateLimitStatus getRemainingSearchQueries() throws TwitterException
    {
        return Twitter4JFactory.getInstance().getTwitter().getRateLimitStatus("search").get("/search/tweets");
    }

    @Override
    public boolean add(TweetEntityBeans tweet)
    {
        tweet.cleanText();
        return DaoFactory.getInstance().add(tweet);
    }

    @Override
    public boolean add(String username, String tweet, Date date, String keyword)
    {
        return this.add(new TweetEntityBeans(username, tweet, date, keyword));
    }

    @Override
    public boolean addAll(List<TweetEntityBeans> tweets)
    {
        for (TweetEntityBeans tweet : tweets)
            this.add(tweet);

        return true;
    }

    @Override
    public List<TweetEntityBeans> getAll()
    {
        return DaoFactory.getInstance().all();
    }
}