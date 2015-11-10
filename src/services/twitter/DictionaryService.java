package services.twitter;

import domain.Annotation;
import domain.Dictionary;
import domain.Tweet;
import twitter4j.RateLimitStatus;
import twitter4j.TwitterException;

import java.io.File;
import java.util.Date;
import java.util.List;

public interface DictionaryService
{
    boolean addFromFile(File dictionary, Annotation annotation);
    Dictionary get(Annotation annotation);
}
