package persistence;

import model.Equipment;
import model.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import player.Character;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * CITATION: The base code can be found at https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
 * The JsonWriterTest class is based off the implementation from the CPSC 210 JsonSerializationDemo at UBC.
 *
 * @author Arjun
 */

public class JsonWriterTest extends JsonTest{
    private Character character;
    private Equipment testEquipment;
    private Equipment testOtherEquipment;

    @BeforeEach
    void runBefore() {
        character = new Character("test", "human", "warrior", 10, 11, 12, 13, 14);
        testEquipment = new Equipment("testItem", 8, 7, 6, 5, 400);
        testOtherEquipment = new Equipment("testItem2", 1, 2, 3, 4, 5);

    }

    @Test
    void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyWorkroom() {
        try {
            JsonWriter writer = new JsonWriter("./data/testWriterCharacterEmptyInventory.json");
            writer.open();
            writer.write(character);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterCharacterEmptyInventory.json");
            character = reader.read();
            assertEquals("test", character.getName());
            assertEquals(0, character.getInventory().getInventorySize());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralWorkroom() {
        try {
            character.equipItem(testEquipment);
            character.equipItem(testOtherEquipment);
            JsonWriter writer = new JsonWriter("./data/testWriterCharacterWithEquipment.json");
            writer.open();
            writer.write(character);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterCharacterWithEquipment.json");
            character = reader.read();
            assertEquals("test", character.getName());
            Inventory inventory = character.getInventory();
            assertEquals(2, inventory.getInventorySize());
            checkEquipment("testItem", 8, 7, 6, 5, 400, inventory.getEquipment(0));
            checkEquipment("testItem2", 1, 2, 3, 4, 5, inventory.getEquipment(1));


        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
