package PokemonApp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class GameUtil {

    private static final Random random = new Random();

    public static void clearTerminal() {
        // Logic to clear the terminal
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void viewLeaderboard(ScoreManager scoreManager) {
        scoreManager.displayTopScores();
    }

    public static void listAllPokemons(List<Pokemon> pokemons) {
        System.out.println("Listing all Pokemons:");
        for (Pokemon pokemon : pokemons) {
            System.out.println(pokemon);
        }
    }

    public static List<Pokemon> generateWildPokemons(int count) {
        List<Pokemon> wildPokemons = new ArrayList<>();
        String[] pokemonNames = {"Charmander", "Squirtle", "Bulbasaur", "Pikachu", "Eevee", "Meowth", "Gastly", "Dratini", "Machop"};
        for (int i = 0; i < count; i++) {
            String name = pokemonNames[random.nextInt(pokemonNames.length)];
            Pokemon pokemon = createPokemon(name);
            wildPokemons.add(pokemon);
        }
        return wildPokemons;
    }

    private static Pokemon createPokemon(String name) {
        int hp = getRandomNumber(30, 50); // HP between 30 and 50
        int attackPower = getRandomNumber(8, 17); // Attack power between 8 and 17
        int maxHp = hp; // Set max HP to initial HP
        int grade = 1;

        switch (name) {
            case "Charmander":
                return new FirePokemon(name, hp, attackPower, maxHp, grade);
            case "Squirtle":
                return new WaterPokemon(name, hp, attackPower, maxHp, grade);
            case "Bulbasaur":
                return new GrassPokemon(name, hp, attackPower, maxHp, grade);
            case "Pikachu":
                return new ElectricPokemon(name, hp, attackPower, maxHp, grade);
            case "Eevee":
                return new NormalPokemon(name, hp, attackPower, maxHp, grade);
            case "Meowth":
                return new NormalPokemon(name, hp, attackPower, maxHp, grade);
            case "Gastly":
                return new GhostPokemon(name, hp, attackPower, maxHp, grade);
            case "Dratini":
                return new DragonPokemon(name, hp, attackPower, maxHp, grade);
            case "Machop":
                return new FightingPokemon(name, hp, attackPower, maxHp, grade);
            default:
                // Default to a normal Pokémon if the name does not match
                return new NormalPokemon(name, hp, attackPower, maxHp, grade);
        }
    }

    public static void printSeparator() {
        System.out.println("----------------------------------");
    }

    public static double getEffectiveness(String attackerType, String defenderType) {
        return Utils.getEffectiveness(attackerType, defenderType);
    }

    public static int calculateDamage(Pokemon attacker, Pokemon defender) {
        return Utils.calculateDamage(attacker, defender);
    }

    public static String getRandomPokeball() {
        return Utils.getRandomPokeball();
    }

    public static boolean catchPokemon(String pokeball, int grade) {
        return Utils.catchPokemon(pokeball, grade);
    }

    public static void generateRandomPokeballAndCatch(Scanner scanner, Pokemon[] wildPokemons, Player player) {
        String pokeball = getRandomPokeball();
        System.out.println("You found a " + pokeball + "!");

        System.out.println("Would you like to use it to catch a Pokémon? (yes/no)");
        String choice = scanner.nextLine().trim().toLowerCase();

        if (choice.equals("yes")) {
            System.out.println("Choose a wild Pokémon to catch:");
            for (int i = 0; i < wildPokemons.length; i++) {
                System.out.println((i + 1) + ". " + wildPokemons[i]);
            }

            int chosenWildIndex = getValidChoice(scanner, 1, wildPokemons.length) - 1;
            Pokemon chosenWildPokemon = wildPokemons[chosenWildIndex];
            chosenWildPokemon.setHp(chosenWildPokemon.getMaxHp()); // Reset HP to max for catching

            boolean caught = catchPokemon(pokeball, chosenWildPokemon.getGrade());
            if (caught) {
                player.addPokemon(chosenWildPokemon);
                System.out.println("You caught " + chosenWildPokemon.getName() + "!");
            } else {
                System.out.println(chosenWildPokemon.getName() + " escaped!");
            }
        }
    }

    private static int getValidChoice(Scanner scanner, int min, int max) {
        int choice = -1;
        while (choice < min || choice > max) {
            try {
                if (scanner.hasNextInt()) {
                    choice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline character
                    if (choice < min || choice > max) {
                        System.out.println("Invalid choice. Please select a number between " + min + " and " + max + ": ");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a number: ");
                    scanner.next(); // clear the invalid input
                }
            } catch (Exception e) {
                System.out.println("An error occurred. Please try again: " + e.getMessage());
                scanner.next(); // clear the invalid input
            }
        }
        return choice;
    }

    private static int getRandomNumber(int min, int max) {
        return min + random.nextInt(max - min + 1);
    }
}