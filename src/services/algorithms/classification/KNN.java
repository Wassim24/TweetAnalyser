package services.algorithms.classification;

import domain.Annotation;
import domain.Tweet;
import services.dao.TweetDaoFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class KNN
{
    private List<Tweet> tweetsToAnnoteList;
    private int numberOfNeighbours;

    public KNN(List<Tweet> tweetsToAnnoteList, int numberOfNeighbours)
    {
        this.tweetsToAnnoteList = tweetsToAnnoteList;
        this.numberOfNeighbours = numberOfNeighbours;
    }

    public List<Tweet> compute()
    {

        List<Float> distancesList = new ArrayList<>();
        List<Integer> neighboursIdsList = new ArrayList<>();
        List<Tweet> annotedTweets = new ArrayList<>();
        List<Tweet> tweetsInDataBase = TweetDaoFactory.getInstance().all();

        float calculatedEuclideanDistance = 0;

        // Loop for iterating over the selected tweets
        for (Tweet unannotedTweet : tweetsToAnnoteList) {

            // Loop for putting the first numberOfNeighbours in the distancesList
            for (Tweet tweetInDB : tweetsInDataBase) {

                calculatedEuclideanDistance = findEuclideanDistance(unannotedTweet.getTweet(), tweetInDB.getTweet(), tweetInDB.getWordsCount());

                distancesList.add(calculatedEuclideanDistance);
                neighboursIdsList.add(tweetInDB.getId());

                if (distancesList.size() == numberOfNeighbours) break;
            }

            //Loop for getting the tweets in DB and comparing them to the neighbours
            for (int i = numberOfNeighbours + 1; i < tweetsInDataBase.size() - 1; i++) {

                calculatedEuclideanDistance = findEuclideanDistance(unannotedTweet.getTweet(), tweetsInDataBase.get(i).getTweet(), tweetsInDataBase.get(i).getWordsCount());

                int minDistIndex = findIndexOfMin(distancesList, calculatedEuclideanDistance);

                // For replacing the distance at minDistIndex with the new calculatedEuclideanDistance and the id
                distancesList.set(minDistIndex, calculatedEuclideanDistance);
                neighboursIdsList.set(minDistIndex, tweetsInDataBase.get(i).getId());
            }

            // For annotating the new tweets based on the min distance of the neighbours annotation
            int tweetInDBAnnotation = tweetsInDataBase.get(neighboursIdsList.get(findIndexOfMin(distancesList)) - 1).getAnnotation();
            System.out.println(neighboursIdsList.get(findIndexOfMin(distancesList)) - 1);
            unannotedTweet.setAnnotation(Annotation.valueOf((tweetInDBAnnotation == -1) ? "NEGATIF" : (tweetInDBAnnotation == 0) ? "NEUTRE" : "POSITIF"));

            //Adding the newly annoted Tweet to the annotedTweetsList

            annotedTweets.add(unannotedTweet);

            //Clearing the lists for the new round :P
            distancesList.clear();
            neighboursIdsList.clear();
        }

        // Returning the liste of the newly annoted Tweets
        return annotedTweets;
    }

    public int findIndexOfMin(List<Float> listOfDistances, float currentDistance) {

        int minIndex = 0;
        for (int i = 0; i < listOfDistances.size(); i++)
            if (currentDistance <= listOfDistances.get(i)) minIndex = i;

        return minIndex;
    }

    public int findIndexOfMin(List<Float> listOfDistances) {

        float min = listOfDistances.get(0);
        int index = 0;

        for (int i = 0; i < listOfDistances.size(); i++)
            if (listOfDistances.get(i) <= min) {
                min = listOfDistances.get(i);
                index = i;
            }

        return index;
    }

    public int findCommonWordsNumber(String tweet, String toCompare) {

        String[] tweetWords = tweet.split(" ");
        String[] toCompareWords = toCompare.split(" ");

        byte commonWordsNumber = 0;

        for (String tweetWord : tweetWords)
            for (String toCompareWord : toCompareWords)
                if(tweetWord.equalsIgnoreCase(toCompareWord)) commonWordsNumber++;

        return commonWordsNumber;
    }

    public int findNumberOfWords(String tweet) {

        tweet = cleanString(tweet);
        System.out.println(tweet + (tweet.length() - tweet.replaceAll(" ", "").length()) + "  ");
        return tweet.length() - tweet.replaceAll(" ", "").length();
    }

    public String cleanString(String tweet) {
        LinkedHashMap<String, String> regexes = new LinkedHashMap<String, String>()
        {{
            put("(((https?):\\/\\/)?((www)?\\.)?)?[a-zA-Z0-9\\-]+\\.[\\da-zA-Z]+(\\/[a-zA-Z0-9]+)?(…)?", "");
            put("@[a-zA-Z0-9-_]+\\s?:?", "");
            put("#[a-zA-Z0-9-_]+\\s?:?", "");
            put("\\s?RT\\s", "");
            put("[^\\w^ àâçéèêëîïôûùüÿñæœ']+", "");
            put("\\d[_]+", "");
            put("\\s{1,}", " ");
            put("\\A\\s{1,}", "");
            put("\\z\\s{1,}", "");
        }};


        for (Map.Entry<String, String> entry : regexes.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            tweet = tweet.replaceAll(key, value);
        }

        return tweet;
    }

    public float findEuclideanDistance(String tweet, String classifiedTweet, int classifiedTweetWordsCount) {

        return 1 - findCommonWordsNumber(tweet, classifiedTweet) / (float) (findNumberOfWords(tweet) + classifiedTweetWordsCount);
    }
}