package domain;

import java.util.*;

public class Tweet implements EntityBeans
{
    private int id;
    private String username, tweet, keyword;
    private Date date;
    private Annotation annotation;
    private boolean isCleanTweet;

    public Tweet(int id, String username, String tweet, Date date, String keyword, Annotation annotation)
    {
        this(username, tweet, date, keyword, annotation);
        this.setId(id);
    }

    public Tweet(String username, String tweet, Date date, String keyword)
    {
        this(username, tweet, date, keyword, Annotation.NONANNOTE);
    }

    public Tweet(String username, String tweet, Date date, String keyword, Annotation annotation)
    {
        this.setUsername(username);
        this.setTweet(tweet);
        this.setKeyword(keyword);
        this.setDate(date);
        this.setAnnotation(annotation);
        this.setId(-1);

        this.cleanTweet();

    }

    public Tweet clone()
    {
        return new Tweet(this.getId(), this.getUsername(), this.getTweet(), this.getDate(), new String(this.getKeyword()), this.getAnnotation());
    }

    public String cleanTweet()
    {
        if (this.isCleanTweet)
            return this.getTweet();

        LinkedHashMap<String, String> regexes = new LinkedHashMap<String, String>()
        {{
            put("…", "");
            put("https?:?\\/?\\/?[a-zA-Z0-9]\\.?[a-zA-Z0-9]*\\/?[a-zA-Z]*[…]?", "");
            put("@[a-zA-Z0-9-_]+\\s?:?\\s?", "");
            put("#[a-zA-Z0-9-_]+\\s?:?", "");
            put("\\bRT\\b", "");
            put("\\brt\\b", "");
            put("\"", "");
            put("[^\\w^ àâçéèêëîïôûùüÿñæœ']+", "");
            put("\\d[a-zA-Z]*", "");
            put("\\s{1,}", " ");
            put("\\B\\s+", "");
            put("\\n", "");
        }};

        this.setTweet(this.getTweet().toLowerCase());
        for (Map.Entry<String, String> entry : regexes.entrySet())
            this.setTweet(this.getTweet().replaceAll(entry.getKey(), entry.getValue()).toLowerCase());

        this.isCleanTweet = true;
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
    public void setAnnotation(Annotation annotation) {this.annotation = annotation;}
    public int getAnnotationValue()
    {
        return this.getAnnotation().getValue();
    }
    public Annotation getAnnotation()
    {
        return this.annotation;
    }
    public int getWordsCount()
    {
        return (tweet.length() - tweet.replaceAll(" ", "").length() + 1);
    }

    public List<String> getTweetNgram(int n) {

        String array[] = this.getTweet().split(" ");
        List<String> ngrams = new ArrayList<>();
        String sequence = "";

        for (int i = 0; i < (array.length - (n - 1)); i++) {
            for (int j = i; j < n+i; j++) {
                sequence += " " + array[j];
            }

            sequence = sequence.replaceFirst(" ", "");
            ngrams.add(sequence);
            sequence = "";
        }
        return ngrams;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tweet tweet1 = (Tweet) o;

        if (isCleanTweet != tweet1.isCleanTweet) return false;
        if (!username.equals(tweet1.username)) return false;
        if (!tweet.equals(tweet1.tweet)) return false;
        if (!keyword.equals(tweet1.keyword)) return false;
        if (!date.equals(tweet1.date)) return false;
        return annotation == tweet1.annotation;
    }

    @Override
    public int hashCode()
    {
        int result = username.hashCode();
        result = 31 * result + tweet.hashCode();
        result = 31 * result + keyword.hashCode();
        result = 31 * result + date.hashCode();
        result = 31 * result + annotation.hashCode();
        result = 31 * result + (isCleanTweet ? 1 : 0);
        return result;
    }
}
