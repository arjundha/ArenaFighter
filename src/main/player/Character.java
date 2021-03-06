package player;

import exceptions.InvalidEquipmentException;
import model.Equipment;
import model.Inventory;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The Character class represents a character in the game. A character needs to be instantiated with a name, a race,
 * a class name, max hp, strength, endurance, dexterity and speed. All characters start at level 1 and with an empty
 * inventory. Characters all start with a STARTING_GOLD, but merchants start with twice as much.
 *
 * The methods in this class can be used to get the various stats of a character including the stats given through
 * inventory items. There are methods to heal and take damage, increase a players stats, level up, add items to a
 * character's inventory and a method to check if they are alive.
 *
 * @author Arjun
 */
public class Character implements Writable {
    private final String name;  // the name of a character
    private final String race;  // the race of a character (human, dwarf, elf etc.)
    private final String className;  // the class of a character (warrior, rogue, merchant etc.)
    private int level;  // represents the current level of a character
    private final List<Integer> hitpoints;  // HP is a list, where the first index is current, and second is max
    private int strength; // The strength of a character
    private int endurance;  // the endurance of a character
    private int dexterity;  // the dexterity of a character
    private int speed;  // the speed modifier of a weapon
    private final Inventory inventory;  // the inventory of a character
    private int gold;  // the amount of gold a character has

    // These fields remain public for tests to accurately run
    public static final int STARTING_LEVEL = 1;  // These fields determine what level a character starts at
    public static final int STARTING_GOLD = 50;  // and what amount of gold they should have at the beginning of a game

    public static final int WARRIOR_HEALTH_INCREASE = 10;  // The amount of HP a warrior increases on level up or train
    public static final int ROGUE_HEALTH_INCREASE = 5;  // The amount of HP a rogue increases on level up
    public static final int MERCHANT_HEALTH_INCREASE = 1;  // Merchants gain little HP when levelling up or training

    public static final int ROGUE_DEX_STAT_BONUS = 5; // Rogue dexterity increases higher than any class
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

    public Character(String name, String race, String className, int level,
                     int currentHP, int maxHP, int str, int end, int dex, int spd, int gold) {
        this.name = name;
        this.race = race;
        this.className = className;
        this.level = level;
        this.hitpoints = Arrays.asList(currentHP, maxHP); // HP is a list
        this.strength = str;
        this.endurance = end;
        this.dexterity = dex;
        this.speed = spd;
        this.inventory = new Inventory();  // this gets built in persistence
        this.gold = gold;
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
            this.dexterity += ROGUE_DEX_STAT_BONUS;  // Rogues gain more dex and speed
            this.speed += FAVOURABLE_STAT_INCREASE;

        } else {  // Merchant
            this.hitpoints.set(1, (this.hitpoints.get(1) + MERCHANT_HEALTH_INCREASE));  // Merchant hp increases
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

    // REQUIRES: damage must be a non negative integer
    // MODIFIES: this
    // EFFECTS: Reduce the current HP of a character by a certain amount (damage)
    public void takeDamage(int damage) {
        int current = this.hitpoints.get(0);
        this.hitpoints.set(0, current - damage);
    }

    // EFFECTS: Returns true if a character is dead (HP is less than or equal to 0)
    public boolean isAlive() {
        return (this.hitpoints.get(0) > 0);
    }

    // MODIFIES: This
    // EFFECTS: adds an equipment to a characters inventory
    public void equipItem(Equipment item) {
        this.inventory.addEquipment(item);
    }

    // EFFECTS: Produce a JSON corresponding to the current character's data
    // CITATION: The base code can be found at https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    //           This method is implemented using the CPSC 210 JsonSerializationDemo as it's base code.
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();

        json.put("name", name);
        json.put("race", race);
        json.put("className", className);
        json.put("level", level);
        json.put("currentHP", getCurrentHealth());
        json.put("maxHP", getMaxHealth());
        json.put("strength", strength);
        json.put("endurance", endurance);
        json.put("dexterity", dexterity);
        json.put("speed", speed);
        json.put("inventory", inventoryToJson());
        json.put("gold", gold);

        return json;
    }

    // EFFECTS: Produce a JSONArray to represent a Character's inventory
    // CITATION: The base code can be found at https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    //           This method is implemented using the CPSC 210 JsonSerializationDemo as it's base code.
    private JSONArray inventoryToJson() {
        JSONArray jsonArray = new JSONArray();

        for (int i = 0; i < inventory.getInventorySize(); i++) {
            Equipment item;
            try {
                item = inventory.getEquipment(i);
                jsonArray.put(item.toJson());
            } catch (InvalidEquipmentException e) {
                e.printStackTrace();
            }
        }
        return jsonArray;
    }

}


