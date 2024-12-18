package PokemonApp;

import java.util.Scanner;

public class GameOperation {
    public static void startGame(Scanner scanner, Player player, ScoreManager scoreManager) {
        Game game = new Game(player, scoreManager);
        game.startGame(scanner);
    }
}