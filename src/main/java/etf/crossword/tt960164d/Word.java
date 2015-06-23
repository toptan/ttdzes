package etf.crossword.tt960164d;


public class Word {

    private final String text;
    private boolean used;

    public Word(String text) {
        this.text = text;
    }
    public String getText() {
        return text;
    }

    public boolean isUsed() {
        return used;
    }
    public void setUsed(boolean isUsed) {
        used = isUsed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Word)) return false;
        Word word1 = (Word) o;
        return getText() != null ? getText().equals(word1.getText()) : word1.getText() == null;
    }

    @Override
    public int hashCode() {
        return text.length();
    }
}
