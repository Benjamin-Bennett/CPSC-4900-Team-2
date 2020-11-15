import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

public class Solitaire {
    protected static final JPanel table = new JPanel();
    // CONSTANTS
    private static final int TABLE_HEIGHT = Card.CARD_HEIGHT * 6;
    private static final int TABLE_WIDTH = (Card.CARD_WIDTH * 7) + 100;
    private static final int NUM_FINAL_DECKS = 4;
    private static final int NUM_PLAY_DECKS = 18;
    private static final Point DECK_POS = new Point(5, 5);
    private static final Point SHOW_POS = new Point(DECK_POS.x + Card.CARD_WIDTH + 5, DECK_POS.y);
    private static final Point FINAL_POS = new Point(SHOW_POS.x + Card.CARD_WIDTH + 150, DECK_POS.y);
    private static final Point PLAY_POS = new Point(DECK_POS.x, FINAL_POS.y + Card.CARD_HEIGHT + 30);
    private static final Card newCardPlace = new Card();// waste card spot
    // GUI COMPONENTS (top level)
    private static final JFrame frame = new JFrame("Klondike Solitaire");
    private static final Card newCardButton = new Card();// reveal waste card
    // GAMEPLAY STRUCTURES
    private static FinalStack[] final_cards;// Foundation Stacks
    private static CardStack[] playCardStack; // Tableau stacks
    private static CardStack deck; // populated with standard 52 card deck
    // other components
    private static int gameBarXOff = TABLE_WIDTH - 120;
    private int gameBarCompXOff = 0;
    private static int gameBarYOff = TABLE_HEIGHT - 490;
    private static int gameBarCompYOff = 40;
    private static int numGameBarComps = 0;
    private static JEditorPane gameTitle = new JEditorPane("text/html", "");
    private static JButton showRulesButton = new JButton("Show Rules");
    private static JButton exitButton = new JButton("Exit");
    private static JButton newGameButton = new JButton("New Game");
    private static JButton toggleTimerButton = new JButton("Pause Timer");
    private static JTextField scoreBox = new JTextField();// displays the score
    private static JTextField timeBox = new JTextField();// displays the time
    private static JTextField statusBox = new JTextField();// status messages

    public static JButton getMainMenuButton() {
        return mainMenuButton;
    }

    private static JButton mainMenuButton= new JButton("Main Menu");
    private static JButton optionsMenuButton = new JButton("Options Menu");
    // TIMER UTILITIES
    private static Timer timer = new Timer();
    private static ScoreClock scoreClock = new ScoreClock();

    // MISC TRACKING VARIABLES
    private static boolean timeRunning = false;// timer running?
    private static int score = 0;// keep track of the score
    private static int time = 0;// keep track of seconds elapsed

    public static int getTableHeight() {
        return TABLE_HEIGHT;
    }

    public static int incGameBarCompYOff()
    {
        numGameBarComps += 1;
        return gameBarCompYOff * numGameBarComps;
    }

    public static int getTableWidth() {
        return TABLE_WIDTH;
    }

    public static int getNumFinalDecks() {
        return NUM_FINAL_DECKS;
    }

    public static int getNumPlayDecks() {
        return NUM_PLAY_DECKS;
    }

    public static Point getDeckPos() {
        return DECK_POS;
    }

    public static Point getShowPos() {
        return SHOW_POS;
    }

    public static Point getFinalPos() {
        return FINAL_POS;
    }

    public static Point getPlayPos() {
        return PLAY_POS;
    }

    public static JPanel getTable() {
        return table;
    }

    public static Card getNewCardPlace() {
        return newCardPlace;
    }

    public static JFrame getFrame() {
        return frame;
    }

    public static Card getNewCardButton() {
        return newCardButton;
    }

    public static FinalStack[] getFinal_cards() {
        return final_cards;
    }

    public static CardStack[] getPlayCardStack() {
        return playCardStack;
    }

    public static CardStack getDeck() {
        return deck;
    }

    public static JEditorPane getGameTitle() {
        return gameTitle;
    }

    public static JButton getShowRulesButton() {
        return showRulesButton;
    }

    public static JButton getNewGameButton() {
        return newGameButton;
    }

    public static JButton getToggleTimerButton() {
        return toggleTimerButton;
    }

    public static JTextField getScoreBox() {
        return scoreBox;
    }

    public static JTextField getTimeBox() {
        return timeBox;
    }

    public static JTextField getStatusBox() {
        return statusBox;
    }

    public static Timer getTimer() {
        return timer;
    }

    public static ScoreClock getScoreClock() {
        return scoreClock;
    }

    public static boolean isTimeRunning() {
        return timeRunning;
    }

    public static int getScore() {
        return score;
    }

    // add/subtract points based on gameplay actions
    protected static void setScore(int deltaScore) {
        Solitaire.score += deltaScore;
        String newScore = "Score: " + Solitaire.score;
        scoreBox.setText(newScore);
        scoreBox.repaint();
    }

    public static int getTime() {
        return time;
    }

    public static void addListenerIfNotPresent(JButton button, ActionListener listener)
    {
        ActionListener[] currentListeners = button.getActionListeners();
        for (ActionListener l: currentListeners)
        {
            button.removeActionListener(l);
        }
        button.addActionListener(listener);
    }

    // moves a card to abs location within a component
    protected static Card moveCard(Card c, int x, int y) {
        c.setBounds(new Rectangle(new Point(x, y), new Dimension(Card.CARD_WIDTH + 10, Card.CARD_HEIGHT + 10)));
        c.setXY(new Point(x, y));
        return c;
    }

    // GAME TIMER UTILITIES
    protected static void updateTimer() {
        Solitaire.time += 1;
        // every 10 seconds elapsed we take away 2 points
        if (Solitaire.time % 10 == 0) {
            setScore(-2);
        }
        String time = "Seconds: " + Solitaire.time;
        timeBox.setText(time);
        timeBox.repaint();
    }

    protected static void startTimer() {
        scoreClock = new ScoreClock();
        // set the timer to update every second
        timer.scheduleAtFixedRate(scoreClock, 1000, 1000);
        timeRunning = true;
    }

    // the pause timer button uses this
    protected static void toggleTimer() {
        if (timeRunning && scoreClock != null) {
            scoreClock.cancel();
            timeRunning = false;
        } else {
            startTimer();
        }
    }

    private static void playNewGame() {
        numGameBarComps = 0;
        table.removeAll();
        deck = new CardStack(true); // deal 52 cards
        deck.shuffle();
        // reset stacks if user starts a new game in the middle of one
        resetStacks();
        // initialize & place final (foundation) decks/stacks
        initializeFinalStacks();
        // place new card distribution button
        table.add(moveCard(newCardButton, DECK_POS.x, DECK_POS.y));
        // initialize & place play (tableau) decks/stacks
        placeTableaus();

        // Dealing new game
        dealNewGame();
        // reset time
        time = 0;

        initializeButtons();

        startTimer();



        /*statusBox.setBounds(605, TABLE_HEIGHT - 70, 180, 30);
        statusBox.setEditable(false);
        statusBox.setOpaque(false);*/

        table.add(statusBox);
        table.add(toggleTimerButton);
        table.add(gameTitle);
        table.add(timeBox);
        table.add(newGameButton);
        table.add(showRulesButton);
        table.add(scoreBox);
        table.add(mainMenuButton);
        table.repaint();
    }

    private static void initializeButtons() {

        addListenerIfNotPresent(newGameButton, new NewGameListener());
        newGameButton.setBounds(gameBarXOff, gameBarYOff + incGameBarCompYOff(), 120, 30);

        addListenerIfNotPresent(showRulesButton, new ShowRulesListener());
        showRulesButton.setBounds(gameBarXOff, gameBarYOff + incGameBarCompYOff(), 120, 30);

        addListenerIfNotPresent(mainMenuButton, new MainMenuListener());
        mainMenuButton.setBounds(TABLE_WIDTH - 120, gameBarYOff + incGameBarCompYOff(),120,30);

        gameTitle.setText("<b>Demon Fan Solitaire</b> <br> CPSC4900 <br> Group 2");
        gameTitle.setEditable(false);
        gameTitle.setOpaque(false);
        gameTitle.setBounds(245, 20, 100, 100);

        toggleTimerButton.setBounds(TABLE_WIDTH - 120, gameBarYOff + incGameBarCompYOff(), 120, 30);
        addListenerIfNotPresent(toggleTimerButton, new ToggleTimerListener());

        scoreBox.setBounds(TABLE_WIDTH - 120, gameBarYOff + incGameBarCompYOff(), 120, 30);
        scoreBox.setText("Score: 0");
        scoreBox.setEditable(false);
        scoreBox.setOpaque(false);

        timeBox.setBounds(TABLE_WIDTH - 120, gameBarYOff + incGameBarCompYOff(), 120, 30);
        timeBox.setText("Seconds: 0");
        timeBox.setEditable(false);
        timeBox.setOpaque(false);
    }
    private static void initializeMenuButtons(){
        addListenerIfNotPresent(newGameButton, new NewGameListener());
        newGameButton.setBounds(TABLE_WIDTH/2-70, TABLE_HEIGHT/2 -30, 120, 30);

        addListenerIfNotPresent(optionsMenuButton, new OptionsMenuListener());
        optionsMenuButton.setBounds(TABLE_WIDTH/2-70, TABLE_HEIGHT/2, 120, 30);

        addListenerIfNotPresent(showRulesButton, new ShowRulesListener());
        showRulesButton.setBounds(TABLE_WIDTH/2-70, TABLE_HEIGHT/2 + 30, 120, 30);

        addListenerIfNotPresent(exitButton, new ExitListener());
        exitButton.setBounds(TABLE_WIDTH/2-70, TABLE_HEIGHT/2 + 60, 120, 30);
    }

    private static void startMainMenu(){
        table.removeAll();
        initializeMenuButtons();
        table.add(gameTitle);
        table.add(newGameButton);
        table.add(optionsMenuButton);
        table.add(showRulesButton);
        table.add(exitButton);
        table.repaint();
    }

    private static void initializeOptionsMenuButtonsButtons()
    {
        addListenerIfNotPresent(mainMenuButton, new MainMenuListener());
        mainMenuButton.setBounds(TABLE_WIDTH/2-70, TABLE_HEIGHT/2 -30, 120, 30);
    }

    private static void startOptionsMenu()
    {
        table.removeAll();
        initializeOptionsMenuButtonsButtons();
        table.add(gameTitle);
        table.add(mainMenuButton);
        table.repaint();
    }

    private static void dealNewGame() {
        for (int x = 0; x < 3; x++) {
            int hld = 0;
            for(int y = 0; y < NUM_PLAY_DECKS - 1; y++)
            {
                Card c = deck.pop();
                if (x == 2)
                {
                    playCardStack[y].putFirst(c.setFaceup());
                }
                else
                {
                    playCardStack[y].putFirst(c);
                }
            }
        }
        playCardStack[NUM_PLAY_DECKS - 1].putFirst(deck.pop().setFaceup());
    }

    private static void placeTableaus() {
        playCardStack = new CardStack[NUM_PLAY_DECKS];
        for (int x = 0; x < NUM_PLAY_DECKS; x++) {
            playCardStack[x] = new CardStack(false);
            playCardStack[x].setXY((DECK_POS.x + ( (x % 6) * (Card.CARD_WIDTH + 10))), (PLAY_POS.y + ((x / 6) * 200) ) );

            table.add(playCardStack[x]);
        }
    }

    private static void initializeFinalStacks() {
        final_cards = new FinalStack[NUM_FINAL_DECKS];
        for (int x = 0; x < NUM_FINAL_DECKS; x++) {
            final_cards[x] = new FinalStack();

            final_cards[x].setXY((FINAL_POS.x + (x * Card.CARD_WIDTH)) + 10, FINAL_POS.y);
            table.add(final_cards[x]);

        }
    }

    private static void resetStacks() {
        if (playCardStack != null && final_cards != null) {
            for (int x = 0; x < NUM_PLAY_DECKS; x++) {
                playCardStack[x].makeEmpty();
            }
            for (int x = 0; x < NUM_FINAL_DECKS; x++) {
                final_cards[x].makeEmpty();
            }
        }
    }

    public static void main(String[] args) {

        Container contentPane;

        frame.setSize(TABLE_WIDTH, TABLE_HEIGHT);

        table.setLayout(null);
        table.setBackground(new Color(0, 180, 0));

        contentPane = frame.getContentPane();
        contentPane.add(table);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        startMainMenu();

        table.addMouseListener(new CardMovementManager());
        table.addMouseMotionListener(new CardMovementManager());

        frame.setVisible(true);

    }

    private static class ScoreClock extends TimerTask {

        @Override
        public void run() {
            updateTimer();
        }
    }

    // BUTTON LISTENERS
    private static class NewGameListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            playNewGame();
        }

    }
    private static class MainMenuListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            startMainMenu();
        }

    }

    private static class OptionsMenuListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e)
        {
            startOptionsMenu();
        }
    }
}