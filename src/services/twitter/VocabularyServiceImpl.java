package services.twitter;

import domain.Vocabulary;
import services.dao.VocabularyDao;
import services.dao.VocabularyDaoFactory;

import java.util.List;

public class VocabularyServiceImpl implements VocabularyService
{
    private static VocabularyServiceImpl ourInstance = new VocabularyServiceImpl();
    public static VocabularyServiceImpl getInstance() {
        return ourInstance;
    }
    private VocabularyServiceImpl() {}

    @Override
    public boolean addAll(List<Vocabulary> vocabularies)
    {
        return VocabularyDaoFactory.getInstance().addAll(vocabularies);
    }

    @Override
    public boolean add(Vocabulary vocabulary)
    {
        return VocabularyDaoFactory.getInstance().add(vocabulary);
    }

    @Override
    public List<Vocabulary> getAll()
    {
        return VocabularyDaoFactory.getInstance().getAll();
    }

    @Override
    public Vocabulary get(String word)
    {
        return VocabularyDaoFactory.getInstance().get(word);
    }
}
