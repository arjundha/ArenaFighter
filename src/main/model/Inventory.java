package model;

import java.util.ArrayList;
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
    public void addEquipment(String itemName, int str, int end, int dex, int spe, int worth) {
        Equipment equipment = new Equipment(itemName, str, end, dex, spe, worth);
        this.equipment.add(equipment);
    }


}
