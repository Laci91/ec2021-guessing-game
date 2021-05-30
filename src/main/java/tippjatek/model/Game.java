package tippjatek.model;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class Game extends BaseEntity {

    private final SortedMap<String, Player> players;

    private final List<Match> matches;

    public Game(List<String> players, List<String> gameNames, Map<Integer, String> results, List<List<String>> rawGuesses, List<String> favourites) {
        this.matches = getMatches(gameNames);
        this.players = new TreeMap<>();
        for (int i=0;i<players.size();i++) {
            SortedMap<Match, Guess> guesses = new TreeMap<>();
            String playerName = players.get(i);
            for (int j=0;j<matches.size();j++) {
                guesses.put(matches.get(j), extractGuess(rawGuesses.get(i).get(j)));
            }
            this.players.put(playerName, new Player(playerName, guesses, favourites.contains(playerName)));
        }

        for(Entry<Integer, String> result: results.entrySet()) {
            MatchScore score = extractScore(result.getValue());
            setResult(result.getKey(), score);
        }
    }

    private List<Match> getMatches(List<String> gameNames) {
        List<Match> matches = new ArrayList<>();
        for (int i=0;i< gameNames.size();i++) {
            matches.add(new Match(i, gameNames.get(i), null));
        }

        return matches;
    }

    private Guess extractGuess(String guess) {
        System.out.println(" Guess is " + guess);
        String[] tokens = guess.split("[:-]");
        return new Guess(Integer.parseInt(tokens[0].trim()), Integer.parseInt(tokens[1].trim()));
    }

    private MatchScore extractScore(String result) {
        System.out.println(" Guess is " + result);
        String[] tokens = result.split("[:-]");
        return new MatchScore(Integer.parseInt(tokens[0].trim()), Integer.parseInt(tokens[1].trim()));
    }

    public List<String> getMatches() {
        return matches.stream().map(Match::getMatchDescription).collect(Collectors.toList());
    }

    public Match getMatch(int index) {
        return matches.get(index);
    }

    public void setResult(int matchIndex, MatchScore result) {
        matches.get(matchIndex).setScore(result);
        players.values().forEach(p -> p.resolveGuess(matches.get(matchIndex)));
    }

    public List<String> getMatchResults() {
        return matches.stream().map(Match::getScoreAsText).collect(Collectors.toList());
    }

    public List<Player> getPlayerObjects() {
        return new ArrayList<>(players.values());
    }

    public List<String> getPlayers() {
        return new ArrayList<>(players.keySet());
    }

    public Player getPlayer(String playerName) {
        return players.get(playerName);
    }
}
