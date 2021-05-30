package tippjatek.model;

public class Match implements Comparable<Match> {

    private final Integer matchId;

    private final String matchDescription;

    private MatchScore score;

    public Match(int matchId, String matchDescription, MatchScore score) {
        this.matchId = matchId;
        this.matchDescription = matchDescription;
        this.score = score;
    }

    public String getMatchDescription() {
        return matchDescription;
    }

    public void setScore(MatchScore score) {
        this.score = score;
    }

    public MatchScore getScore() {
        return score;
    }

    public String getScoreAsText() {
        return score == null ? "" : score.getScoreText();
    }

    @Override
    public int compareTo(Match o) {
        return this.matchId.compareTo(o.matchId);
    }

    @Override
    public int hashCode() {
        return matchId;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Match && matchId.equals(((Match) o).matchId);
    }
}
