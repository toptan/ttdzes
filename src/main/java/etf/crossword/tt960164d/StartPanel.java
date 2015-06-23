package etf.crossword.tt960164d;

import javax.swing.*;

public class StartPanel extends JPanel {
    private JTextField xField;
    private JTextField yField;

    private StartPanel() {
        initUI();
    }

    public static void main(String[] args) {
        StartPanel startPanel = new StartPanel();
        while (true) {
            int result = JOptionPane.showConfirmDialog(null, startPanel,
                    "Please Enter X and Y Values", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    CrosswordApp m = new CrosswordApp(startPanel.getXFiled(), startPanel.getYFiled());
                    m.setVisible(true);
                    return;
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }
            } else {
                break;
            }
        }
    }
    private void initUI() {
        xField = new JTextField(5);
        yField = new JTextField(5);

        add(new JLabel("x:"));
        add(xField);
        add(Box.createHorizontalStrut(15));
        add(new JLabel("y:"));
        add(yField);
    }

    private int getXFiled() throws NumberFormatException {
        return Integer.parseInt(xField.getText());
    }
    private int getYFiled() throws NumberFormatException {
        return Integer.parseInt(yField.getText());
    }
}

