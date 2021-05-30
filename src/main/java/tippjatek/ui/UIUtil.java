package tippjatek.ui;

public class UIUtil {

    public static String getPlayerDisplayName(String playerName) {
        return playerName.split("#")[0];
    }
}
