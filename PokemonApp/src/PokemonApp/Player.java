package PokemonApp;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private List<Pokemon> pokemons;
    private Pokemon[] activePokemons; // Array to store active Pokémon for battle
    private int score;

    // No-argument constructor
    public Player() {
        this.name = "";
        this.pokemons = new ArrayList<>();
        this.activePokemons = new Pokemon[2]; // Initialize with a size of 2 for two active Pokémon
        this.score = 0;
    }

    public Player(String name) {
        this.name = name;
        this.pokemons = new ArrayList<>();
        this.activePokemons = new Pokemon[2]; // Initialize with a size of 2 for two active Pokémon
        this.score = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Pokemon> getPokemons() {
        return pokemons;
    }

    public void addPokemon(Pokemon pokemon) {
        pokemons.add(pokemon);
    }

    public void clearPokemons() {
        pokemons.clear();
    }

    public void setActivePokemons(Pokemon firstPokemon, Pokemon secondPokemon) {
        activePokemons[0] = firstPokemon;
        activePokemons[1] = secondPokemon;
    }

    public Pokemon[] getActivePokemons() {
        return activePokemons;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int score) {
        this.score += score;
    }
}
