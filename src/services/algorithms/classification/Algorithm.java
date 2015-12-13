package services.algorithms.classification;

public enum Algorithm
{
    DICTIONARY("Dictionary"), KNN("KNN"), BAYES("Bayes Naive"),BAYES_MIXTE("Bayes Mixte"), FREQUENCY_BAYES("Bayes Frequency"),
    FREQUENCY_BAYES_MIXTE("Bayes Frequency Mixte"),  NONE("Without");

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
