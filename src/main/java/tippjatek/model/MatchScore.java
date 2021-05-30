package tippjatek.model;

public class MatchScore extends BaseEntity {

    private final int homeGoals;

    private final int awayGoals;

    public MatchScore(int homeGoals, int awayGoals) {
        this.homeGoals = homeGoals;
        this.awayGoals = awayGoals;
    }

    public int getHomeGoals() {
        return homeGoals;
    }

    public int getAwayGoals() {
        return awayGoals;
    }

    public String getScoreText() {
        return String.format("%d-%d", homeGoals, awayGoals);
    }
}
