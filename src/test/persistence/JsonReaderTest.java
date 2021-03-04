package persistence;

import org.junit.jupiter.api.Test;
import player.Character;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonReaderTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Character player = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyWorkRoom() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyInventory.json");
        try {
            Character player = reader.read();
            assertEquals("test name", player.getName());
            assertEquals("human", player.getRace());
            assertEquals("warrior", player.getClassName());
            assertEquals(1, player.getLevel());
            assertEquals(10, player.getCurrentHealth());
            assertEquals(10, player.getMaxHealth());
            assertEquals(11, player.getStrength());
            assertEquals(12, player.getEndurance());
            assertEquals(13, player.getDexterity());
            assertEquals(14, player.getSpeed());
            assertEquals(100, player.getGold());

            assertEquals(0, player.getInventory().getInventorySize());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
