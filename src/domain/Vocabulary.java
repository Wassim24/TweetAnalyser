package domain;

public class Vocabulary implements EntityBeans
{
    private String word;
    private int posocc, negocc, neuocc;

    public Vocabulary(String word, int posocc, int negocc, int neuocc)
    {
        this.setWord(word);
        this.setPosocc(posocc);
        this.setNegocc(negocc);
        this.setNeuocc(neuocc);
    }

    public boolean equals(Object vocabulary)
    {
        if(vocabulary == this)
            return true;

        if(!(vocabulary instanceof Vocabulary))
            return false;

        return this.getWord().equals(((Vocabulary) vocabulary).getWord());
    }

    public void setWord(String word) {this.word = word;}
    public void setPosocc(int posocc) {this.posocc = posocc;}
    public void setNegocc(int negocc) {this.negocc = negocc;}
    public void setNeuocc(int neuocc) {this.neuocc = neuocc;}
    public int getNegocc() {return this.negocc;}
    public int getPosocc() {return this.posocc;}
    public String getWord() {return this.word;}
    public int getNeuocc() {return this.neuocc;}
}
