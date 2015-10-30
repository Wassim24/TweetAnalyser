package domain;

import java.util.Date;
import java.util.LinkedHashMap;

public class TweetEntityBeans
{
    private int id;
    private String username, tweet, keyword;
    private Date date;
    private Annotation annotation;
    private int wordsCount;

    public TweetEntityBeans(int id, String username, String tweet, Date date, String keyword, Annotation annotation)
    {
        this(username, tweet, date, keyword, annotation);
        this.setId(id);
    }

    public TweetEntityBeans(String username, String tweet, Date date, String keyword)
    {
        this(username, tweet, date, keyword, Annotation.NONANNOTE);
    }

    public TweetEntityBeans(String username, String tweet, Date date, String keyword, Annotation annotation)
    {
        this.setUsername(username);
        this.setTweet(tweet);
        this.setKeyword(keyword);
        this.setDate(date);
        this.setAnnotation(annotation);
        this.setId(-1);
    }

    public String getUsername()
    {
        return this.username;
    }
    public void setUsername(String username)
    {
        this.username = username;
    }
    public String getTweet()
    {
        return this.tweet;
    }
    public void setTweet(String tweet)
    {
        this.tweet = tweet;
    }
    public String getKeyword()
    {
        return this.keyword;
    }
    public void setKeyword(String keyword)
    {
        this.keyword = keyword;
    }
    public void setDate(Date date)
    {
        this.date = date;
    }
    public Date getDate()
    {
        return this.date;
    }
    private void setId(int id)
    {
        this.id = id;
    }
    public int getId()
    {
        return this.id;
    }
    public String toString()
    {
        return this.getUsername() + " : "+ this.getTweet();
    }
    public void setAnnotation(Annotation annotation) {this.annotation = annotation;}
    public int getAnnotation()
    {
        return this.annotation.getValue();
    }
    public void setWordsCount(int wordsCount) {this.wordsCount = wordsCount;}
    public int getWordsCount() {return (tweet.length() - tweet.replaceAll(" ", "").length() + 1);}
}
