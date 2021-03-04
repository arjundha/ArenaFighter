package persistence;

import model.Equipment;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
