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
                + "\nFinally, just one last question before we can get you started!", race.toUpperCase());

        return new Character("testname", "testrace", "testclass",
                10, 10, 10, 10, 10);

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
}
