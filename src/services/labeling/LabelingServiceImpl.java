package services.labeling;

import domain.Annotation;
import domain.TweetEntityBeans;
import services.dao.DaoFactory;
import services.strings.StringsServiceImpl;

import java.util.ArrayList;
import java.util.List;

public class LabelingServiceImpl implements LabelingService {

    private static LabelingServiceImpl labelingServiceImpl = null;

    private LabelingServiceImpl() {}

    public static LabelingServiceImpl getInstance() {
        if (labelingServiceImpl == null)
            labelingServiceImpl = new LabelingServiceImpl();

        return labelingServiceImpl;
    }

    @Override
    public List<TweetEntityBeans> KNNLabeling(List<TweetEntityBeans> tweetsToAnnoteList, int numberOfNeighbours) {

        List<Float> distancesList = new ArrayList<>();
        List<Integer> neighboursIdsList = new ArrayList<>();
        List<TweetEntityBeans> annotedTweets = new ArrayList<>();
        List<TweetEntityBeans> tweetsInDataBase = DaoFactory.getInstance().all();

        float calculatedEuclideanDistance = 0;

        // Loop for iterating over the selected tweets
        for (TweetEntityBeans unannotedTweet : tweetsToAnnoteList) {

            // Loop for putting the first numberOfNeighbours in the distancesList
            for (TweetEntityBeans tweetInDB : tweetsInDataBase) {

                calculatedEuclideanDistance = StringsServiceImpl.getInstance().
                        findEuclideanDistance(unannotedTweet.getTweet(), tweetInDB.getTweet(), tweetInDB.getWordsCount());

                distancesList.add(calculatedEuclideanDistance);
                neighboursIdsList.add(tweetInDB.getId());

                if (distancesList.size() == numberOfNeighbours) break;
            }

            //Loop for getting the tweets in DB and comparing them to the neighbours
            for (int i = numberOfNeighbours + 1; i < tweetsInDataBase.size() - 1; i++) {

                calculatedEuclideanDistance = StringsServiceImpl.getInstance().
                        findEuclideanDistance(unannotedTweet.getTweet(), tweetsInDataBase.get(i).getTweet(), tweetsInDataBase.get(i).getWordsCount());

                int minDistIndex = findIndexOfMin(distancesList, calculatedEuclideanDistance);

                // For replacing the distance at minDistIndex with the new calculatedEuclideanDistance and the id
                distancesList.set(minDistIndex, calculatedEuclideanDistance);
                neighboursIdsList.set(minDistIndex, tweetsInDataBase.get(i).getId());
            }

            // For annotating the new tweets based on the min distance of the neighbours annotation
            int tweetInDBAnnotation = tweetsInDataBase.get(neighboursIdsList.get(findIndexOfMin(distancesList)) - 1).getAnnotation();
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

    @Override
    public int findIndexOfMin(List<Float> listOfDistances, float currentDistance) {

        int minIndex = 0;
        for (int i = 0; i < listOfDistances.size(); i++)
            if (currentDistance <= listOfDistances.get(i)) minIndex = i;

        return minIndex;
    }

    @Override
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
}
