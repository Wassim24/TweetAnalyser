package services.dao;

import domain.Vocabulary;

import java.util.List;

public interface VocabularyDao {

    boolean addAll(List<Vocabulary> vocabularies);

    boolean add(Vocabulary vocabulary);

    List<Vocabulary> get();
}
