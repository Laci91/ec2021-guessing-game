package tippjatek.ui.matches;

import tippjatek.gamesheet.GameSheetManager;
import tippjatek.model.Game;
import tippjatek.model.MatchScore;
import tippjatek.model.Player;
import tippjatek.ui.DisplayStrings;
import tippjatek.ui.UIUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

public class MatchesComponent extends JScrollPane {

    private MatchesComponent(Component component) {
        super(component);
    }

    public void recalculateComponent(Game game, boolean favouritesOnly) {
        DefaultTableModel model = getTableModel(game, favouritesOnly);
        JTable view = (JTable)this.getViewport().getView();
        view.setModel(model);
        setupHeaderCellRenderer(game, favouritesOnly, view);
        view.repaint();
    }

    public static MatchesComponent fromGame(JFrame parent, Game game, boolean favouritesOnly) {
        DefaultTableModel dtm = getTableModel(game, favouritesOnly);
        JTable table = getTable(dtm, game, favouritesOnly);
        JList<String> rowHeader = getRowHeader(parent, game, table, favouritesOnly);
        MatchesComponent matchesComponent = new MatchesComponent(table);
        matchesComponent.setRowHeaderView(rowHeader);
        return matchesComponent;
    }

    private static JList<String> getRowHeader(JFrame parent, Game game, JTable table, boolean favouritesOnly) {
        ListModel<String> lm = new AbstractListModel<String>() {
            final String[] headers = game.getMatches().toArray(new String[0]);

            @Override
            public int getSize() {
                return headers.length + 1;
            }

            @Override
            public String getElementAt(int index) {
                return index == 0 ? DisplayStrings.SCORE : headers[index - 1];
            }
        };

        JList<String> rowHeader = new JList<>(lm);
        rowHeader.setFixedCellWidth(250);
        rowHeader.setFixedCellHeight(table.getRowHeight());
        rowHeader.setCellRenderer(new RowHeaderRenderer(table));
        rowHeader.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                int index = rowHeader.locationToIndex(e.getPoint());
                if (index == 0) {
                    return;
                }

                int matchIndex = index - 1;
                new MatchResultDialog(parent, game.getMatches().get(matchIndex), (result) -> registerResult(matchIndex, result));
            }

            private void registerResult(int matchIndex, MatchScore result) {
                game.setResult(matchIndex, result);
                table.getModel().setValueAt(result.getScoreText(), matchIndex + 1, 0);
                List<String> players = game.getPlayerObjects().stream().filter(pl -> !favouritesOnly || pl.isFavourite()).map(Player::getName).collect(Collectors.toList());
                for (int i=0;i<players.size();i++) {
                    table.getModel().setValueAt(game.getPlayer(players.get(i)).getScore(), 0, i + 1);
                }
                table.repaint();
                new GameSheetManager().registerResult(matchIndex, result.getScoreText());
            }
        });
        return rowHeader;
    }

    private static JTable getTable(DefaultTableModel dtm, Game game, boolean favouritesOnly) {
        JTable table = new SingleCellRendererTable(dtm, game, favouritesOnly);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        setupHeaderCellRenderer(game, favouritesOnly, table);

        table.setRowHeight(table.getRowHeight() - 4);
        table.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int colIndex = table.columnAtPoint(e.getPoint());
                if (colIndex == 0) {
                    return;
                }
                game.getPlayerObjects().get(colIndex - 1).switchFavourite();
                new GameSheetManager().changeFavouriteStatus((String)table.getTableHeader().getColumnModel().getColumn(colIndex).getHeaderValue());
                table.getTableHeader().getColumnModel().getColumn(colIndex).setHeaderRenderer(new VerticalHeaderRenderer(game.getPlayerObjects().get(colIndex - 1).isFavourite()));
                table.repaint();
            }
        });
        return table;
    }

    private static void setupHeaderCellRenderer(Game game, boolean favouritesOnly, JTable table) {
        Enumeration<TableColumn> columns = table.getTableHeader().getColumnModel().getColumns();
        List<Player> availablePlayers = game.getPlayerObjects().stream().filter(pl -> !favouritesOnly || pl.isFavourite()).collect(Collectors.toList());
        int columnIndex = 0;
        while(columns.hasMoreElements()) {
            TableColumn column = columns.nextElement();

            Player player = columnIndex == 0 ? null : availablePlayers.get(columnIndex - 1);
            column.setHeaderRenderer(new VerticalHeaderRenderer(player != null && player.isFavourite()));
            columnIndex += 1;
        }
    }

    private static DefaultTableModel getTableModel(Game game, boolean favouritesOnly) {
        DefaultTableModel dtm = new DefaultTableModel();
        dtm.setColumnIdentifiers(new Vector<>());
        List<String> results = new ArrayList<>();
        results.add("");
        results.addAll(game.getMatchResults());
        dtm.addColumn(DisplayStrings.RESULTS, new Vector<>(results));
        for (String playerName: game.getPlayers()) {
            Player player = game.getPlayer(playerName);
            if (favouritesOnly && !player.isFavourite()) {
                continue;
            }
            List<String> playerValues = new ArrayList<>();
            playerValues.add(String.valueOf(player.getScore()));
            playerValues.addAll(player.getGuessesAsText());
            dtm.addColumn(UIUtil.getPlayerDisplayName(playerName), new Vector<>(playerValues));
        }

        return dtm;
    }
}
