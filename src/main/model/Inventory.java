package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The Inventory class represents a collection of Equipment objects. An inventory can have as few as
 * 0 Equipment, to infinitely many. There are methods for getting an Equipment at a certain index, adding equipment,
 * removing equipment and getting the number of Equipment in an inventory. There are also methods to get the summation
 * of certain Equipment values of an inventory, like strength, endurance, dexterity or gold.
 *
 * @author Arjun
 */

// Represents all the equipment currently owned by a player (their inventory of equipment)
public class Inventory {
    private final List<Equipment> equipment;

    // EFFECTS: constructs an empty inventory
    public Inventory() {
        this.equipment = new ArrayList<>();
    }

    // REQUIRES: 0 <= index < this.equipment.size()
    // EFFECTS: produce a specific equipment at a specified index
    public Equipment getEquipment(int index) {
        return this.equipment.get(index);
    }

    // EFFECTS: produce the total strength modification value from elements in the Inventory
    public int getTotalStrength() {
        int strength = 0;
        for (Equipment equipment : this.equipment) {
            strength += equipment.getStrength();
        }
        return strength;
    }

    // EFFECTS: produce the total endurance modification value from elements in the Inventory
    public int getTotalEndurance() {
        int endurance = 0;
        for (Equipment equipment : this.equipment) {
            endurance += equipment.getEndurance();
        }
        return endurance;
    }

    // EFFECTS: produce the total dexterity modification value from elements in the Inventory
    public int getTotalDexterity() {
        int dexterity = 0;
        for (Equipment equipment : this.equipment) {
            dexterity += equipment.getDexterity();
        }
        return dexterity;
    }

    // EFFECTS: produce the total speed modification value from elements in the Inventory
    public int getTotalSpeed() {
        int speed = 0;
        for (Equipment equipment : this.equipment) {
            speed += equipment.getSpeed();
        }
        return speed;
    }

    public int getInventorySize() {
        return this.equipment.size();
    }

    //  These two methods were methods that were originally used, but then broken down into different methods.
    // I am keeping them in here in case they may be useful in future phases.

//    // EFFECTS: produce a list of all the current stat modifiers from equipment in an inventory
//    public List<Integer> getModifierTotals() {
//        int strength = 0;
//        int endurance = 0;
//        int dexterity = 0;
//        int speed = 0;
//
//        for (Equipment equipment : this.equipment) {  // Sum up all the totals
//            strength += equipment.getStrength();
//            endurance += equipment.getEndurance();
//            dexterity += equipment.getDexterity();
//            speed += equipment.getSpeed();
//        }
//
//        // Return the totals as a list [0 - 3]
//        return Arrays.asList(strength, endurance, dexterity, speed);
//    }
//
//    // REQUIRES: worth > 0
//    // MODIFIES: this
//    // EFFECTS: create and add a new equipment to the inventory of equipment
//    public void addNewEquipment(String itemName, int str, int end, int dex, int spd, int worth) {
//        Equipment equipment = new Equipment(itemName, str, end, dex, spd, worth);
//        this.equipment.add(equipment);
//    }

    // MODIFIES: this
    // EFFECTS: adds a new equipment to the inventory of equipment
    public void addEquipment(Equipment item) {
        this.equipment.add(item);
    }

    // REQUIRES: 0 <= index < this.equipment.size()
    // MODIFIES: this
    // EFFECTS: remove an equipment from an Inventory
    public void removeEquipment(int index) {
        this.equipment.remove(index);
    }


}
