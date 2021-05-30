package tippjatek.ui.standings;

import tippjatek.model.Game;
import tippjatek.model.Player;
import tippjatek.ui.DisplayStrings;
import tippjatek.ui.UIUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

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
        dtm.setColumnIdentifiers(columnHeaders);
        Map<String, Integer> scores = game.getPlayerObjects().stream().collect(Collectors.toMap(Player::getName, Player::getScore, (a, b) -> a));
        List<Map.Entry<String, Integer>> scoreEntries = new ArrayList<>(scores.entrySet());
        scoreEntries.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        Vector<String> rowValues;
        int lastPlace = 1;
        for (int i=0;i< scoreEntries.size();i++) {
            Map.Entry<String, Integer> currentEntry = scoreEntries.get(i);
            rowValues = new Vector<>();
            if (i == 0 || scoreEntries.get(i - 1).getValue().equals(currentEntry.getValue())) {
                rowValues.add(lastPlace + ".");
            } else {
                int currentPlace = i + 1;
                rowValues.add(currentPlace + ".");
                lastPlace = currentPlace;
            }

            rowValues.add(UIUtil.getPlayerDisplayName(currentEntry.getKey()));
            rowValues.add(currentEntry.getValue().toString());
            dtm.addRow(rowValues);
        }

        return dtm;
    }

    private static JTable getTable(DefaultTableModel dtm) {
        JTable table = new JTable(dtm);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        return table;
    }
}
