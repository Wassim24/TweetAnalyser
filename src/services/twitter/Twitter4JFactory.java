package services.twitter;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;

public class Twitter4JFactory
{
    private static Twitter4JFactory twitter4JConfig = null;
    private TwitterFactory twitterFactory;

    private Twitter4JFactory()
    {
        this.setTwitterFactory(new TwitterFactory());
    }

    public static Twitter4JFactory getInstance()
    {
        if (twitter4JConfig == null)
            twitter4JConfig = new Twitter4JFactory();

        return twitter4JConfig;
    }

    public TwitterFactory getTwitterFactory()
    {
        return this.twitterFactory;
    }
    private void setTwitterFactory(TwitterFactory twitterFactory)
    {
        this.twitterFactory = twitterFactory;
    }
    public Twitter getTwitter()
    {
        return this.getTwitterFactory().getInstance();
    }
}