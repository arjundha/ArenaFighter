package model;

import java.util.ArrayList;
import java.util.List;

// Represents all the equipment currently owned by a player (their inventory of equipment)
public class Inventory {
    private List<Equipment> items;

    // EFFECTS: constructs an empty inventory
    public Inventory() {
        this.items = new ArrayList<>();
    }


}
