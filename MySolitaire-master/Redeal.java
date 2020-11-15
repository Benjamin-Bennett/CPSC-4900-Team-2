import java.util.*;
import javax.swing.*;


public class Redeal {

    private static Vector<Card> redealDeck = new Vector<Card>();
    public static int redealCounter = 0;
    public static void redeal() {

        if (redealCounter < 6) {
            for (int y = 0; y < Solitaire.NUM_PLAY_DECKS - 1; y++) {
                while (!Solitaire.playCardStack[y].empty()) {
                    Card temp = Solitaire.playCardStack[y].pop();
                    temp.setFacedown();
                    redealDeck.add(temp);
                }
            }
            Collections.shuffle(redealDeck);
            while (!redealDeck.isEmpty()) {
                for (int y = 0; y < Solitaire.NUM_PLAY_DECKS; y++) {
                    System.out.println(redealDeck);
                    System.out.println(redealDeck.size());
                    if (redealDeck.size() == 0) {
                        break;
                    }
                    Card temp = redealDeck.firstElement();
                    if (Solitaire.playCardStack[y].showSize() < 3) {
                        Solitaire.playCardStack[y].putFirst(temp);
                        redealDeck.remove(0);
                    } else {
                        y++;
                    }
                    if (y == Solitaire.NUM_PLAY_DECKS - 1) {
                        y = -1;
                    }
                }
            }
            for (int y = 0; y < Solitaire.NUM_PLAY_DECKS; y++) {
                Card temp = Solitaire.playCardStack[y].pop();
                Solitaire.playCardStack[y].putFirst(temp.setFaceup());
            }
            Solitaire.redealButton.validate();
            Solitaire.redealButton.repaint();
            Solitaire.table.repaint();
            redealCounter++;
        }
        else{
            JOptionPane.showMessageDialog(null, "Out of redeals!");
        }
    }
}
