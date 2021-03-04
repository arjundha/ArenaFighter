package persistence;

import org.json.JSONObject;
import player.Character;


import java.io.*;

/**
 * The JsonWriter class is based off the implementation from the CPSC 210 JsonSerializationDemo at UBC.
 * This class contains all methods necessary for writing a Character and their Inventory/Equipment to a JSON file.
 *
 * @author Arjun
 */

// Represents a writer that writes JSON representation of Character to file
public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private String destination;

    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    // be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of Character to file
    public void write(Character c) {
        JSONObject json = c.toJson();
        saveToFile(json.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
    }
}
