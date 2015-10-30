package services.strings;

import java.util.LinkedHashMap;
import java.util.Map;

public class StringsServiceImpl implements StringsService {

    private static StringsServiceImpl stringsServiceImpl = null;
    private StringsServiceImpl(){}

    public static StringsServiceImpl getInstance() {
        if(stringsServiceImpl == null)
            stringsServiceImpl = new StringsServiceImpl();

        return stringsServiceImpl;
    }

    @Override
    public int findCommonWordsNumber(String tweet, String toCompare) {

        String[] tweetWords = tweet.split(" ");
        String[] toCompareWords = toCompare.split(" ");

        byte commonWordsNumber = 0;

        for (String tweetWord : tweetWords)
            for (String toCompareWord : toCompareWords)
                if(tweetWord.equalsIgnoreCase(toCompareWord)) commonWordsNumber++;

        return commonWordsNumber;
    }

    @Override
    public int findNumberOfWords(String tweet) {

        tweet = cleanString(tweet);
        return tweet.length() - tweet.replaceAll(" ", "").length() + 1;
    }

    @Override
    public String cleanString(String tweet) {
        LinkedHashMap<String, String> regexes = new LinkedHashMap<String, String>()
        {{
                put("(((https?):\\/\\/)?((www)?\\.)?)?[a-zA-Z0-9\\-]+\\.[\\da-zA-Z]+(\\/[a-zA-Z0-9]+)\\W+", "");
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

    @Override
    public float findEuclideanDistance(String tweet, String classifiedTweet, int classifiedTweetWordsCount) {

        return 1 - findCommonWordsNumber(tweet, classifiedTweet) / (float) (findNumberOfWords(tweet) + classifiedTweetWordsCount);
    }

}
