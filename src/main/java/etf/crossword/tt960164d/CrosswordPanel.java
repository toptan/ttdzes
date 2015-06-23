package etf.crossword.tt960164d;

import etf.crossword.tt960164d.CrosswordGridLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class CrosswordPanel extends JPanel {


    private int x;
    private int y;

    private JButton[][] btns;
    private JPanel[][] box;
    private JLabel[][] top;
    private JLabel[][] bottom;
    private JLabel[][] center;

    public CrosswordPanel(int x, int y) {
        this.x = x;
        this.y = y;
        initGui();
    }

    private void initGui() {

        top = new JLabel[x][y];
        center = new JLabel[x][y];
        bottom = new JLabel[x][y];
        btns = new JButton[x][y];
        box = new JPanel[x][y];

        setLayout(new CrosswordGridLayout(x, y, 3, 3));

        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {

                box[i][j] = new JPanel();
                box[i][j].setBackground(Color.WHITE);
                box[i][j].setLayout(new BorderLayout());


                top[i][j] = new JLabel(" ");
                top[i][j].setBackground(Color.WHITE);
                top[i][j].setHorizontalAlignment(SwingConstants.RIGHT);
                top[i][j].setVisible(false);
                box[i][j].add(top[i][j], BorderLayout.NORTH);

                center[i][j] = new JLabel(" ");
                center[i][j].setBackground(Color.WHITE);
                center[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                center[i][j].setFont(new Font(center[i][j].getFont().getName(), Font.BOLD, 20));

                bottom[i][j] = new JLabel(" ");
                bottom[i][j].setBackground(Color.WHITE);
                bottom[i][j].setVisible(false);
                bottom[i][j].setHorizontalAlignment(SwingConstants.LEFT);
                box[i][j].add(bottom[i][j], BorderLayout.SOUTH);


                btns[i][j] = new JButton();
                final JButton finalBtn = btns[i][j];

                finalBtn.setBackground(Color.WHITE);
                finalBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        finalBtn.setBackground((finalBtn.getBackground() == Color.BLACK) ? Color.WHITE : Color.BLACK);
                    }
                });
                box[i][j].add(finalBtn);
                add(box[i][j]);
            }
        }
    }
    public void disableAll() {
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                if (btns[i][j].getBackground() == Color.WHITE) {
                    box[i][j].remove(btns[i][j]);

                    top[i][j].setVisible(true);
                    box[i][j].add(center[i][j], BorderLayout.CENTER);
                    bottom[i][j].setVisible(true);

                }
                btns[i][j].setEnabled(false);
            }
        }
    }
    public char[][] initCrossword() {
        char[][] crossword = new char[x][y];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                if (btns[i][j].getBackground() == Color.BLACK) {
                    crossword[i][j] = CrosswordApp.FILLED;
                } else {
                    crossword[i][j] = CrosswordApp.BLANK;
                }
            }
        }
        return crossword;
    }
    public void fillBoard(char[][] crossword) {
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                if (crossword[i][j] != CrosswordApp.FILLED) {
                    center[i][j].setText(crossword[i][j] + "");
                }
            }
        }
    }
    public void setTopLabel(int index, int i, int j) {
        top[i][j].setText(index + "");
    }
    public void setBottomLabel(int index, int i, int j) {
        bottom[i][j].setText(index + "");
    }
}
