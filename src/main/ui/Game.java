package ui;

import org.json.JSONException;
import persistence.JsonReader;
import persistence.JsonWriter;
import player.Character;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

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
    private static final Font NORMAL_FONT = new Font("Arial", Font.PLAIN, 21);

    private JPanel mainArea;
    private JTextArea mainTextArea;

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
//        generateStartMenu();
    }

    private void createTitle() {
        JPanel titlePanel = new JPanel();
        titlePanel.setBounds(100, 100, 600, 100);
        titlePanel.setBackground(BACKGROUND);

        JLabel titleLabel = new JLabel("Arena Fighter");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(TITLE_FONT);

        titlePanel.add(titleLabel);
        generateStartMenu();
        getContentPane().add(titlePanel);
    }

    private void generateStartMenu() {
        JPanel newGame = new JPanel();
        newGame.setBounds(100, 500, 600, 80);
        newGame.setBackground(BACKGROUND);

        JButton newGameButton = createMenuButton();
        newGameButton.setText("NEW GAME");
        newGameButton.addActionListener(new StartNewGameHandler());
        JButton loadGameButton = createMenuButton();
        loadGameButton.setText("LOAD GAME");
        loadGameButton.addActionListener(new LoadGameHandler());

        newGame.add(newGameButton);
        newGame.add(Box.createHorizontalStrut(100));
        newGame.add(loadGameButton);
        getContentPane().add(newGame);
    }

    private JButton createMenuButton() {
        JButton button = new JButton();
        button.setBackground(Color.white);
        button.setForeground(Color.black);
        button.setFont(NORMAL_FONT);
        button.setFocusPainted(false);

        return button;
    }

    private class StartNewGameHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            player = createCharacter();
            runGame();
        }
    }

    private Character createCharacter() {
        return new Character("Arjun", "human", "merchant", 10, 10, 10,10, 10);
    }

    private class LoadGameHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            loadCharacter();
            runGame();
        }
    }

    private void runGame() {
        getContentPane().removeAll();
        getContentPane().repaint();
        generateHeader();
        generateGameScreen();
    }

    private void generateGameScreen() {
        mainArea = new JPanel();
        mainArea.setBounds(50, 50, 700, 300);
        mainArea.setBackground(Color.blue);
        generateTextArea();
        getContentPane().add(mainArea);
        generateMenu();

    }

    private void generateMenu() {
        JPanel menuArea = new JPanel();
        menuArea.setBounds(50, 360, 700, 290);
        menuArea.setLayout(new GridLayout(4, 2));
        fillMenu(menuArea);
        getContentPane().add(menuArea);
    }

    private void fillMenu(JPanel menuArea) {
        for (int i = 1; i <= 8; i++) {
            if (i == 1) {
                menuArea.add(shopButton());
            } else if (i == 2) {
                menuArea.add(trainButton());
            } else if (i == 3) {
                menuArea.add(healButton());
            } else if (i == 4) {
                menuArea.add(fightButton());
            } else if (i == 5) {
                menuArea.add(statsButton());
            } else if (i == 6) {
                menuArea.add(inventoryButton());
            } else if (i == 7) {
                menuArea.add(saveButton());
            } else {
                menuArea.add(quitButton());
            }
        }
    }

    private JButton shopButton() {
        JButton button = createMenuButton();
        button.setText("1) Shop");
        button.addActionListener(new ShopHandler());
        return button;
    }

    private class ShopHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

    private JButton trainButton() {
        JButton button = createMenuButton();
        button.setText("2) Train");
        button.addActionListener(new ShopHandler());
        return button;
    }

    private JButton healButton() {
        JButton button = createMenuButton();
        button.setText("3) Heal");
        button.addActionListener(new ShopHandler());
        return button;
    }

    private JButton fightButton() {
        JButton button = createMenuButton();
        button.setText("4) Fight");
        button.addActionListener(new ShopHandler());
        return button;
    }

    private JButton statsButton() {
        JButton button = createMenuButton();
        button.setText("5) View stats");
        button.addActionListener(new ShopHandler());
        return button;
    }

    private JButton inventoryButton() {
        JButton button = createMenuButton();
        button.setText("6) View inventory");
        button.addActionListener(new ShopHandler());
        return button;
    }

    private JButton saveButton() {
        JButton button = createMenuButton();
        button.setText("7) Save");
        button.addActionListener(new ShopHandler());
        return button;
    }

    private JButton quitButton() {
        JButton button = createMenuButton();
        button.setText("8) Quit");
        button.addActionListener(new ShopHandler());
        return button;
    }

    private void generateTextArea() {
        mainTextArea = new JTextArea("What would you like to do?");
        mainTextArea.setBounds(0, 0, 700, 300);
        mainTextArea.setBackground(Color.BLACK);
        mainTextArea.setForeground(Color.WHITE);
        mainTextArea.setFont(NORMAL_FONT);
        mainTextArea.setLineWrap(true);
        mainTextArea.setEditable(false);
//        JScrollPane scroll = new JScrollPane(mainTextArea);
//        scroll.setBounds(10,60,780,500);
        mainArea.add(mainTextArea);

    }

    private void generateHeader() {
        JPanel header = new JPanel();
        header.setBounds(0,0, WIDTH - 10, 50);
        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
        header.setBackground(Color.GRAY);
        fillHeader(header);
        getContentPane().add(header);
    }

    private void fillHeader(JPanel header) {
        header.add(createHeaderLabel(String.format("  %s", player.getName())));
        header.add(Box.createGlue());
        header.add(createHeaderLabel(String.format("Level: %d", player.getLevel())));
        header.add(Box.createGlue());
        header.add(createHeaderLabel(String.format("HP: %d/%d", player.getCurrentHealth(), player.getMaxHealth())));
        header.add(Box.createGlue());
        header.add(createHeaderLabel(String.format("Gold: %d   ", player.getGold())));
    }

    private JLabel createHeaderLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.BLACK);
        label.setFont(NORMAL_FONT);

        return label;
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
