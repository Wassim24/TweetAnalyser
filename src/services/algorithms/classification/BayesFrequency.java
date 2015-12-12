package services.algorithms.classification;

import domain.Annotation;
import domain.Tweet;
import domain.Vocabulary;
import services.twitter.VocabularyServiceImpl;

import java.util.*;

public class BayesFrequency
{
    private BayesFrequency() {}

    public static List<Tweet> compute(List<Tweet> toAnnotate, int ngramme)
    {
        return compute(VocabularyServiceImpl.getInstance().getAllKey(ngramme), toAnnotate, ngramme);
    }

    public static List<Tweet> compute(Map<Vocabulary, String> vocabularies, List<Tweet> toAnnotate, int ngramme)
    {
        List<String> wordsWithFrequency;
        int frequency;
        double probPositive, probNegative, probNeutre, maxValue;

        ArrayList<Tweet> response = new ArrayList<Tweet>();
        for(Tweet p : toAnnotate)
            response.add(p.clone());

        for (Tweet tweet : response)
        {
            probPositive = 1; probNegative = 1; probNeutre = 1;
            wordsWithFrequency = generateNgrams(ngramme, tweet.getTweet());

            for (String word : new HashSet<String>(wordsWithFrequency))
            {
                frequency = Collections.frequency(wordsWithFrequency, word);

                for (Vocabulary v : vocabularies.keySet())
                {
                    if(v.getWord().length() <= 3 || v == null)
                        continue;

                    if ( v.getWord().equalsIgnoreCase(word) ) {

                        probPositive *= Math.pow(v.getPosocc(), frequency);
                        probNegative *= Math.pow(v.getNegocc(), frequency);
                        probNeutre *= Math.pow(v.getNeuocc(), frequency);
                    }
                }
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
                if (array[j].length() < 3)
                    continue parentloop;
                else
                    sequence += " " + array[j];

            sequence = sequence.replaceFirst(" ", "");
            ngrams.add(sequence);
        }

        return ngrams;
    }
}
