package services.algorithms.classification;

public enum Algorithm
{
    DICTIONARY("Dictionary"), KNN("KNN"), SIMPLE_BAYES("Bayésien"), NONE("Without");
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
