package services.dao;

import domain.Annotation;
import domain.Tweet;

import java.util.List;

public interface TweetDao
{
    boolean add(Tweet tweet);
    List<Tweet> getAll();
    List<Tweet> getAll(Annotation annotation);
}