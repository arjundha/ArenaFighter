package ui;

import exceptions.InvalidEquipmentException;
import model.Equipment;
import model.Inventory;
import org.json.JSONException;
import persistence.JsonReader;
import persistence.JsonWriter;
import player.Character;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

/**
 * The ConsoleGame class is the main handler of gameplay. It requires no parameters, and once a new ConsoleGame is
 * instantiated it will begin a new game in the console with no GUI.
 * This class contains methods for printing menus, printing information, handling shopping,
 * handling combat and also for handling user input.
 *
 * @author Arjun
 */
public class ConsoleGame {
    private Scanner input;
    private static final String JSON_SAVE = "./data/save.json";
    private final JsonWriter jsonWriter;
    private final JsonReader jsonReader;
    private Character player;

    private static final int NUMBER_OF_LEVELS = 5; // This represents the number of levels in the game, it ends when the
    // player level exceeds this number.

    private static final int NUMBER_OF_EQUIPMENT_PAIRS_SOLD = 4;  // Number represents the number of weapons and armour
    // sold divided by two (it controls the while loop
    // in generateStore

    // This number is used to determine the number that randomInteger is allowed to reach when generating equipment.
    // If more equipment is added to that function, this number needs to change.
    private static final int RANDOM_ITEM_SELECTOR = 12;
    private static final int PRICE_OF_SMALL_TRAINING = 30;  // Price of a small training room in gold
    private static final int PRICE_OF_LARGE_TRAINING = 50;  // Price of a large training room in gold
    private static final int PRICE_OF_SMALL_POTION = 10;  // Price of a small potion
    private static final int PRICE_OF_LARGE_POTION = 20;  // Price of a large potion
    private static final int HEALTH_FROM_SMALL_POTION = 10;  // Amount of health healed from a small potion
    private static final int HEALTH_FROM_LARGE_POTION = 25;  // Amount of health healed from a large potion
    private static final int DEXTERITY_ROLL = 60; // Used for determining if an attack will miss (DEX > RANDOM_INT)

    // EFFECTS: runs the game application
    public ConsoleGame() throws FileNotFoundException {
        jsonWriter = new JsonWriter(JSON_SAVE);  // we need to check right away if the save file exists
        jsonReader = new JsonReader(JSON_SAVE);
        setUpGame();
    }

    // EFFECTS: Controls the main menu of the game, allowing players to load an old game or start new.
    private void setUpGame() {
        System.out.println("\r  ___                        ______ _       _     _            \n"
                + " / _ \\                       |  ___(_)     | |   | |           \n"
                + "/ /_\\ \\_ __ ___ _ __   __ _  | |_   _  __ _| |__ | |_ ___ _ __ \n"
                + "|  _  | '__/ _ | '_ \\ / _` | |  _| | |/ _` | '_ \\| __/ _ | '__|\n"
                + "| | | | | |  __| | | | (_| | | |   | | (_| | | | | ||  __| |   \n"
                + "\\_| |_|_|  \\___|_| |_|\\__,_| \\_|   |_|\\__, |_| |_|\\__\\___|_|   \n"
                + "                                       __/ |                   \n"
                + "                                      |___/                    ");

        input = new Scanner(System.in);
        boolean sentinel = true;
        while (sentinel) {
            printSetUpMenu();
            String command = input.nextLine().trim();
            if (command.equals("3")) {
                sentinel = false;
            } else {
                handleSetUpSelection(command);
            }
        }
        System.out.println("\nClosing the game.");
    }

    // EFFECTS: Print the main menu options for the player
    private void printSetUpMenu() {
        System.out.println("\nMENU");
        System.out.println("1) New Game - this may overwrite any saved data you have.");
        System.out.println("2) Load Game - continue from where you left off.");
        System.out.println("3) Quit - close the application.");
    }

    // MODIFIES: this
    // EFFECTS: Handles the input from the player and directs to the corresponding function
    private void handleSetUpSelection(String selection) {
        switch (selection) {
            case "1":
                player = createCharacter(); // Start a new character
                runGame();
                break;

            case "2":
                loadCharacter(); // Begin the game with your loaded character

                if (Objects.isNull(player)) { // We don't want to continue if we failed to load the game
                    break;
                }

                runGame();
                break;

            default:
                System.out.println("\nPlease select a valid option.\n");
        }
    }

    // MODIFIES: this
    // EFFECTS: Handles and processes user input to initialize a game with Character
    private void runGame() {
        boolean alive = true;  // sentinel value for if the player is alive
        boolean quit = false;  // A check to see if the player chose to quit the game
        input = new Scanner(System.in);

        while (alive && player.getLevel() <= NUMBER_OF_LEVELS) { // If the character is dead, we want to end the loop
            printMenu();
            String command = input.nextLine().trim();
            if (command.equals("7")) {
                saveCharacter();
            } else if (command.equals("8")) {
                quit = saveBeforeQuit();
                break; // lets get out of this while loop!
            } else {
                handleMenuSelection(player, command);
            }
            alive = player.isAlive();  // Check if we should quit the loop
        }
        if (quit) {
            System.out.println("\nReturning to main menu.");
        } else {
            gameOver();
        }
    }

    // EFFECTS: Prompts the player to save the game before they quit
    private boolean saveBeforeQuit() {
        input = new Scanner(System.in);
        while (true) {
            System.out.println("Do you want to save before you quit?");
            System.out.println("\t1) Yes");
            System.out.println("\t2) No");
            String command = input.nextLine().trim();
            if (command.equals("1")) {
                saveCharacter();
                return true;
            } else if (command.equals("2")) {
                return true;
            } else {
                System.out.println("Please choose a valid option");
            }
        }
    }

    // EFFECTS: Handles the end of the game, and checks to see if the player one or lost
    private void gameOver() {
        if (player.isAlive()) {  // If the while loop ended and the player is alive
            System.out.println("\nYou are now the champion of the arena!");
        } else {
            System.out.println("\nYou have died.");
        }
        System.out.println("\nThank you for playing!");
    }

    // EFFECTS: Generate a character based on user input
    private Character createCharacter() {
        input = new Scanner(System.in);

        System.out.println("\nAh, hello newcomer! You must be here to register in the arena. \nBefore you can get "
                + "started, I just have to fill out some paperwork.\nWhat is your name?");
        String name = input.nextLine().trim();
        System.out.printf("\nLovely to meet you, %s! Now... what is your race?", name);

        String race = chooseRace();
        System.out.printf("\n%s? I could tell, I just didn't want to assume anything. "
                        + "\nFinally, just one last question before we can get you started!",
                race.substring(0, 1).toUpperCase() + race.substring(1));

        String playerClass = chooseClass();
        System.out.printf("\nA %s %s, haven't seen one of those before! Well, good luck in there!\n",
                playerClass, race);

        if (race.equals("human")) {
            return new Character(name, race, playerClass, 10, 10, 10, 10, 10);

        } else if (race.equals("dwarf")) {
            return new Character(name, race, playerClass, 12, 12, 12, 7, 7);

        } else {
            return new Character(name, race, playerClass, 8, 8, 8, 14, 12);
        }
    }

    // EFFECTS: Produce a race based on user input
    private String chooseRace() {
        input = new Scanner(System.in);
        while (true) {
            System.out.println("\nAre you a...");
            System.out.println("\t1) Human - a jack of all trades, but a master of none.");
            System.out.println("\t2) Dwarf - strong and durable.");
            System.out.println("\t3) Elf - quick and nimble.");
            String selection = input.nextLine().trim();  // User should respond with 1, 2 or 3

            switch (selection) {  // This switch handles the user input according to the options above
                case "1":
                    return "human";  // If they input 1, they become a human

                case "2":
                    return "dwarf";  // 2 is a dwarf

                case "3":
                    return "elf";  // 3 is an elf

                default:
                    System.out.println("\nI'm not sure I heard you correctly, just tell me if you are 1, 2 or 3.");
                    break;  // We continue until they select a viable option!
            }
        }
    }

    // EFFECTS: Produce a class based on user input
    private String chooseClass() {
        input = new Scanner(System.in);
        while (true) {
            System.out.println("\nWhat class are you?");

            System.out.println("\t1) Warrior - these brutes gain the most HP, Strength and endurance.");
            System.out.println("\t2) Rogue - as masters of stealth, "
                    + "rogues have the highest dexterity and speed growth.");
            System.out.println("\t3) Merchant - while they may be generally weak in ever regard, "
                    + "merchants start with the most gold, and also gain more gold than other classes.");

            String selection = input.nextLine().trim();  // User should respond with 1, 2 or 3

            switch (selection) {  // This switch handles the user input according to the options above
                case "1":
                    return "warrior";  // If they input 1, they become a warrior

                case "2":
                    return "rogue";  // 2 is a rogue

                case "3":
                    return "merchant";  // 3 is a merchant

                default:
                    System.out.println("\nI'm not sure I heard you correctly, just tell me if you are 1, 2 or 3.");
                    break;  // We continue until they select a viable option!
            }
        }
    }

    // EFFECTS: Displays menu options to the player
    private void printMenu() {
        System.out.println("\n\nLooks like there's still some time before my next fight, what should I do?");
        System.out.println("\t1) Shop - spend my gold for better weapons and armour at a nearby store.");
        System.out.println("\t2) Train - rent a training space to increase my stats by a little.");
        System.out.println("\t3) Heal - buy a health potion to heal my wounds.");
        System.out.println("\t4) View my current stats.");
        System.out.println("\t5) View my current inventory.");
        System.out.println("\t6) Fight!");
        System.out.println("\t7) Save Game");
        System.out.println("\t8) Quit");
    }

    // EFFECTS: Handles the input from the player and directs to the corresponding function
    private void handleMenuSelection(Character player, String selection) {
        switch (selection) {
            case "1":
                shop(player);
                break;

            case "2":
                train(player);
                break;

            case "3":
                heal(player);
                break;

            case "4":
                printCharacter(player);
                break;

            case "5":
                printInventory(player.getInventory());
                break;

            case "6":
                fight(player);
                break;

            default:
                System.out.println("\nI don't think I want to do that. Let's think again...\n");
        }
    }

    // REQUIRES: User must input an integer when asked for a selection
    // EFFECTS: Allow users to spend gold and add items to their inventory
    private void shop(Character player) {
        input = new Scanner(System.in);
        System.out.println("\nWelcome! Here to buy some equipment?");
        Inventory shopInventory = generateShop();
        boolean sentinel = true;

        while (sentinel) {
            System.out.println("\nWhat would you like to buy? (or press \"b\" to go back)");
            System.out.printf("(You currently have %d gold)\n", player.getGold());
            printInventory(shopInventory);
            System.out.println("\n");
            String selection = input.nextLine().trim();  // User response based on item

            if (selection.equals("b")) {
                System.out.println("\nI think I've shopped enough for now.");
                sentinel = false;  // Back out of the loop and to the main menu

            } else if (!isInteger(selection)) {
                System.out.println("\nPlease select a valid option.");

            } else {
                try {
                    buyItem(player, shopInventory, Integer.parseInt(selection));
                } catch (InvalidEquipmentException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // MODIFIES: Inventory and Character
    // EFFECTS: Remove an item from a shop inventory and place it in a Character inventory after spending gold
    private void buyItem(Character character, Inventory shop, int item) throws InvalidEquipmentException {
        if (shop.getInventorySize() == 0) {  // Check if the inventory is empty
            System.out.println("\nSorry, I am sold out!");

        } else if (0 < item && item <= shop.getInventorySize()) {  // Check if the user input is within the options
            int index = item - 1;  // get the index of the element we want
            Equipment equipment = shop.getEquipment(index);
            int cost = equipment.getWorth();  // How much does it cost?

            if (character.getGold() >= cost) {  // Can afford it
                character.equipItem(equipment);  // Add the equipment to your inventory
                character.spendGold(cost);  // Spend the gold needed to buy the item
                shop.removeEquipment(index);  // Remove the equipment from the store
                System.out.printf("You buy the %s.", equipment.getName());  // Informative message

            } else {  // Cant afford it
                System.out.println("\nAh, it appears as if you do not have enough gold for that, sorry.");
            }

        } else {
            System.out.println("\nI don't think I have that in stock...");
        }
    }

    // EFFECTS: Produce a randomized inventory to be sold in a shop
    private Inventory generateShop() {
        System.out.println("\nAh now, let me see what we have today...\n");
        int count = 0;
        Inventory shopInventory = new Inventory();

        while (count < NUMBER_OF_EQUIPMENT_PAIRS_SOLD) {  // We want the shop to only contain a certain number of items
            shopInventory.addEquipment(generateWeapon(generateRandomInteger(RANDOM_ITEM_SELECTOR)));
            shopInventory.addEquipment(generateArmour(generateRandomInteger(RANDOM_ITEM_SELECTOR)));
            count += 1;
        }
        return shopInventory;
    }

    // REQUIRES: itemNumber must be within [0, RANDOM_ITEM_SELECTOR]
    // EFFECTS: produce an equipment (stylized as a weapon) based on the number received
    private Equipment generateWeapon(int itemNumber) {
        if (itemNumber == 0 || itemNumber == 1 || itemNumber == 2) {
            return new Equipment("iron dagger", 1, 0, 1, 0, 25);

        } else if (itemNumber == 3 || itemNumber == 4 || itemNumber == 5) {
            return new Equipment("iron sword", 2, 0, 0, 0, 25);

        } else if (itemNumber == 6 || itemNumber == 7) {
            return new Equipment("swift bow", 1, 0, 2, 2, 50);

        } else if (itemNumber == 8 || itemNumber == 9) {
            return new Equipment("dark blade", 3, 0, 2, 0, 50);

        } else if (itemNumber == 10) {
            return new Equipment("demonic dagger", 4, 0, 6, 5, 100);

        } else if (itemNumber == 11) {
            return new Equipment("holy longsword", 11, 0, 4, 0, 100);

        } else {
            return new Equipment("weapon of champions", 10, 10, 10, 10, 200);

        }
    }

    // REQUIRES: itemNumber must be within [0, RANDOM_ITEM_SELECTOR]
    // EFFECTS: produce an equipment (stylized as armour) based on the number received
    private Equipment generateArmour(int itemNumber) {
        if (itemNumber == 0 || itemNumber == 1 || itemNumber == 2) {
            return new Equipment("wooden shield", 0, 2, 0, 0, 25);

        } else if (itemNumber == 3 || itemNumber == 4 || itemNumber == 5) {
            return new Equipment("leather boots", 0, 0, 0, 2, 25);

        } else if (itemNumber == 6 || itemNumber == 7) {
            return new Equipment("armour of strength", 2, 3, 0, 0, 50);

        } else if (itemNumber == 8 || itemNumber == 9) {
            return new Equipment("tunic of speed", 0, 1, 1, 3, 50);

        } else if (itemNumber == 10) {
            return new Equipment("godly armour", 2, 10, 1, 1, 100);

        } else if (itemNumber == 11) {
            return new Equipment("token of true sight", 1, 1, 12, 1, 100);

        } else {
            return new Equipment("amulet of champions", 10, 10, 10, 10, 200);

        }
    }

    // EFFECTS: Provides core UI for training a character and receiving user input
    private void train(Character player) {
        input = new Scanner(System.in);
        System.out.println("\nIt looks like I can spend some gold to train my stats!");

        boolean sentinel = true;

        while (sentinel) {
            System.out.println("\nWhat should I do...? (or press \"b\" to go back)");
            System.out.printf("(You currently have %d gold)\n", player.getGold());
            System.out.printf("\t1) Rent a small training room (%dg).\n", PRICE_OF_SMALL_TRAINING);
            System.out.printf("\t2) Rent a large training room (%dg).\n", PRICE_OF_LARGE_TRAINING);
            String selection = input.nextLine().trim();  // User response

            if (selection.equals("b")) {
                System.out.println("\nMaybe I'll train more later.");
                sentinel = false;  // Back out of the loop and to the main menu

            } else {
                trainCharacter(player, selection);
            }
        }
    }

    // MODIFIES: Character (May increase the stats of a character and reduces their gold)
    // EFFECTS: Spend gold to increase the stats of a player character
    private void trainCharacter(Character player, String selection) {
        int gold = player.getGold();
        String playerClass = player.getClassName();

        if (selection.equals("1")) {  // Small training (ONLY TRAINS ONCE)
            if (gold >= PRICE_OF_SMALL_TRAINING) {
                player.spendGold(PRICE_OF_SMALL_TRAINING);
                player.increaseStats(playerClass);
                System.out.println("\nGreat! I feel a bit stronger!");

            } else {
                System.out.println("\nHmm I don't think I can afford this right now.");
            }

        } else if (selection.equals("2")) {  // Large training (TRAINS TWICE)
            if (gold >= PRICE_OF_LARGE_TRAINING) {
                player.spendGold(PRICE_OF_LARGE_TRAINING);
                player.increaseStats(playerClass);
                player.increaseStats(playerClass);  // Bonus training due to large training
                System.out.println("\nAmazing! I feel a lot stronger!");

            } else {
                System.out.println("\nHmm I don't think I can afford this right now.");
            }

        } else {
            System.out.println("\nI don't think I can do that.");
        }
    }

    // EFFECTS: Generate UI to guide players through selecting potions to buy
    private void heal(Character player) {
        input = new Scanner(System.in);
        System.out.println("\nIt might be a good idea to drink a potion and heal before my fight.");

        boolean sentinel = true;

        while (sentinel) {
            System.out.println("\nThere seems to be a variety of potions, which should I buy? "
                    + "(or press \"b\" to go back)");
            System.out.printf("(You currently have %d gold and your HP is %d/%d)\n",
                    player.getGold(), player.getCurrentHealth(), player.getMaxHealth());

            System.out.printf("\t1) Drink a small potion (%dg).\n", PRICE_OF_SMALL_POTION);
            System.out.printf("\t2) Drink a large potion (%dg).\n", PRICE_OF_LARGE_POTION);

            String selection = input.nextLine().trim();  // User response


            if (selection.equals("b")) {
                System.out.println("\nPerhaps I'll try a potion later.");
                sentinel = false;  // Back out of the loop and to the main menu

            } else {
                healCharacter(player, selection);
            }

        }

    }

    // MODIFIES: Character (May increase the current health of a character and reduces their gold)
    // EFFECTS: Spend gold to increase the current health of a player character
    private void healCharacter(Character player, String selection) {
        int gold = player.getGold();

        if (selection.equals("1")) {  // Small potion
            if (gold >= PRICE_OF_SMALL_POTION) {  // can you afford it?
                player.spendGold(PRICE_OF_SMALL_POTION);  // Charge the player gold
                player.healCharacter(HEALTH_FROM_SMALL_POTION);
                System.out.println("\nGreat! I feel way better!");
                System.out.printf("(HP: %d/%d)\n", player.getCurrentHealth(), player.getMaxHealth());


            } else {
                System.out.println("\nHmm I don't think I can afford this right now.");
            }

        } else if (selection.equals("2")) {  // Large potion heals more but costs more
            if (gold >= PRICE_OF_LARGE_POTION) {
                player.spendGold(PRICE_OF_LARGE_POTION);
                player.healCharacter(HEALTH_FROM_LARGE_POTION);
                System.out.println("\nAmazing! I feel a lot healthier!");
                System.out.printf("(HP: %d/%d)\n", player.getCurrentHealth(), player.getMaxHealth());

            } else {
                System.out.println("\nHmm I don't think I can afford this right now.");
            }

        } else {
            System.out.println("\nI don't think I can do that.");
        }
    }

    // EFFECTS: Produce a round of combat.
    private void fight(Character player) {
        System.out.printf("\nAlright %s, let us see who you will be facing next...", player.getName());
        Character enemy = generateEnemy(player.getLevel());  // Generate an enemy
        printEnemy(enemy);  // Inform the player of who they are fighting

        if (player.getSpeed() > enemy.getSpeed()) {  // The faster character goes first
            combat(player, enemy);
        } else {
            combat(enemy, player);
        }

        System.out.println("\nThe round is over.");

        if (player.isAlive()) {  // If the player was the winner, level them up and increase their stats
            levelUpPlayer(player);
        }
    }

    // EFFECTS: Produce an enemy corresponding to a players level
    private Character generateEnemy(int level) {
        // These are enemies that correspond to a player level
        if (level == 1) {
            return new Character("Goblin Knight", "goblin", "knight",
                    10, 10, 10, 10, 10);

        } else if (level == 2) {
            return new Character("Huntress", "elf", "hunter",
                    20, 8, 6, 30, 20);

        } else if (level == 3) {
            return new Character("Lich King", "lich", "wizard",
                    20, 40, 10, 40, 10);

        } else if (level == 4) {
            return new Character("Shadow Assassin", "???", "assassin",
                    30, 40, 10, 40, 40);

        } else {
            return new Character("Champion of the Arena", "tiefling", "champion",
                    80, 20, 80, 30, 10);
        }
    }

    // EFFECTS: Handles turn based combat
    private void combat(Character player1, Character player2) {
        input = new Scanner(System.in);
        int playerOneHealth = player1.getCurrentHealth();  // Sentinel values for player health
        int playerTwoHealth = player2.getCurrentHealth();

        while (playerOneHealth > 0 && playerTwoHealth > 0) {  // End the while loop if either player is dead
            System.out.println("\n(press enter to continue)");
            String wait = input.nextLine();  // User response to continue the game (prevents wall of text)
            attack(player1, player2);  // faster player attacks first
            playerTwoHealth = player2.getCurrentHealth();

            if (playerTwoHealth <= 0) {  // This if statement is needed to prevent bugs
                break;
            }

            System.out.println("\n(press enter to continue)");
            String wait2 = input.nextLine();  // User response to continue the game (prevents wall of text)
            attack(player2, player1);
            playerOneHealth = player1.getCurrentHealth();
        }
    }

    // MODIFIES: Character defender by potentially lowering their current HP
    // EFFECTS: Allow attackers to damage a defender or miss if they have low dexterity
    private void attack(Character attacker, Character defender) {
        int chanceToHit = generateRandomInteger(DEXTERITY_ROLL);  // Produce a random number. If DEX > this number
        // then the attack will hit, or else it misses.

        if (attacker.getDexterity() >= chanceToHit) {  // If attacker DEX is greater than the random number, it hits
            System.out.printf("\n%s hits %s.", attacker.getName(), defender.getName());
            int damage = attacker.getStrength() - defender.getEndurance() / 2;  // Calculate damage (END mitigates some)

            if (damage <= 0) {  // Always deal at least 1 damage if an attack hits
                defender.takeDamage(1);
            } else {
                defender.takeDamage(damage);  // If damage is > 0, deal the full number
            }

            battleMessage(attacker.getName(), defender.getName(), defender.getCurrentHealth());  // Print results

        } else {
            System.out.printf("\n%s missed.", attacker.getName());  // Let the player know the attack missed.
        }
    }

    // EFFECTS: Print an informative message of the results of a combat attack.
    private void battleMessage(String attacker, String defender, int defenderHealth) {
        if (defenderHealth <= 0) {
            System.out.printf("\n%s has slain %s.", attacker, defender);
        } else {
            System.out.printf("\n%s is still alive, with %d health remaining.", defender, defenderHealth);
        }
    }

    // MODIFIES: Character
    // EFFECTS: Increase a character's level by one, and increase their stats once with informative messages.
    private void levelUpPlayer(Character player) {
        System.out.println("\nYou won the fight! You feel a bit stronger!");
        player.levelUp();
        player.increaseStats(player.getClassName());
        System.out.printf("\nYou are now level %d. Your stats have increased, and you gained some gold for winning "
                + "the fight.", player.getLevel());
    }

    // EFFECTS: Print out detailed information of an arena enemy.
    private void printEnemy(Character enemy) {
        System.out.println("\nYour opponent is...");
        System.out.printf("\n%s the %s %s!", enemy.getName(), enemy.getRace(), enemy.getClassName());
    }

    // EFFECTS: Print out detailed information for each equipment in an inventory
    private void printInventory(Inventory inventory) {
        if (inventory.getInventorySize() == 0) {
            System.out.println("\nMy inventory is empty.");
        } else {
            for (int i = 0; i < inventory.getInventorySize(); i++) {  // Print out each Equipment in an Inventory
                Equipment item;
                try {
                    item = inventory.getEquipment(i);
                    System.out.printf("\n%d. %s: "
                                    + "\nStrength - %d   Endurance - %d   Dexterity - %d   Speed - %d   VALUE: %d\n",
                            i + 1,
                            item.getName().substring(0, 1).toUpperCase() + item.getName().substring(1), // capitalize
                            item.getStrength(),
                            item.getEndurance(),
                            item.getDexterity(),
                            item.getSpeed(),
                            item.getWorth());
                } catch (InvalidEquipmentException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    // EFFECTS: Print out all the stats of a character except for inventory
    private void printCharacter(Character player) {
        System.out.printf("\n\nName: %s\nRace: %s\nClass: %s\n\nLEVEL: %s"
                        + "\nHP: %d/%d\nSTR: %d\nEND: %d\nDEX: %d\nSPD: %d\n\nGOLD: %d\n",
                player.getName(),
                player.getRace().substring(0, 1).toUpperCase() + player.getRace().substring(1), // Capitalize
                player.getClassName().substring(0, 1).toUpperCase() + player.getClassName().substring(1), // Capitalize
                player.getLevel(),
                player.getCurrentHealth(),
                player.getMaxHealth(),
                player.getStrength(),
                player.getEndurance(),
                player.getDexterity(),
                player.getSpeed(),
                player.getGold());
    }

    // REQUIRES: upperbound must be a positive integer represent the highest integer you want to possibly get
    // EFFECTS: Generates a random integer from [0, upperbound] inclusive
    private int generateRandomInteger(int upperbound) {
        Random random = new Random();  // Start a random class object
        return random.nextInt(upperbound + 1);  // Return a random int (the + 1 is needed to include upperbound)
    }

    // EFFECTS: Determine if a string is an integer
    public boolean isInteger(String input) {
        try {
            Integer.parseInt(input);  // Try to see if you can parse the string into an integer
            return true;

        } catch (Exception e) {  // Catch the error if it is not able to be parsed, and return false
            return false;
        }
    }

    // EFFECTS: saves the Character to the save file
    // CITATION: The base code can be found at https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    //           This method is implemented using the CPSC 210 JsonSerializationDemo as it's base code.
    private void saveCharacter() {
        try {
            jsonWriter.open();
            jsonWriter.write(player);
            jsonWriter.close();
            System.out.println("Saved your character " + player.getName() + " to " + JSON_SAVE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_SAVE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads Character from the save file
    // CITATION: The base code can be found at https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    //           This method is implemented using the CPSC 210 JsonSerializationDemo as it's base code.
    private void loadCharacter() {
        try {
            player = jsonReader.read();
            System.out.println("Loaded your character " + player.getName() + " from " + JSON_SAVE);

        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_SAVE);

        } catch (JSONException e) {
            System.out.println("Unable to read from file: " + JSON_SAVE + ". The data may be corrupted.");
        }
    }

    // EFFECTS: Starts a console game.
    public static void main(String[] args) {
        try {
            new ConsoleGame();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to run application: The application is missing the save file.");
        }
    }
}
