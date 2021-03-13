package ui;

import org.json.JSONException;
import persistence.JsonReader;
import persistence.JsonWriter;
import player.Character;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * The Game class is the main handler of gameplay. It requires no parameters, and once a new Game is
 * instantiated it will begin a new game with GUI.
 * This class contains methods for printing menus, printing information, handling shopping,
 * handling combat and also for handling user input.
 *
 * CITATION:
 * To implement the Java Swing, I learned
 *
 * @author Arjun
 */
public class Game extends JFrame {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 700;
    private static final Color BACKGROUND = Color.DARK_GRAY;
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 50);
    private static final Font NORMAL_FONT = new Font("Arial", Font.PLAIN, 25);

    private static final String JSON_SAVE = "./data/save.json";
    private final JsonWriter jsonWriter;
    private final JsonReader jsonReader;
    private Character player;

    public Game() throws FileNotFoundException {
        super("Arena Fighter");
        jsonWriter = new JsonWriter(JSON_SAVE);  // we need to check right away if the save file exists
        jsonReader = new JsonReader(JSON_SAVE);
        initializeGraphics();
        startScreen();
    }

    // MODIFIES: this
    // EFFECTS:  draws the JFrame window where this Game will operate
    private void initializeGraphics() {
        setLayout(null);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(BACKGROUND);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void startScreen() {
        createTitle();
        createStartButtons();
    }

    private void createTitle() {
        JPanel titlePanel = new JPanel();
        titlePanel.setBounds(100, 100, 600, 100);
        titlePanel.setBackground(BACKGROUND);

        JLabel titleLabel = new JLabel("Arena Fighter");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(TITLE_FONT);

        titlePanel.add(titleLabel);
        getContentPane().add(titlePanel);
    }

    private void createStartButtons() {
        JPanel newGame = new JPanel();
        newGame.setBounds(100, 500, 600, 80);
        newGame.setBackground(BACKGROUND);

        JButton newGameButton = createMainMenuButton("NEW GAME");
        JButton loadGameButton = createMainMenuButton("LOAD GAME");

        newGame.add(newGameButton);
        newGame.add(Box.createHorizontalStrut(100));
        newGame.add(loadGameButton);
        getContentPane().add(newGame);
    }

    private JButton createMainMenuButton(String label) {
        JButton button = new JButton(label);
        button.setBackground(Color.white);
        button.setForeground(Color.black);
        button.setFont(NORMAL_FONT);
        button.setFocusPainted(false);

        return button;
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

    public static void main(String[] args) {
        try {
            new Game();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to run application: The application is missing the save file.");
        }
    }

}
