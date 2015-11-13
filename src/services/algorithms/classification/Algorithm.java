package services.algorithms.classification;

public enum Algorithm
{
    DICTIONARY("Dictionary"), KNN("KNN"), NONE("Without");
    private String value;

    Algorithm(String i)
    {
        this.value = i;
    }
    public String toString()
    {
        return this.value;
    }
}
