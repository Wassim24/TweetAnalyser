package services.twitter;

import twitter4j.RateLimitStatus;
import twitter4j.Status;
import twitter4j.TwitterException;

import java.util.List;

public interface TweetService
{
    public List<Status> search(String keyword) throws TwitterException;
    public RateLimitStatus getRemainingSearchQueries() throws TwitterException;
}
