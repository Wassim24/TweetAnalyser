package services.algorithms.classification;

import domain.Annotation;
import domain.Tweet;
import domain.Vocabulary;
import services.twitter.VocabularyServiceImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class BayesNgrams {

    private BayesNgrams() {}

    public static List<Tweet> compute(List<Tweet> toAnnotate, int ngrams)
    {
        Map<String, Vocabulary> vocabularies = VocabularyServiceImpl.getInstance().getAllKey(ngrams);
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
            for (String s : new HashSet<String>(tweet.getTweetNgram(ngrams)))
            {
                if (vocabularies.get(s) == null || s.length() <= 3)
                    continue;

            /*
             * P(class|t) = Ym∈t P(m|class) * P(class)
             * P(m|class) = P(m|c) = (n(m, c) + 1) / (n(c) + N)
             */

                probPositive *= (vocabularies.get(s).getPosocc() + 1) / (double)(countPositive + size);
                probNegative *= (vocabularies.get(s).getNegocc() + 1) / (double)(countNegative + size);
                probNeutre *= (vocabularies.get(s).getNeuocc() + 1) / (double)(countNeutre + size);
            }

            probPositive *= countPositive / (double) size;
            probNegative *= countNegative / (double) size;
            probNeutre *= countNeutre / (double) size;

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
