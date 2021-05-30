package tippjatek.ui.matches;

import tippjatek.model.Game;
import tippjatek.model.Match;
import tippjatek.model.Player;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.stream.Collectors;

public class SingleCellRendererTable extends JTable {

    private final DefaultTableCellRenderer goodGuessRenderer = new DefaultTableCellRenderer();
    private final DefaultTableCellRenderer onePointGuessRenderer = new DefaultTableCellRenderer();
    private final DefaultTableCellRenderer badGuessRenderer = new DefaultTableCellRenderer();
    private final DefaultTableCellRenderer boldTableCellRenderer = new DefaultTableCellRenderer();
    private final DefaultTableCellRenderer resultsCellRenderer = new DefaultTableHeaderCellRenderer();
    private final DefaultTableCellRenderer centerCellRenderer = new DefaultTableHeaderCellRenderer();
    private final Game model;
    private final boolean favouritesOnly;

    public SingleCellRendererTable(DefaultTableModel dtm, Game model, boolean favouritesOnly) {
        super(dtm);
        this.model = model;
        this.favouritesOnly = favouritesOnly;
        goodGuessRenderer.setBackground(Color.GREEN);
        goodGuessRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        onePointGuessRenderer.setBackground(Color.YELLOW);
        onePointGuessRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        badGuessRenderer.setBackground(Color.RED);
        badGuessRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        boldTableCellRenderer.setFont(boldTableCellRenderer.getFont().deriveFont(Font.BOLD));
        boldTableCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        resultsCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        resultsCellRenderer.setBackground(Color.LIGHT_GRAY);
        resultsCellRenderer.setOpaque(true);

        centerCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
    }

    @Override
    public TableCellRenderer getCellRenderer(int row, int column) {
        if (row == 0) {
            return (TableCellRenderer) this.prepareRenderer(boldTableCellRenderer, row, column);
        } else if (column == 0) {
            return (TableCellRenderer) this.prepareRenderer(resultsCellRenderer, row, column);
        }

        int playerIndex = column - 1;
        String playerName = model.getPlayerObjects().stream().filter(pl -> !favouritesOnly || pl.isFavourite()).collect(Collectors.toList()).get(playerIndex).getName();
        Match matchObject = model.getMatch(row - 1);
        Player player = model.getPlayer(playerName);

        if (matchObject.getScore() == null) {
            return (TableCellRenderer) this.prepareRenderer(centerCellRenderer, row, column);
        }
        switch (player.getSuccessForMatch(matchObject)) {
            case 3:
                return (TableCellRenderer) this.prepareRenderer(goodGuessRenderer, row, playerIndex);
            case 1:
                return (TableCellRenderer) this.prepareRenderer(onePointGuessRenderer, row, playerIndex);
            case 0:
                return (TableCellRenderer) this.prepareRenderer(badGuessRenderer, row, playerIndex);
            case Integer.MIN_VALUE:
                return super.getCellRenderer(row, column);
            default:
                throw new IllegalStateException("Match score should be one of 3, 1 or 0");
        }
    }
}
