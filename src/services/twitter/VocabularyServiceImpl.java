package services.twitter;

import domain.Tweet;
import domain.Vocabulary;
import services.dao.TweetDaoFactory;
import services.dao.VocabularyDaoFactory;

import java.util.*;

public class VocabularyServiceImpl implements VocabularyService
{
    private static int MINIMUM_VOCABULARY_LENGTH_PER_WORD = 3;
    private static VocabularyServiceImpl ourInstance = new VocabularyServiceImpl();
    public static VocabularyServiceImpl getInstance() {
        return ourInstance;
    }
    private VocabularyServiceImpl() {}

    @Override
    public boolean addAll(List<Vocabulary> vocabularies) { return VocabularyDaoFactory.getInstance().addAll(vocabularies); }

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
    public void buildVocabulary(int ngrams)
    {
        int posOcc, negOcc, neuOcc, k;
        List<Vocabulary> vocabularies = new ArrayList<>();
        List<Tweet> tweets = TweetDaoFactory.getInstance().getAll();
        Vocabulary vocabulary;
        String[] checkWords;

        // Looping through each tweet in database and getting its words
        for (Tweet tweet : tweets)
        {
            // Getting the words of a tweet with split
            for (String wordToCompare : generateNgrams(ngrams, tweet.getTweet()))
            {
                /* verification de la taille des grammes */
                checkWords = wordToCompare.split(" ");
                for (k = 0; k < ngrams && checkWords[k].length() > MINIMUM_VOCABULARY_LENGTH_PER_WORD; k++);
                if (k != ngrams)
                    continue;

                posOcc = 0; negOcc = 0; neuOcc = 0;

                // Looping through the tweets and comparing previous words with each tweet words
                for (Tweet tweetToCompareTo : tweets)
                {
                    // For each tweet generate the ngrams and compare with the previous
                    for (String wordToCompareTo : generateNgrams(ngrams, tweetToCompareTo.getTweet()))
                    {
                        /* verification de la taille des grammes */
                        checkWords = wordToCompareTo.split(" ");
                        for (k = 0; k < ngrams && checkWords[k].length() > MINIMUM_VOCABULARY_LENGTH_PER_WORD; k++);
                        if (k != ngrams)
                            continue;

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
                vocabulary = new Vocabulary(wordToCompare, posOcc, negOcc, neuOcc, ngrams);
                if (!vocabularies.contains(vocabulary))
                    vocabularies.add(vocabulary);
            }
        }

        VocabularyDaoFactory.getInstance().addAll(vocabularies);
    }

    private List<String> generateNgrams(int n, String text)
    {
        String array[] = text.split(" ");
        List<String> ngrams = new ArrayList<String>();
        String sequence;

        for (int i = 0; i < (array.length - (n - 1)); i++)
        {
            sequence = "";

            for (int j = i; j < n + i; j++)
                sequence += " " + array[j];

            sequence = sequence.replaceFirst(" ", "");
            ngrams.add(sequence);
        }

        return ngrams;
    }
}
