package tippjatek.ui.guide;

import tippjatek.gamesheet.GameSheetManager;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class UserGuideComponent extends JPanel {
    public UserGuideComponent() {
        super();
        JLabel mainLabel = new JLabel(
            "<html>Ez az alkalmaz\u00E1s a 2021-es foci EB tippj\u00E1t\u00E9k k\u00F6vet\u00E9s\u00E9re szolg\u00E1l. <br/>" +
            "Az alkalmaz\u00E1s a h\u00E1tt\u00E9rben a m\u00E1s j\u00F3l ismert Excel t\u00E1bl\u00E1zatot haszn\u00E1lja adatt\u00E1rol\u00E1sra, de a telep\u00EDtett Excel verzi\u00F3t\u00F3l f\u00FCggetlen\u00FCl m\u0171k\u00F6dik. <br/>" +
            "Lehet\u0151s\u00E9ged van rajta megn\u00E9zni az \u00F6sszes be\u00E9rkezett tippet, bevinni az \u00FAj eredm\u00E9nyeket illetve a jelenlegi \u00E1ll\u00E1s k\u00F6vet\u00E9s\u00E9t. <br/><br/>" +
            "<p><b>Meccsek</b><br/>Ez az oldal mutatja az \u00F6sszes j\u00E1t\u00E9kos tippj\u00E9t, hasonl\u00F3an a r\u00E9gi Excel form\u00E1tumhoz. <br/>" +
            "A tippverseny r\u00E9sztvev\u0151i k\u00F6z\u00FCl lehet\u0151s\u00E9g van kedvenceket megjel\u00F6lni, akiket a \"Kedvencek\" oldalon c\u00E9lzottan is tudsz k\u00F6vetni. <br/>" +
            "Ehhez a fejl\u00E9cben a j\u00E1t\u00E9kos nev\u00E9re kell kattintani, ekkor a j\u00E1t\u00E9kos neve s\u00E1rga sz\u00EDnre v\u00E1lt, jelezve a kijel\u00F6l\u00E9st. <br/> Ism\u00E9telt kattint\u00E1s eset\u00E9n a kijel\u00F6l\u00E9s megsz\u0171nik. <br/>" +
            "\u00DAj eredm\u00E9nyt a m\u00E9rk\u0151z\u00E9s nev\u00E9re kattintva tudsz bevinni - ekkor egy \u00FAj ablak fog felugrani. <br/>" +
            "Ezen az ablakon a cs\u00FAszk\u00E1k be\u00E1ll\u00EDt\u00E1s\u00E1val tudod az eredm\u00E9nyt m\u00F3dositani, majd a megfelel\u0151 eredm\u00E9nyt a \"K\u00FCld\u00E9s\" gombbal r\u00F6gz\u00EDteni. <br/>" +
            "A cs\u00FAszk\u00E1k sk\u00E1l\u00E1ja alapb\u00F3l 0-5 k\u00F6z\u00F6tti \u00E9rt\u00E9keket tud felvenni. <br/> Ha esetleg egy meccsen 5-n\u00E9l t\u00F6bb g\u00F3lt l\u0151ne egy csapat, akkor a \"10es sk\u00E1la\" gombbal lehet ezt b\u0151viteni. <br/>" +
            "A K\u00FCld\u00E9s gombra kattintva a felugr\u00F3 ablak bez\u00E1rul, \u00E9s a t\u00E1bl\u00E1zat friss\u00FCl az \u00FAj eredm\u00E9nnyel. </p><br/>" +
            "<p><b>\u00C1ll\u00E1s</b><br/> Itt a tippverseny jelenlegi \u00E1ll\u00E1sa l\u00E1that\u00F3 pontsz\u00E1m szerint rendezve. </p><br/>" +
            "<p><b>Kedvencek</b><br/>Pontosan megegyezik a \"Meccsek\" oldallal, annyi kiv\u00E9tellel hogy itt csak a kedvencekk\u00E9nt megjel\u00F6lt j\u00E1t\u00E9kosok tipposzlopa l\u00E1that\u00F3. </p><br/>" +
            "<p><b>Technikai inform\u00E1ciok</b><br/>A program forr\u00E1sk\u00F3dja el\u00E9rhet\u0151:</html>");
        mainLabel.setFont(mainLabel.getFont().deriveFont(18f));
        JButton openGithubRepoButton = new JButton("Mutasd a program forr\u00E1s\u00E1t");
        openGithubRepoButton.addActionListener(action -> {
            try {
                Desktop.getDesktop().browse(new URI("https://github.com/Laci91/ec2021-guessing-game"));
            } catch (IOException | URISyntaxException e) {
                throw new RuntimeException(e);
            }
        });

        JLabel excelLabel = new JLabel("<html>Ha szeretn\u00E9d megn\u00E9zni a h\u00E1tt\u00E9rben l\u00E9v\u0151 Excel f\u00E1jlt, erre a gombra kattintva megteheted:</p></html>");
        JButton openExcelButton = new JButton("Mutasd az Excel f\u00E1jlt!");
        openExcelButton.addActionListener(action -> new GameSheetManager().openExcelFile());

        BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        this.setLayout(layout);
        this.add(mainLabel);
        this.add(openGithubRepoButton);
        this.add(excelLabel);
        this.add(openExcelButton);
    }
}
