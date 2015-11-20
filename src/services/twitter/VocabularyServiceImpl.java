package services.twitter;

import domain.Tweet;
import domain.Vocabulary;
import org.sqlite.util.StringUtils;
import services.dao.TweetDaoFactory;
import services.dao.VocabularyDaoFactory;

import java.util.*;

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
    public List<Vocabulary> getAll(int ngramme)
    {
        return VocabularyDaoFactory.getInstance().getAll(ngramme);
    }

    @Override
    public Map<String, Vocabulary> getAllKey(int ngramme)
    {
        Map<String, Vocabulary> m = new HashMap<String, Vocabulary>();

        for (Vocabulary vocabulary : this.getAll(ngramme))
            m.put(vocabulary.getWord(), vocabulary);

        return m;
    }

    @Override
    public Vocabulary get(String word)
    {
        return VocabularyDaoFactory.getInstance().get(word);
    }

    @Override
    public void buildVocabulary(int ngramme)
    {
        if (ngramme <= 0)
            return;

        int posOcc, negOcc, neuOcc, i, j, k;
        List<Vocabulary> vocabularies = new ArrayList<Vocabulary>();
        List<Tweet> tweets = TweetDaoFactory.getInstance().all();
        String response[] = new String[ngramme];
        Vocabulary vocabulary;

        // Looping through each tweet in database and getting its words
        for (Tweet tweet : tweets)
        {
            // Getting the words of a tweet with split
            String wordsToCompare[] = tweet.getTweet().split(" ");
            for (i = 0; i < wordsToCompare.length - (ngramme - 1); i++)
            {
                for (k = 0; k < ngramme && wordsToCompare[i + k].length() > 3; k++);
                if (k != ngramme)
                    continue;

                posOcc = 0; negOcc = 0; neuOcc = 0;
                // Looping through the tweets and comparing previous words with each tweet words
                for (Tweet tweetToCompareTo : tweets)
                {
                    // Getting the annotation of a tweet
                    // Looping through each tweet to to compare the words
                    String wordsToCompareTo[] = tweetToCompareTo.getTweet().split(" ");
                    for (j = 0; j < wordsToCompareTo.length - (ngramme - 1); j++)
                    {
                        for (k = 0; k < ngramme && wordsToCompareTo[j + k].length() > 3; k++);
                        if (k != ngramme)
                            continue;

                        // Comparing the words
                        for (k = 0; k < ngramme && wordsToCompareTo[j + k].equals(wordsToCompare[i + k]); k++);

                        if (k == ngramme)
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

                System.arraycopy(wordsToCompare, i, response, 0, ngramme);
                // Checking if the word has already been added or not
                vocabulary = new Vocabulary(StringUtils.join(Arrays.asList(response), " "), posOcc, negOcc, neuOcc, ngramme);

                if (!vocabularies.contains(vocabulary))
                    vocabularies.add(vocabulary);
            }
        }

        VocabularyDaoFactory.getInstance().addAll(vocabularies);
    }
}
