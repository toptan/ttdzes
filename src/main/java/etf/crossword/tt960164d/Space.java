package etf.crossword.tt960164d;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;


public class Space {

    private final Set<Word> wordSet = new HashSet<>();
    private final Stack<Set<Word>> wordStackRemoved = new Stack<>();
    private final Set<SlotsLimitation> slotsLimitationsSet = new HashSet<>();

    private final Point start;
    private final Point direction;
    private final int length;
    private boolean used;

    public Space(Point start, Point direction, int length) {
        this.start = start;
        this.direction = direction;
        this.length = length;
    }

    public Point getStart() {
        return start;
    }
    public Point getDirection() {
        return direction;
    }
    public int getLength() {
        return length;
    }
    public int removeWords(char ch, int index) {
        Set<Word> removedWords = new HashSet<>();
        for (Word w : wordSet) {
            if (w.getText().charAt(index) != ch) {
                removedWords.add(w);
            }
        }
        wordSet.removeAll(removedWords);
        wordStackRemoved.push(removedWords);
        return wordSet.size();
    }
    public void addWords() {
        wordSet.addAll(wordStackRemoved.pop());
    }

    public boolean isUsed() {
        return used;
    }
    public void setUsed(boolean used) {
        this.used = used;
    }

    public Set<SlotsLimitation> getSlotsLimitationsSet() {
        return slotsLimitationsSet;
    }
    public void addSlotLimitation(SlotsLimitation slotsLimitation) {
        slotsLimitationsSet.add(slotsLimitation);
    }

    public void addWords(Set<Word> words) {
        wordSet.addAll(words);
    }
    public Set<Word> getWordSet() {
        return wordSet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Space)) return false;

        Space space = (Space) o;

        return getStart().equals(space.getStart()) && getDirection().equals(space.getDirection());

    }
    @Override
    public int hashCode() {
        int result = getStart().hashCode();
        result = 31 * result + getDirection().hashCode();
        return result;
    }

}
