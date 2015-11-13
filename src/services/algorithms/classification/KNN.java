package services.algorithms.classification;

import domain.Tweet;
import services.dao.TweetDaoFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KNN
{
    private KNN() {}

    public static List<Tweet> compute(List<Tweet> tweetsToAnnote)
    {
        return compute(tweetsToAnnote, 10);
    }

    public static List<Tweet> compute(List<Tweet> tweetsToAnnote, int numberOfNeighbours)
    {
        List<Float> distancesList = new ArrayList<Float>();
        List<Integer> neighboursIdsList = new ArrayList<Integer>();
        List<Tweet> annotedTweets = new ArrayList<Tweet>(), tweetsInDataBase = TweetDaoFactory.getInstance().all();

        // Loop for iterating over the selected tweets
        for (Tweet unannotedTweet : tweetsToAnnote)
        {
            // Loop for putting the first numberOfNeighbours in the distancesList
            for (Tweet tweetInDB : tweetsInDataBase)
            {
                distancesList.add(findEuclideanDistance(unannotedTweet, tweetInDB));
                neighboursIdsList.add(tweetInDB.getId());

                if (distancesList.size() == numberOfNeighbours)
                    break;
            }

            //Loop for getting the tweets in DB and comparing them to the neighbours
            for (int i = numberOfNeighbours + 1; i < tweetsInDataBase.size() - 1; i++)
            {
                float calculatedEuclideanDistance = findEuclideanDistance(unannotedTweet, tweetsInDataBase.get(i));
                int minDistIndex = findIndexOfMin(distancesList, calculatedEuclideanDistance);

                // For replacing the distance at minDistIndex with the new calculatedEuclideanDistance and the id
                distancesList.set(minDistIndex, calculatedEuclideanDistance);
                neighboursIdsList.set(minDistIndex, tweetsInDataBase.get(i).getId());
            }

            // For annotating the new tweets based on the min distance of the neighbours annotation
            unannotedTweet.setAnnotation(tweetsInDataBase.get(neighboursIdsList.get(findIndexOfMin(distancesList)) - 1).getAnnotation());
            //Adding the newly annoted Tweet to the annotedTweetsList
            annotedTweets.add(unannotedTweet);

            //Clearing the lists for the new round :P
            distancesList.clear();
            neighboursIdsList.clear();
        }

        // Returning the liste of the newly annoted Tweets
        return annotedTweets;
    }

    private static int findIndexOfMin(List<Float> listOfDistances, float currentDistance)
    {
        int minIndex = 0;

        for (int i = 0; i < listOfDistances.size(); i++)
            if (currentDistance > listOfDistances.get(i))
            {
                currentDistance = listOfDistances.get(i);
                minIndex = i;
            }

        return minIndex;
    }

    private static int findIndexOfMin(List<Float> listOfDistances)
    {
        return findIndexOfMin(listOfDistances, Integer.MIN_VALUE);
    }

    private static float findEuclideanDistance(Tweet toClassifyTweet, Tweet classifiedTweet)
    {
        List<String> classifiedTweetWords = Arrays.asList(classifiedTweet.getTweet().toLowerCase().split(" "));
        int commonWordsNumber = 0;

        for (String tweetWord : toClassifyTweet.getTweet().split(" "))
            if (classifiedTweetWords.contains(tweetWord.toLowerCase()))
                commonWordsNumber++;

        return 1 - commonWordsNumber / (float)(toClassifyTweet.getWordsCount() + classifiedTweet.getWordsCount());
    }
}