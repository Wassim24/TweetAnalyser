package services.algorithms.classification;

import domain.Annotation;
import domain.Dictionary;
import domain.Tweet;
import services.dao.DictionaryDaoFactory;

import java.util.ArrayList;
import java.util.List;

public class Glossary
{
    private Glossary() {}

    public static List<Tweet> compute(List<Tweet> toAnnotate)
    {
        Dictionary positiveWords = DictionaryDaoFactory.getInstance().get(Annotation.POSITIF), negativeWords = DictionaryDaoFactory.getInstance().get(Annotation.NEGATIF);

        ArrayList<Tweet> response = new ArrayList<Tweet>();
        for(Tweet p : toAnnotate)
            response.add(p.clone());

        for (Tweet tweetToAnnotate : response)
        {
            int delta = 0;

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
        }

        return response;
    }
}