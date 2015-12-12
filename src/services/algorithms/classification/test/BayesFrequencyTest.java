package services.algorithms.classification.test;

import domain.Tweet;
import domain.Vocabulary;
import services.algorithms.classification.BayesFrequency;
import services.twitter.VocabularyServiceImpl;

import java.util.*;

/**
 * Created by r99t on 12-12-15.
 */
public class BayesFrequencyTest
{
    public static List<Tweet> test(List<Tweet> toAnnotate, List<Vocabulary> learningSet, int ngramme)
    {
        Map<Vocabulary, String> vocabularies = VocabularyServiceImpl.getInstance().getAllKey(ngramme);

        Iterator it = vocabularies.entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry pair = (Map.Entry)it.next();

            if (!learningSet.contains(pair.getValue()))
                it.remove();
        }

        return BayesFrequency.compute(vocabularies, toAnnotate, ngramme);
    }
}
