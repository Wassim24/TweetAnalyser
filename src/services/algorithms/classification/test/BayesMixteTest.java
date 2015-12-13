package services.algorithms.classification.test;

import domain.Tweet;
import domain.Vocabulary;
import services.algorithms.classification.Bayes;
import services.algorithms.classification.BayesMixte;
import services.twitter.VocabularyServiceImpl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BayesMixteTest
{
    public static List<Tweet> test(List<Tweet> toAnnotate, List<Vocabulary> learningSet, int ngramme)
    {
        Map<Vocabulary, String> vocabularies = VocabularyServiceImpl.getInstance().getAllKey(ngramme);

        Iterator it = vocabularies.entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry pair = (Map.Entry)it.next();

            if (!learningSet.contains(pair.getKey()))
                it.remove();
        }

        return BayesMixte.compute(vocabularies, toAnnotate, ngramme);
    }
}
