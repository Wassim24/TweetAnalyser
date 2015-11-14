package services.algorithms.classification;

import domain.Annotation;
import domain.Tweet;
import services.dao.DictionaryDaoFactory;

import java.util.List;

public class Glossary {

    private String wordsOfTweet[];

    private List<String> positiveWords;
    private List<String> negativeWords;

    private List<Tweet> tweetsToAnnote;

    public Glossary(List<Tweet> tweetsToAnnote) {

        positiveWords = DictionaryDaoFactory.getInstance().get(Annotation.POSITIF).getWords();
        negativeWords = DictionaryDaoFactory.getInstance().get(Annotation.NEGATIF).getWords();
        this.tweetsToAnnote = tweetsToAnnote;
    }

    public List<Tweet> compute() {

        for (Tweet tweet : tweetsToAnnote) {

            wordsOfTweet = getWordsOfTweet(tweet);

            int positiveWordsCount = countPositiveWords();
            int negativeWordsCount = countNegativeWords();

            if(positiveWordsCount < negativeWordsCount) tweet.setAnnotation(Annotation.NEGATIF);
            else if(positiveWordsCount == negativeWordsCount) tweet.setAnnotation(Annotation.NEUTRE);
            else tweet.setAnnotation(Annotation.POSITIF);
        }

        return tweetsToAnnote;
    }

    private String[] getWordsOfTweet(Tweet tweet)
    {
        return tweet.getTweet().split(" ");
    }

    private int countPositiveWords() {

        int positiveWordsCount = 0;

        for (String word : wordsOfTweet) {
            for (String positiveWord : positiveWords) {
                if(word.equals(positiveWord)) positiveWordsCount += 1;
            }
        }

        return positiveWordsCount;
    }

    private int countNegativeWords() {

        int negativeWordsCount = 0;

        for (String word : wordsOfTweet) {
            for (String negativeWord : negativeWords) {
                if(word.equals(negativeWord)) negativeWordsCount += 1;
            }
        }

        return negativeWordsCount;
    }
}
