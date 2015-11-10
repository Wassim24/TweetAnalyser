package services.twitter;

import domain.Tweet;
import services.dao.TweetDaoFactory;
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
    public List<Tweet> search(String keyword) throws TwitterException
    {
        return Twitter4JFactory.getInstance().getTwitterFactory().getInstance().search(new Query(keyword)).getTweets().stream().map(s -> new Tweet(s.getUser().getScreenName(), s.getText(), s.getCreatedAt(), keyword)).collect(Collectors.toList());
    }

    @Override
    public RateLimitStatus getRemainingSearchQueries() throws TwitterException
    {
        return Twitter4JFactory.getInstance().getTwitter().getRateLimitStatus("search").get("/search/tweets");
    }

    @Override
    public boolean add(Tweet tweet)
    {
        tweet.cleanString();
        return TweetDaoFactory.getInstance().add(tweet);
    }

    @Override
    public boolean add(String username, String tweet, Date date, String keyword)
    {
        return this.add(new Tweet(username, tweet, date, keyword));
    }

    @Override
    public boolean addAll(List<Tweet> tweets)
    {
        for (Tweet tweet : tweets)
            this.add(tweet);

        return true;
    }

    @Override
    public List<Tweet> getAll()
    {
        return TweetDaoFactory.getInstance().all();
    }
}