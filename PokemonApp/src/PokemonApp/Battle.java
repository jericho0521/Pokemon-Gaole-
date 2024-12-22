package PokemonApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.Scanner;

public class Battle {
    private Pokemon[] playerPokemons;
    private Pokemon[] wildPokemons;
    private Player player;
    private int battleScore;
    private int result;
    private Scanner scanner;
    private Random random = new Random();

    public Battle(Pokemon[] playerPokemons, Pokemon[] wildPokemons, Player player, Scanner scanner) {
        this.playerPokemons = playerPokemons;
        this.wildPokemons = wildPokemons;
        this.player = player;
        this.battleScore = 0;
        this.result = 0;
        this.scanner = scanner;
    }

    public void start() {
        System.out.println("\nThe battle begins!");
        Utils.printSeparator();

        displayPokemonStats();

        while (!allPlayerPokemonsDefeated() && !allWildPokemonsDefeated()) {

            // Player's turn
            int j = 0;
            while (j < 2) {
                if (playerPokemons[j] != null && !playerPokemons[j].isDefeated()) {
                    System.out.println("\nChoose which wild Pokémon " + player.getName() + "'s " + playerPokemons[j].getName() + " will attack:");
                    displayTargetOptions();
                    int targetChoice = getValidChoice(scanner, 1, 2);

                    Pokemon targetPokemon = (targetChoice == 1) ? wildPokemons[0] : wildPokemons[1];

                    if (!targetPokemon.isDefeated()) {
                        int playerDamage = calculatePlayerDamage(playerPokemons[j], targetPokemon);
                        if (targetPokemon.getHp() - playerDamage < 0)
                            targetPokemon.setHp(0);
                        else
                            targetPokemon.setHp(targetPokemon.getHp() - playerDamage);
                        System.out.println("\n" + player.getName() + "'s " + playerPokemons[j].getName() + " used " + playerPokemons[j].getMove() + " and dealt " + playerDamage + " damage to " + targetPokemon.getName());
                        displayEffectiveness(playerPokemons[j].getMoveType(), targetPokemon.getType());
                        if (targetPokemon.isDefeated()) {
                            System.out.println(targetPokemon.getName() + " has been defeated!");
                            battleScore += targetPokemon.getMaxHp(); // Add the HP of defeated Pokémon to the score
                            if (allWildPokemonsDefeated()) {
                                break;
                            }
                        }
                        j++;
                    } else {
                        System.out.println("You cannot attack a defeated Pokémon. Choose a different target.");
                    }
                } else
                    j++;
            }

            // Wild Pokémon's turn
            for (int i = 0; i < 2; i++) {
                if (wildPokemons[i] != null && !wildPokemons[i].isDefeated()) {
                    int targetIndex = random.nextBoolean() ? 0 : 1;
                    if (playerPokemons[targetIndex] != null && !playerPokemons[targetIndex].isDefeated()) {
                        int wildDamage = calculateWildDamage(wildPokemons[i], playerPokemons[targetIndex]);
                        if (playerPokemons[targetIndex].getHp() - wildDamage < 0)
                            playerPokemons[targetIndex].setHp(0);
                        else
                            playerPokemons[targetIndex].setHp(playerPokemons[targetIndex].getHp() - wildDamage);
                        System.out.println("\nWild " + wildPokemons[i].getName() + " used " + wildPokemons[i].getMove() + " and dealt " + wildDamage + " damage to " + player.getName() + "'s " + playerPokemons[targetIndex].getName());
                        displayEffectiveness(wildPokemons[i].getMoveType(), playerPokemons[targetIndex].getType());
                        if (playerPokemons[targetIndex].isDefeated()) {
                            System.out.println(playerPokemons[targetIndex].getName() + " has been defeated!");
                        }
                    }
                }
            }

            displayPokemonStats(); // Show updated stats after each round
        }

        if (allPlayerPokemonsDefeated()) {
            System.out.println("\nAll your Pokémon have fainted. You lost the battle!");
        } else {
            this.result = 1;
            System.out.println("\nYou defeated the wild Pokémon!");
        }
    }

    public int getBattleScore() {
        return battleScore;
    }

    public int getResult() {
        return result;
    }

    private int calculatePlayerDamage(Pokemon attacker, Pokemon defender) {
        JFrame diceFrame = new JFrame("Dice Roll");
        diceFrame.setSize(400, 300);
        diceFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        diceFrame.setLayout(new BorderLayout());

        JLabel diceLabel = new JLabel("Press Roll to roll the dice!", SwingConstants.CENTER);
        diceLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        diceFrame.add(diceLabel, BorderLayout.CENTER);

        JButton rollButton = new JButton("Roll Dice");
        diceFrame.add(rollButton, BorderLayout.SOUTH);

        int[] diceResults = new int[2];

        rollButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                diceResults[0] = random.nextInt(6) + 1;
                diceResults[1] = random.nextInt(6) + 1;
                diceLabel.setText("You rolled: " + diceResults[0] + " and " + diceResults[1]);
                rollButton.setEnabled(false);
            }
        });

        diceFrame.setVisible(true);

        while (rollButton.isEnabled()) {
            // Wait for dice roll to complete
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        diceFrame.dispose();

        int dice1 = diceResults[0];
        int dice2 = diceResults[1];

        int baseDamage;
        if (dice1 == 6 && dice2 == 6) {
            System.out.println("Triple Damage!");
            baseDamage = 30; // Arbitrary triple damage value
        } else if (dice1 == dice2) {
            System.out.println("Double Damage!");
            baseDamage = 20; // Arbitrary double damage value
        } else if (dice1 + dice2 >= 6 && dice1 + dice2 <= 10) {
            System.out.println("Normal Damage!");
            baseDamage = 10; // Arbitrary normal damage value
        } else if (dice1 + dice2 < 6) {
            System.out.println("Less than Normal Damage!");
            baseDamage = 5; // Arbitrary less than normal damage value
        } else {
            System.out.println("No Damage or Special Effect.");
            baseDamage = 0;
        }

        // Apply type effectiveness
        double effectiveness = Utils.getEffectiveness(attacker.getMoveType(), defender.getType());
        int totalDamage = (int) (baseDamage * effectiveness);

        System.out.println("You dealt " + totalDamage + " damage!");
        return totalDamage;
    }

    private int calculateWildDamage(Pokemon attacker, Pokemon defender) {
        // Wild Pokémon attacks with their base attack power modified by type effectiveness
        return Utils.calculateDamage(attacker, defender);
    }

    private boolean allPlayerPokemonsDefeated() {
        for (Pokemon p : playerPokemons) {
            if (p != null && !p.isDefeated()) {
                return false;
            }
        }
        return true;
    }

    private boolean allWildPokemonsDefeated() {
        for (Pokemon p : wildPokemons) {
            if (p != null && !p.isDefeated()) {
                return false;
            }
        }
        return true;
    }

    private int getValidChoice(Scanner scanner, int min, int max) {
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

    private void displayPokemonStats() {
        System.out.println("\nYour Active Pokémon:");
        for (int i = 0; i < playerPokemons.length; i++) {
            if (playerPokemons[i] != null) {
                System.out.println((i + 1) + ". " + playerPokemons[i]);
            }
        }
        System.out.println("\nWild Pokémon:");
        for (int i = 0; i < wildPokemons.length; i++) {
            if (wildPokemons[i] != null) {
                System.out.println((i + 1) + ". " + wildPokemons[i]);
            }
        }
    }

    private void displayTargetOptions() {
        for (int i = 0; i < wildPokemons.length; i++) {
            if (wildPokemons[i] != null && !wildPokemons[i].isDefeated()) {
                System.out.println((i + 1) + ". " + wildPokemons[i]);
            }
        }
    }

    private void displayEffectiveness(String attackerType, String defenderType) {
        double effectiveness = Utils.getEffectiveness(attackerType, defenderType);
        if (effectiveness > 1) {
            System.out.println("It's super effective!");
        } else if (effectiveness == 0) {
            System.out.println("It has no effect...");
        } else if (effectiveness < 1) {
            System.out.println("It's not very effective...");
        }
    }
}
