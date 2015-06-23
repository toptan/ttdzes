package etf.crossword.tt960164d;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CrosswordApp extends JFrame {


    public static final char FILLED = '#';
    public static final char BLANK = ' ';

    private JList list;
    private DefaultListModel model;
    private int xField;
    private int yField;
    private ArrayList<Set<Word>> words;
    private char[][] crossword;
    private Space[] slots;
    private int mode;
    private boolean go;


    private JTextArea description;
    private JButton btnStart;

    private JLabel lblTime;
    private CrosswordPanel crosswordPanel;
    private ArrayList<char[][]> results;
    private JPanel south;


    public CrosswordApp(int xField, int yField) {
        this.xField = xField;
        this.yField = yField;
        initUI();
    }

    public static void main(String args[]) {
        CrosswordApp m = new CrosswordApp(5, 5);
        m.setVisible(true);
    }


    // PUBLIC
    public void descriptionAdd(String s) {
        description.append(s);
    }
    public void setTime(long time) {
        lblTime.setText(time + "ms");
    }
    public boolean isGo() {
        return go;
    }
    public void setGo(boolean go) {
        this.go = go;
    }
    public void setBoard(char[][] board) {
        crosswordPanel.fillBoard(board);
    }


    //PRIVATE
    private void initUI() {

        setTitle("Crossword");
        setSize(630, 800);
        setLayout(new BorderLayout());

        crosswordPanel = new CrosswordPanel(xField, yField);
        crosswordPanel.setBorder(new EmptyBorder(50, 50, 50, 50));
        add(crosswordPanel, BorderLayout.CENTER);

        btnStart = new JButton("Start");
        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CrosswordApp.this.remove(btnStart);

                crossword = crosswordPanel.initCrossword();
                crosswordPanel.disableAll();
                if (checkMode() == 1) {
                    addPanelMode1();
                } else {
                    addPanelMode2();
                }

                makeCrosswordSlot();
                addWordsDialog();
                addWordsToSlot();
                makeSlotLimitation();

                if (mode == 1) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Solver s = new Solver(CrosswordApp.this, crossword, slots);
                            results = s.solve(1);
                            remove(south);
                            addPanelMode2();
                            for (int i = 0; i < results.size(); i++) {
                                model.addElement("Result " + (i + 1));
                            }
                        }
                    }).start();
                } else {

                    Solver s = new Solver(CrosswordApp.this, crossword, slots);
                    results = s.solve(2);

                    for (int i = 0; i < results.size(); i++) {
                        model.addElement("Result " + (i + 1));
                    }
                }
            }
        });
        add(btnStart, BorderLayout.SOUTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    private void addWordsToSlot() {
        for (Space s : slots) {
            s.addWords(words.get(s.getLength()));
        }
    }
    private int checkMode() {
        Object[] options = {"Mode1",
                "Mode2"};
        int n = JOptionPane.showOptionDialog(this,
                "Choose",
                "Choose mode",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]);

        if (n == JOptionPane.YES_OPTION) {
            return 1;
        } else {
            return 0;
        }
    }
    private void addPanelMode1() {
        mode = 1;
        south = new JPanel();
        south.setLayout(new BorderLayout());

        description = new JTextArea(8, this.getWidth());
        /*DefaultCaret caret = (DefaultCaret)description.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);*/

        description.setEditable(false);
        JScrollPane pane = new JScrollPane(description);
        south.add(pane, BorderLayout.CENTER);
        JButton next = new JButton("Next");
        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setGo(true);
            }
        });
        JPanel east = new JPanel();
        east.add(next);
        south.add(east, BorderLayout.EAST);
        add(south, BorderLayout.SOUTH);
        validate();
        repaint();
    }
    private void addPanelMode2() {
        mode = 2;
        south = new JPanel();
        south.setLayout(new BorderLayout());
        lblTime = new JLabel();
        south.add(lblTime, BorderLayout.NORTH);

        model = new DefaultListModel();
        list = new JList(model);
        JScrollPane pane = new JScrollPane(list);
        list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) { //This line prevents double events
                    setBoard(results.get(list.getSelectedIndex()));
                }
            }
        });
        south.add(pane, BorderLayout.SOUTH);
        add(south, BorderLayout.SOUTH);
        validate();
        repaint();
    }
    private int initWordsSets() {
        int size = (xField > yField ? xField : yField);
        words = new ArrayList<>(size);
        for (int i = 0; i < size + 1; i++) {
            Set<Word> wordSet = new HashSet<>();
            words.add(i, wordSet);
        }
        return size;
    }
    private void addWordsDialog() {
        int size = initWordsSets();

        Object[] options = {"Choose file",
                "Add word"};
        int n = JOptionPane.showOptionDialog(this,
                "Add words",
                "Choose?",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]);

        if (n == JOptionPane.YES_OPTION) {
            JFileChooser chooser = new JFileChooser();
            int returnVal = chooser.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try (BufferedReader br = new BufferedReader(new FileReader(chooser.getSelectedFile()))) {
                    for (String word; (word = br.readLine()) != null; ) {
                        if (word.length() <= size) {
                            words.get(word.length()).add(new Word(word));
                        }
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        } else if (n == JOptionPane.NO_OPTION) {
            while (true) {
                String word = JOptionPane.showInputDialog(this, "Enter word");
                if ((word == null) || (word.equals(""))) {
                    break;
                }
                if (word.length() <= size) {
                    words.get(word.length()).add(new Word(word));
                }
            }
        }
    }
    private void makeCrosswordSlot() {
        ArrayList<Space> s = new ArrayList<>();
        int index = 1;
        for (int i = 0; i < xField; i++) {
            for (int j = 0; j < yField; j++) {

                if ((j == 0) || (crossword[i][j - 1] == CrosswordApp.FILLED)) {
                    int length = 0;
                    while ((j + length < yField) && (crossword[i][j + length] != CrosswordApp.FILLED)) {
                        length++;

                    }
                    if (length != 0) {
                        crosswordPanel.setBottomLabel(index++, i, j);
                        s.add(new Space(new Point(i, j), new Point(0, 1), length));
                    }
                }
                if ((i == 0) || (crossword[i - 1][j] == CrosswordApp.FILLED)) {
                    int length = 0;
                    while ((i + length < xField) && (crossword[i + length][j] != CrosswordApp.FILLED)) {
                        length++;
                    }
                    if (length != 0) {
                        crosswordPanel.setTopLabel(index++, i, j);
                        s.add(new Space(new Point(i, j), new Point(1, 0), length));
                    }
                }
            }
        }
        slots = new Space[s.size()];
        s.toArray(slots);
    }
    private void makeSlotLimitation() {
        for (int i = 0; i < slots.length - 1; i++) {
            Point sStart = slots[i].getStart();
            Point sEnd = new Point(sStart.x + ((slots[i].getDirection().x > 0) ? slots[i].getDirection().x * slots[i].getLength() - 1 : 0), sStart.y + ((slots[i].getDirection().y > 0) ? slots[i].getDirection().y * slots[i].getLength() - 1 : 0));
            for (int j = i + 1; j < slots.length; j++) {
                if (slots[i].getDirection().x != slots[j].getDirection().x) {
                    Point eStart = slots[j].getStart();
                    Point eEnd = new Point(eStart.x + ((slots[j].getDirection().x > 0) ? slots[j].getDirection().x * slots[j].getLength() - 1 : 0), eStart.y + ((slots[j].getDirection().y > 0) ? slots[j].getDirection().y * slots[j].getLength() - 1 : 0));
                    if ((sStart.x == sEnd.x) && (sStart.x >= eStart.x) && (sEnd.x <= eEnd.x) && (sStart.y <= eStart.y) && (sEnd.y >= eEnd.y)) {
                        slots[i].addSlotLimitation(new SlotsLimitation(slots[j], sStart.x - eStart.x, eStart.y - sStart.y));
                        slots[j].addSlotLimitation(new SlotsLimitation(slots[i], eStart.y - sStart.y, sStart.x - eStart.x));
                    } else if ((sStart.x <= eStart.x) && (sEnd.x >= eEnd.x) && (sStart.y >= eStart.y) && (sEnd.y <= eEnd.y)) {
                        slots[i].addSlotLimitation(new SlotsLimitation(slots[j], sStart.y - eStart.y, eStart.x - sStart.x));
                        slots[j].addSlotLimitation(new SlotsLimitation(slots[i], eStart.x - sStart.x, sStart.y - eStart.y));
                    }
                }
            }
        }
    }


}
