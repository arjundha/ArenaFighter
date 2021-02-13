package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import player.Character;

import static org.junit.jupiter.api.Assertions.*;

public class CharacterTest {
    private Character testWarrior;
    private Character testRogue;
    private Character testMerchant;
    private Equipment testEquipment;

    @BeforeEach
    void runBefore() {
        testWarrior = new Character("test warrior", "human", "warrior", 10, 11, 12, 13, 14);
        testRogue = new Character("test rogue", "human", "rogue", 10, 11, 12, 13, 14);
        testMerchant = new Character("test merchant", "human", "merchant", 10, 11, 12, 13, 14);
        testEquipment = new Equipment("testName", 8, 7, 6, 5, 400);

    }

    @Test
    void testConstructorWarriorAndRogue() {  // These do not get constructed differently
        assertEquals("test warrior", testWarrior.getName());
        assertEquals("human", testWarrior.getRace());
        assertEquals("warrior", testWarrior.getClassName());
        assertEquals(10, testWarrior.getCurrentHealth());
        assertEquals(10, testWarrior.getMaxHealth());
        assertEquals(11, testWarrior.getStrength());
        assertEquals(12, testWarrior.getEndurance());
        assertEquals(13, testWarrior.getDexterity());
        assertEquals(14, testWarrior.getSpeed());
        assertEquals(Character.STARTING_LEVEL, testWarrior.getLevel());
        assertEquals(Character.STARTING_GOLD, testWarrior.getGold());
        assertEquals(0, testWarrior.getInventory().getInventorySize());
    }

    @Test
    void testConstructorMerchant() {
        assertEquals("test merchant", testMerchant.getName());
        assertEquals("human", testMerchant.getRace());
        assertEquals("merchant", testMerchant.getClassName());
        assertEquals(10, testMerchant.getCurrentHealth());
        assertEquals(10, testMerchant.getMaxHealth());
        assertEquals(11, testMerchant.getStrength());
        assertEquals(12, testMerchant.getEndurance());
        assertEquals(13, testMerchant.getDexterity());
        assertEquals(14, testMerchant.getSpeed());
        assertEquals(Character.STARTING_LEVEL, testMerchant.getLevel());
        assertEquals(Character.STARTING_GOLD * 2, testMerchant.getGold());
        assertEquals(0, testMerchant.getInventory().getInventorySize());
    }

    @Test
    void testLevelUpNonMerchant() {
        testWarrior.levelUp();
        assertEquals(2, testWarrior.getLevel());
        assertEquals(Character.STARTING_GOLD * 2, testWarrior.getGold());
    }

    @Test
    void testLevelUpMerchant() {
        testMerchant.levelUp();
        assertEquals(2, testMerchant.getLevel());
        assertEquals(Character.STARTING_GOLD * 4, testMerchant.getGold());
    }

    @Test
    void testEquipItem() {
        assertEquals(0, testRogue.getInventory().getInventorySize());
        testRogue.equipItem(testEquipment);
        assertEquals(1, testRogue.getInventory().getInventorySize());
    }

    @Test
    void spendGoldNoGoldSpent() {
        testRogue.spendGold(0);
        assertEquals(Character.STARTING_GOLD, testRogue.getGold());
    }

    @Test
    void spendGoldSpendSomeGold() {
        testRogue.spendGold(Character.STARTING_GOLD / 2);
        assertEquals(Character.STARTING_GOLD - Character.STARTING_GOLD / 2, testRogue.getGold());
    }

    @Test
    void spendAllGold() {
        testRogue.spendGold(Character.STARTING_GOLD);
        assertEquals(0, testRogue.getGold());
    }

    @Test
    void testTakeDamageTakeNoDamage() {
        assertEquals(10, testWarrior.getCurrentHealth());
        testWarrior.takeDamage(0);
        assertEquals(10, testWarrior.getCurrentHealth());
    }

    @Test
    void testTakeDamageSomeDamageAndLive() {
        assertEquals(10, testWarrior.getCurrentHealth());
        testWarrior.takeDamage(5);
        assertEquals(5, testWarrior.getCurrentHealth());
        assertTrue(testWarrior.isAlive());
    }

    @Test
    void testTakeDamageAndDie() {
        assertEquals(10, testWarrior.getCurrentHealth());
        testWarrior.takeDamage(10);
        assertEquals(0, testWarrior.getCurrentHealth());
        assertFalse(testWarrior.isAlive());
    }

    @Test
    void testTakeDamageOverkill() {
        assertEquals(10, testWarrior.getCurrentHealth());
        testWarrior.takeDamage(11);
        assertEquals(-1, testWarrior.getCurrentHealth());
        assertFalse(testWarrior.isAlive());
    }

    @Test
    void testIsAliveWhenAlive() {
        assertEquals(10, testWarrior.getCurrentHealth());
        assertTrue(testWarrior.isAlive());
    }

    @Test
    void testIsAliveWhenDead() {
        assertEquals(10, testWarrior.getCurrentHealth());
        testWarrior.takeDamage(11);
        assertFalse(testWarrior.isAlive());
    }

    @Test
    void testHealCharacterAlreadyFullHealth() {
        assertEquals(10, testWarrior.getCurrentHealth());
        assertEquals(10, testWarrior.getMaxHealth());
        testWarrior.healCharacter(10);
        assertEquals(10, testWarrior.getCurrentHealth());
        assertEquals(10, testWarrior.getMaxHealth());
    }

    @Test
    void testHealCharacterExactAmount() {
        assertEquals(10, testWarrior.getCurrentHealth());
        assertEquals(10, testWarrior.getMaxHealth());
        testWarrior.takeDamage(5);
        assertEquals(5, testWarrior.getCurrentHealth());
        assertEquals(10, testWarrior.getMaxHealth());

        testWarrior.healCharacter(5);
        assertEquals(10, testWarrior.getCurrentHealth());
        assertEquals(10, testWarrior.getMaxHealth());
    }

    @Test
    void testHealCharacterOverHeal() {
        assertEquals(10, testWarrior.getCurrentHealth());
        assertEquals(10, testWarrior.getMaxHealth());
        testWarrior.takeDamage(5);
        assertEquals(5, testWarrior.getCurrentHealth());
        assertEquals(10, testWarrior.getMaxHealth());

        testWarrior.healCharacter(6);
        assertEquals(10, testWarrior.getCurrentHealth());
        assertEquals(10, testWarrior.getMaxHealth());
    }

    @Test
    void testHealCharacterNotEnoughToFullyHeal() {
        assertEquals(10, testWarrior.getCurrentHealth());
        assertEquals(10, testWarrior.getMaxHealth());
        testWarrior.takeDamage(5);
        assertEquals(5, testWarrior.getCurrentHealth());
        assertEquals(10, testWarrior.getMaxHealth());

        testWarrior.healCharacter(4);
        assertEquals(9, testWarrior.getCurrentHealth());
        assertEquals(10, testWarrior.getMaxHealth());
    }

    @Test
    void testHealCharacterNoHeal() {
        assertEquals(10, testWarrior.getCurrentHealth());
        assertEquals(10, testWarrior.getMaxHealth());
        testWarrior.takeDamage(5);
        assertEquals(5, testWarrior.getCurrentHealth());
        assertEquals(10, testWarrior.getMaxHealth());

        testWarrior.healCharacter(0);
        assertEquals(5, testWarrior.getCurrentHealth());
        assertEquals(10, testWarrior.getMaxHealth());
    }

    @Test
    void testIncreaseStatsWarrior() {
        assertEquals(10, testWarrior.getCurrentHealth());
        assertEquals(10, testWarrior.getMaxHealth());
        assertEquals(11, testWarrior.getStrength());
        assertEquals(12, testWarrior.getEndurance());
        assertEquals(13, testWarrior.getDexterity());
        assertEquals(14, testWarrior.getSpeed());

        testWarrior.increaseStats(testWarrior.getClassName());

        assertEquals(10, testWarrior.getCurrentHealth());
        assertEquals(10 + Character.WARRIOR_HEALTH_INCREASE, testWarrior.getMaxHealth());
        assertEquals(11 + Character.FAVOURABLE_STAT_INCREASE, testWarrior.getStrength());
        assertEquals(12 + Character.FAVOURABLE_STAT_INCREASE, testWarrior.getEndurance());
        assertEquals(13 + Character.UNFAVOURABLE_STAT_INCREASE, testWarrior.getDexterity());
        assertEquals(14 + Character.UNFAVOURABLE_STAT_INCREASE, testWarrior.getSpeed());
    }

    @Test
    void testIncreaseStatsRogue() {
        assertEquals(10, testRogue.getCurrentHealth());
        assertEquals(10, testRogue.getMaxHealth());
        assertEquals(11, testRogue.getStrength());
        assertEquals(12, testRogue.getEndurance());
        assertEquals(13, testRogue.getDexterity());
        assertEquals(14, testRogue.getSpeed());

        testRogue.increaseStats(testRogue.getClassName());

        assertEquals(10, testRogue.getCurrentHealth());
        assertEquals(10 + Character.ROGUE_HEALTH_INCREASE, testRogue.getMaxHealth());
        assertEquals(11 + Character.UNFAVOURABLE_STAT_INCREASE, testRogue.getStrength());
        assertEquals(12 + Character.UNFAVOURABLE_STAT_INCREASE, testRogue.getEndurance());
        assertEquals(13 + Character.ROGUE_DEX_STAT_BONUS, testRogue.getDexterity());
        assertEquals(14 + Character.FAVOURABLE_STAT_INCREASE, testRogue.getSpeed());
    }

    @Test
    void testIncreaseStatsMerchant() {
        assertEquals(10, testMerchant.getCurrentHealth());
        assertEquals(10, testMerchant.getMaxHealth());
        assertEquals(11, testMerchant.getStrength());
        assertEquals(12, testMerchant.getEndurance());
        assertEquals(13, testMerchant.getDexterity());
        assertEquals(14, testMerchant.getSpeed());

        testMerchant.increaseStats(testMerchant.getClassName());

        assertEquals(10, testMerchant.getCurrentHealth());
        assertEquals(10 + Character.MERCHANT_HEALTH_INCREASE, testMerchant.getMaxHealth());
        assertEquals(11 + Character.UNFAVOURABLE_STAT_INCREASE, testMerchant.getStrength());
        assertEquals(12 + Character.UNFAVOURABLE_STAT_INCREASE, testMerchant.getEndurance());
        assertEquals(13 + Character.UNFAVOURABLE_STAT_INCREASE, testMerchant.getDexterity());
        assertEquals(14 + Character.UNFAVOURABLE_STAT_INCREASE, testMerchant.getSpeed());
    }
}
