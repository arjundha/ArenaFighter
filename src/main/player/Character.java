package player;

import model.Inventory;

import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class Character {
    private String name;  // the name of a character
    private String race;  // the race of a character (human, dwarf, elf etc.)
    private String className;  // the class of a character (warrior, rogue, merchant etc.)
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

    public static final int WARRIOR_HEALTH_INCREASE = 10;  // The amount of HP a warrior increases on level up or train
    public static final int ROGUE_HEALTH_INCREASE = 5;  // The amount of HP a rogue increases on level up
    public static final int MERCHANT_HEALTH_INCREASE = 1;  // Merchants gain little HP when levelling up or training

    public static final int FAVOURABLE_STAT_INCREASE = 3; //
    public static final int UNFAVOURABLE_STAT_INCREASE = 1; //


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

        if (className.equals("merchant")) {  // in this game, merchants always start with more gold
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

    // MODIFIES: this
    // EFFECTS: increase level by one and gain gold
    //          Merchants earn twice as much gold as other classes
    public void levelUp() {
        this.level += 1;
        // Merchants gain twice as much gold on level up
        if (this.className.equals("merchant")) {
            this.gold += STARTING_GOLD * 2;
        } else {
            this.gold += STARTING_GOLD;
        }
    }

    // MODIFIES: this
    // EFFECTS: randomly increase a players stats based on which class they are
    //          Warriors gain the most HP, strength and endurance
    //          Rogues gain the most dexterity and speed
    //          Merchants gain the least of all stats, but gain more gold on level up to compensate
    public void increaseStats(String className) {
        if (className.equals("warrior")) {
            this.hitpoints.set(1, (this.hitpoints.get(1) + WARRIOR_HEALTH_INCREASE));  // Warrior hp increases
            this.strength += FAVOURABLE_STAT_INCREASE;  // Warriors gain more strength and endurance
            this.endurance += FAVOURABLE_STAT_INCREASE;
            this.dexterity += UNFAVOURABLE_STAT_INCREASE;
            this.speed += UNFAVOURABLE_STAT_INCREASE;

        } else if (className.equals("rogue")) {
            this.hitpoints.set(1, (this.hitpoints.get(1) + ROGUE_HEALTH_INCREASE));  // ROGUE hp increases
            this.strength += UNFAVOURABLE_STAT_INCREASE;
            this.endurance += UNFAVOURABLE_STAT_INCREASE;
            this.dexterity += FAVOURABLE_STAT_INCREASE;  // Rogues gain more dex and speed
            this.speed += FAVOURABLE_STAT_INCREASE;

        } else {
            this.hitpoints.set(1, (this.hitpoints.get(1) + MERCHANT_HEALTH_INCREASE));  // Warrior hp increases
            this.strength += UNFAVOURABLE_STAT_INCREASE;  // Merchants don't gain much from stat increases, but
            this.endurance += UNFAVOURABLE_STAT_INCREASE; // they gain a lot of gold on level up
            this.dexterity += UNFAVOURABLE_STAT_INCREASE;
            this.speed += UNFAVOURABLE_STAT_INCREASE;
        }
    }

    // REQUIRES: amount >= this.gold
    // MODIFIES: this
    // EFFECTS: Removes a certain amount of gold from a character, where amount is the gold to be spent
    public void spendGold(int amount) {
        this.gold -= amount;
    }

    // REQUIRES: amount must be a non negative integer
    // MODIFIES: this
    // EFFECTS: Heal a character by increasing their current health by a certain amount, or till it reached the max
    public void healCharacter(int amount) {
        int maxHP = this.hitpoints.get(1);
        int newHP = this.hitpoints.get(0) + amount;
//        if (newHP > maxHP) {
//            this.hitpoints.set(0, maxHP);
//        } else {
//            this.hitpoints.set(0, newHP);
//        }
        this.hitpoints.set(0, Math.min(newHP, maxHP));  // set HP to the whatever is smaller (new or max)
    }


}


