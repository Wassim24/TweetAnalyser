package services.twitter;

import domain.TweetEntityBeans;
import twitter4j.RateLimitStatus;
import twitter4j.Status;
import twitter4j.TwitterException;

import java.util.Date;
import java.util.List;

public interface TweetService
{
     List<TweetEntityBeans> search(String keyword) throws TwitterException;
     RateLimitStatus getRemainingSearchQueries() throws TwitterException;
     boolean add(TweetEntityBeans tweet);
     boolean add(String username, String tweet, Date date, String keyword);
     boolean addAll(List<TweetEntityBeans> tweets);
     List<TweetEntityBeans> getAll();
}
