package ui;

import player.Character;

import java.util.Scanner;

/**
 *
 */
public class Game {
    private Scanner input;

    // EFFECTS: runs the game application
    public Game() {
        runGame();
    }

    // MODIFIES: this
    // EFFECTS: Handles and processes user input to initialize a game
    private void runGame() {
        Character player = createCharacter();  // Begin the game by creating a character
        System.out.println("Name: " + player.getName());
        System.out.println("Race: " + player.getRace());
        System.out.println("Class: " + player.getClassName());
        System.out.println("Level: " + player.getLevel());
        System.out.println("Current HP: " + player.getCurrentHealth());
        System.out.println("Max HP: " + player.getMaxHealth());
        System.out.println("Str: " + player.getStrength());
        System.out.println("End: " + player.getEndurance());
        System.out.println("Dex: " + player.getDexterity());
        System.out.println("Spd: " + player.getSpeed());
        System.out.println("Inv: " + player.getInventory());
        System.out.println("Gold: " + player.getGold());

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
}
