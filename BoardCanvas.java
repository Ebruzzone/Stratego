import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

class BoardCanvas extends Canvas implements MouseListener, MouseMotionListener {

    private BufferedImage buffer;
    private Graphics2D graphics;
    private Cell[][] boardPawn;
    private BoxCanvas box;
    private BoxCanvas boxOpp;
    private int sizeCell;
    private int nx;
    private int ny;
    private int oldX = -1;
    private int oldY = -1;
    private Point lastSelected;
    private AI ai;
    private AI_Move ai_move;
    private ThreadThinkAI thinkAI;

    BoardCanvas(BoxCanvas box, BoxCanvas boxOpp, int sizeCell, Cell[][] cells, AI ai, ThreadThinkAI ttai) {
        super();
        boardPawn = cells;
        nx = boardPawn[0].length;
        ny = boardPawn.length;

        this.sizeCell = sizeCell;
        setSize(sizeCell * nx, sizeCell * ny);

        thinkAI = ttai;
        this.box = box;
        this.boxOpp = boxOpp;
        this.ai = ai;
        ai.board = boardPawn;
        lastSelected = null;

        ai_move = new AI_Move(ttai, boardPawn);
        new Thread(ai_move).start();

        addMouseListener(this);
        addMouseMotionListener(this);
    }

    void loadBoard() {

        for (int i = 5; i < ny; i++) {
            for (int j = 0; j < nx; j++) {

            }
        }
    }

    public void setSize(int w, int h) {
        super.setSize(w + 1, h + 1);
        createBuffer();
    }

    private void reset() {
        paint(graphics);
    }

    public void update(Graphics g) {
        paint(g);
    }

    public void paint(Graphics g) {
        g.drawImage(buffer, 0, 0, this);
    }

    private void createBuffer() {
        buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        graphics = buffer.createGraphics();
        update();
    }

    void update() {

        update(graphics);
        reset();
        drawBackgroundSquares();
        repaint();
        drawPawns();

        if (Stratego.state == Stratego.STATE.drawOpp) {

            new Thread(new ThreadAddOppPawn(boardPawn, this)).start();
            Stratego.state = Stratego.STATE.endDrawOpp;
        }
    }

    public void mouseClicked(MouseEvent e) {

    }

    public void mousePressed(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {
        int x = e.getX() / sizeCell;
        int y = e.getY() / sizeCell;
        processClicked(x, y);
    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

        oldY = -1;
        oldX = -1;
        update();
    }

    private void processClicked(int x, int y) {
        if (isValidBoardSquare(x, y)) {
            if (Stratego.state == Stratego.STATE.setup) {
                if (box.getSelectedPieceKind() == -1 && !boardPawn[y][x].empty) {
                    box.pushPawn(boardPawn[y][x].force);
                    boardPawn[y][x].empty = true;
                    update();
                    return;
                }

                if (box.getSelectedPieceKind() == -1) {
                    return;
                }

                if (boardSquareIsWater(x, y) || !boardPawn[y][x].empty || y < 6) {
                    return;
                }

                Pawn piece = box.popPawn();

                boardPawn[y][x].force = (piece.getForce());
                boardPawn[y][x].empty = false;
                boardPawn[y][x].player = Stratego.player;
                boardPawn[y][x].pawn = piece.clonePawn();

                boardPawn[y][x].pawn.setGraphics((Graphics2D) graphics.create(x * sizeCell, y * sizeCell, sizeCell, sizeCell));

                update();


            } else if (Stratego.state == Stratego.STATE.combat && Stratego.getTurn()) {

                if (!boardPawn[y][x].empty && !boardPawn[y][x].player.equals("AI")) {

                    resetColors();

                    lastSelected = new Point(x, y);

                    boardPawn[y][x].color = Color.yellow;

                    if (boardPawn[y][x].force == 0 || boardPawn[y][x].force == 11) {
                        update();
                        return;
                    }

                    if (boardPawn[y][x].force == 2) {
                        boolean f1 = true, f2 = true, f3 = true, f4 = true;

                        for (int i = 1; i < 10; i++) {
                            if (f1 && isValidBoardSquare(x + i, y)) {
                                f1 = drawNearPawn(x + i, y);
                            }

                            if (f2 && isValidBoardSquare(x - i, y)) {
                                f2 = drawNearPawn(x - i, y);
                            }

                            if (f3 && isValidBoardSquare(x, y + i)) {
                                f3 = drawNearPawn(x, y + i);
                            }

                            if (f4 && isValidBoardSquare(x, y - i)) {
                                f4 = drawNearPawn(x, y - i);
                            }
                        }
                    } else {

                        if (isValidBoardSquare(x + 1, y)) {
                            drawNearPawn(x + 1, y);
                        }
                        if (isValidBoardSquare(x - 1, y)) {
                            drawNearPawn(x - 1, y);
                        }
                        if (isValidBoardSquare(x, y + 1)) {
                            drawNearPawn(x, y + 1);
                        }
                        if (isValidBoardSquare(x, y - 1)) {
                            drawNearPawn(x, y - 1);
                        }
                    }

                    update();

                } else if (boardPawn[y][x].color == Color.green) {
                    movePawn(x, y);
                    resetColors();
                    Stratego.resetTurn();
                    lastSelected = null;
                    ai_move.doTurn();
                    update();
                } else if (boardPawn[y][x].color == Color.pink) {
                    attackPawn(x, y);
                    resetColors();
                    Stratego.resetTurn();
                    lastSelected = null;
                    ai_move.doTurn();
                    update();
                }
            }
        }
    }

    private void movePawn(int x, int y) {
        boardPawn[y][x].player = Stratego.player;
        boardPawn[y][x].empty = false;
        boardPawn[y][x].selected = false;
        boardPawn[y][x].pawn = boardPawn[lastSelected.y][lastSelected.x].pawn;
        boardPawn[y][x].pawn.setGraphics((Graphics2D) graphics.create(x * sizeCell, y * sizeCell, sizeCell, sizeCell));
        boardPawn[y][x].pawn.draw();
        boardPawn[y][x].force = boardPawn[lastSelected.y][lastSelected.x].pawn.getForce();
        boardPawn[lastSelected.y][lastSelected.x].pawn = null;
        boardPawn[lastSelected.y][lastSelected.x].empty = true;
        boardPawn[lastSelected.y][lastSelected.x].selected = false;
        boardPawn[lastSelected.y][lastSelected.x].player = null;

        PawnInfoAI pai=ai_move.pawns.remove(boardPawn[lastSelected.y][lastSelected.x]);
        pai.justMove=true;
        pai.explor=pai.explor||((y + x - lastSelected.y - lastSelected.x) > 1 || (y + x - lastSelected.y - lastSelected.x) < -1);
        pai.posy=y;
        pai.posx=x;
        ai_move.pawns.put(boardPawn[y][x],pai);
    }

    private void attackPawn(int x, int y) {

        Pawn pOpp = boardPawn[y][x].pawn, p = boardPawn[lastSelected.y][lastSelected.x].pawn;
        Boolean flag = p.fight(pOpp.getForce());

        //pari
        if (flag == null) {

            boxOpp.popPawn(pOpp.getForce());
            box.popPawn(p.getForce());

            boardPawn[lastSelected.y][lastSelected.x].pawn = null;
            boardPawn[lastSelected.y][lastSelected.x].empty = true;
            boardPawn[lastSelected.y][lastSelected.x].selected = false;
            boardPawn[lastSelected.y][lastSelected.x].player = null;
            boardPawn[y][x].pawn = null;
            boardPawn[y][x].empty = true;
            boardPawn[y][x].selected = false;
            boardPawn[y][x].player = null;

            ai_move.pawns.remove(boardPawn[lastSelected.y][lastSelected.x]);
            ai_move.pawns.remove(boardPawn[y][x]);

        } else if (flag) {//win

            boxOpp.popPawn(pOpp.getForce());

            boardPawn[y][x].player = Stratego.player;
            boardPawn[y][x].empty = false;
            boardPawn[y][x].selected = false;
            boardPawn[y][x].pawn = boardPawn[lastSelected.y][lastSelected.x].pawn;
            boardPawn[y][x].pawn.setGraphics((Graphics2D) graphics.create(x * sizeCell, y * sizeCell, sizeCell, sizeCell));
            boardPawn[y][x].pawn.draw();
            boardPawn[y][x].force = boardPawn[lastSelected.y][lastSelected.x].pawn.getForce();
            boardPawn[lastSelected.y][lastSelected.x].pawn = null;
            boardPawn[lastSelected.y][lastSelected.x].empty = true;
            boardPawn[lastSelected.y][lastSelected.x].selected = false;
            boardPawn[lastSelected.y][lastSelected.x].player = null;

            ai_move.pawns.remove(boardPawn[y][x]);

            PawnInfoAI pai=ai_move.pawns.remove(boardPawn[lastSelected.y][lastSelected.x]);
            pai.justMove=true;
            pai.explor=pai.explor||((y + x - lastSelected.y - lastSelected.x) > 1 || (y + x - lastSelected.y - lastSelected.x) < -1);
            pai.posy=y;
            pai.posx=x;
            ai_move.pawns.put(boardPawn[y][x],pai);

            if (pOpp.getForce() == 0) {
                Stratego.winning();
            }
        } else {//lose

            box.popPawn(p.getForce());

            boardPawn[lastSelected.y][lastSelected.x].pawn = null;
            boardPawn[lastSelected.y][lastSelected.x].empty = true;
            boardPawn[lastSelected.y][lastSelected.x].selected = false;
            boardPawn[lastSelected.y][lastSelected.x].player = null;

            ai_move.pawns.remove(boardPawn[lastSelected.y][lastSelected.x]);

            if (p.getForce() == 0) {//da togliere!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                Stratego.losing();
            }
        }
    }

    private void resetColors() {

        for (int i = 0; i < ny; i++) {
            for (int j = 0; j < nx; j++) {
                boardPawn[i][j].color = Color.lightGray;
            }
        }

        oldX = -1;
        oldY = -1;
    }

    private boolean drawNearPawn(int x, int y) {
        if (!boardPawn[y][x].empty) {
            if (boardPawn[y][x].player.equals("AI")) {

                boardPawn[y][x].color = Color.pink;
                return false;
            }
        } else if (!boardSquareIsWater(x, y)) {

            boardPawn[y][x].color = Color.green;
            return true;
        }

        return false;
    }

    private void drawBackgroundSquares() {
        for (int i = 0; i < ny; i++) {
            for (int j = 0; j < nx; j++) {
                if (oldY == i && oldX == j) {
                    continue;
                }

                graphics.setColor(Color.black);
                graphics.drawRect(j * sizeCell, i * sizeCell, sizeCell, sizeCell);

                graphics.setColor(boardPawn[i][j].color);

                graphics.fillRect(j * sizeCell + 1, i * sizeCell + 1, sizeCell - 1, sizeCell - 1);

            }
        }
        graphics.setColor(Color.blue);
        graphics.fillRect(sizeCell * 2 + 1, sizeCell * 4 + 1, sizeCell * 2 - 1, sizeCell * 2 - 1);
        graphics.fillRect(sizeCell * 6 + 1, sizeCell * 4 + 1, sizeCell * 2 - 1, sizeCell * 2 - 1);
    }

    private void drawPawns() {

        for (Cell[] aBoardPawn : boardPawn) {
            for (int i = 0; i < boardPawn[0].length; i++) {
                if (!aBoardPawn[i].empty) {
                    if (aBoardPawn[i].player.equals("AI")) {
                        aBoardPawn[i].pawn.drawOpp();
                    } else {
                        aBoardPawn[i].pawn.draw();
                    }
                }
            }
        }
    }

    private boolean isValidBoardSquare(int x, int y) {
        return x >= 0 && x < nx && y >= 0 && y < ny;
    }

    private boolean boardSquareIsWater(int x, int y) {
        return (x == 2 || x == 3 || x == 6 || x == 7) && (y == 4 || y == 5);
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        drawCells(e.getX() / sizeCell, e.getY() / sizeCell);
    }

    private void drawCells(int x, int y) {
        if (isValidBoardSquare(x, y) && boardPawn[y][x].color == Color.lightGray) {

            oldX = -1;
            oldY = -1;

            update();

            if (!boardPawn[y][x].empty && !boardPawn[y][x].player.equals("AI1")) {/////////////////////DA CAMBIARE////////////////
                graphics.setColor(Color.red);
                graphics.drawRect(x * sizeCell, y * sizeCell, sizeCell, sizeCell);
                graphics.setColor(Color.cyan);
                graphics.fillRect(x * sizeCell + 1, y * sizeCell + 1, sizeCell - 1, sizeCell - 1);

                oldX = x;
                oldY = y;

                boardPawn[y][x].pawn.draw();
            }
        }
    }

    void addOpp() {

        thinkAI.doThink();
        ai.setup(graphics);

        updateHashMapAI();

        update();
    }

    private void updateHashMapAI(){
        for (int i=0;i<10;i++){
            for (int j=0;j<10;j++){
                if(!boardPawn[i][j].empty){
                    if(i<4){
                        ai_move.pawns.put(boardPawn[i][j],new PawnInfoAI(true,j,i,boardPawn[i][j].force));
                    }else {
                        ai_move.pawns.put(boardPawn[i][j],new PawnInfoAI(false,j,i,-1));
                    }
                }
            }
        }
    }

    void stopThinkAI() {
        thinkAI.endThink();
    }
}
