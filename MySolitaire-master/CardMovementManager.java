import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CardMovementManager extends MouseAdapter {
    /*
     * This class handles all of the logic of moving the Card components as well
     * as the game logic. This determines where Cards can be moved according to
     * the rules of Klondike solitiaire
     */
    private Card prevCard = null;// tracking card for waste stack
    private Card movedCard = null;// card moved from waste stack
    private boolean sourceIsFinalDeck = false;
    private boolean putBackOnDeck = true;// used for waste card recycling
    private boolean checkForWin = false;// should we check if game is over?
    private boolean gameOver = true;// easier to negate this than affirm it
    private Point start = null;// where mouse was clicked
    private Point stop = null;// where mouse was released
    private Card card = null; // card to be moved
    // used for moving single cards
    private CardStack source = null;
    private CardStack dest = null;
    // used for moving a stack of cards
    private CardStack transferStack = new CardStack(false);

    private boolean validPlayStackMove(Card source, Card dest) {
        int s_val = source.getValue().ordinal();
        int d_val = dest.getValue().ordinal();
        Card.Suit s_suit = source.getSuit();
        Card.Suit d_suit = dest.getSuit();

        // destination card should be one higher value
        if ((s_val + 1) == d_val) {
            // destination card should be opposite color
            switch (s_suit) {
                case SPADES:
                    return d_suit == Card.Suit.HEARTS || d_suit == Card.Suit.DIAMONDS;
                case CLUBS:
                    return d_suit == Card.Suit.HEARTS || d_suit == Card.Suit.DIAMONDS;
                case HEARTS:
                    return d_suit == Card.Suit.SPADES || d_suit == Card.Suit.CLUBS;
                case DIAMONDS:
                    return d_suit == Card.Suit.SPADES || d_suit == Card.Suit.CLUBS;
            }
            return false; // this never gets reached
        } else
            return false;
    }

    private boolean validFinalStackMove(Card source, Card dest) {
        int s_val = source.getValue().ordinal();
        int d_val = dest.getValue().ordinal();
        Card.Suit s_suit = source.getSuit();
        Card.Suit d_suit = dest.getSuit();
        if (s_val == (d_val + 1)) // destination must one lower
        {
            return s_suit == d_suit;
        } else
            return false;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        start = e.getPoint();
        boolean stopSearch = false;
        Solitaire.getStatusBox().setText("");
        transferStack.makeEmpty();

        /*
         * Here we use transferStack to temporarily hold all the cards above
         * the selected card in case player wants to move a stack rather
         * than a single card
         */
        for (int x = 0; x < Solitaire.getNumPlayDecks(); x++) {
            if (stopSearch)
                break;
            source = Solitaire.getPlayCardStack()[x];
            // pinpointing exact card pressed
            for (Component ca : source.getComponents()) {
                Card c = (Card) ca;
                if (c.getFaceStatus() && source.contains(start)) {
                    transferStack.putFirst(c);
                }
                if (c.contains(start) && source.contains(start) && c.getFaceStatus()) {
                    card = c;
                    stopSearch = true;
                    System.out.println("Transfer Size: " + transferStack.showSize());
                    break;
                }
            }

        }
        // SHOW (WASTE) CARD OPERATIONS
        // display new show card
        if (Solitaire.getNewCardButton().contains(start) && Solitaire.getDeck().showSize() > 0) {
            if (putBackOnDeck && prevCard != null) {
                System.out.println("Putting back on show stack: ");
                prevCard.getValue();
                prevCard.getSuit();
                Solitaire.getDeck().putFirst(prevCard);
            }

            System.out.print("poping deck ");
            Solitaire.getDeck().showSize();
            if (prevCard != null)
                Solitaire.table.remove(prevCard);
            Card c = Solitaire.getDeck().pop().setFaceup();
            Solitaire.table.add(Solitaire.moveCard(c, Solitaire.getShowPos().x, Solitaire.getShowPos().y));
            c.repaint();
            Solitaire.table.repaint();
            prevCard = c;
        }

        // preparing to move show card
        if (Solitaire.getNewCardPlace().contains(start) && prevCard != null) {
            movedCard = prevCard;
        }

        // FINAL (FOUNDATION) CARD OPERATIONS
        for (int x = 0; x < Solitaire.getNumFinalDecks(); x++) {

            if (Solitaire.getFinal_cards()[x].contains(start)) {
                source = Solitaire.getFinal_cards()[x];
                card = source.getLast();
                transferStack.putFirst(card);
                sourceIsFinalDeck = true;
                break;
            }
        }
        putBackOnDeck = true;

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        stop = e.getPoint();
        // used for status bar updates
        boolean validMoveMade = false;

        // SHOW CARD MOVEMENTS
        if (movedCard != null) {
            // Moving from SHOW TO PLAY
            for (int x = 0; x < Solitaire.getNumPlayDecks(); x++) {
                dest = Solitaire.getPlayCardStack()[x];
                // to empty play stack, only kings can go
                if (dest.empty() && movedCard != null && dest.contains(stop)
                        && movedCard.getValue() == Card.Value.KING) {
                    System.out.print("moving new card to empty spot ");
                    movedCard.setXY(dest.getXY());
                    Solitaire.table.remove(prevCard);
                    dest.putFirst(movedCard);
                    Solitaire.table.repaint();
                    movedCard = null;
                    putBackOnDeck = false;
                    Solitaire.setScore(5);
                    validMoveMade = true;
                    break;
                }
                // to populated play stack
                if (movedCard != null && dest.contains(stop) && !dest.empty() && dest.getFirst().getFaceStatus()
                        && validPlayStackMove(movedCard, dest.getFirst())) {
                    System.out.print("moving new card ");
                    movedCard.setXY(dest.getFirst().getXY());
                    Solitaire.table.remove(prevCard);
                    dest.putFirst(movedCard);
                    Solitaire.table.repaint();
                    movedCard = null;
                    putBackOnDeck = false;
                    Solitaire.setScore(5);
                    validMoveMade = true;
                    break;
                }
            }
            // Moving from SHOW TO FINAL
            for (int x = 0; x < Solitaire.getNumFinalDecks(); x++) {
                dest = Solitaire.getFinal_cards()[x];
                // only aces can go first
                if (dest.empty() && dest.contains(stop)) {
                    if (movedCard.getValue() == Card.Value.ACE) {
                        dest.push(movedCard);
                        Solitaire.table.remove(prevCard);
                        dest.repaint();
                        Solitaire.table.repaint();
                        movedCard = null;
                        putBackOnDeck = false;
                        Solitaire.setScore(10);
                        validMoveMade = true;
                        break;
                    }
                }
                if (!dest.empty() && dest.contains(stop) && validFinalStackMove(movedCard, dest.getLast())) {
                    System.out.println("Destin" + dest.showSize());
                    dest.push(movedCard);
                    Solitaire.table.remove(prevCard);
                    dest.repaint();
                    Solitaire.table.repaint();
                    movedCard = null;
                    putBackOnDeck = false;
                    checkForWin = true;
                    Solitaire.setScore(10);
                    validMoveMade = true;
                    break;
                }
            }
        }// END SHOW STACK OPERATIONS

        // PLAY STACK OPERATIONS
        if (card != null && source != null) { // Moving from PLAY TO PLAY
            for (int x = 0; x < Solitaire.getNumPlayDecks(); x++) {
                dest = Solitaire.getPlayCardStack()[x];
                // MOVING TO POPULATED STACK
                if (card.getFaceStatus() && dest.contains(stop) && source != dest && !dest.empty()
                        && validPlayStackMove(card, dest.getFirst()) && transferStack.showSize() == 1) {
                    Card c = null;
                    if (sourceIsFinalDeck)
                        c = source.pop();
                    else
                        c = source.popFirst();

                    c.repaint();
                    // if playstack, turn next card up
                    if (source.getFirst() != null) {
                        Card temp = source.getFirst().setFaceup();
                        temp.repaint();
                        source.repaint();
                    }

                    dest.setXY(dest.getXY().x, dest.getXY().y);
                    dest.putFirst(c);

                    dest.repaint();

                    Solitaire.table.repaint();

                    System.out.print("Destination ");
                    dest.showSize();
                    if (sourceIsFinalDeck)
                        Solitaire.setScore(15);
                    else
                        Solitaire.setScore(10);
                    validMoveMade = true;
                    break;
                } else if (dest.empty() && card.getValue() == Card.Value.KING && transferStack.showSize() == 1) {// MOVING TO EMPTY STACK, ONLY KING ALLOWED
                    Card c = null;
                    if (sourceIsFinalDeck)
                        c = source.pop();
                    else
                        c = source.popFirst();

                    c.repaint();
                    // if playstack, turn next card up
                    if (source.getFirst() != null) {
                        Card temp = source.getFirst().setFaceup();
                        temp.repaint();
                        source.repaint();
                    }

                    dest.setXY(dest.getXY().x, dest.getXY().y);
                    dest.putFirst(c);

                    dest.repaint();

                    Solitaire.table.repaint();

                    System.out.print("Destination ");
                    dest.showSize();
                    Solitaire.setScore(5);
                    validMoveMade = true;
                    break;
                }
                // Moving STACK of cards from PLAY TO PLAY
                // to EMPTY STACK
                if (dest.empty() && dest.contains(stop) && !transferStack.empty()
                        && transferStack.getFirst().getValue() == Card.Value.KING) {
                    System.out.println("King To Empty Stack Transfer");
                    while (!transferStack.empty()) {
                        System.out.println("popping from transfer: " + transferStack.getFirst().getValue());
                        dest.putFirst(transferStack.popFirst());
                        source.popFirst();
                    }
                    if (source.getFirst() != null) {
                        Card temp = source.getFirst().setFaceup();
                        temp.repaint();
                        source.repaint();
                    }

                    dest.setXY(dest.getXY().x, dest.getXY().y);
                    dest.repaint();

                    Solitaire.table.repaint();
                    Solitaire.setScore(5);
                    validMoveMade = true;
                    break;
                }
                // to POPULATED STACK
                if (dest.contains(stop) && !transferStack.empty() && source.contains(start)
                        && validPlayStackMove(transferStack.getFirst(), dest.getFirst())) {
                    System.out.println("Regular Stack Transfer");
                    while (!transferStack.empty()) {
                        System.out.println("popping from transfer: " + transferStack.getFirst().getValue());
                        dest.putFirst(transferStack.popFirst());
                        source.popFirst();
                    }
                    if (source.getFirst() != null) {
                        Card temp = source.getFirst().setFaceup();
                        temp.repaint();
                        source.repaint();
                    }

                    dest.setXY(dest.getXY().x, dest.getXY().y);
                    dest.repaint();

                    Solitaire.table.repaint();
                    Solitaire.setScore(5);
                    validMoveMade = true;
                    break;
                }
            }
            // from PLAY TO FINAL
            for (int x = 0; x < Solitaire.getNumFinalDecks(); x++) {
                dest = Solitaire.getFinal_cards()[x];

                if (card.getFaceStatus() == true && source != null && dest.contains(stop) && source != dest) {// TO EMPTY STACK
                    if (dest.empty())// empty final should only take an ACE
                    {
                        if (card.getValue() == Card.Value.ACE) {
                            Card c = source.popFirst();
                            c.repaint();
                            if (source.getFirst() != null) {

                                Card temp = source.getFirst().setFaceup();
                                temp.repaint();
                                source.repaint();
                            }

                            dest.setXY(dest.getXY().x, dest.getXY().y);
                            dest.push(c);

                            dest.repaint();

                            Solitaire.table.repaint();

                            System.out.print("Destination ");
                            dest.showSize();
                            card = null;
                            Solitaire.setScore(10);
                            validMoveMade = true;
                            break;
                        }// TO POPULATED STACK
                    } else if (validFinalStackMove(card, dest.getLast())) {
                        Card c = source.popFirst();
                        c.repaint();
                        if (source.getFirst() != null) {

                            Card temp = source.getFirst().setFaceup();
                            temp.repaint();
                            source.repaint();
                        }

                        dest.setXY(dest.getXY().x, dest.getXY().y);
                        dest.push(c);

                        dest.repaint();

                        Solitaire.table.repaint();

                        System.out.print("Destination ");
                        dest.showSize();
                        card = null;
                        checkForWin = true;
                        Solitaire.setScore(10);
                        validMoveMade = true;
                        break;
                    }
                }

            }
        }// end cycle through play decks

        // SHOWING STATUS MESSAGE IF MOVE INVALID
        if (!validMoveMade && dest != null && card != null) {
            Solitaire.getStatusBox().setText("That Is Not A Valid Move");
        }
        // CHECKING FOR WIN
        if (checkForWin) {
            boolean gameNotOver = false;
            // cycle through final decks, if they're all full then game over
            for (int x = 0; x < Solitaire.getNumFinalDecks(); x++) {
                dest = Solitaire.getFinal_cards()[x];
                if (dest.showSize() != 13) {
                    // one deck is not full, so game is not over
                    gameNotOver = true;
                    break;
                }
            }
            if (!gameNotOver)
                gameOver = true;
        }

        if (checkForWin && gameOver) {
            JOptionPane.showMessageDialog(Solitaire.table, "Congratulations! You've Won!");
            Solitaire.getStatusBox().setText("Game Over!");
        }
        // RESET VARIABLES FOR NEXT EVENT
        start = null;
        stop = null;
        source = null;
        dest = null;
        card = null;
        sourceIsFinalDeck = false;
        checkForWin = false;
        gameOver = false;
    }// end mousePressed()
}
