package domain;

import java.util.ArrayList;
import java.util.List;

public class Dictionary implements EntityBeans
{
    private List<String> words;
    private Annotation annotation;

    public Dictionary(Annotation annotation)
    {
        this.setAnnotation(annotation);
        this.setWords(new ArrayList<String>());
    }

    public Dictionary(List<String> words, Annotation annotation)
    {
        this.setWords(words);
        this.setAnnotation(annotation);
    }
    public List<String> getWords()
    {
        return this.words;
    }
    public void setWords(List<String> words)
    {
        words.forEach(s -> {
            s = s.toLowerCase();
            System.out.println(s);
        });

        this.words = words;
    }
    public Annotation getAnnotation()
    {
        return this.annotation;
    }
    public void setAnnotation(Annotation annotation)
    {
        this.annotation = annotation;
    }
}
