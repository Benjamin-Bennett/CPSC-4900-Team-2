import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ToggleTimerListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        Solitaire.toggleTimer();
        if (!Solitaire.isTimeRunning()) {
            Solitaire.getToggleTimerButton().setText("Start Timer");
        } else {
            Solitaire.getToggleTimerButton().setText("Pause Timer");
        }
    }

}
