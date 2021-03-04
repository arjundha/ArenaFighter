package persistence;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import model.Equipment;
import model.Inventory;
import org.json.*;
import player.Character;

// Represents a reader that reads workroom from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads workroom from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Character read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseWorkRoom(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses character from JSON object and returns it
    private Character parseWorkRoom(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        String race = jsonObject.getString("race");
        String className = jsonObject.getString("className");
        int level = Integer.parseInt(jsonObject.getString("level"));
        int currentHP = Integer.parseInt(jsonObject.getString("currentHP"));
        int maxHP = Integer.parseInt(jsonObject.getString("maxHP"));
        int str = Integer.parseInt(jsonObject.getString("strength"));
        int end = Integer.parseInt(jsonObject.getString("endurance"));
        int dex = Integer.parseInt(jsonObject.getString("dexterity"));
        int speed = Integer.parseInt(jsonObject.getString("speed"));
//        Integer inventory = Integer.parseInt(jsonObject.getString("level"));

        int gold = Integer.parseInt(jsonObject.getString("gold"));

        Character c = new Character(name, race, className, level, currentHP, maxHP, str, end, dex, speed, gold);
        addThingies(c, jsonObject);
        return c;
    }

    // MODIFIES: wr
    // EFFECTS: parses thingies from JSON object and adds them to workroom
    private void addThingies(Character c, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("thingies");
        for (Object json : jsonArray) {
            JSONObject nextThingy = (JSONObject) json;
            addEquipment(c, nextThingy);
        }
    }

    // MODIFIES: inv
    // EFFECTS: parses equipment from JSON object and adds it to the inventory
    private void addEquipment(Character c, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        int strength = Integer.parseInt(jsonObject.getString("strength"));
        int endurance = Integer.parseInt(jsonObject.getString("endurance"));
        int dexterity = Integer.parseInt(jsonObject.getString("dexterity"));
        int speed = Integer.parseInt(jsonObject.getString("speed"));
        int worth = Integer.parseInt(jsonObject.getString("worth"));
        Equipment item = new Equipment(name, strength, endurance, dexterity, speed, worth);
        c.equipItem(item);
    }
}
