package services.twitter;

import domain.Vocabulary;

import java.util.List;
import java.util.Map;

public interface VocabularyService
{
    boolean addAll(List<Vocabulary> vocabularies);
    boolean add(Vocabulary vocabulary);
    List<Vocabulary> getAll();
    List<Vocabulary> getAll(int ngramme);
    Map<String, Vocabulary> getAllKey(int ngramme);
    Vocabulary get(String word);
    void buildVocabulary(int ngramme);
}
