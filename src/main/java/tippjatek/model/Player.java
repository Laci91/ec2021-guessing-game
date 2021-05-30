package tippjatek.model;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.stream.Collectors;

public class Player extends BaseEntity {

    private final String name;

    private final SortedMap<Match, Guess> guesses;

    private boolean favourite;

    public Player(String name, SortedMap<Match, Guess> guesses, boolean favourite) {
        this.name = name;
        this.guesses = guesses;
        this.favourite = favourite;
    }

    public String getName() {
        return name;
    }

    public Map<Match, Guess> getGuesses() {
        return guesses;
    }

    public List<String> getGuessesAsText() {
        return guesses.values().stream().map(Guess::getGuess).collect(Collectors.toList());
    }

    public void resolveGuess(Match match) {
        guesses.get(match).registerResult(match.getScore());
    }

    public int getScore() {
        return guesses.values().stream().mapToInt(Guess::getScore).sum();
    }

    public int getSuccessForMatch(Match match) {
        return guesses.get(match).getSuccess();
    }

    public void switchFavourite() {
        this.favourite = !favourite;
    }

    public boolean isFavourite() {
        return favourite;
    }
}
