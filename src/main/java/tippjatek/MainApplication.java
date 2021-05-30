package tippjatek;

import com.formdev.flatlaf.FlatLightLaf;
import tippjatek.gamesheet.GameSheetManager;
import tippjatek.model.Game;
import tippjatek.ui.DisplayStrings;
import tippjatek.ui.matches.MatchesComponent;
import tippjatek.ui.guide.UserGuideComponent;
import tippjatek.ui.standings.StandingsComponent;

import javax.swing.*;

public class MainApplication {

    public static void main(String[] args) {
        Game game = new GameSheetManager().assembleGameObject();

        SwingUtilities.invokeLater(() -> {
            setLookAndFeel();

            JFrame frame = new JFrame(DisplayStrings.TITLE);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setSize(1250, 1250);

            MatchesComponent matchesComponent = MatchesComponent.fromGame(frame, game, false);
            StandingsComponent standingsComponent = StandingsComponent.fromGame(game);
            MatchesComponent favouritesComponent = MatchesComponent.fromGame(frame, game, true);

            JTabbedPane tabbedPane = new JTabbedPane();
            tabbedPane.addTab(DisplayStrings.MATCHES, matchesComponent);
            tabbedPane.addTab(DisplayStrings.STANDINGS, standingsComponent);
            tabbedPane.addTab(DisplayStrings.FAVOURITES, favouritesComponent);
            tabbedPane.addTab(DisplayStrings.README, new UserGuideComponent());
            tabbedPane.addChangeListener(e -> {
                if (tabbedPane.getSelectedIndex() == 1) {
                    standingsComponent.recalculateStandings();
                } else if (tabbedPane.getSelectedIndex() == 2) {
                    favouritesComponent.recalculateComponent(game);
                }
            });
            frame.getContentPane().add(tabbedPane);
            frame.setVisible(true);
        });
    }

    private static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }
    }
}
