package ui;

import model.Equipment;
import model.Inventory;
import player.Character;

import java.util.Random;
import java.util.Scanner;

/**
 *
 */
public class Game {
    private Scanner input;

    private static final int NUMBER_OF_EQUIPMENT_PAIRS_SOLD = 4;  // Number represents the number of weapons and armour
                                                            // sold divided by two (it controls the while loop
                                                            // in generateStore

    // This number is used to determine the number that randomInteger is allowed to reach when generating equipment.
    // If more equipment is added to that function, this number needs to change.
    private static final int RANDOM_ITEM_SELECTOR = 12;

    private static final int PRICE_OF_SMALL_TRAINING = 30;  // Price of a small training room in gold
    private static final int PRICE_OF_LARGE_TRAINING = 50; // Price of a large training room in gold


    // EFFECTS: runs the game application
    public Game() {
        runGame();
    }

    // MODIFIES: this
    // EFFECTS: Handles and processes user input to initialize a game
    private void runGame() {
        Character player = createCharacter();  // Begin the game by creating a character
        boolean alive = true;
        input = new Scanner(System.in);

        while (alive) {
            printMenu();
            String command = input.nextLine().trim();
            handleMenuSelection(player, command);
            alive = !player.isDead();

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
                System.out.println("HEAL");
                break;

            case "4":
                printCharacter(player);
                break;

            case "5":
                printInventory(player.getInventory());
                break;

            case "6":
                System.out.println("FIGHT");
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
            } else {
                buyItem(player, shopInventory, Integer.parseInt(selection));
            }
        }
    }

    // MODIFIES: Inventory and Character
    // EFFECTS: Remove an item from a shop inventory and place it in a Character inventory after spending gold
    private void buyItem(Character character, Inventory shop, int item) {
        if (shop.inventorySize() == 0) {  // Check if the inventory is empty
            System.out.println("\nSorry, I am sold out!");

        } else if (0 < item && item <= shop.inventorySize()) {  // Check if the user input is within the options
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
            System.out.printf("\t1) Rent a small training room (%dg).", PRICE_OF_SMALL_TRAINING);
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


    // EFFECTS: Print out detailed information for each equipment in an inventory
    private void printInventory(Inventory inventory) {
        if (inventory.inventorySize() == 0) {
            System.out.println("\nMy inventory is empty.");
        } else {
            for (int i = 0; i < inventory.inventorySize(); i++) {
                Equipment item = inventory.getEquipment(i);
                System.out.printf("\n%d. %s: Strength - %d   Endurance - %d   Dexterity - %d   Speed - %d   VALUE: %d",
                        i + 1,
                        item.getName().substring(0, 1).toUpperCase() + item.getName().substring(1),  // Capitalize name
                        item.getStrength(),
                        item.getEndurance(),
                        item.getDexterity(),
                        item.getSpeed(),
                        item.getWorth());
            }
        }

    }


    // EFFECTS: Print out all the stats of a character except for inventory
    private void printCharacter(Character player) {
        System.out.printf("\n\nName: %s\nRace: %s\nClass: %s\n\nLEVEL: %s"
                        +  "\nHP: %d/%d\nSTR: %d\nEND: %d\nDEX: %d\nSPD: %d\n\nGOLD: %d\n",
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
}
