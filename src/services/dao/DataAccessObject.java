package services.dao;

import domain.TweetEntityBeans;
import java.util.List;

public interface DataAccessObject
{
    public boolean add(TweetEntityBeans tweetEntityBeans);
    public List<TweetEntityBeans> all();
}