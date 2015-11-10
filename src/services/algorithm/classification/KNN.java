package services.algorithm.classification;

import domain.Annotation;
import domain.Tweet;
import services.dao.TweetDaoFactory;

import java.util.ArrayList;
import java.util.List;

public class KNN
{
    private List<Tweet> tweetsToAnnote;
    private int numberOfNeighbours;

    public KNN(List<Tweet> tweetsToAnnote, int numberOfNeighbours)
    {
        this.tweetsToAnnote = tweetsToAnnote;
        this.numberOfNeighbours = numberOfNeighbours;
    }

    public List<Tweet> compute()
    {
        List<Float> distancess = new ArrayList<Float>();
        List<Integer> neighboursIds = new ArrayList<Integer>();
        List<Tweet> annotedTweets = new ArrayList<Tweet>();
        List<Tweet> tweetsInDatabase = TweetDaoFactory.getInstance().all();

        float calculatedEuclideanDistance = 0;
        // Loop for iterating over the selected tweets
        for (Tweet unannotedTweet : this.tweetsToAnnote)
        {
            // Loop for putting the first numberOfNeighbours in the distancesList
            for (Tweet tweetInDB : tweetsInDatabase)
            {
                calculatedEuclideanDistance = this.findEuclideanDistance(unannotedTweet, tweetInDB);

                distancess.add(calculatedEuclideanDistance);
                neighboursIds.add(tweetInDB.getId());

                if (distancess.size() == this.numberOfNeighbours)
                    break;
            }

            //Loop for getting the tweets in DB and comparing them to the neighbours
            for (int i = this.numberOfNeighbours + 1; i < tweetsInDatabase.size() - 1; i++)
            {
                calculatedEuclideanDistance = this.findEuclideanDistance(unannotedTweet, tweetsInDatabase.get(i));

                int minDistIndex = findIndexOfMin(distancess, calculatedEuclideanDistance);

                // For replacing the distance at minDistIndex with the new calculatedEuclideanDistance and the id
                distancess.set(minDistIndex, calculatedEuclideanDistance);
                neighboursIds.set(minDistIndex, tweetsInDatabase.get(i).getId());
            }

            // For annotating the new tweets based on the min distance of the neighbours annotation
            int tweetInDBAnnotation = tweetsInDatabase.get(neighboursIds.get(findIndexOfMin(distancess)) - 1).getAnnotation();
            unannotedTweet.setAnnotation(Annotation.valueOf((tweetInDBAnnotation == -1) ? "NEGATIF" : (tweetInDBAnnotation == 0) ? "NEUTRE" : "POSITIF"));

            //Adding the newly annoted Tweet to the annotedTweetsList
            annotedTweets.add(unannotedTweet);

            //Clearing the lists for the new round :P
            distancess.clear();
            neighboursIds.clear();
        }

        // Returning the liste of the newly annoted Tweets
        return annotedTweets;
    }

    private int findIndexOfMin(List<Float> listOfDistances, float currentDistance)
    {
        for (int i = 0; i < listOfDistances.size(); i++)
            if (currentDistance <= listOfDistances.get(i))
                return i;

        return 0;
    }

    private int findIndexOfMin(List<Float> listOfDistances)
    {
        int index = 0;
        float min = Integer.MIN_VALUE;

        for (int i = 0; i < listOfDistances.size(); i++)
            if (listOfDistances.get(i) <= min)
            {
                min = listOfDistances.get(i);
                index = i;
            }

        return index;
    }

    private float findEuclideanDistance(Tweet toClassifyTweet, Tweet classifiedTweet)
    {
        float commonWordsNumber = 0;

        String[] toCompareWords = classifiedTweet.getTweet().split(" ");
        for (String tweetWord : toClassifyTweet.getTweet().split(" "))
            for (String toCompareWord : toCompareWords)
                if (tweetWord.equalsIgnoreCase(toCompareWord))
                    commonWordsNumber++;

        return 1 - commonWordsNumber / (float) (toClassifyTweet.getWordsCount() + classifiedTweet.getWordsCount());
    }
}
