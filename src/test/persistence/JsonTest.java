package persistence;

import model.Equipment;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The JsonTest class is extended by the two other persistence test files: JsonReaderTest and JsonWriterTest.
 * It is based off the implementation from the CPSC 210 JsonSerializationDemo at UBC.
 * The method in this file is used to check if an Equipment object from JSON matches the expected parameters.
 *
 * @author Arjun
 */

public class JsonTest {

    protected void checkEquipment(String name, int str, int end, int dex, int spd, int worth, Equipment item) {
        assertEquals(name, item.getName());
        assertEquals(str, item.getStrength());
        assertEquals(end, item.getEndurance());
        assertEquals(dex, item.getDexterity());
        assertEquals(spd, item.getSpeed());
        assertEquals(worth, item.getWorth());
    }

}
