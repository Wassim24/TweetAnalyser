package domain;

import jdk.nashorn.internal.runtime.regexp.joni.Regex;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.regex.Pattern;

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

    public String cleanText()
    {
        LinkedHashMap<String, String> regexes = new LinkedHashMap<String, String>()
        {{
            put("(https?)://(www\\d?|[a-zA-Z0-9]+)?.[a-zA-Z0-9-]+(\\:|.)([a-zA-Z0-9.]+|(\\d+)?)([/?:].*)?", "");

            put("@[a-zA-Z0-9-_]+\\s?:?", "");
            put("#[a-zA-Z0-9-_]+\\s?:?", "");
            put("\\s?RT\\s", "");
            put("[^\\w^ àâçéèêëîïôûùüÿñæœ']+", "");
            put("\\d", "");
        }};

        regexes.forEach((regex, replaceWith) ->  {
            setTweet(getTweet().replaceAll(regex, replaceWith));
        });

        System.out.println(this.getTweet());
        return this.getTweet();
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
