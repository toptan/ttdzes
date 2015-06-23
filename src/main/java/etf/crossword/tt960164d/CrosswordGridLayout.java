package etf.crossword.tt960164d;

import java.awt.*;

public class CrosswordGridLayout extends GridLayout {

    public CrosswordGridLayout(int rows, int cols, int a, int b) {
        super(rows, cols, a, b);
    }

    public void layoutContainer(Container parent) {
        synchronized (parent.getTreeLock()) {
            Insets insets = parent.getInsets();
            int ncomponents = parent.getComponentCount();
            int nrows = getRows();
            int ncols = getColumns();
            boolean ltr = parent.getComponentOrientation().isLeftToRight();

            if (ncomponents == 0) {
                return;
            }
            if (nrows > 0) {
                ncols = (ncomponents + nrows - 1) / nrows;
            } else {
                nrows = (ncomponents + ncols - 1) / ncols;
            }

            int totalGapsWidth = (ncols - 1) * getHgap();
            int widthWOInsets = parent.getWidth() - (insets.left + insets.right);
            int widthOnComponent = (widthWOInsets - totalGapsWidth) / ncols;
            int extraWidthAvailable = (widthWOInsets - (widthOnComponent * ncols + totalGapsWidth)) / 2;

            int totalGapsHeight = (nrows - 1) * getVgap();
            int heightWOInsets = parent.getHeight() - (insets.top + insets.bottom);
            int heightOnComponent = (heightWOInsets - totalGapsHeight) / nrows;
            int extraHeightAvailable = (heightWOInsets - (heightOnComponent * nrows + totalGapsHeight)) / 2;

            int size = Math.min(widthOnComponent, heightOnComponent);
            widthOnComponent = size;
            heightOnComponent = size;
            if (ltr) {
                for (int c = 0, x = insets.left + extraWidthAvailable; c < ncols; c++, x += widthOnComponent + getHgap()) {
                    for (int r = 0, y = insets.top + extraHeightAvailable; r < nrows; r++, y += heightOnComponent + getVgap()) {
                        int i = r * ncols + c;
                        if (i < ncomponents) {
                            parent.getComponent(i).setBounds(x, y, widthOnComponent, heightOnComponent);
                        }
                    }
                }
            } else {
                for (int c = 0, x = (parent.getWidth() - insets.right - widthOnComponent) - extraWidthAvailable; c < ncols; c++, x -= widthOnComponent + getHgap()) {
                    for (int r = 0, y = insets.top + extraHeightAvailable; r < nrows; r++, y += heightOnComponent + getVgap()) {
                        int i = r * ncols + c;
                        if (i < ncomponents) {
                            parent.getComponent(i).setBounds(x, y, widthOnComponent, heightOnComponent);
                        }
                    }
                }
            }
        }
    }
}