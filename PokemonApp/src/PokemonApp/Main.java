package PokemonApp;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Player player = new Player();
        ScoreManager scoreManager = new ScoreManager();

        Game game = new Game(player, scoreManager);
        Scanner scanner = new Scanner(System.in);
        game.startGame(scanner); // Start the game with login/register interface
        scanner.close();
        System.exit(0);
        return;
    }
}