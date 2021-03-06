package domain;

public class Vocabulary implements EntityBeans
{
    private int id, posocc, negocc, neuocc, ngramme;
    private String word;

    public Vocabulary(String word, int posocc, int negocc, int neuocc, int ngramme)
    {
        this.setWord(word);
        this.setPosocc(posocc);
        this.setNegocc(negocc);
        this.setNeuocc(neuocc);
        this.setNgramme(ngramme);
    }

    public Vocabulary(String word)
    {
        this.setWord(word);
    }

    public Vocabulary(int id, String word, int posocc, int negocc, int neuocc, int ngramme)
    {
        this(word, posocc, negocc, neuocc, ngramme);
        this.setId(id);
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
    public int getId() {return this.id;}
    public void setId(int id) {this.id = id;}
    public int getNgramme() {return this.ngramme;}
    public void setNgramme(int ngramme) {this.ngramme = ngramme;}
}
