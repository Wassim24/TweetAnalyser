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
        int frequency = 1;
        double probPositive, probNegative, probNeutre, maxValue;

        int countPositive = 0, countNegative = 0, countNeutre = 0, size = vocabularies.size();
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
            wordsWithFrequency = generateNgrams(ngramme, tweet.getTweet());

            for (String word : new HashSet<>(wordsWithFrequency))
            {
                frequency = Collections.frequency(wordsWithFrequency, word);

                for (Vocabulary v : vocabularies.keySet())
                {
                    if(v.getWord().length() <= 3 || v == null)
                        continue;

                    if ( v.getWord().equalsIgnoreCase(word) ) {

                        probPositive *= (v.getPosocc() + 1) / (double) (countPositive + size);
                        probNegative *= (v.getNegocc() + 1) / (double) (countNegative + size);
                        probNeutre *= (v.getNeuocc() + 1) / (double) (countNeutre + size);
                    }
                }
            }

            probPositive = Math.pow(probPositive, frequency);
            probNegative = Math.pow(probNegative, frequency);
            probNeutre = Math.pow(probNeutre, frequency);

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
