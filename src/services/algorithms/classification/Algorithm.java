package services.algorithms.classification;

public enum Algorithm
{
    DICTIONARY("Dictionary"), KNN("KNN"), BAYES("Bayes n-grammes"), FREQUENCY_BAYES("Bayes frequency n-grammes"), NONE("Without");
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
