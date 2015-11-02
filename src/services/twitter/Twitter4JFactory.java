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

    /*new ConfigurationBuilder()
    .setDebugEnabled(true)
    .setOAuthConsumerKey("mu1kZEx0ncu1sy5MGgWQhhWZq")
    .setOAuthConsumerSecret("NMNLU9ak68JaCSAWTj1zrAveETP47Bs4Ni9z5mHoXWZAuvyD9O")
    .setOAuthAccessToken("3736207823-IfYaEzWsE8gDDw7PcsYDHouQQhsQAAKP6W9Jt2h")
    .setOAuthAccessTokenSecret("f2cIePhEIzeHIlsGA5Whkfm0tBt44nZh2rIVNwHYBKE70")
                    /*.setHttpProxyHost("cache-etu.univ-lille1.fr")
                    .setHttpProxyPort(3128)*/
    //.build()

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