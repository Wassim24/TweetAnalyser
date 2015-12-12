package services.twitter;

import domain.Tweet;
import domain.Vocabulary;

import java.util.List;
import java.util.Map;

public interface VocabularyService
{
    boolean addAll(List<Vocabulary> vocabularies);
    boolean add(Vocabulary vocabulary);

    List<Vocabulary> getAll();
    List<Vocabulary> getAll(int ngramme);
    Map<Vocabulary, String> getAllKey(int ngramme);
    Vocabulary get(String word);

    void buildVocabulary(int ngramme, List<Tweet> addTweetList);
    void buildAllVocabulary(int tongramme);

    List<Vocabulary> buildAllVocabulary(int tongramme, List<Tweet> addTweetList);

    void buildAllVocabulary();
    void addToVocabulary(Tweet tweet);
}
