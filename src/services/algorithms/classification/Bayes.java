package services.algorithms.classification;

import domain.Annotation;
import domain.Tweet;
import domain.Vocabulary;
import services.dao.TweetDaoFactory;
import services.twitter.VocabularyServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Bayes
{
    private Bayes() {}

    public static List<Tweet> compute(List<Tweet> toAnnotate)
    {
        Map<String, Vocabulary> vocabularies = VocabularyServiceImpl.getInstance().getAllKey();
        int countPositive = 0, countNegative = 0, countNeutre = 0, size = vocabularies.size();
        double probPositive, probNegative, probNeutre, maxValue;

        for (Vocabulary vocabulary : vocabularies.values())
        {
            if (vocabulary.getPosocc() > 0)
                countPositive++;

            if (vocabulary.getNegocc() > 0)
                countNegative++;

            if (vocabulary.getNeuocc() > 0)
                countNeutre++;
        }

        for (Tweet tweet : toAnnotate)
        {
            probPositive = 1; probNegative = 1; probNeutre = 1;

            for (String s : tweet.getTweet().split(" "))
            {
                if (vocabularies.get(s) == null)
                    continue;

                probPositive *= (vocabularies.get(s).getPosocc() + 1) / (double)(countPositive + size);
                probNegative *= (vocabularies.get(s).getNegocc() + 1) / (double)(countNegative + size);
                probNeutre *= (vocabularies.get(s).getNeuocc() + 1) / (double)(countNeutre + size);
            }

            maxValue = Math.max(probPositive, Math.max(probNegative, probNeutre));

            if (maxValue == probNeutre)
                tweet.setAnnotation(Annotation.NEUTRE);
            else
                if (maxValue == probNegative)
                    tweet.setAnnotation(Annotation.NEGATIF);
                else
                    tweet.setAnnotation(Annotation.POSITIF);
        }

        return toAnnotate;
    }
}
