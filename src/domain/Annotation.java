package domain;

public enum Annotation
{
    NEGATIF(-1), NEUTRE(0), POSITIF(1), NONANNOTE(-2);
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
