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
import java.util.Random;

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
    private int stock;  // This number is the number of items in stock in the store right now.
    private Inventory shop;

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
    }

    // MODIFIES: this
    // EFFECTS: creates the title screen of the game
    private void createTitle() {
        JPanel titlePanel = new JPanel();
        titlePanel.setBounds(100, 100, 600, 100);  // near the top middle of the screen
        titlePanel.setBackground(BACKGROUND);

        JLabel titleLabel = new JLabel("Arena Fighter");  // game title
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(TITLE_FONT);

        titlePanel.add(titleLabel);  // add it all to the screen
        generateStartMenu();
        getContentPane().add(titlePanel);
    }

    // MODIFIES: this
    // EFFECTS: creates the main menu for the game
    private void generateStartMenu() {
        JPanel newGame = new JPanel();
        newGame.setBounds(100, 500, 600, 80);  // this is the lower portion of the title screen
        newGame.setBackground(BACKGROUND);

        JButton newGameButton = createMenuButton();  // add two different buttons to the screen
        newGameButton.setText("NEW GAME");
        newGameButton.addActionListener(new StartNewGameHandler());
        JButton loadGameButton = createMenuButton();
        loadGameButton.setText("LOAD GAME");
        loadGameButton.addActionListener(new LoadGameHandler());

        newGame.add(newGameButton);
        newGame.add(Box.createHorizontalStrut(100));  // space the buttons apart from each other
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
        for (int i = 1; i <= 8; i++) {  // Add all 8 buttons
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

    // EFFECTS: creates a button that will take user to shop menu
    private JButton shopButton() {
        JButton button = createMenuButton();
        button.setText("1) Shop");
        button.addActionListener(new ShopHandler());
        return button;
    }

    // EFFECTS: handles the button on click to take user to shop menu
    private class ShopHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            stock = 8;
            shopMenu();

        }
    }

    // MODIFIES: this
    // EFFECTS: produce the GUI interface for shopping
    private void shopMenu() {
        clear();
        mainTextArea.append("Welcome to my store!\nAh now, let me see what we have today...\n");
        shop = generateShop();
        displayShop();
        fillShopMenuArea();
    }

    // MODIFIES: this
    // EFFECTS: display the shop in the GUI
    private void displayShop() {
        if (shop.getInventorySize() == 0) {
            mainTextArea.append("Good luck in the arena!\n");
        } else {
            for (int i = 0; i < shop.getInventorySize(); i++) {  // Print out each Equipment in an Inventory
                Equipment item = shop.getEquipment(i);
                mainTextArea.append(String.format("\n%d. %s: "
                                + "\nStrength - %d   Endurance - %d   Dexterity - %d   Speed - %d   VALUE: %d\n",
                        i + 1,
                        item.getName().substring(0, 1).toUpperCase() + item.getName().substring(1),  // Capitalize name
                        item.getStrength(),
                        item.getEndurance(),
                        item.getDexterity(),
                        item.getSpeed(),
                        item.getWorth()));
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: Create a menu for shopping
    private void fillShopMenuArea() {
        if (stock == 0) { // Don't repopulate the menu if they are sold out
            mainTextArea.append("Wow! I'm sold out! Thanks!\n");
        } else {
            for (int i = 1; i <= stock; i++) {  // Add a new buy item button
                menuArea.add(buyButton(i));
            }
        }
        JButton stats = statsButton();
        stats.setText(String.format("%d) View Stats", stock + 1));  // We want these numbers to be accurate
        menuArea.add(stats);
        JButton inv = inventoryButton();
        inv.setText(String.format("%d) View Inventory", stock + 2));
        menuArea.add(inv);
        JButton back = backButton();
        back.setText(String.format("%d) Back", stock + 3));
        menuArea.add(back);

        refresh();
    }

    // EFFECTS: Creates a button that when clicked will allow players to train their character
    private JButton buyButton(int index) {
        JButton button = createMenuButton();
        button.setText(String.format("%d) Buy item %d", index, index));
        button.addActionListener(new BuyHandler(index));
        return button;
    }

    // MODIFIES: Character (May increase the stats of a character and reduces their gold)
    // EFFECTS: Spend gold to increase the stats of a player character
    private class BuyHandler implements ActionListener {
        private final int item;

        // EFFECTS: Construct a buy handler with a specific item in mind
        public BuyHandler(int item) {
            this.item = item;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            buyItem(item);
        }
    }

    // MODIFIES: Inventory and Character
    // EFFECTS: Remove an item from a shop inventory and place it in a Character inventory after spending gold
    private void buyItem(int item) {
        if (shop.getInventorySize() == 0) {  // Check if the inventory is empty
            mainTextArea.append("Sorry, I am sold out!\n");

        } else if (0 < item && item <= shop.getInventorySize()) {  // Check if the user input is within the options
            int index = item - 1;  // get the index of the element we want
            Equipment equipment = shop.getEquipment(index);
            int cost = equipment.getWorth();  // How much does it cost?

            if (player.getGold() >= cost) {  // Can afford it
                player.equipItem(equipment);  // Add the equipment to your inventory
                player.spendGold(cost);  // Spend the gold needed to buy the item
                shop.removeEquipment(index);  // Remove the equipment from the store
                stock--;  // reduce shop stock by 1
                menuArea.removeAll(); // reset the menu so you cant buy an item thats gone
                mainTextArea.setText(null); // reset the text area so its not messy
                fillShopMenuArea();
                displayShop();
                updateHeader();
                mainTextArea.append(String.format("\nYou buy the %s.\n", equipment.getName()));  // Informative message

            } else {  // Cant afford it
                mainTextArea.append("Ah, it appears as if you do not have enough gold for that, sorry.\n");
            }

        } else {
            mainTextArea.append("I don't think I have that in stock...\n");
        }
    }

    // EFFECTS: Produce a randomized inventory to be sold in a shop
    private Inventory generateShop() {
        int count = 0;
        Inventory shopInventory = new Inventory();

        while (count < NUMBER_OF_EQUIPMENT_PAIRS_SOLD) {  // We want the shop to only contain a certain number of items
            shopInventory.addEquipment(generateWeapon(generateRandomInteger()));
            shopInventory.addEquipment(generateArmour(generateRandomInteger()));
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

    // REQUIRES: upperbound must be a positive integer represent the highest integer you want to possibly get
    // EFFECTS: Generates a random integer from [0, upperbound] inclusive
    private int generateRandomInteger() {
        Random random = new Random();  // Start a random class object
        return random.nextInt(Game.RANDOM_ITEM_SELECTOR + 1);  // Return a random int + 1 to include upperbound
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
        menuArea.add(smallTrainingButton());  // add the two different training buttons
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

    // EFFECTS: Generate GUI for fighting
    private JButton fightButton() {
        JButton button = createMenuButton();
        button.setText("4) Fight");
        button.addActionListener(new ShopHandler());
        return button;
    }

    // EFFECTS: Generate GUI for viewing stats in the JTextArea
    private JButton statsButton() {
        JButton button = createMenuButton();
        button.setText("5) View stats");
        button.addActionListener(new StatsHandler());
        return button;
    }

    // EFFECTS: Allows player stats information to be displayed when button is pressed
    private class StatsHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            printCharacter(player);
        }
    }

    // MODIFIES: this
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

    // EFFECTS: Generate GUI for viewing inventory in the JTextArea
    private JButton inventoryButton() {
        JButton button = createMenuButton();
        button.setText("6) View inventory");
        button.addActionListener(new InventoryHandler());
        return button;
    }

    // EFFECTS: adds functionality to button so when it is clicked, players view their inventory
    private class InventoryHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            printInventory(player.getInventory());
        }
    }

    // MODIFIES: this
    // EFFECTS: Print out detailed information for each equipment in an inventory
    private void printInventory(Inventory inventory) {
        if (inventory.getInventorySize() == 0) {
            mainTextArea.append("My inventory is empty.\n");
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

    // EFFECTS: on press, the player is saved in the JSON
    private JButton saveButton() {
        JButton button = createMenuButton();
        button.setText("7) Save");
        button.addActionListener(new SaveHandler());
        return button;
    }

    // EFFECTS: adds functionality to the save button
    private class SaveHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            saveCharacter();
        }
    }

    // EFFECTS: when pressed, the player will quit the game
    private JButton quitButton() {
        JButton button = createMenuButton();
        button.setText("8) Quit");
        button.addActionListener(new QuitHandler());
        return button;
    }

    // MODIFIES: this
    // EFFECTS: player will be prompted to save the game before quitting, then quit
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

    // MODIFIES: this
    // EFFECTS: generates the main JTextArea for game information to be displayed
    private void generateTextArea() {
        mainTextArea = new JTextArea("What would you like to do?\n");  // initial text
//        mainTextArea.setBounds(0, 0, 700, 370);
        mainTextArea.setBounds(50, 50, 700, 370);

        mainTextArea.setBackground(Color.BLACK);
        mainTextArea.setForeground(Color.WHITE);
        mainTextArea.setFont(NORMAL_FONT);
        mainTextArea.setLineWrap(true);  // handle horizontal overflow automatically
        mainTextArea.setEditable(false);  // we dont want editing
        scrollPane = new JScrollPane(mainTextArea);  // make it so it can scroll and handle overflow
        scrollPane.setBounds(50,50,700,370);
//        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
//        mainArea.add(mainTextArea);
        scrollBar = scrollPane.getVerticalScrollBar();
        getContentPane().add(scrollPane);
        refresh();
    }

    // MODIFIES: this
    // EFFECTS: generates the main JTextArea for game information to be displayed
    private void generateHeader() {
        header = new JPanel();
        header.setBounds(0,0, WIDTH - 10, 50);
        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
        header.setBackground(Color.GRAY);
        fillHeader();  // we need to fill the header with info!
        getContentPane().add(header);
    }

    // MODIFIES: this
    // EFFECTS: display player info in the header JPanel
    private void fillHeader() {
        header.add(createHeaderLabel(String.format("  %s", player.getName())));
        header.add(Box.createGlue());
        header.add(createHeaderLabel(String.format("Level: %d", player.getLevel())));
        header.add(Box.createGlue());
        header.add(createHeaderLabel(String.format("HP: %d/%d", player.getCurrentHealth(), player.getMaxHealth())));
        header.add(Box.createGlue());
        header.add(createHeaderLabel(String.format("Gold: %d   ", player.getGold())));
    }

    // EFFECTS: Produce a JLabel with certain text displayed
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

    // MODIFIES: this
    // EFFECTS: Scroll the JTextArea down to the bottom
    private void scrollDown() {
        int x;
        mainTextArea.selectAll();
        x = mainTextArea.getSelectionEnd(); // get the end of the JTextArea
        mainTextArea.select(x,x);
    }

    // MODIFIES: this
    // EFFECTS: removes all text in the JTextArea mainTextArea and all buttons in the JPanel menuArea
    private void clear() {
        mainTextArea.setText("");  // this can be changed to null or "" and do the same thing (removes text)
        menuArea.removeAll();  // get rid of those buttons!
        refresh();  // repaint and refresh
    }

    // MODIFIES: this
    // EFFECTS: updates the displayed GUI
    private void refresh() {
        revalidate();
        repaint();
    }

    // MODIFIES: this
    // EFFECTS: update the header JPanel with new player information
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
