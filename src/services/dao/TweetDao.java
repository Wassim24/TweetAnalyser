package services.dao;

import domain.Tweet;

import java.util.List;

public interface TweetDao
{
    boolean add(Tweet tweet);
    List<Tweet> all();
}