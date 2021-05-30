package tippjatek.model;

public class Guess extends BaseEntity {

    private final MatchScore guess;

    private MatchScore real;

    public Guess(int home, int away) {
        this.guess = new MatchScore(home, away);
    }

    public String getGuess() {
        return String.format("%d-%d", guess.getHomeGoals(), guess.getAwayGoals());
    }

    public int getScore() {
        if (real == null) {
            return 0;
        }

        if (guess.getHomeGoals() == real.getHomeGoals() && guess.getAwayGoals() == real.getAwayGoals()) {
            return 3;
        } else if (Integer.signum(guess.getHomeGoals() - guess.getAwayGoals()) == Integer.signum(real.getHomeGoals() - real.getAwayGoals())) {
            return 1;
        }

        return 0;
    }

    public int getSuccess() {
        if (real == null) {
            return Integer.MIN_VALUE;
        }

        return getScore();
    }

    public void registerResult(MatchScore score) {
        this.real = score;
    }
}
