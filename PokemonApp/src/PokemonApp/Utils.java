package PokemonApp;

import java.util.HashMap;
import java.util.Map;

public class Utils {
    private static final Map<String, Map<String, Double>> effectivenessChart = new HashMap<>();

    static {
        Map<String, Double> fire = new HashMap<>();
        fire.put("Grass", 2.0);
        fire.put("Water", 0.5);
        fire.put("Fire", 0.5);
        fire.put("Electric", 1.0);
        fire.put("Normal", 1.0);
        fire.put("Ghost", 1.0);
        fire.put("Dragon", 0.5);
        fire.put("Fighting", 1.0);

        Map<String, Double> water = new HashMap<>();
        water.put("Fire", 2.0);
        water.put("Grass", 0.5);
        water.put("Water", 0.5);
        water.put("Electric", 1.0);
        water.put("Normal", 1.0);
        water.put("Ghost", 1.0);
        water.put("Dragon", 0.5);
        water.put("Fighting", 1.0);

        Map<String, Double> grass = new HashMap<>();
        grass.put("Water", 2.0);
        grass.put("Fire", 0.5);
        grass.put("Grass", 0.5);
        grass.put("Electric", 1.0);
        grass.put("Normal", 1.0);
        grass.put("Ghost", 1.0);
        grass.put("Dragon", 0.5);
        grass.put("Fighting", 1.0);

        Map<String, Double> electric = new HashMap<>();
        electric.put("Water", 2.0);
        electric.put("Grass", 0.5);
        electric.put("Fire", 1.0);
        electric.put("Electric", 0.5);
        electric.put("Normal", 1.0);
        electric.put("Ghost", 1.0);
        electric.put("Dragon", 0.5);
        electric.put("Fighting", 1.0);

        Map<String, Double> normal = new HashMap<>();
        normal.put("Fire", 1.0);
        normal.put("Water", 1.0);
        normal.put("Grass", 1.0);
        normal.put("Electric", 1.0);
        normal.put("Normal", 1.0);
        normal.put("Ghost", 0.0); // Normal attacks do not affect Ghost
        normal.put("Dragon", 1.0);
        normal.put("Fighting", 1.0);

        Map<String, Double> ghost = new HashMap<>();
        ghost.put("Fire", 1.0);
        ghost.put("Water", 1.0);
        ghost.put("Grass", 1.0);
        ghost.put("Electric", 1.0);
        ghost.put("Normal", 0.0); // Ghost attacks do not affect Normal
        ghost.put("Ghost", 2.0);  // Ghost attacks are super effective against Ghost
        ghost.put("Dragon", 1.0);
        ghost.put("Fighting", 0.0); // Ghost attacks do not affect Fighting

        Map<String, Double> fighting = new HashMap<>();
        fighting.put("Normal", 2.0); // Fighting attacks are super effective against Normal
        fighting.put("Ghost", 0.0); // Fighting attacks do not affect Ghost
        fighting.put("Dragon", 1.0);
        fighting.put("Fire", 1.0);
        fighting.put("Water", 1.0);
        fighting.put("Grass", 1.0);
        fighting.put("Electric", 1.0);

        Map<String, Double> dragon = new HashMap<>();
        dragon.put("Dragon", 2.0); // Dragon attacks are super effective against Dragon
        dragon.put("Normal", 1.0);
        dragon.put("Ghost", 1.0);
        dragon.put("Fire", 1.0);
        dragon.put("Water", 1.0);
        dragon.put("Grass", 1.0);
        dragon.put("Electric", 1.0);
        dragon.put("Fighting", 1.0);

        effectivenessChart.put("Fire", fire);
        effectivenessChart.put("Water", water);
        effectivenessChart.put("Grass", grass);
        effectivenessChart.put("Electric", electric);
        effectivenessChart.put("Normal", normal);
        effectivenessChart.put("Ghost", ghost);
        effectivenessChart.put("Fighting", fighting);
        effectivenessChart.put("Dragon", dragon);
    }

    public static double getEffectiveness(String attackType, String defendType) {
        return effectivenessChart.getOrDefault(attackType, new HashMap<>()).getOrDefault(defendType, 1.0);
    }

    public static int calculateDamage(Pokemon attacker, Pokemon defender) {
        double effectiveness = getEffectiveness(attacker.getMoveType(), defender.getType());
        return (int) (attacker.getAttackPower() * effectiveness);
    }

    public static String getRandomPokeball() {
        String[] pokeballs = {"Poke Ball", "Great Ball", "Ultra Ball", "Master Ball"};
        return pokeballs[(int) (Math.random() * pokeballs.length)];
    }

    public static boolean catchPokemon(String pokeball, int grade) {
        double catchProbability = switch (pokeball) {
            case "Poke Ball" -> grade == 1 ? 0.5 : (grade == 2 ? 0.25 : 0.1);
            case "Great Ball" -> grade == 1 ? 0.75 : (grade == 2 ? 0.5 : 0.25);
            case "Ultra Ball" -> grade == 1 ? 0.9 : (grade == 2 ? 0.75 : 0.5);
            case "Master Ball" -> 1.0;
            default -> 0.0;
        };
        return Math.random() < catchProbability;
    }

    public static int getRandomNumber(int min, int max) {
        return min + (int) (Math.random() * ((max - min) + 1));
    }

    public static void printSeparator() {
        System.out.println("----------------------------------------");
    }
}