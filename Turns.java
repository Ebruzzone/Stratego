import javax.swing.*;

class Turns {
    private String string;
    private int turn;
    private JLabel label;

    Turns(){
        string="Turn: ";
        turn =0;
    }

    String getTurn(){
        return string+turn;
    }

    int getNturn(){
        return turn;
    }

    void setTurn(int turn) {
        this.turn = turn;
        label.setText(this.getTurn());
        label.repaint();
    }

    void addTurn(){
        turn++;
        label.setText(this.getTurn());
        label.repaint();
    }

    void setLabel(JLabel label) {
        this.label = label;
    }
}
