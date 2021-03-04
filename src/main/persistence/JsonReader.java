package persistence;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import model.Equipment;
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
        int level = jsonObject.getInt("level");
        int currentHP = jsonObject.getInt("currentHP");
        int maxHP = jsonObject.getInt("maxHP");
        int str = jsonObject.getInt("strength");
        int end = jsonObject.getInt("endurance");
        int dex = jsonObject.getInt("dexterity");
        int speed = jsonObject.getInt("speed");
//        Integer inventory = Integer.parseInt(jsonObject.getString("level"));

        int gold = jsonObject.getInt("gold");

        Character c = new Character(name, race, className, level, currentHP, maxHP, str, end, dex, speed, gold);
        buildInventory(c, jsonObject);
        return c;
    }

    // MODIFIES: wr
    // EFFECTS: parses thingies from JSON object and adds them to workroom
    private void buildInventory(Character c, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("inventory");
        for (Object json : jsonArray) {
            JSONObject item = (JSONObject) json;
            addEquipment(c, item);
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
