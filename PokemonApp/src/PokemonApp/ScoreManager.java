package PokemonApp;

import java.io.*;
import java.util.*;

public class ScoreManager {
    private static final String SCORES_FILE = "scores.txt";
    private Map<String, Integer> scores = new HashMap<>();

    public ScoreManager() {
        loadScores();
    }

    public void addScore(String playerName, int score) {
        if (scores.containsKey(playerName)) {
            int currentScore = scores.get(playerName);
            if (score > currentScore) {
                scores.put(playerName, score);
            }
        } else {
            scores.put(playerName, score);
        }
        saveScores();
    }

    public void displayTopScores() {
        List<Map.Entry<String, Integer>> scoreList = new ArrayList<>(scores.entrySet());
        scoreList.sort((a, b) -> b.getValue() - a.getValue());

        System.out.println("Top Scores:");
        for (int i = 0; i < Math.min(5, scoreList.size()); i++) {
            Map.Entry<String, Integer> entry = scoreList.get(i);
            System.out.println((i + 1) + ". " + entry.getKey() + ": " + entry.getValue());
        }
    }

    private void loadScores() {
        File file = new File(SCORES_FILE);
        if (!file.exists()) {
            return; // No scores to load
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String playerName = parts[0];
                int score = Integer.parseInt(parts[1]);
                scores.put(playerName, score);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveScores() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SCORES_FILE))) {
            for (Map.Entry<String, Integer> entry : scores.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}