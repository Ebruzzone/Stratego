import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class Stratego extends JFrame implements ActionListener{

	public static final int Setup=0;
	public static final int SetupDone=5;
	public static final int Connecting=10;
	public static final int YourMove=15;
	public static final int HisMove=20;
	public static final int GameOver=25;
	
	private Panel panel;
	private BoxCanvas boxCanvas;
	private BoardCanvas boardCanvas;
	private Controller controller;
	protected JButton commit;
	private int state;
	private JTextField ip;
	private JTextField port;
	private JComboBox combo;
	
	Stratego(Controller _controller){
		setTitle("Stratego");
		setSize(720,720);
		state = Stratego.Setup;
		setDefaultCloseOperation(EXIT_ON_CLOSE); 
		controller = _controller;
		controller.setStratego(this);

		combo = new JComboBox();
		combo.addItem("Server");
		combo.addItem("Client");
		ip = new JTextField("localhost");
		port = new JTextField("1234");
		Panel connectionPanel = new Panel();
		//Container paneConnectionPanel = connectionPanel.getContentPane();
		connectionPanel.setLayout(new BorderLayout());
		connectionPanel.add(combo, BorderLayout.WEST);
		connectionPanel.add(ip, BorderLayout.CENTER);
		connectionPanel.add(port, BorderLayout.EAST);

		panel = new Panel();
		boxCanvas = new BoxCanvas(controller);
		boardCanvas =	new BoardCanvas(controller);	
		commit = new JButton("Make Connection");
		commit.setEnabled(false);
		Container contentpane = getContentPane();
		contentpane.setLayout(new BorderLayout());
		contentpane.add(connectionPanel, BorderLayout.SOUTH);
		contentpane.add(boxCanvas, BorderLayout.EAST);
		contentpane.add(boardCanvas, BorderLayout.WEST);
		contentpane.add(commit, BorderLayout.NORTH);
		
		commit.addActionListener(this);
	}
	
	public BoxCanvas getBoxCanvas(){
		return boxCanvas;
	}
	
	public BoardCanvas getBoardCanvas(){
		return boardCanvas;
	}
	
	public void update() {
		boolean done = controller.getBox().checkAllPiecesPlaced();
		switch(state){
			case Stratego.Setup		: 	if(done){ setup2setupdone(); }	break;										
			case Stratego.SetupDone	:	if(!done){ setupdone2setup(); }	break;
			case Stratego.Connecting:	break;
			case Stratego.YourMove	:	break;
			case Stratego.HisMove	:	break;
			case Stratego.GameOver	:	break;
		}
	}
	
	public void actionPerformed(ActionEvent e){
		String action = e.getActionCommand();
		if(action == "Make Connection"){
			setupdone2connecting();
		}else if(action == "Abort Connection"){
			connecting2setupdone();
		}
	}
	
	public int getPlayState(){return state; }
	
	public static void main(String args[]){
		Controller controller = new Controller();

		Stratego stratego = new Stratego(controller);
		Box box = new Box(controller);
		Board board = new Board(controller);

		stratego.show();
	}
	
	protected void setup2setupdone(){
		state=Stratego.SetupDone;
		commit.setEnabled(true);
	}
	
	protected void setupdone2setup(){
		state=Stratego.Setup;
		commit.setEnabled(false);
	}
	
	protected void setupdone2connecting(){
		if(state==Stratego.SetupDone){
			commit.setText("Abort Connection");
			setConnectionChooser(false);
			state=Stratego.Connecting;
		}
	}
	
	protected void connecting2setupdone(){
		if(state==Stratego.Connecting){
			commit.setText("Make Connection");
			setConnectionChooser(true);
			state=Stratego.SetupDone;
		}
	}
	
	private void setConnectionChooser(boolean b){
		combo.setEnabled(b);
		ip.setEditable(b);
		port.setEditable(b);
	}
}
