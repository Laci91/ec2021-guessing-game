package tippjatek.ui.standings;

import tippjatek.ui.matches.DefaultTableHeaderCellRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class FavouriteTableCellRenderer extends DefaultTableCellRenderer {

    private final DefaultTableCellRenderer favouriteRenderer = new DefaultTableCellRenderer();

    public FavouriteTableCellRenderer() {
        favouriteRenderer.setBackground(Color.YELLOW);
    }

    @Override
    public Component getTableCellRendererComponent (JTable table, Object obj, boolean isSelected, boolean hasFocus, int row, int column) {
        if ("Y".equals(table.getModel().getValueAt(row, 3))) {
            return table.prepareRenderer(favouriteRenderer, row, column);
        }
        return table.prepareRenderer(new DefaultTableCellRenderer(), row, column);
    }
}
