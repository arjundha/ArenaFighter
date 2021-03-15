package ui;

import model.Equipment;
import model.Inventory;
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
    private static final Font NORMAL_FONT = new Font("Arial", Font.PLAIN, 21);

    private JPanel header;
    private JPanel mainArea;
    private JPanel menuArea;
    private JTextArea mainTextArea;
    private JScrollPane scrollPane;
    private JScrollBar scrollBar;

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

    // MODIFIES: this
    // EFFECTS: initiates the game for the user
    private void startScreen() {
        createTitle();
//        generateStartMenu();
    }

    // MODIFIES: this
    // EFFECTS: creates the title screen of the game
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

    // MODIFIES: this
    // EFFECTS: creates the main menu for the game
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

    // EFFECTS: creates a button to be used in menues
    private JButton createMenuButton() {
        JButton button = new JButton();
        button.setBackground(Color.white);
        button.setForeground(Color.black);
        button.setFont(NORMAL_FONT);
        button.setFocusPainted(false);

        return button;
    }

    // EFFECTS: adds functionality to the New Game button
    private class StartNewGameHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            player = createCharacter();
            runGame();
        }
    }

    // MODIFIES: player
    // EFFECTS: generates a new player character for the user to use in the game
    private Character createCharacter() {
        return new Character("Arjun", "human", "merchant", 10, 10, 10,10, 10);
    }

    // EFFECTS: adds functionality to the load game button
    private class LoadGameHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            loadCharacter();
            runGame();
        }
    }

    // MODIFIES: this
    // EFFECTS: generates the game screen after removing the main menu screen
    private void runGame() {
        getContentPane().removeAll();
        getContentPane().repaint();
        generateHeader();
        generateGameScreen();
    }

    // MODIFIES: this
    // EFFECTS: generates the panel for game dialog to be displayed
    private void generateGameScreen() {
        mainArea = new JPanel();
        mainArea.setBounds(50, 50, 700, 370);
        mainArea.setBackground(Color.blue);
        generateTextArea();
//        getContentPane().add(mainArea);
        generateMenu();

    }

    // MODIFIES: this
    // EFFECTS: Generates the menu panel for buttons to be displayed
    private void generateMenu() {
        menuArea = new JPanel();
        menuArea.setBounds(50, 430, 700, 200);
        menuArea.setLayout(new GridLayout(4, 2));
        fillMenu(menuArea);
        getContentPane().add(menuArea);
        refresh();
    }

    // MODIFIES: this
    // EFFECTS: fills the menuArea with buttons needed for the game
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

    // EFFECTS: Generates the menu for training a character
    private JButton trainButton() {
        JButton button = createMenuButton();
        button.setText("2) Train");
        button.addActionListener(new TrainHandler());
        return button;
    }

    // MODIFIES: this
    // EFFECTS: Adds functionality to the trainButton
    private class TrainHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            trainMenu();
        }
    }

    // MODIFIES: this
    // EFFECTS: Generates the menu for training a character
    private void trainMenu() {
        clear();
        mainTextArea.append("It looks like I can spend some gold to train my stats!\nWhat should I do...?\n");
        menuArea.add(smallTrainingButton());
        menuArea.add(largeTrainingButton());
        JButton stats = statsButton();
        stats.setText("3) View Stats");
        menuArea.add(stats);
        menuArea.add(backButton());
        refresh();
    }

    // EFFECTS: Creates a button that when clicked will allow players to train their character
    private JButton smallTrainingButton() {
        JButton button = createMenuButton();
        button.setText(String.format("1) Rent a small training room (%dg)", PRICE_OF_SMALL_TRAINING));
        button.addActionListener(new SmallTrainHandler());
        return button;
    }

    // MODIFIES: Character (May increase the stats of a character and reduces their gold)
    // EFFECTS: Spend gold to increase the stats of a player character
    private class SmallTrainHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (player.getGold() >= PRICE_OF_SMALL_TRAINING) {
                player.spendGold(PRICE_OF_SMALL_TRAINING);
                player.increaseStats(player.getClassName());  // Small training (ONLY TRAINS ONCE)
                updateHeader();
                mainTextArea.append("Great! I feel a bit stronger!\n");

            } else {
                mainTextArea.append("I don't have enough gold for a small training room.\n");
            }
        }
    }

    // EFFECTS: Creates a button that allows players to do a large training when clicked
    private JButton largeTrainingButton() {
        JButton button = createMenuButton();
        button.setText(String.format("2) Rent a large training room (%dg)", PRICE_OF_LARGE_TRAINING));
        button.addActionListener(new LargeTrainHandler());
        return button;
    }

    // MODIFIES: Character (May increase the stats of a character and reduces their gold)
    // EFFECTS: Spend gold to increase the stats of a player character
    private class LargeTrainHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (player.getGold() >= PRICE_OF_LARGE_TRAINING) {
                player.spendGold(PRICE_OF_LARGE_TRAINING);
                player.increaseStats(player.getClassName()); // Large training (TRAINS TWICE)
                player.increaseStats(player.getClassName());
                updateHeader();
                mainTextArea.append("Amazing! I feel a lot stronger!\n");

            } else {
                mainTextArea.append("Hmm I don't think I can afford a large training room right now.\n");
            }
        }
    }

    private JButton backButton() {
        JButton button = createMenuButton();
        button.setText("4) Back");
        button.addActionListener(new BackHandler());
        return button;
    }

    private class BackHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            clear();
            mainTextArea.setText("Maybe I'll come back later.\n\nWhat should I do next?\n");
            fillMenu(menuArea);
            refresh();
        }
    }

    // EFFECTS: Generate GUI to guide players through selecting potions to buy
    private JButton healButton() {
        JButton button = createMenuButton();
        button.setText("3) Heal");
        button.addActionListener(new HealHandler());
        return button;
    }

    // EFFECTS: Generate GUI to guide players through selecting potions to buy
    private class HealHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            healMenu();
        }
    }

    // MODIFIES: this
    // EFFECTS: Generate GUI to guide players through selecting potions to buy
    private void healMenu() {
        clear();
        mainTextArea.append("It might be a good idea to drink a potion and heal before my fight."
                + "\nThere seems to be a variety of potions, which should I buy?\n");
        menuArea.add(smallHealButton());
        menuArea.add(largeHealButton());
        JButton stats = statsButton();
        stats.setText("3) View Stats");
        menuArea.add(stats);
        menuArea.add(backButton());
        refresh();
    }

    // EFFECTS: Creates a button that when clicked will allow players to buy a small potion to heal
    private JButton smallHealButton() {
        JButton button = createMenuButton();
        button.setText(String.format("1) Drink a small potion (%dg)", PRICE_OF_SMALL_POTION));
        button.addActionListener(new SmallHealHandler());
        return button;
    }

    // MODIFIES: Character (May increase the current health of a character and reduces their gold)
    // EFFECTS: Spend gold to increase the current health of a player character
    private class SmallHealHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (player.getGold() >= PRICE_OF_SMALL_POTION) {
                player.spendGold(PRICE_OF_SMALL_POTION);
                player.healCharacter(HEALTH_FROM_SMALL_POTION);
                updateHeader();
                mainTextArea.append("Great! I feel way better!\n");

            } else {
                mainTextArea.append("Doesn't seem like I can afford a small potion right now.\n");
            }
        }
    }

    // EFFECTS: Creates a button that allows players to buy a large potion
    private JButton largeHealButton() {
        JButton button = createMenuButton();
        button.setText(String.format("2) Drink a large potion (%dg)", PRICE_OF_LARGE_POTION));
        button.addActionListener(new LargeHealHandler());
        return button;
    }

    // MODIFIES: Character (May increase the current health of a character and reduces their gold)
    // EFFECTS: Spend gold to increase the current health of a player character
    private class LargeHealHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (player.getGold() >= PRICE_OF_LARGE_POTION) {  // Can you afford the potion?
                player.spendGold(PRICE_OF_LARGE_POTION);
                player.healCharacter(HEALTH_FROM_LARGE_POTION);  // Heal a large amount of HP
                updateHeader();  // Update the info in the header
                mainTextArea.append("Amazing! I feel a lot healthier!\n");

            } else {
                mainTextArea.append("Looks like I don't have enough gold for these large potions right now.\n");
            }
        }
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
        button.addActionListener(new StatsHandler());
        return button;
    }

    private class StatsHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            printCharacter(player);
        }
    }

    // EFFECTS: Print out all the stats of a character except for inventory
    private void printCharacter(Character player) {
        mainTextArea.append(String.format("\nName: %s\nRace: %s\nClass: %s\n\nLEVEL: %s"
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
                player.getGold()));
        scrollDown();
    }

    private JButton inventoryButton() {
        JButton button = createMenuButton();
        button.setText("6) View inventory");
        button.addActionListener(new InventoryHandler());
        return button;
    }

    private class InventoryHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            printInventory(player.getInventory());
        }
    }

    // EFFECTS: Print out detailed information for each equipment in an inventory
    private void printInventory(Inventory inventory) {
        if (inventory.getInventorySize() == 0) {
            mainTextArea.append("My inventory is empty.\n");
//            scrollBar.setValue(scrollBar.getMaximum());
            scrollDown();


        } else {
            mainTextArea.append("\nINVENTORY:");
            for (int i = 0; i < inventory.getInventorySize(); i++) {  // Print out each Equipment in an Inventory
                Equipment item = inventory.getEquipment(i);
                mainTextArea.append(String.format("\n%d. %s: "
                                + "\nStrength - %d   Endurance - %d   Dexterity - %d   Speed - %d   VALUE: %d\n",
                        i + 1,
                        item.getName().substring(0, 1).toUpperCase() + item.getName().substring(1),  // Capitalize name
                        item.getStrength(),
                        item.getEndurance(),
                        item.getDexterity(),
                        item.getSpeed(),
                        item.getWorth()));
                scrollDown();
            }
        }

    }

    private JButton saveButton() {
        JButton button = createMenuButton();
        button.setText("7) Save");
        button.addActionListener(new SaveHandler());
        return button;
    }

    private class SaveHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            saveCharacter();
        }
    }

    private JButton quitButton() {
        JButton button = createMenuButton();
        button.setText("8) Quit");
        button.addActionListener(new QuitHandler());
        return button;
    }

    private class QuitHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int opt = JOptionPane.showConfirmDialog(null,
                    "Would you like to save before you quit?", "Save Before Quit?", JOptionPane.YES_NO_OPTION);
            if (opt == 0) {
                saveCharacter();
                setVisible(false); //you can't see me!
                dispose();
            } else if (opt == 1) {
                setVisible(false); //you can't see me!
                dispose();
            }
        }
    }

    private void generateTextArea() {
        mainTextArea = new JTextArea("What would you like to do?\n");
//        mainTextArea.setBounds(0, 0, 700, 370);
        mainTextArea.setBounds(50, 50, 700, 370);

        mainTextArea.setBackground(Color.BLACK);
        mainTextArea.setForeground(Color.WHITE);
        mainTextArea.setFont(NORMAL_FONT);
        mainTextArea.setLineWrap(true);
        mainTextArea.setEditable(false);
        scrollPane = new JScrollPane(mainTextArea);
        scrollPane.setBounds(50,50,700,370);
//        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
//        mainArea.add(mainTextArea);
        scrollBar = scrollPane.getVerticalScrollBar();
        getContentPane().add(scrollPane);
        revalidate();
        repaint();

    }

    private void generateHeader() {
        header = new JPanel();
        header.setBounds(0,0, WIDTH - 10, 50);
        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
        header.setBackground(Color.GRAY);
        fillHeader();
        getContentPane().add(header);
    }

    private void fillHeader() {
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

    // EFFECTS: saves the Character to the save file
    // CITATION: The base code can be found at https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    //           This method is implemented using the CPSC 210 JsonSerializationDemo as it's base code.
    private void saveCharacter() {
        try {
            jsonWriter.open();
            jsonWriter.write(player);
            jsonWriter.close();
            JOptionPane.showMessageDialog(this,
                    "Saved your character " + player.getName() + " to " + JSON_SAVE);
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
            JOptionPane.showMessageDialog(this,
                    "Loaded your character " + player.getName() + " from " + JSON_SAVE);

        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_SAVE);

        } catch (JSONException e) {
            System.out.println("Unable to read from file: " + JSON_SAVE + ". The data may be corrupted.");
        }
    }

    private void scrollDown() {
        int x;
        mainTextArea.selectAll();
        x = mainTextArea.getSelectionEnd();
        mainTextArea.select(x,x);
    }

    private void clear() {
        mainTextArea.setText("");
        menuArea.removeAll();
        refresh();
    }

    private void refresh() {
        revalidate();
        repaint();
    }

    private void updateHeader() {
        header.removeAll();
        fillHeader();
        refresh();
    }

    public static void main(String[] args) {
        try {
            new Game();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to run application: The application is missing the save file.");
        }
    }

}
