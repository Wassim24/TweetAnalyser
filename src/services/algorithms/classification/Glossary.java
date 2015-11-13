package services.algorithms.classification;

import domain.Annotation;
import domain.Dictionary;
import domain.Tweet;
import services.dao.DictionaryDaoFactory;

import java.util.List;

public class Glossary
{
    private Glossary() {}

    public static List<Tweet> compute(List<Tweet> tweetsToAnnote)
    {
        Dictionary positiveWords = DictionaryDaoFactory.getInstance().get(Annotation.POSITIF), negativeWords = DictionaryDaoFactory.getInstance().get(Annotation.NEGATIF);

        for (Tweet tweetToAnnotate : tweetsToAnnote)
        {
            int delta = 0;

            tweetToAnnotate.cleanString();
            for (String wordToFindAnnotation : tweetToAnnotate.getTweet().split(" "))
            {
                if (positiveWords.getWords().contains(wordToFindAnnotation.toLowerCase()))
                    delta++;

                if (negativeWords.getWords().contains(wordToFindAnnotation.toLowerCase()))
                    delta--;
            }

            if (delta == 0)
                tweetToAnnotate.setAnnotation(Annotation.NEUTRE);
            else
                if (delta < 0)
                    tweetToAnnotate.setAnnotation(Annotation.NEGATIF);
                else
                    tweetToAnnotate.setAnnotation(Annotation.POSITIF);

            System.out.println(tweetToAnnotate.getAnnotationValue());
        }

        return tweetsToAnnote;
    }
}
