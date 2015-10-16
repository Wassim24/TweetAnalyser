package services.twitter;

import domain.TweetEntityBeans;
import twitter4j.RateLimitStatus;
import twitter4j.Status;
import twitter4j.TwitterException;

import java.util.Date;
import java.util.List;

public interface TweetService
{
    public List<TweetEntityBeans> search(String keyword) throws TwitterException;
    public RateLimitStatus getRemainingSearchQueries() throws TwitterException;
    public boolean add(TweetEntityBeans tweet);
    public boolean add(String username, String tweet, Date date, String keyword);
    public boolean addAll(List<TweetEntityBeans> tweets);
    public List<TweetEntityBeans> getAll();
}
