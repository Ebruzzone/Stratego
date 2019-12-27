import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;

public class AI_Move implements Runnable {

    private boolean alive;
    private ThreadThinkAI thinkAI;
    private Cell[][] board;

    HashMap<Cell,PawnInfoAI> pawns;

    AI_Move(ThreadThinkAI ttai, Cell[][] board){
        alive=true;
        thinkAI=ttai;
        this.board=board;
        pawns=new HashMap<Cell, PawnInfoAI>();
    }

    void doTurn(){
        synchronized (this){
            this.notify();
        }
    }

    void end(){
        alive=false;
    }

    @Override
    public void run() {

        while (alive) {

            synchronized (this){
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            thinkAI.doThink();

            double gain=0;
            HashMap<Point,PawnInfoAI> p=new HashMap<Point, PawnInfoAI>();
            LinkedList<PawnInfoAI> aiPawns=new LinkedList<PawnInfoAI>();

            for (PawnInfoAI pai:pawns.values()){
                if(pai.ai){
                    aiPawns.add(pai);
                }

                p.put(new Point(pai.posx,pai.posy),pai);
            }

            

            Stratego.Nturn.addTurn();

            Stratego.setTurn();
            thinkAI.endThink();
        }
    }
}
