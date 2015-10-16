package domain;

/**
 * Created by r99t on 17-10-15.
 */
public enum Annotation
{
    NEGATIF(-1), NEUTRE(0), POSITIF(1);
    private int value;

    private Annotation(int i)
    {
        this.value = i;
    }

    public int getValue()
    {
        return this.value;
    }
}
