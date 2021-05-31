package tippjatek.ui.standings;

import tippjatek.model.Game;
import tippjatek.model.Player;
import tippjatek.ui.DisplayStrings;
import tippjatek.ui.UIUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

public class StandingsComponent extends JScrollPane {

    private final Game game;

    private StandingsComponent(Component component, Game game) {
        super(component);
        this.game = game;
    }

    public void recalculateStandings() {
        ((JTable)this.getViewport().getView()).setModel(getTableModel(game));
    }

    public static StandingsComponent fromGame(Game game) {
        Component component = buildComponent(game);
        return new StandingsComponent(component, game);
    }

    private static Component buildComponent(Game game) {
        DefaultTableModel dtm = getTableModel(game);
        return getTable(dtm);
    }

    private static DefaultTableModel getTableModel(Game game) {
        DefaultTableModel dtm = new DefaultTableModel();
        Vector<String> columnHeaders = new Vector<>();
        columnHeaders.add(DisplayStrings.PLACE);
        columnHeaders.add(DisplayStrings.NAME);
        columnHeaders.add(DisplayStrings.SCORE);
        columnHeaders.add("Kedvenc?");
        dtm.setColumnIdentifiers(columnHeaders);
        List<Player> players = game.getPlayerObjects();
        players.sort(Comparator.comparingInt(Player::getScore).reversed());

        Vector<String> rowValues;
        int lastPlace = 1;
        for (int i=0;i<players.size();i++) {
            rowValues = new Vector<>();
            Player currentPlayer = players.get(i);
            if (i == 0 || players.get(i - 1).getScore() == currentPlayer.getScore()) {
                rowValues.add(lastPlace + ".");
            } else {
                int currentPlace = i + 1;
                rowValues.add(currentPlace + ".");
                lastPlace = currentPlace;
            }

            rowValues.add(UIUtil.getPlayerDisplayName(currentPlayer.getName()));
            rowValues.add(String.valueOf(currentPlayer.getScore()));
            rowValues.add(currentPlayer.isFavourite() ? "Y" : "N");
            dtm.addRow(rowValues);
        }

        return dtm;
    }

    private static JTable getTable(DefaultTableModel dtm) {
        JTable table = new JTable(dtm) {
            @Override
            public TableCellRenderer getCellRenderer(int row, int column) {
                return new FavouriteTableCellRenderer();
            }
        };
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.getColumnModel().getColumn(3).setMinWidth(0);
        table.getColumnModel().getColumn(3).setMaxWidth(0);
        table.getColumnModel().getColumn(3).setWidth(0);
        table.getColumnModel().getColumn(3).setResizable(false);
        table.getColumnModel().removeColumn(table.getColumnModel().getColumn(3));
        return table;
    }
}
