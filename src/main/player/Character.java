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
    private List<Integer> hitpoints;
    private int strength; // The strength of a character
    private int endurance;  // the endurance of a character
    private int dexterity;  // the dexterity of a character
    private int speed;  // the speed modifier of a weapon
    private Inventory inventory;  // the inventory of a character
    private int gold;  // the amount of gold a character has

    public static final int STARTING_GOLD = 50;

    /*
    REQUIRES: all integer values must be > 0
    EFFECTS:
     */
    public Character(String name, String race, String className, int hp, int str, int end, int dex, int spd) {
        this.name = name;
        this.race = race;
        this.className = className;
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

}
