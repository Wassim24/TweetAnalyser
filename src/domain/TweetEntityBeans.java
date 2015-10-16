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
    private Annotation annotation;

    public TweetEntityBeans(int id, String username, String tweet, Date date, String keyword, Annotation annotation)
    {
        this(username, tweet, date, keyword, annotation);
        this.setId(id);
    }

    public TweetEntityBeans(String username, String tweet, Date date, String keyword)
    {
        this(username, tweet, date, keyword, Annotation.NEUTRE);
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

    public String cleanCurrentText()
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

        regexes.forEach((regex, replaceWith) ->
        {
            this.setTweet(this.getTweet().replaceAll(regex, replaceWith));
        });

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
    public void setAnnotation(Annotation annotation)
    {
        this.annotation = annotation;
    }
    public int getAnnotation()
    {
        return this.annotation.getValue();
    }
}
