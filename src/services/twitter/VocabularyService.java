package services.twitter;

import domain.Vocabulary;

import java.util.List;

public interface VocabularyService
{
    boolean addAll(List<Vocabulary> vocabularies);
    boolean add(Vocabulary vocabulary);
    List<Vocabulary> getAll();
    Vocabulary get(String word);
}
