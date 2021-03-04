package persistence;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import model.Equipment;
import org.json.*;
import player.Character;

/**
 * The JsonReader class is based off the implementation from the CPSC 210 JsonSerializationDemo at UBC.
 * This class contains all methods necessary for reading a Character and their Inventory/Equipment from a JSON file.
 *
 * @author Arjun
 */

// Represents a reader that reads workroom from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads Character from source file and returns it;
    // throws IOException if an error occurs reading data from file
    public Character read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseWorkRoom(jsonObject);
    }

    // EFFECTS: reads source file as a string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses character from JSON object and returns it as a Character object
    private Character parseWorkRoom(JSONObject jsonObject) {
        String name = jsonObject.getString("name");  // These are the parameters for building a character
        String race = jsonObject.getString("race");
        String className = jsonObject.getString("className");
        int level = jsonObject.getInt("level");
        int currentHP = jsonObject.getInt("currentHP");
        int maxHP = jsonObject.getInt("maxHP");
        int str = jsonObject.getInt("strength");
        int end = jsonObject.getInt("endurance");
        int dex = jsonObject.getInt("dexterity");
        int speed = jsonObject.getInt("speed");
        int gold = jsonObject.getInt("gold");

        Character c = new Character(name, race, className, level, currentHP, maxHP, str, end, dex, speed, gold);
        buildInventory(c, jsonObject);  // We need to rebuild the Inventory
        return c;
    }

    // MODIFIES: Character c
    // EFFECTS: parses Equipment from JSON object and adds them to the Character Inventory
    private void buildInventory(Character c, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("inventory");
        for (Object json : jsonArray) {  // for each Equipment in the JSONArray
            JSONObject item = (JSONObject) json;
            addEquipment(c, item);  // Equip the item to the Character
        }
    }

    // MODIFIES: Character c
    // EFFECTS: parses equipment from JSON object and adds it to the inventory of the Character
    private void addEquipment(Character c, JSONObject jsonObject) {
        String name = jsonObject.getString("name");  // Retrieve all the parameters in the Character
        int strength = jsonObject.getInt("strength");
        int endurance = jsonObject.getInt("endurance");
        int dexterity = jsonObject.getInt("dexterity");
        int speed = jsonObject.getInt("speed");
        int worth = jsonObject.getInt("worth");
        Equipment item = new Equipment(name, strength, endurance, dexterity, speed, worth);
        c.equipItem(item);  // Equip the item to the Character (add it to their inventory)
    }
}
