package etf.crossword.tt960164d;

import java.awt.*;
import java.util.ArrayList;


public class Solver {

    private final char[][] crossword;
    private final Space[] slots;
    private final CrosswordApp crosswordApps;
    private int[][] letterUsage;
    private ArrayList<char[][]> results;


    public Solver(CrosswordApp crosswordApps, char[][] crossword, Space[] slots) {
        this.crosswordApps = crosswordApps;
        this.crossword = crossword;
        this.slots = slots;
    }

    public ArrayList<char[][]> solve(int mode) {
        results = new ArrayList<>();
        letterUsage = new int[crossword.length][crossword[0].length];
        if (mode == 1) {
            printSetsOfSlots();
            waitForClick();
            fillCrosswordModeOne(0, 0);
            StringBuilder sb = new StringBuilder("");
            sb.append("\nIgra zavrsena. Broj nadjenih resenja je ").append(results.size()).append("\n");
            sb.append("Klikom na dugme 'Next' pojavice vam se lista resenja");
            crosswordApps.descriptionAdd(sb.toString());
            crosswordApps.setBoard(crossword);
            waitForClick();
        } else {
            long time = System.currentTimeMillis();
            fillCrosswordModeTwo(0, 0);
            crosswordApps.setTime(System.currentTimeMillis() - time);
        }


        return results;
    }

    private void fillCrosswordModeOne(int slotUsed, int slotIndex) {
        if (slotUsed == slots.length) {
            results.add(copyBoard(crossword));
            printResultFound(results.size());
            waitForClick();
            return;
        }
        slots[slotIndex].setUsed(true);
        for (Word word : slots[slotIndex].getWordSet()) {
            putWordInSlot(word, slots[slotIndex]);

            ArrayList<Space> slotsIndex = new ArrayList<>();
            int step = 0;
            for (SlotsLimitation s : slots[slotIndex].getSlotsLimitationsSet()) {
                if (!s.getSlot().isUsed()) {
                    int wordsNumber = s.getSlot().removeWords(word.getText().charAt(s.getIndex()), s.getSlotIndex());
                    slotsIndex.add(s.getSlot());
                    if (wordsNumber == 0) {
                        step = -1;
                        break;
                    }
                }
            }
            printCurrentWord(word);



            if (step == 0) {
                int space = 0;
                boolean oneElement = false;
                for (int i = 0; i < slots.length; i++) {
                    if (!slots[i].isUsed()) {
                        space = i;
                        if (slots[i].getWordSet().size() == 1) {
                            oneElement = true;
                            break;
                        }

                    }
                }
                crosswordApps.setBoard(crossword);
                printFoundWord(oneElement, space);
                waitForClick();
                printSetsOfSlots();
                fillCrosswordModeOne(slotUsed + 1, space);
            } else {

                printNotFoundWord();

                waitForClick();
            }

            for (Space s : slotsIndex) {
                s.addWords();
            }
            removeWordFromSlot(word, slots[slotIndex]);
        }
        slots[slotIndex].setUsed(false);

        printRUp(slotIndex + 1);
        crosswordApps.setBoard(crossword);
        waitForClick();
    }



    private void fillCrosswordModeTwo(int slotUsed, int slotIndex) {
        if (slotUsed == slots.length) {
            results.add(copyBoard(crossword));
            return;
        }
        slots[slotIndex].setUsed(true);
        for (Word word : slots[slotIndex].getWordSet()) {
            if (!word.isUsed()) {
                ArrayList<Space> slotsIndex = new ArrayList<>();
                int step = 0;
                for (SlotsLimitation s : slots[slotIndex].getSlotsLimitationsSet()) {
                    if (!s.getSlot().isUsed()) {
                        int wordsNumber = s.getSlot().removeWords(word.getText().charAt(s.getIndex()), s.getSlotIndex());
                        slotsIndex.add(s.getSlot());
                        if (wordsNumber == 0) {
                            step = -1;
                            break;
                        }
                    }
                }
                if (step == 0) {
                    putWordInSlot(word, slots[slotIndex]);
                    int space = 0;
                    for (int i = 0; i < slots.length; i++) {
                        if (!slots[i].isUsed()) {
                            space = i;
                            if (slots[i].getWordSet().size() == 1) {
                                break;
                            }
                        }
                    }
                    fillCrosswordModeTwo(slotUsed + 1, space);
                    removeWordFromSlot(word, slots[slotIndex]);
                }

                for (Space s : slotsIndex) {
                    s.addWords();
                }
            }
        }
        slots[slotIndex].setUsed(false);
    }


    private void printRUp(int slotIndex) {
        StringBuilder sb = new StringBuilder("");
        sb.append("\nNema vise reči slot ").append(slotIndex).append(", povratak na prethodni slot\n");
        crosswordApps.descriptionAdd(sb.toString());
    }
    private void printResultFound(int num) {
        StringBuilder sb = new StringBuilder("");
        sb.append("\nRESENJE BROJ ").append(num).append("\n");
        crosswordApps.descriptionAdd(sb.toString());
    }
    private void printFoundWord(boolean oneElement, int space) {
        StringBuilder sb = new StringBuilder("");
        if (oneElement) {
            sb.append("\nZa trenutno izabranu rec ostala nam je samo jedana moguca rec u ").append(space + 1).append(". slotu.\n")
                    .append("Biramo nju za sledecu iteraciju!\n\n");
        } else {
            sb.append("\nBiramo prvi slobodan slot za sledecu iteraciju. Slot broj ").append(space + 1).append("\n");

        }
        crosswordApps.descriptionAdd(sb.toString());
    }
    private void printCurrentWord(Word word) {
        StringBuilder sb = new StringBuilder("");
        sb.append("\nIzabrana rec je ").append(word.getText()).append("\n");
        crosswordApps.descriptionAdd(sb.toString());
    }
    private void printNotFoundWord() {
        StringBuilder sb = new StringBuilder("");
        sb.append("\nZa trenutno izabranu rec jedan od slotova je ostao bez mogucih resenja\n");
        crosswordApps.descriptionAdd(sb.toString());
    }
    private void printSetsOfSlots() {
        StringBuilder sb = new StringBuilder("");
        sb.append("Sadrzaj skupova\n");
        for (int i = 0; i < slots.length; i++) {
            if (!slots[i].isUsed()) {
                sb.append("Slot ").append(i + 1).append(": ");
                for (Word w : slots[i].getWordSet()) {
                    sb.append(w.getText()).append(", ");
                }
                sb.setLength(Math.max(sb.length() - 2, 0));
                sb.append("\n");
            }
        }
        crosswordApps.descriptionAdd(sb.toString());
    }

    private void putWordInSlot(Word word, Space slot) {
        Point position = new Point(slot.getStart());
        for (int i = 0; i < slot.getLength(); i++) {
            crossword[position.x][position.y] = word.getText().charAt(i);
            letterUsage[position.x][position.y]++;
            position.x += slot.getDirection().x;
            position.y += slot.getDirection().y;
        }
        word.setUsed(true);
    }
    private void removeWordFromSlot(Word word, Space slot) {
        Point position = new Point(slot.getStart());
        for (int i = 0; i < slot.getLength(); i++) {
            letterUsage[position.x][position.y]--;
            if (letterUsage[position.x][position.y] == 0) {
                crossword[position.x][position.y] = CrosswordApp.BLANK;
            }
            position.x += slot.getDirection().x;
            position.y += slot.getDirection().y;
        }
        word.setUsed(false);
    }

    private void waitForClick() {
        while (!crosswordApps.isGo()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        crosswordApps.setGo(false);
    }

    private char[][] copyBoard(char[][] crossword) {
        char[][] board = new char[crossword.length][crossword[0].length];
        for (int i = 0; i < crossword.length; i++) {
            for (int j = 0; j < crossword[0].length; j++) {
                board[i][j] = crossword[i][j];
            }
        }
        return board;
    }
}

