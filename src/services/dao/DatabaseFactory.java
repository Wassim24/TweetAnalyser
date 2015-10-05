package services.dao;

import java.util.Date;

interface DatabaseFactory
{
    public boolean add(String username, String tweet, Date date, String keyword);
}