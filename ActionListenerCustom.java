import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionListenerCustom implements ActionListener {

    private Stratego stratego;

    ActionListenerCustom(Stratego s){
        stratego=s;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(stratego.isFullScreen){
            stratego.setFullScreen(true);
        }else {
            stratego.setFullScreen(false);
        }
    }
}
