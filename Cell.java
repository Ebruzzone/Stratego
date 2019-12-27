import java.awt.*;

class Cell{

    String player;
    int force;
    boolean empty;
    Pawn pawn;
    boolean selected;
    Color color;

    Cell(){
        empty=true;
        selected=false;
        color=Color.lightGray;
    }
}
