package services.labeling;

import domain.TweetEntityBeans;

import java.util.List;

public interface LabelingService {

    List<TweetEntityBeans> KNNLabeling(List<TweetEntityBeans> notClassifiedTweets, int numberOfNeighbours);
    int findIndexOfMin(List<Float> listOfDistances, float currentDistance);
    int findIndexOfMin(List<Float> listOfDistances);

}
