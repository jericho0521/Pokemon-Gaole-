package PokemonApp;

import java.io.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Game {
    private Player player;
    private List<Pokemon> wildPokemons = new ArrayList<>();
    private ScoreManager scoreManager;
    private Scanner scanner = new Scanner(System.in);
    private Random random = new Random();
    private static final String USER_DATA_FILE = "userdata.txt";

    public Game(Player player, ScoreManager scoreManager) {
        this.player = player;
        this.scoreManager = scoreManager;
    }

    public void startGame(Scanner scanner) {
        ensureUserDataFileExists();
        loginOrRegister(scanner);
        mainMenu(scanner);
    }

    private void ensureUserDataFileExists() {
        File file = new File(USER_DATA_FILE);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loginOrRegister(Scanner scanner) {
        while (true) {
            System.out.println("Welcome to Pokemon Ga-Ole!");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.print("Enter your choice [1-2]: ");
            int choice = getValidChoice(scanner, 1, 2);

            if (choice == 1) {
                if (login()) {
                    break;
                }
            } else {
                if (register()) {
                    break;
                }
            }
        }
    }

    private boolean login() {
        System.out.println("Enter your username: ");
        String username = scanner.next();
        System.out.println("Enter your password: ");
        String password = scanner.next();

        if (validateUser(username, password)) {
            player.setName(username);
            loadProgress(username);
            System.out.println("Login successful.");
            return true;
        } else {
            System.out.println("Invalid username or password. Please try again.");
            return false;
        }
    }

    private boolean register() {
        System.out.println("Register a new player.");
        System.out.println("Enter your username: ");
        String username = scanner.next();
        System.out.println("Enter your password: ");
        String password = scanner.next();

        if (saveUser(username, password)) {
            player.setName(username);
            System.out.println("Registration successful.");
            return true;
        } else {
            System.out.println("Username already exists. Please try again.");
            return false;
        }
    }

    private boolean validateUser(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username) && parts[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean saveUser(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username)) {
                    return false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_DATA_FILE, true))) {
            writer.write(username + "," + password + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    private void loadProgress(String username) {
        String filename = username + "_progress.txt";
        File file = new File(filename);
        if (!file.exists()) {
            return; // No progress to load
        }

        player.clearPokemons(); // Clear any existing Pokémon to prevent doubling

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String name = parts[0];
                String type = parts[1];
                int hp = Integer.parseInt(parts[2]);
                int attackPower = Integer.parseInt(parts[3]);
                int maxHp = Integer.parseInt(parts[4]);
                int grade = Integer.parseInt(parts[5]);
                Pokemon pokemon = createPokemon(type, name, hp, attackPower, maxHp, grade);
                player.addPokemon(pokemon);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveProgress() {
        String filename = player.getName() + "_progress.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Pokemon pokemon : player.getPokemons()) {
                writer.write(String.format("%s,%s,%d,%d,%d,%d\n", pokemon.getName(), pokemon.getType(), pokemon.getHp(), pokemon.getAttackPower(), pokemon.getMaxHp(), pokemon.getGrade()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Pokemon createPokemon(String type, String name, int hp, int attackPower, int maxHp, int grade) {
        switch (type) {
            case "Fire":
                return new FirePokemon(name, hp, attackPower, maxHp, grade);
            case "Water":
                return new WaterPokemon(name, hp, attackPower, maxHp, grade);
            case "Grass":
                return new GrassPokemon(name, hp, attackPower, maxHp, grade);
            case "Electric":
                return new ElectricPokemon(name, hp, attackPower, maxHp, grade);
            case "Normal":
                return new NormalPokemon(name, hp, attackPower, maxHp, grade);
            case "Ghost":
                return new GhostPokemon(name, hp, attackPower, maxHp, grade);
            case "Dragon":
                return new DragonPokemon(name, hp, attackPower, maxHp, grade);
            case "Fighting":
                return new FightingPokemon(name, hp, attackPower, maxHp, grade);
            default:
                return new NormalPokemon(name, hp, attackPower, maxHp, grade);
        }
    }

    private void generateWildPokemons(int count) {
        wildPokemons.clear();
        wildPokemons.addAll(GameUtil.generateWildPokemons(count));
    }

    private int getValidChoice(Scanner scanner, int min, int max) {
        int choice;
        while (true) {
            try {
                choice = Integer.parseInt(scanner.nextLine());
                if (choice >= min && choice <= max) {
                    break;
                }
                System.out.println("Invalid choice. Please enter a number between " + min + " and " + max + ": ");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number between " + min + " and " + max + ": ");
            }
        }
        return choice;
    }

    private void chooseBattlePokemons(Scanner scanner) {
        System.out.println("Choose your first Pokémon for battle:");
        Pokemon firstPokemon = choosePokemon(scanner, null);

        System.out.println("Choose your second Pokémon for battle:");
        Pokemon secondPokemon = choosePokemon(scanner, firstPokemon);

        player.setActivePokemons(firstPokemon, secondPokemon); // Set the active Pokémon
    }

    private Pokemon choosePokemon(Scanner scanner, Pokemon exclude) {
        for (int i = 0; i < player.getPokemons().size(); i++) {
            if (player.getPokemons().get(i) != exclude) {
                System.out.println((i + 1) + ". " + player.getPokemons().get(i));
            }
        }
        int choice = getValidChoice(scanner, 1, player.getPokemons().size());
        Pokemon chosenPokemon = player.getPokemons().get(choice - 1);
        while (chosenPokemon == exclude) {
            System.out.println("You cannot choose the same Pokémon twice. Choose again:");
            choice = getValidChoice(scanner, 1, player.getPokemons().size());
            chosenPokemon = player.getPokemons().get(choice - 1);
        }
        return chosenPokemon;
    }

    private void battle(Scanner scanner) {
        Pokemon[] playerPokemons = player.getActivePokemons();

        Pokemon[] wildPokemons = new Pokemon[2];
        wildPokemons[0] = GameUtil.generateWildPokemons(1).get(0);
        wildPokemons[1] = GameUtil.generateWildPokemons(1).get(0);

        Battle battle = new Battle(playerPokemons, wildPokemons, player, scanner);
        battle.start();

        int battleScore = battle.getBattleScore(); // Retrieve the battle score from the Battle class

        // Extra battle logic
        boolean extraBattleOccurred = false;
        if (Math.random() <= 0.2) { // 20% chance of extra battle
            System.out.println("\nAn extra battle is starting!");
            extraBattleOccurred = true;
            Pokemon[] extraWildPokemons = generateExtraBattlePokemons(2);
            Battle extraBattle = new Battle(playerPokemons, extraWildPokemons, player, scanner);
            extraBattle.start();

            int extraBattleScore = extraBattle.getBattleScore(); // Retrieve the extra battle score
            battleScore += extraBattleScore; // Add the extra battle score to the battle score

            // Generate a random Pokeball and allow the player to catch a Pokémon after the extra battle
            if (extraBattle.getResult() == 1) {
                generateRandomPokeballAndCatch(scanner, extraWildPokemons);
            }
            System.out.println("\nBattle finish! Your total score is: " + battleScore);
        }

        player.addScore(battleScore); // Add the score to the player's total score
        scoreManager.addScore(player.getName(), player.getScore()); // Update the top scores

        // Generate a random Pokeball and allow the player to catch a Pokémon after the regular battle only if no extra battle occurred
        if (!extraBattleOccurred && battle.getResult() == 1) {
            generateRandomPokeballAndCatch(scanner, wildPokemons);
            System.out.println("\nBattle finish! Your total score is: " + battleScore);
        }
        else if (!extraBattleOccurred && battle.getResult() == 0) {
            System.out.println("\nBattle finish! Your total score is: " + battleScore);
        }
    }

    private void healAllPokemons() {
        for (Pokemon pokemon : player.getPokemons()) {
            pokemon.setHp(pokemon.getMaxHp());
        }
    }

    private void viewPokemon() {
        System.out.println("\nYour Pokémon:");
        for (Pokemon pokemon : player.getPokemons()) {
            System.out.println(pokemon);
        }
    }

    private void generateRandomPokeballAndCatch(Scanner scanner, Pokemon[] wildPokemons) {
        String pokeball = Utils.getRandomPokeball();
        System.out.println("Congratulations! You won the battle!");
        System.out.println("You found a " + pokeball + "!");

        System.out.println("Would you like to use it to catch a Pokémon? (yes/no)");
        scanner.nextLine();
        String choice = scanner.nextLine().trim().toLowerCase();

        while (!choice.equals("yes") && !choice.equals("no")) {
            System.out.println("Invalid choice. Please enter 'yes' or 'no'.");
            choice = scanner.nextLine().trim().toLowerCase();
        }

        if (choice.equals("yes")) {
            System.out.println("Choose a wild Pokémon to catch:");
            for (int i = 0; i < wildPokemons.length; i++) {
                System.out.println((i + 1) + ". " + wildPokemons[i]);
            }

            int chosenWildIndex = getValidChoice(scanner, 1, wildPokemons.length) - 1;
            Pokemon chosenWildPokemon = wildPokemons[chosenWildIndex];

            boolean caught = Utils.catchPokemon(pokeball, chosenWildPokemon.getGrade());
            if (caught) {
                chosenWildPokemon.setHp(chosenWildPokemon.getMaxHp()); // Restore to full HP when caught
                player.addPokemon(chosenWildPokemon);
                System.out.println("You caught " + chosenWildPokemon.getName() + "!");
            } else {
                System.out.println(chosenWildPokemon.getName() + " escaped!");
            }
        }
    }

    private void mainMenu(Scanner scanner) {
        while (true) {
            System.out.println();
            System.out.println("Choose an action:");
            System.out.println("1. Battle");
            System.out.println("2. View Pokémon");
            System.out.println("3. View Top Scores");
            System.out.println("4. Exit");
            System.out.print("Enter your choice [1-4]: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character
                switch (choice) {
                    case 1:
                        healAllPokemons();
                        GameUtil.clearTerminal();
                        if (player.getPokemons().size() < 2) {
                            System.out.println("You need at least two Pokémon. Catch additional Pokémon.");
                            while (player.getPokemons().size() < 2) {
                                generateWildPokemons(3);
                                for (int i = 0; i < wildPokemons.size(); i++) {
                                    System.out.println((i + 1) + ". " + wildPokemons.get(i));
                                }
                                int selectedPokemon = getValidChoice(scanner, 1, wildPokemons.size());
                                player.addPokemon(wildPokemons.get(selectedPokemon - 1));
                                wildPokemons.remove(selectedPokemon - 1);
                            }
                        }
                        chooseBattlePokemons(scanner);
                        battle(scanner);
                        break;
                    case 2:
                        GameUtil.clearTerminal();
                        viewPokemon();
                        break;
                    case 3:
                        GameUtil.clearTerminal();
                        scoreManager.displayTopScores();
                        break;
                    case 4:
                        try {
                            saveProgress();
                        } catch (Exception e) {
                            System.out.println("An error occurred while saving progress: " + e.getMessage());
                        }
                        return;
                    default:
                        System.out.println("Invalid choice. Please choose a number between 1 and 4.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 4.");
                scanner.next(); // Clear the invalid input
            }
        }
    }

    private Pokemon[] generateExtraBattlePokemons(int count) {
        String[] extraPokemonNames = {"Snorlax", "Snorlax", "Dragonite", "Dragonite", "Gengar", "Gengar", "Lucario", "Lucario", "Garchomp", "Garchomp"};
        String[] types = {"Normal", "Normal", "Dragon", "Dragon", "Ghost", "Ghost", "Fighting", "Fighting", "Dragon", "Dragon"};
        String[] move = {"Body Slam", "Hammer Arm", "Dragon Tail", "Hyper Beam", "Shadow Ball", "Thunder", "Aura Sphere", "Dragon Pulse", "Fire Blast", "Dragon Rush", };
        String[] moveTypes = {"Normal", "Fighting", "Dragon", "Normal", "Ghost", "Electric", "Fighting", "Dragon", "Fire", "Dragon"};
        int[] grade = {2, 3, 3, 2, 2, 3, 2, 3, 2, 3};

        Pokemon[] extraBattlePokemons = new Pokemon[count];
        for (int i = 0; i < count; i++) {
            int index = random.nextInt(extraPokemonNames.length);
            int hp = random.nextInt(21) + 50; // HP between 50 and 70
            int attack = random.nextInt(11) + 20; // Attack between 20 and 30
            extraBattlePokemons[i] = createPokemon(types[index], extraPokemonNames[index], hp, attack, hp, grade[index]);
            extraBattlePokemons[i].setMove(move[index]); // Set the move 
            extraBattlePokemons[i].setMoveType(moveTypes[index]); // Set the move types
        }
        return extraBattlePokemons;
    }

    private int calculateBattleScore(Pokemon[] wildPokemons) {
        int score = 0;
        for (Pokemon wildPokemon : wildPokemons) {
            if (wildPokemon.isDefeated()) {
                score += wildPokemon.getMaxHp();
            }
        }
        return score;
    }
}
