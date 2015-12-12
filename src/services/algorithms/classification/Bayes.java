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
        if (ngramme > 2) ngramme = 2;

        Map<Vocabulary, String> vocabularies = VocabularyServiceImpl.getInstance().getAllKey(ngramme);
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


        for (Tweet tweet : toAnnotate)
        {
            probPositive = 1; probNegative = 1; probNeutre = 1;

            for (String word : generateNgrams(ngramme, tweet.getTweet()))
            {

                for (Vocabulary v : vocabularies.keySet()) {

                    if(v.getWord().length() <= 3)
                        continue;
                    if ( v.getWord().equalsIgnoreCase(word) ) {
                        probPositive *= (v.getPosocc() + 1) / (double) (countPositive + size);
                        probNegative *= (v.getNegocc() + 1) / (double) (countNegative + size);
                        probNeutre *= (v.getNeuocc() + 1) / (double) (countNeutre + size);
                    }
                }
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

    public static List<Tweet> validate(List<Tweet> toAnnotate, List<Vocabulary> learningSet, int ngramme)
    {
        Map<Vocabulary, String> vocabularies = VocabularyServiceImpl.getInstance().getAllKey(ngramme);
        int countPositive = 0, countNegative = 0, countNeutre = 0, size = vocabularies.size();
        double probPositive, probNegative, probNeutre, maxValue;

        Iterator it = vocabularies.entrySet().iterator();
        while (it.hasNext()) {

            Map.Entry pair = (Map.Entry)it.next();

            if ( !learningSet.contains(pair.getKey()) ) {
                it.remove();
            }
        }

        for (Vocabulary vocabulary : vocabularies.keySet())
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

            for (String word : generateNgrams(ngramme, tweet.getTweet()))
            {

                for (Vocabulary v : vocabularies.keySet()) {

                    if(v.getWord().length() <= 3)
                        continue;

                    if ( v.getWord().equalsIgnoreCase(word) )
                    {
                        probPositive *= (v.getPosocc() + 1) / (double)(countPositive + size);
                        probNegative *= (v.getNegocc() + 1) / (double)(countNegative + size);
                        probNeutre *= (v.getNeuocc() + 1) / (double)(countNeutre + size);
                    }

                }

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
