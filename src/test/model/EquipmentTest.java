package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EquipmentTest {
    private Equipment testEquipment;

    @BeforeEach
    void runBefore() {
        testEquipment = new Equipment("testName", 8, 7, 6, 5, 400);
    }

    @Test
    void testConstructor() {
        assertEquals("testName", testEquipment.getName());
        assertEquals(8, testEquipment.getStrength());
        assertEquals(7, testEquipment.getEndurance());
        assertEquals(6, testEquipment.getDexterity());
        assertEquals(5, testEquipment.getSpeed());
        assertEquals(400, testEquipment.getWorth());
    }
}
