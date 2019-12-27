import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Stack;

class BoxCanvas extends Canvas implements MouseListener {

    private Stack<Pawn>[] pawns;
    private Graphics2D graphics;
    private int selectedPieceKind;
    private int size;
    private int sizeCell;
    private BufferedImage buffer;
    private Color color;
    private Stratego stratego;

    BoxCanvas(int s,int sizeCell,boolean clickable,Color c,Stratego st) {
        super();

        stratego=st;

        pawns = new Stack[12];
        this.sizeCell=sizeCell;
        color=c;
        initBox();
        selectedPieceKind = -1;
        size = s;
        setSize(s, s*12);
        createBuffer();

        if(clickable){
            addMouseListener(this);
        }
    }

    private void createBuffer() {
        buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        graphics = buffer.createGraphics();
        update();
    }

    public void mouseClicked(MouseEvent e) {

    }

    public void mousePressed(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {
        int y = (e.getY()) / size;
        processClicked(y);
    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    private void update() {
        update(graphics);
        drawBackground();
        drawBox();
        repaint();
    }

    public void update(Graphics g){ paint(g); }

    public void paint(Graphics g){
        g.drawImage(buffer,0,0,this);
    }

    private void processClicked(int pieceKind) {
        if (Stratego.state==Stratego.STATE.setup) {
            setSelectedPieceKind(pieceKind);
        }
    }

    private void drawBackground() {
        graphics.setColor(Color.black);

        for (int i = 0; i < 12; i++) {
            graphics.drawRect(0, i * size, size, size);
        }

        graphics.setColor(Color.white);

        for (int i = 0; i < 12; i++) {
            graphics.fillRect(1, i * size + 1, size - 1, size - 1);
        }
    }

    private void drawBox() {
        int pieceKind = getSelectedPieceKind();
        drawHighlight(graphics.create(1, pieceKind * size + 1, size - 1, size - 1));
        for (int i = 0; i < 12; i++) {
            drawPawn(i,pawns[i].peek().getType(),getPieceAmount(i),graphics.create(0,i*size,size,size));
        }
    }

    private void drawHighlight(Graphics g) {
        g.setColor(Color.yellow);
        g.fillRect(0, 0, getWidth(), getWidth());
    }

    private void drawPawn(int force, String type, int ammount, Graphics g) {
        int y = getWidth() / 4 + 1;
        g.setColor(color);
        g.setFont(new Font("verdana", Font.BOLD, 12));

        if (type.length() > 7) {
            g.drawString(type.substring(0, 3), 3, y);
            g.drawString(type.substring(3, type.length()), 3, y * 2);
        } else if (type.length() > 3) {
            g.drawString(type, 3, y);
        } else {
            g.drawString("Spy", 3, y);
        }

        g.setColor(Color.black);
        String s = force + "  #" + ammount;
        s = s.replace("  #", force > 9 ? " #" : "  #");
        g.drawString(s, 3, y * 3 + 2);
    }

    private int getPieceAmount(int _piece) {
        return pawns[_piece].size()-1;
    }

    private void setSelectedPieceKind(int _piece) {
        if (selectedPieceKind == _piece) {
            selectedPieceKind = -1;
        } else if (getPieceAmount(_piece) == 0) {
            selectedPieceKind = -1;
        } else {
            selectedPieceKind = _piece;
        }

        update();
    }

    int getSelectedPieceKind() {
        return selectedPieceKind;
    }

    Pawn popPawn() {
        Pawn Pawn = pawns[selectedPieceKind].pop();

        if (getPieceAmount(selectedPieceKind) == 0) {
            selectedPieceKind = nextPieceSelection(selectedPieceKind);
        }

        update();

        for (Stack<Pawn> pawn : pawns) {
            if (pawn.size()>1) {
                return Pawn;
            }
        }

        stratego.setEnable(true);

        return Pawn;
    }

    void popPawn(int force) {
        Pawn Pawn = pawns[force].pop();

        update();
    }

    private void initBox() {
        initStacks();
        pushPawn(1, new Pawn("Flag", sizeCell, Stratego.player, null));
        pushPawn(1, new Pawn("Marshal", sizeCell, Stratego.player, null));
        pushPawn(1, new Pawn("General", sizeCell, Stratego.player, null));
        pushPawn(2, new Pawn("Colonel", sizeCell, Stratego.player, null));
        pushPawn(3, new Pawn("Major", sizeCell, Stratego.player, null));
        pushPawn(4, new Pawn("Captain", sizeCell, Stratego.player, null));
        pushPawn(4, new Pawn("Lieutenant", sizeCell, Stratego.player, null));
        pushPawn(4, new Pawn("Sergeant", sizeCell, Stratego.player, null));
        pushPawn(5, new Pawn("Miner", sizeCell, Stratego.player, null));
        pushPawn(8, new Pawn("Scout", sizeCell, Stratego.player, null));
        pushPawn(1, new Pawn("Spy", sizeCell, Stratego.player, null));
        pushPawn(6, new Pawn("Bomb!", sizeCell, Stratego.player, null));
    }

    private void initStacks() {
        for (int i = 0; i < 12; i++) {
            pawns[i] = new Stack<Pawn>();
        }
    }

    private void pushPawn(int ammount, Pawn Pawn) {

        for (int i = 0; i < ammount+1; i++) {
            pawns[Pawn.getForce()].push(Pawn.clonePawn());
        }
    }

    private int nextPieceSelection(int currentSelection) {
        for (int i = currentSelection + 1; i < pawns.length; i++) {
            if (getPieceAmount(i) > 0) {
                return i;
            }
        }

        return -1;
    }

    void pushPawn(int force) {
        stratego.setEnable(false);
        pawns[force].push(new Pawn(force,sizeCell));
        update();
    }

    void notClickableMore(){
        removeMouseListener(this);

        initBox();

        update();
    }
}
