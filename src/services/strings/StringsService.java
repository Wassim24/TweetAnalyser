package services.strings;

/**
 * Created by wassim on 29/10/15.
 */
public interface StringsService
{
    int findCommonWordsNumber(String tweet, String toCompare);
    int findNumberOfWords(String tweet);
    String cleanString(String tweet);
    float findEuclideanDistance(String tweet, String classifiedTweet, int classifiedTweetWordsCount);


}
