import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ShowRulesListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        JDialog ruleFrame = new JDialog(Solitaire.getFrame(), true);
        ruleFrame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        ruleFrame.setSize(Solitaire.getTableHeight(), Solitaire.getTableWidth());
        JScrollPane scroll;
        JEditorPane rulesTextPane = new JEditorPane("text/html", "");
        rulesTextPane.setEditable(false);
        String rulesText = "</p>\n" +
                "The original Fan solitaire game is very difficult to win and so several variations have been developed over the years that make the game easier and hence more enjoyable.\n" +
                "Solitaire City contains six of the best Fan based games - Demon Fan, Three Shuffles and a Draw, Super Flower Garden, Shamrocks, La Belle Lucie and the original Fan rules.\n" +
                "<p>\n" +
                "\n" +
                "<h2>Demon Fan Card Layout</h2>\n" +
                "\n" +
                "<p>\n" +
                "Fan Games are played with a single pack of 52 playing cards. After thoroughly shuffling the \n" +
                "are dealt. Each of the first sixteen fans consist of three cards and the final two fans contain just two.\n" +
                "The top card of each fan is turned face up. A game of Demon Fan can then begin.\n" +
                "</p>\n" +
                "\n" +
                "\n" +
                "<h2>Objective</h2>\n" +
                "The object of the game is to build the four foundations up in ascending suit sequence from Ace to King" +
                "\n" +
                "<h2>Demon Fan Rules</h2>\n" +
                "Cards must be moved one at a time. The exposed face up card at the top of each fan is available for play." +
                " As each Ace becomes available it is transferred to the right of the fans to start one of the four foundations. " +
                "The foundations are built up in ascending suit sequence from Ace to the King. An exposed fan card may be" +
                " transferred to another fan if it forms a descending sequence of alternating colours, e.g. 7 on 8 or 8 on 9." +
                " If the movement of a card exposes a face down card, then it is turned face up and is available for play. " +
                "When a fan becomes empty, it cannot be filled again, not even by a King. When no more moves can be made," +
                " all the remaining cards in the fans are gathered together, re-shuffled and dealt again into fans of three" +
                " cards and the game continues. Demon Fan rules allow you to gather and re-shuffle the cards six times" +
                " giving a generous seven deals including the first one. You'll either be able to win the game by transferring" +
                " all the cards to the foundations or the game will block after the final shuffle." +
                "<h2>La Belle Lucie Card Layout</h2>\n" +
                "\n" +
                "Demon Fan is the only fan game in Solitaire City where some of the cards are dealt face down. In the other fan games, all 52 cards are dealt face up and are visible from the start.\n" +
                "The number of cards in the final two fans can vary between fan games too. Most of the other fan games are dealt with seventeen fans of three cards with a single\n" +
                "card making up the final fan. Here's how a game of La Belle Lucie is dealt:\n" +
                "\n" +
                "\n" +
                "<h2>Three Shuffles and a Draw Rules</h2>\n" +
                "<p>\n" +
                "Three Shuffles and a Draw is played exactly the same as La Belle Lucie, except after the final re-deal/shuffle,\n" +
                "you're allowed to move any card that's buried near the bottom of a fan, to the top of that fan in order to free it.\n" +
                "This is called <em>\"The Draw\"</em> and can only be done once each game, hence the name <em>Three Shuffles</em> (including the first one)\n" +
                "and a single <em>Draw</em>.\n" +
                "</p>\n" +
                "\n" +
                "<h2>Special Controls</h2>\n" +
                "<p>\n" +
                "Click on a fan to open it up so you can see the cards beneath. Click anywhere outside the fan to close it again.\n" +
                "After the final re-shuffle in Three Shuffles and a Draw, click on the fan to open it up and then click on any of the buried cards to move it to the top of the fan.\n" +
                "The cards that can be moved will highlight in blue as your pointer hovers over them after you've opened the fan.\n" +
                "<h2>Strategy</h2>\n" +
                "Once you move a card from one fan to another, you block all the cards beneath until the top card can be moved to the foundations or you re-shuffle.\n" +
                "Therefore, consider whether the card you are about to build on can be moved to another fan first. Kings cannot be moved anywhere except to the foundations so it's\n" +
                "always safe to build on a King as the cards beneath it are already blocked. Similarly, it's always safe to build on a packed sequence of cards as the cards beneath are blocked.\n" +
                "It's also safe to build onto a fan containing just one card. However there is no real point in moving the last card in a fan to another fan as the empty space that's\n" +
                "created cannot be built on.";
        rulesTextPane.setText(rulesText);
        ruleFrame.add(scroll = new JScrollPane(rulesTextPane));
        ruleFrame.setVisible(true);
    }
}

