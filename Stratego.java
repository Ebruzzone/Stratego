import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

class Stratego extends JFrame implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == Load) {
            loadBoard = true;
        } else if (e.getSource() == LoadB) {
            loadBoard = null;
        }

        setup();

        new Thread(new Runnable() {
            @Override
            public void run() {

                synchronized (o){
                    try {
                        o.wait();
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }

                finish();
            }
        }).start();
    }

    enum STATE {
        setup, drawOpp, endDrawOpp, combat, win, winOpp
    }

    static STATE state = STATE.setup;
    static String player;           //human player name
    private BoxCanvas box;          //box con le pedine del player umano
    private BoxCanvas boxOpp;       //box con le pedine dell'AI
    private BoardCanvas board;      //board 10*10
    private JButton start;          //button per iniziare appena si è finito di settare tutto il campo di gioco
    private JButton start2;         //button per iniziare appena si è finito di settare tutto il campo di gioco
    private JButton Load;             //button per dare l'ok
    private JButton LoadB;             //button per dare l'ok
    private JButton New;             //button per dare l'ok
    private JTextField textName;     //area per il nome dell'umano
    private JPanel panel;
    private final int totX;
    private final int totY;
    private final int boxSize = 56;
    private final int cellSize = 68;
    private static boolean turn;            //true se è il turno dell'umano
    private Boolean loadBoard;
    private JLayeredPane layered;
    private static final GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
    boolean isFullScreen;
    private DisplayMode dm;
    static final Turns Nturn = new Turns();
    private JLabel background;
    private static final Object o=new Object();

    private Stratego() {

        layered = new JLayeredPane();
        this.setContentPane(layered);

        loadBoard = false;
        setTitle("Stratego");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);

        device.setFullScreenWindow(this);
        dm = device.getDisplayMode();
        isFullScreen = true;

        totX = this.getWidth();
        totY = this.getHeight();

        JPanel mainPanel = new JPanel();

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        panel = new JPanel(new GridBagLayout());
        panel.setVisible(true);
        panel.setSize(totX, totY);

        GridBagConstraints constr = new GridBagConstraints();
        constr.insets = new Insets(5, 5, 5, 5);

        constr.anchor = GridBagConstraints.CENTER;

        constr.gridx = 1;
        constr.gridy = 0;

        New = new JButton("New game");
        New.setEnabled(false);
        New.setFont(new Font("verdana", Font.PLAIN, 12));

        New.addActionListener(this);

        Load = new JButton("Load board");
        Load.setEnabled(false);
        Load.setFont(new Font("verdana", Font.PLAIN, 12));

        Load.addActionListener(this);

        LoadB = new JButton("Load game");
        LoadB.setEnabled(false);
        LoadB.setFont(new Font("verdana", Font.PLAIN, 12));

        LoadB.addActionListener(this);

        //area per il nome dell'umano
        JLabel text = new JLabel("Insert your name:");
        text.setFont(new Font("verdana", Font.BOLD, 18));
        text.setForeground(Color.blue);

        textName = new JTextField(15);
        textName.setLayout(null);
        textName.setFont(new Font("verdana", Font.PLAIN, 18));
        textName.setEditable(true);
        textName.setText("");

        panel.add(text, constr);

        constr.gridx = 1;
        constr.gridy = 1;

        panel.add(textName, constr);

        constr.gridx = 0;
        constr.gridy = 3;

        panel.add(Load, constr);

        constr.gridx = 1;
        constr.gridy = 3;

        panel.add(New, constr);

        constr.gridx = 2;
        constr.gridy = 3;

        panel.add(LoadB, constr);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (player == null) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (textName.getText().length() > 2 && textName.getText().length() < 12 && textName.getText().matches("[a-zA-Z]+")) {
                        New.setEnabled(true);
                        Load.setEnabled(true);
                        LoadB.setEnabled(true);
                    } else {
                        New.setEnabled(false);
                        Load.setEnabled(false);
                        LoadB.setEnabled(false);
                    }
                }
            }
        }).start();

        background = new JLabel(new ImageIcon(this.getClass().getResource("Background.png")));
        background.setVisible(true);
        background.setSize(totX, totY);

        panel.setOpaque(false);

        layered.add(panel, 1);

        layered.add(background, 10);
        layered.setVisible(true);
        this.show();
    }

    private void setup() {

        layered.remove(panel);
        layered.remove(background);

        setTurn();

        int sizeNameX = 150;
        int sizeNameY = 30;
        int boardX = (totX - cellSize * 10 - 14) / 2;
        int buttonX = 75;

        Stratego.player = textName.getText();

        AffineTransform affineTransform = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(affineTransform, true, true);
        double length = new Font("verdana", Font.BOLD, 12).getStringBounds(player, frc).getWidth();

        ThreadThinkAI thinkAI = new ThreadThinkAI(new ImageIcon(this.getClass().getResource("loaderScaled.gif")));
        new Thread(thinkAI).start();

        AI ai = new AI(cellSize);

        //oggetto per far partire l'azione dei due pulsanti di start
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startPressed();
            }
        };

        box = getNewBox(true, Color.blue);
        boxOpp = getNewBox(false, Color.red);

        Cell[][] cells = new Cell[10][10];

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                cells[i][j] = new Cell();
            }
        }

        board = new BoardCanvas(box, boxOpp, cellSize, cells, ai, thinkAI);

        if(loadBoard==null){
            board.loadBoard();
        } else if (loadBoard) {
            board.loadBoard();
        }

        //rettangolo per il nome AI
        NameCanvas nameAI = new NameCanvas("Artificial Intelligence", sizeNameX, sizeNameY, Color.red);
        nameAI.setBounds(0, 0, sizeNameX, sizeNameY);

        //rettangolo per il nome dell'umano
        NameCanvas namePlayer = new NameCanvas(player, sizeNameX, sizeNameY, Color.blue);

        namePlayer.setBounds(totX - (sizeNameX + (int) length) / 2 - 10, 0, (int) length + 10, sizeNameY);

        start = new JButton("Start!");
        start.setFont(new Font("verdana", Font.BOLD, 12));
        start.setBounds(boxSize + 10, totY / 2 - 34, buttonX, 30);

        start.addActionListener(al);
        layered.add(start, 1);

        start2 = new JButton("Start!");
        start2.setFont(new Font("verdana", Font.BOLD, 12));
        start2.setBounds(totX - boxSize - buttonX - 24, totY / 2 - 34, buttonX, 30);

        start2.addActionListener(al);
        layered.add(start2, 1);

        box.setBounds(totX - boxSize - 12, sizeNameY, boxSize, boxSize * 12);
        boxOpp.setBounds(0, sizeNameY, boxSize, boxSize * 12);
        board.setBounds(boardX, 0, cellSize * 10 + 1, cellSize * 10 + 1);

        layered.add(board, 2);
        layered.add(box, 1);
        layered.add(boxOpp, 1);
        layered.add(namePlayer, 1);
        layered.add(nameAI, 1);

        JButton TFS = new JButton("Toggle Full Screen");
        TFS.setBounds(590, 690, 170, 50);
        TFS.setEnabled(true);
        TFS.setFont(new Font("verdana", Font.PLAIN, 12));
        ActionListenerCustom actionListener = new ActionListenerCustom(this);

        TFS.addActionListener(actionListener);

        layered.add(TFS, 1);

        JLabel text = new JLabel(Nturn.getTurn());
        text.setFont(new Font("verdana", Font.BOLD, 18));
        text.setForeground(Color.black);
        text.setBounds(800, 700, 90, 30);

        Nturn.setLabel(text);

        layered.add(text, 1);

        //setEnable(false);

        this.repaint();
        thinkAI.setup(layered);
        thinkAI.doThink();
        thinkAI.endThink();
    }

    void setFullScreen(boolean flag) {
        if (flag) {
            isFullScreen = false;
            setVisible(false);
            dispose();
            setUndecorated(false);
            device.setFullScreenWindow(null);
            setBounds(0, 0, 1366, 768);
            setVisible(true);
            repaint();
        } else {
            isFullScreen = true;
            dm = device.getDisplayMode();
            setVisible(false);
            dispose();
            setUndecorated(true);
            device.setFullScreenWindow(this);
            device.setDisplayMode(dm);
            setVisible(true);
            repaint();
        }
    }

    static void resetTurn() {
        turn = false;
    }

    static void setTurn() {
        turn = true;
    }

    static boolean getTurn() {
        return turn;
    }

    private void startPressed() {
        layered.remove(start);
        layered.remove(start2);
        box.notClickableMore();
        Stratego.state = STATE.drawOpp;
        board.addOpp();
        this.repaint();
        resetTurn();
    }

    private BoxCanvas getNewBox(boolean clickable, Color c) {
        return new BoxCanvas(boxSize, cellSize, clickable, c, this);
    }

    void setEnable(boolean enable) {
        start.setEnabled(enable);
        start2.setEnabled(enable);
        this.repaint();
    }

    static void winning() {
        state = STATE.win;

        synchronized (o){
            o.notify();
        }
    }

    static void losing() {
        state = STATE.winOpp;

        synchronized (o){
            o.notify();
        }
    }

    private void finish(){
        layered.removeAll();

        JButton winOrLose=new JButton();
        winOrLose.setText(state==STATE.win?"WIN":"LOSE");
        winOrLose.setForeground(Color.red);
        winOrLose.setBounds(500,300,366,168);

        winOrLose.setFont(new Font("verdana", Font.BOLD, 50));

        ActionListenerCustom actionListener = new ActionListenerCustom(this);

        winOrLose.addActionListener(actionListener);

        layered.add(winOrLose);
        layered.repaint();
    }

    public static void main(String args[]) {

        new Stratego();
    }
}
