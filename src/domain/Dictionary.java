package domain;

import java.util.ArrayList;
import java.util.List;

public class Dictionary implements EntityBeans
{
    private int id;
    private List<String> words;
    private Annotation annotation;
    private String word;

    public Dictionary(int id, String word, Annotation annotation) {
        this.setId(id);
        this.setWord(word);
        this.setAnnotation(annotation);
    }

    public Dictionary(Annotation annotation)
    {
        this.setAnnotation(annotation);
        this.setWords(new ArrayList<>());
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

    public int getId() {return this.id;}
    public void setId(int id) {this.id = id;}

    public String getWord() {return this.word;}
    public void setWord(String word) {this.word = word;}
}
