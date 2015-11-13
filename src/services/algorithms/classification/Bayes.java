package services.algorithms.classification;

import domain.Tweet;
import domain.Vocabulary;
import services.dao.TweetDaoFactory;

import java.util.ArrayList;
import java.util.List;

public class Bayes {

    public Bayes() {

    }

    public void buildVocabulary() {

        List<Vocabulary> vocabularies = new ArrayList<>();

        List<Tweet> tweets = TweetDaoFactory.getInstance().all();

        Vocabulary vocabulary;

        int posOcc = 0, negOcc = 0, neuOcc = 0;

        // Looping through each tweet in database and getting its words
        for (Tweet tweet : tweets) {

            // Getting the words of a tweet with split
            for (String wordToCompare : tweet.getTweet().split(" ")) {

                // Looping through the tweets and comparing previous words with each tweet words
                for (Tweet tweetToCompareTo : tweets) {

                    // Getting the annotation of a tweet
                    int annotation = tweetToCompareTo.getAnnotationValue();

                    // Looping through each tweet to to compare the words
                    for (String wordToCompareTo : tweetToCompareTo.getTweet().split(" ")) {

                        // Comparing the words
                        if(wordToCompareTo.equals(wordToCompare))

                            if(annotation == -1) negOcc++;

                            else if (annotation == 0) neuOcc++;

                            else posOcc++;
                    }
                }

                // Checking if the word has already been added or not
                vocabulary = new Vocabulary(wordToCompare, posOcc, negOcc, neuOcc);

                if(!vocabularies.contains(vocabulary))vocabularies.add(vocabulary);

                posOcc = 0; negOcc = 0; neuOcc = 0;
            }

        }

        for (Vocabulary voca : vocabularies) {
            System.out.println(voca.getWord() + " == > " + "Pos : " + voca.getPosocc() + " Neg : " + voca.getNegocc() + " Neu : " + voca.getNeuocc() );
        }
    }
}
