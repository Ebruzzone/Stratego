import java.util.Date;
import java.util.Random;

public class ThreadAddOppPawn implements Runnable {

    private Cell[][] board;
    private Random r;
    private BoardCanvas boardCanvas;

    ThreadAddOppPawn(Cell[][] board,BoardCanvas bc){
        this.board=board;
        r=new Random(new Date().getTime());
        boardCanvas=bc;
    }

    private boolean allDrew(){

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 10; j++) {
                if(board[i][j].empty){
                    return false;
                }
            }
        }

        return true;
    }

    private void addPawnOpp(){

        int i= r.nextInt(4),j= r.nextInt(10);

        while (!board[i][j].empty) {
            i = r.nextInt(4);
            j = r.nextInt(10);
        }

        board[i][j].empty=false;
    }

    @Override
    public void run() {

        while (true){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(allDrew()){
                Stratego.state=Stratego.STATE.combat;
                Stratego.setTurn();
                boardCanvas.stopThinkAI();
                return;
            }

            addPawnOpp();
            boardCanvas.update();
        }
    }
}
