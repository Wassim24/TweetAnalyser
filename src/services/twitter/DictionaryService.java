package services.twitter;

import domain.Annotation;
import domain.Dictionary;

import java.io.File;
import java.util.List;

public interface DictionaryService
{
    boolean addFromFile(File dictionary, Annotation annotation);
    Dictionary get(Annotation annotation);
    List<Dictionary> getAll();
}
