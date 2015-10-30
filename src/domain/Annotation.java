package domain;

public enum Annotation
{
    NONANNOTE(-2), NEGATIF(-1), NEUTRE(0), POSITIF(1);
    private int value;

    Annotation(int i)
    {
        this.value = i;
    }

    public int getValue()
    {
        return this.value;
    }
}
