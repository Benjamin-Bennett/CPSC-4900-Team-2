import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

class Card extends JPanel {
    final static public int CARD_HEIGHT = 150;
    final static public int CARD_WIDTH = 100;
    final static public int CORNER_ANGLE = 25;
    private final int x_offset = 10;
    private final int y_offset = 20;
    private final int new_x_offset = x_offset + (CARD_WIDTH - 30);
    private Suit _suit;
    // functions
    private Value _value;
    private Boolean _faceup;
    private Point _location; // location relative to container
    private Point whereAmI; // used to create abs postion rectangle for contains
    private int x; // used for relative positioning within CardStack Container
    private int y;

    Card(Suit suit, Value value) {
        _suit = suit;
        _value = value;
        _faceup = false;
        _location = new Point();
        x = 0;
        y = 0;
        _location.x = x;
        _location.y = y;
        whereAmI = new Point();
    }

    Card() {
        _suit = Card.Suit.CLUBS;
        _value = Card.Value.ACE;
        _faceup = false;
        _location = new Point();
        x = 0;
        y = 0;
        _location.x = x;
        _location.y = y;
        whereAmI = new Point();
    }

    public Suit getSuit() {
        System.out.println(_suit);
        return _suit;
    }

    public void setSuit(Suit suit) {
        _suit = suit;
    }

    public Value getValue() {
        System.out.println(_value);
        return _value;
    }

    public void setValue(Value value) {
        _value = value;
    }

    public Point getWhereAmI() {
        return whereAmI;
    }

    public void setWhereAmI(Point p) {
        whereAmI = p;
    }

    public Point getXY() {
        return new Point(x, y);
    }

    public void setXY(Point p) {
        x = p.x;
        y = p.y;

    }

    public Boolean getFaceStatus() {
        return _faceup;
    }

    public Card setFaceup() {
        _faceup = true;
        return this;
    }

    public Card setFacedown() {
        _faceup = false;
        return this;
    }

    @Override
    public boolean contains(Point p) {
        Rectangle rect = new Rectangle(whereAmI.x, whereAmI.y, Card.CARD_WIDTH, Card.CARD_HEIGHT);
        return (rect.contains(p));
    }

   /** private void drawSuit(Graphics2D g, String suit, Color color) {
        g.setColor(color);
        g.drawString(suit, _location.x + x_offset, _location.y + y_offset);
        g.drawString(suit, _location.x + x_offset, _location.y + CARD_HEIGHT - 5);
    }

    private void drawValue(Graphics2D g, String value) {
        g.drawString(value, _location.x + new_x_offset, _location.y + y_offset);
        g.drawString(value, _location.x + new_x_offset, _location.y + y_offset + CARD_HEIGHT - 25);
    }
*/
    private void drawPic(String suit, Color color){

    }
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        RoundRectangle2D rect2 = new RoundRectangle2D.Double(_location.x, _location.y, CARD_WIDTH, CARD_HEIGHT,
                CORNER_ANGLE, CORNER_ANGLE);
        g2d.setColor(Color.WHITE);
        g2d.fill(rect2);
        g2d.setColor(Color.black);
        g2d.draw(rect2);
        // DRAW THE CARD SUIT AND VALUE IF FACEUP
        if (_faceup) {
            /**switch (_suit) {
                case HEARTS:
                    drawSuit(g2d, "Hearts", Color.RED);
                    break;
                case DIAMONDS:
                    drawSuit(g2d, "Diamonds", Color.RED);
                    break;
                case SPADES:
                    drawSuit(g2d, "Spades", Color.BLACK);
                    break;
                case CLUBS:
                    drawSuit(g2d, "Clubs", Color.BLACK);
                    break;
             */
            BufferedImage image = null;
            try {
                image = ImageIO.read(new File("MySolitaire-master\\PNG"+"\\" + getSuit() + getValue() + ".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            super.paintComponent(g);
            setOpaque(false);
            g.drawImage(image, 0, 0,CARD_WIDTH, CARD_HEIGHT,  this);

            /**int new_x_offset = x_offset + (CARD_WIDTH - 30);
            switch (_value) {
                case ACE:
                    drawValue(g2d, "A");
                    break;
                case TWO:
                    drawValue(g2d, "2");
                    break;
                case THREE:
                    drawValue(g2d, "3");
                    break;
                case FOUR:
                    drawValue(g2d, "4");
                    break;
                case FIVE:
                    drawValue(g2d, "5");
                    break;
                case SIX:
                    drawValue(g2d, "6");
                    break;
                case SEVEN:
                    drawValue(g2d, "7");
                    break;
                case EIGHT:
                    drawValue(g2d, "8");
                    break;
                case NINE:
                    drawValue(g2d, "9");
                    break;
                case TEN:
                    drawValue(g2d, "10");
                    break;
                case JACK:
                    drawValue(g2d, "J");
                    break;
                case QUEEN:
                    drawValue(g2d, "Q");
                    break;
                case KING:
                    drawValue(g2d, "K");
                    break;
             */


        } else {
            // DRAW THE BACK OF THE CARD IF FACEDOWN
            RoundRectangle2D rect = new RoundRectangle2D.Double(_location.x, _location.y, CARD_WIDTH, CARD_HEIGHT,
                    CORNER_ANGLE, CORNER_ANGLE);
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.fill(rect);
            g2d.setColor(Color.black);
            g2d.draw(rect);
            BufferedImage image = null;
            try {
                image = ImageIO.read(new File("MySolitaire-master\\PNG\\red_back.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            super.paintComponent(g);
            setOpaque(false);
            g.drawImage(image, 0, 0,100, 150,  this);
        }

    }

    public enum Value {
        ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING
    }

    public enum Suit {
        SPADES, CLUBS, DIAMONDS, HEARTS
    }

}// END Card