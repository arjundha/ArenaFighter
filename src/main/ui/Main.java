package ui;

import java.io.FileNotFoundException;

/**
 * Class used for starting up a new game. Run this file to begin the game.
 */

public class Main {
    public static void main(String[] args) {
        try {
            new Game();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to run application: The application is missing the save file.");
        }
    }
}
