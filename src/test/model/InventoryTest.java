package model;

import exceptions.InvalidEquipmentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InventoryTest {
    Inventory testInventory;
    Inventory testInventoryWithEquipment;
    Equipment testEquipment;

    @BeforeEach
    void runBefore() {
        testInventory = new Inventory();
        testInventoryWithEquipment = new Inventory();
        testEquipment = new Equipment("testName", 8, 7, 6, 5, 400);
        testInventoryWithEquipment.addEquipment(testEquipment);
    }

    @Test
    void testEmptyInventorySize() {
        assertEquals(0, testInventory.getInventorySize());
    }

    @Test
    void testInventoryOneItem() {
        assertEquals(1, testInventoryWithEquipment.getInventorySize());
    }

    @Test
    void testInventoryRemoveEquipment() {
        assertEquals(1, testInventoryWithEquipment.getInventorySize());
        try {
            testInventoryWithEquipment.removeEquipment(0);
            assertEquals(0, testInventoryWithEquipment.getInventorySize());
        } catch (InvalidEquipmentException e) {
            fail("Unexpected InvalidEquipmentException.");
        }
    }

    @Test
    void testInventoryRemoveEquipmentNumberLargerThanSize() {
        assertEquals(1, testInventoryWithEquipment.getInventorySize());
        try {
            testInventoryWithEquipment.removeEquipment(1);
            fail("Uncaught InvalidEquipmentException");
        } catch (InvalidEquipmentException e) {
            // expected
        }
    }

    @Test
    void testInventoryRemoveEquipmentNegativeNumber() {
        assertEquals(1, testInventoryWithEquipment.getInventorySize());
        try {
            testInventoryWithEquipment.removeEquipment(-1);
            fail("Uncaught InvalidEquipmentException");
        } catch (InvalidEquipmentException e) {
            // expected
        }
    }

    @Test
    void testGetEquipment() {
        try {
            testEquipment = testInventoryWithEquipment.getEquipment(0);
            assertEquals("testName", testEquipment.getName());
            assertEquals(8, testEquipment.getStrength());
            assertEquals(7, testEquipment.getEndurance());
            assertEquals(6, testEquipment.getDexterity());
            assertEquals(5, testEquipment.getSpeed());
            assertEquals(400, testEquipment.getWorth());
        } catch (InvalidEquipmentException e) {
            fail("Unexpected InvalidEquipmentException.");
        }

    }

    @Test
    void testGetEquipmentNumberLargerThanSize() {
        try {
            testEquipment = testInventoryWithEquipment.getEquipment(1);
            fail("Uncaught InvalidEquipmentException");
        } catch (InvalidEquipmentException e) {
            // expected
        }
    }

    @Test
    void testGetEquipmentNegativeNumber() {
        try {
            testEquipment = testInventoryWithEquipment.getEquipment(-1);
            fail("Uncaught InvalidEquipmentException");
        } catch (InvalidEquipmentException e) {
            // expected
        }
    }

    @Test
    void testGetStats() {
        testInventory.addEquipment(testEquipment);
        testInventory.addEquipment(testEquipment);
        testInventory.addEquipment(testEquipment);

        assertEquals(24, testInventory.getTotalStrength());
        assertEquals(21, testInventory.getTotalEndurance());
        assertEquals(18, testInventory.getTotalDexterity());
        assertEquals(15, testInventory.getTotalSpeed());
    }

    @Test
    void testAddEquipment() {
        assertEquals(0, testInventory.getInventorySize());
        testInventory.addEquipment(testEquipment);
        assertEquals(1, testInventory.getInventorySize());

        Equipment addedEquipment;
        try {
            addedEquipment = testInventory.getEquipment(0);
            assertEquals("testName", addedEquipment.getName());
            assertEquals(8, addedEquipment.getStrength());
            assertEquals(7, addedEquipment.getEndurance());
            assertEquals(6, addedEquipment.getDexterity());
            assertEquals(5, addedEquipment.getSpeed());
            assertEquals(400, addedEquipment.getWorth());
        } catch (InvalidEquipmentException e) {
            fail("Unexpected InvalidEquipmentException.");
        }
    }


}