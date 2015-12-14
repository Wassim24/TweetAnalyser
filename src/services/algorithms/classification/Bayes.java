package services.algorithms.classification;

import domain.Annotation;
import domain.Tweet;
import domain.Vocabulary;
import services.twitter.VocabularyServiceImpl;

import java.util.*;

public class Bayes
{
    private Bayes() {}

    public static List<Tweet> compute(List<Tweet> toAnnotate, int ngramme)
    {
        return compute(VocabularyServiceImpl.getInstance().getAllKey(ngramme), toAnnotate, ngramme);
    }

    public static List<Tweet> compute(Map<Vocabulary, String> vocabularies, List<Tweet> toAnnotate, int ngramme)
    {
        int countPositive = 0, countNegative = 0, countNeutre = 0, size = vocabularies.size();
        double probPositive, probNegative, probNeutre, maxValue;

        for (Vocabulary vocabulary : vocabularies.keySet())
        {
            if (vocabulary.getPosocc() > 0)
                countPositive++;

            if (vocabulary.getNegocc() > 0)
                countNegative++;

            if (vocabulary.getNeuocc() > 0)
                countNeutre++;
        }

        ArrayList<Tweet> response = new ArrayList<Tweet>();

        for(Tweet p : toAnnotate)
            response.add(p.clone());

        for (Tweet tweet : response)
        {
            probPositive = 1; probNegative = 1; probNeutre = 1;

            for (String word : new HashSet<>(generateNgrams(ngramme, tweet.getTweet())))
            {
                for (Vocabulary v : vocabularies.keySet())
                {
                    if (v.getWord().equalsIgnoreCase(word))
                    {
                        probPositive *= (v.getPosocc() + 1) / (double) (countPositive + size);
                        probNegative *= (v.getNegocc() + 1) / (double) (countNegative + size);
                        probNeutre *= (v.getNeuocc() + 1) / (double) (countNeutre + size);
                    }
                }
            }

            probPositive *= (countPositive / (double) size);
            probNegative *= (countNegative / (double) size);
            probNeutre *= (countNeutre / (double) size);

            maxValue = Math.max(probPositive, Math.max(probNegative, probNeutre));

            if (maxValue == probNeutre)
                tweet.setAnnotation(Annotation.NEUTRE);
            else
                if (maxValue == probNegative)
                    tweet.setAnnotation(Annotation.NEGATIF);
                else
                    tweet.setAnnotation(Annotation.POSITIF);
        }

        return response;
    }

    private static List<String> generateNgrams(int n, String text)
    {
        String array[] = text.split(" ");
        List<String> ngrams = new ArrayList<String>();
        String sequence;

        parentloop:
        for (int i = 0; i < (array.length - (n - 1)); i++)
        {
            sequence = "";

            for (int j = i; j < n + i; j++)
                if (array[j].length() < 4)
                    continue parentloop;
                else
                    sequence += " " + array[j];

            sequence = sequence.replaceFirst(" ", "");
            ngrams.add(sequence);
        }

        return ngrams;
    }
}
