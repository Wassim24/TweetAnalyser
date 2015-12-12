package services.twitter;

import domain.Annotation;
import domain.Tweet;
import domain.Vocabulary;
import services.dao.TweetDaoFactory;
import services.dao.VocabularyDaoFactory;

import java.util.*;

public class VocabularyServiceImpl implements VocabularyService
{
    private static int MINIMUM_VOCABULARY_LENGTH_PER_WORD = 3, DEFAULT_TO_NGRAMME = 4;
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
    public Map<Vocabulary, String> getAllKey(int ngramme)
    {
        Map<Vocabulary, String> m = new HashMap();
        List<Vocabulary> vocab = this.getAll(ngramme);

        for (Vocabulary v : vocab) {
            m.put(v, v.getWord());
        }

        return m;
    }

    @Override
    public Vocabulary get(String word)
    {
        return VocabularyDaoFactory.getInstance().get(word);
    }

    @Override
    public void buildVocabulary(int ngrams, List<Tweet> addTweetList)
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
                posOcc = 0; negOcc = 0; neuOcc = 0;

                // Looping through the tweets and comparing previous words with each tweet words
                for (Tweet tweetToCompareTo : addTweetList)
                {
                    // For each tweet generate the ngrams and compare with the previous
                    for (String wordToCompareTo : generateNgrams(ngrams, tweetToCompareTo.getTweet()))
                    {
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

    @Override
    public void buildAllVocabulary(int to_ngramme)
    {
        List<Tweet> databaseTweet = TweetDaoFactory.getInstance().getAll();
        for (int i = 1; i <= to_ngramme; i++)
            this.buildVocabulary(i, databaseTweet);
    }

    @Override
    public List<Vocabulary> buildAllVocabulary(int ngrams, List<Tweet> addTweetList)
    {
        int posOcc, negOcc, neuOcc, k;
        List<Vocabulary> vocabularies = new ArrayList<>();
        List<Tweet> tweets = TweetDaoFactory.getInstance().getAll();
        Vocabulary vocabulary;

        // Looping through each tweet in database and getting its words
        for (Tweet tweet : tweets)
        {
            // Getting the words of a tweet with split
            for (String wordToCompare : generateNgrams(ngrams, tweet.getTweet()))
            {
                /* verification de la taille des grammes */
                posOcc = 0; negOcc = 0; neuOcc = 0;

                // Looping through the tweets and comparing previous words with each tweet words
                for (Tweet tweetToCompareTo : addTweetList)
                {
                    // For each tweet generate the ngrams and compare with the previous
                    for (String wordToCompareTo : generateNgrams(ngrams, tweetToCompareTo.getTweet()))
                    {
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

        return vocabularies;
    }

    @Override
    public void buildAllVocabulary()
    {
        this.buildAllVocabulary(DEFAULT_TO_NGRAMME);
    }

    @Override
    public void addToVocabulary(Tweet tweet)
    {
        Vocabulary v;

        for (int i = 1; i <= DEFAULT_TO_NGRAMME; i++)
            for (String s : generateNgrams(i, tweet.toString()))
            {
                v = this.get(s);

                if (v == null)
                    v = new Vocabulary(s, tweet.getAnnotation() == Annotation.POSITIF ? 1 : 0, tweet.getAnnotation() == Annotation.NEGATIF ? 1 : 0, tweet.getAnnotation() == Annotation.NEUTRE ? 1 : 0, i);
                else
                    switch (tweet.getAnnotation())
                    {
                        case POSITIF:
                            v.setPosocc(v.getPosocc() + 1);
                            break;

                        case NEGATIF:
                            v.setNegocc(v.getNegocc() + 1);
                            break;

                        case NEUTRE:
                            v.setNeuocc(v.getNeuocc() + 1);
                            break;
                    }

                this.add(v);
            }
    }

    private List<String> generateNgrams(int n, String text)
    {
        String array[] = text.split(" ");
        List<String> ngrams = new ArrayList<String>();
        String sequence;

        parentloop:
        for (int i = 0; i < (array.length - (n - 1)); i++)
        {
            sequence = "";

            for (int j = i; j < n + i; j++)
                if (array[j].length() < MINIMUM_VOCABULARY_LENGTH_PER_WORD)
                    continue parentloop;
                else
                    sequence += " " + array[j];

            sequence = sequence.replaceFirst(" ", "");
            ngrams.add(sequence);
        }

        return ngrams;
    }
}
