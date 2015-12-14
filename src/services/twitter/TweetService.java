package services.twitter;

import domain.Annotation;
import domain.Tweet;
import twitter4j.RateLimitStatus;
import twitter4j.TwitterException;

import java.util.Date;
import java.util.List;

public interface TweetService
{
     List<Tweet> search(String keyword) throws TwitterException;
     RateLimitStatus getRemainingSearchQueries() throws TwitterException;
     boolean add(Tweet tweet);
     boolean add(String username, String tweet, Date date, String keyword);
     boolean addAll(List<Tweet> tweets);
     List<Tweet> getAll();
     List<Tweet> getBetween(int start, int limit);
}
