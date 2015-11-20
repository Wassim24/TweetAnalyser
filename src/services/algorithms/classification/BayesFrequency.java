package services.algorithms.classification;

import domain.Annotation;
import domain.Tweet;
import domain.Vocabulary;
import services.twitter.VocabularyServiceImpl;

import java.util.*;

public class BayesFrequency
{
    private BayesFrequency() {}

    public static List<Tweet> compute(List<Tweet> toAnnotate)
    {
        List<String> wordsWithFrequency;
        Map<String, Vocabulary> vocabularies = VocabularyServiceImpl.getInstance().getAllKey();
        int frequency;
        double probPositive, probNegative, probNeutre, maxValue;

        for (Tweet tweet : toAnnotate)
        {
            probPositive = 1; probNegative = 1; probNeutre = 1;

            tweet.cleanString();
            wordsWithFrequency = Arrays.asList(tweet.getTweet().split(" "));
            for (String s : new HashSet<String>(wordsWithFrequency))
            {
                if (vocabularies.get(s) == null || s.length() <= 2)
                    continue;

                frequency = Collections.frequency(wordsWithFrequency, s);

                /*
                 * P(t|c) = Ym∈t P(m|c)^(n of m in wordsWithFrequency)
                 */
                probPositive *= Math.pow(vocabularies.get(s).getPosocc(), frequency);
                probNegative *= Math.pow(vocabularies.get(s).getNegocc(), frequency);
                probNeutre *= Math.pow(vocabularies.get(s).getNeuocc(), frequency);
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
