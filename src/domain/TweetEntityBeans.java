package domain;

import java.util.Date;

/**
 * Created by r99t on 06-10-15.
 */
public class TweetEntityBeans
{
    private int id;
    private String username, tweet, keyword;
    private Date date;

    public TweetEntityBeans(int id, String username, String tweet, Date date, String keyword)
    {
        this(username, tweet, date, keyword);
        this.setId(id);
    }

    public TweetEntityBeans(String username, String tweet, Date date, String keyword)
    {
        this.setId(id);
        this.setUsername(username);
        this.setTweet(tweet);
        this.setKeyword(keyword);
        this.setDate(date);
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
}
