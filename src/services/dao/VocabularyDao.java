package services.dao;

import domain.Vocabulary;

import java.util.List;

public interface VocabularyDao
{
    boolean addAll(List<Vocabulary> vocabularies);
    boolean add(Vocabulary vocabulary);
    Vocabulary get(String word);
    List<Vocabulary> getAll();
    List<Vocabulary> getAll(int ngramme);
}
