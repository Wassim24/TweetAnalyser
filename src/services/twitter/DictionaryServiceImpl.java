package services.twitter;


import domain.Annotation;
import domain.Dictionary;
import services.dao.DictionaryDaoFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class DictionaryServiceImpl implements DictionaryService
{
    private static DictionaryServiceImpl ourInstance = new DictionaryServiceImpl();
    public static DictionaryServiceImpl getInstance() {
        return ourInstance;
    }
    private DictionaryServiceImpl() {}

    @Override
    public boolean addFromFile(File dictionary, Annotation annotation)
    {
        Dictionary d = new Dictionary(annotation);
        try
        {
            Files.readAllLines(dictionary.toPath(), StandardCharsets.ISO_8859_1).forEach(s -> {
                for (String s1 : s.split(", "))
                    if (s1.trim().length() > 0)
                        d.getWords().add(s1.trim());
            });
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return DictionaryDaoFactory.getInstance().addAll(d);
    }

    @Override
    public Dictionary get(Annotation annotation)
    {
        return DictionaryDaoFactory.getInstance().get(annotation);
    }
}
