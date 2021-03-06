package services.dao;

import domain.Annotation;
import domain.Dictionary;

import java.util.List;

public interface DictionaryDao
{
    boolean addAll(Dictionary dictionary);
    boolean add(String word, Annotation annotation);
    Dictionary get(Annotation annotation);

    Dictionary get(Annotation annotation, int start, int limit);

    List<Dictionary> getAll();
}