package player;

import model.Inventory;

import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class Character {
    private String name;  // the name of a character
    private String race;  // the race of a character (Human, dwarf, elf etc.)
    private String className;  // the class of a character (Warrior, rogue, wizard etc.)
    private int level;  // represents the current level of a character
    private List<Integer> hitpoints;  // HP is a list, where the first index is current, and second is max
    private int strength; // The strength of a character
    private int endurance;  // the endurance of a character
    private int dexterity;  // the dexterity of a character
    private int speed;  // the speed modifier of a weapon
    private Inventory inventory;  // the inventory of a character
    private int gold;  // the amount of gold a character has

    private static final int STARTING_LEVEL = 1;  // These fields determine what level a character starts at
    public static final int STARTING_GOLD = 50;  // and what amount of gold they should have at the beginning of a game

    /*
    REQUIRES: all integer values must be > 0
    EFFECTS: Construct a well-formed character
     */
    public Character(String name, String race, String className, int hp, int str, int end, int dex, int spd) {
        this.name = name;
        this.race = race;
        this.className = className;
        this.level = STARTING_LEVEL;  // a new character ALWAYS starts at the same level
        this.hitpoints = Arrays.asList(hp, hp); // HP is a list, where the first index is current, and second is max
        this.strength = str;
        this.endurance = end;
        this.dexterity = dex;
        this.speed = spd;
        this.inventory = new Inventory();

        if (className.equals("wizard")) {  // in this game, wizards always start with more gold due to aLcHeMy
            this.gold = STARTING_GOLD * 2;
        } else {
            this.gold = STARTING_GOLD;
        }
    }

    // EFFECTS: Produce the name of a character
    public String getName() {
        return this.name;
    }

    // EFFECTS: Produce the race of a character
    public String getRace() {
        return this.race;
    }

    // EFFECTS: Produce the name of a character
    public String getClassName() {
        return this.className;
    }

    // EFFECTS: Produce the current level of a character
    public int getLevel() {
        return this.level;
    }

    // EFFECTS: Produce the current hp of a character
    public int getCurrentHealth() {
        return this.hitpoints.get(0);
    }

    // EFFECTS: Produce the max possible hp of a character
    public int getMaxHealth() {
        return this.hitpoints.get(1);
    }

    // EFFECTS: Produce the total strength of a character, including equipment modifiers
    public int getStrength() {
        return this.strength + this.inventory.getTotalStrength();
    }

    // EFFECTS: Produce the total endurance of a character, including equipment modifiers
    public int getEndurance() {
        return this.endurance + this.inventory.getTotalEndurance();
    }

    // EFFECTS: Produce the total dexterity of a character, including equipment modifiers
    public int getDexterity() {
        return this.dexterity + this.inventory.getTotalDexterity();
    }

    // EFFECTS: Produce the total speed of a character, including equipment modifiers
    public int getSpeed() {
        return this.speed + this.inventory.getTotalSpeed();
    }

    // EFFECTS: Produce the current inventory of a character
    public Inventory getInventory() {
        return this.inventory;
    }

    // EFFECTS: Produce the current amount of gold a character has
    public int getGold() {
        return this.gold;
    }
}


