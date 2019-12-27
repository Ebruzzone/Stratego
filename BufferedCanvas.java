import java.awt.*;
import java.awt.image.BufferedImage; 
import java.awt.event.*;

abstract class BufferedCanvas extends Canvas implements MouseListener{
	
	protected Controller controller;
	protected BufferedImage buffer;
	protected Graphics2D graphics;	
		
	BufferedCanvas(Controller _controller){
		controller = _controller;
		addMouseListener(this);
	}

	public void mouseClicked(MouseEvent e){

	}
	
	public void mousePressed(MouseEvent e){
	
	}
	
	public void mouseEntered(MouseEvent e){
	
	}
	public void mouseExited(MouseEvent e){
	
	}
	public void mouseReleased(MouseEvent e){
	
	}
	
	public void update(Graphics g){ paint(g); }

	public void paint(Graphics g){
		g.drawImage(buffer,0,0,this);
	}
	
	abstract void update();

	protected void createBuffer(){
		buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB );
		graphics = buffer.createGraphics() ;
		update();
	}
}
