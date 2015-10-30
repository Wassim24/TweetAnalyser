package services.dao;

import domain.TweetEntityBeans;
import javafx.collections.ObservableList;

import java.util.List;

public interface DataAccessObject
{
    boolean add(TweetEntityBeans tweetEntityBeans);
    List<TweetEntityBeans> all();
}