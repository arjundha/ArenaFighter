package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Represents all the equipment currently owned by a player (their inventory of equipment)
public class Inventory {
    private List<Equipment> equipment;

    // EFFECTS: constructs an empty inventory
    public Inventory() {
        this.equipment = new ArrayList<>();
    }

    // REQUIRES: worth > 0
    // MODIFIES: this
    // EFFECTS: adds a new equipment to the inventory of equipment
    public void addEquipment(String itemName, int str, int end, int dex, int spd, int worth) {
        Equipment equipment = new Equipment(itemName, str, end, dex, spd, worth);
        this.equipment.add(equipment);
    }

    // REQUIRES: 0 <= index < this.equipment.size()
    // MODIFIES: this
    // EFFECTS: remove an equipment from an Inventory
    public void removeEquipment(int index) {
        this.equipment.remove(index);
    }

    // EFFECTS: produce a list of all the current stat modifiers from equipment in an inventory
    public List<Integer> getModifierTotals() {
        int strength = 0;
        int endurance = 0;
        int dexterity = 0;
        int speed = 0;
//        List<Integer> totals = new ArrayList<>();
        for (Equipment equipment : this.equipment) {
            strength += equipment.getStrength();
            endurance += equipment.getEndurance();
            dexterity += equipment.getDexterity();
            speed += equipment.getDexterity();
        }

        return Arrays.asList(strength, endurance, dexterity, speed);

    }


}
