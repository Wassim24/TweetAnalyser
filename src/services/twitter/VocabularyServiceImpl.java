package services.twitter;

import domain.Tweet;
import domain.Vocabulary;
import services.dao.TweetDaoFactory;
import services.dao.VocabularyDaoFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public Map<String, Vocabulary> getAllKey()
    {
        Map<String, Vocabulary> m = new HashMap<String, Vocabulary>();

        for (Vocabulary vocabulary : this.getAll())
            m.put(vocabulary.getWord(), vocabulary);

        return m;
    }

    @Override
    public Vocabulary get(String word)
    {
        return VocabularyDaoFactory.getInstance().get(word);
    }

    @Override
    public void buildVocabulary()
    {
        int posOcc, negOcc, neuOcc;
        List<Vocabulary> vocabularies = new ArrayList<>();
        List<Tweet> tweets = TweetDaoFactory.getInstance().all();
        Vocabulary vocabulary;

        // Looping through each tweet in database and getting its words
        for (Tweet tweet : tweets)
        {
            // Getting the words of a tweet with split
            for (String wordToCompare : tweet.getTweet().split(" "))
            {
                if (wordToCompare.length() <= 2) continue;

                posOcc = 0; negOcc = 0; neuOcc = 0;
                // Looping through the tweets and comparing previous words with each tweet words
                for (Tweet tweetToCompareTo : tweets)
                {
                    // Getting the annotation of a tweet
                    // Looping through each tweet to to compare the words
                    for (String wordToCompareTo : tweetToCompareTo.getTweet().split(" "))
                    {
                        if (wordToCompareTo.length() <= 2) continue;

                        // Comparing the words
                        if (wordToCompareTo.equals(wordToCompare))
                            switch (tweetToCompareTo.getAnnotation())
                            {
                                case NEGATIF:
                                    negOcc++;
                                    break;
                                case NEUTRE:
                                    neuOcc++;
                                    break;
                                default:
                                    posOcc++;
                            }
                    }
                }

                // Checking if the word has already been added or not
                vocabulary = new Vocabulary(wordToCompare, posOcc, negOcc, neuOcc);

                if(!vocabularies.contains(vocabulary))
                    vocabularies.add(vocabulary);
            }
        }

        VocabularyDaoFactory.getInstance().addAll(vocabularies);
    }
}
