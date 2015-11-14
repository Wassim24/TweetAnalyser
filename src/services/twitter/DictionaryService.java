package services.twitter;

import domain.Annotation;
import domain.Dictionary;

import java.io.File;

public interface DictionaryService
{
    boolean addFromFile(File dictionary, Annotation annotation);
    Dictionary get(Annotation annotation);
}
