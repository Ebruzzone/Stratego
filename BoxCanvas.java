import java.awt.*;
import java.awt.image.BufferedImage; 
import java.awt.event.*;

class BoxCanvas extends BufferedCanvas {
	
	BoxCanvas(Controller _controller){
		super(_controller);
		controller.setBoxCanvas(this);
		setSize(50,600);
	}
	
	public void setSize(int w, int h){
		super.setSize( (h/12), ((h/12)*12)+1 );
		createBuffer();
	}
	
	public void mouseClicked(MouseEvent e){
		int y = e.getY()/getWidth();
		processClicked(y);
	}

	public void update(){
		drawBackground();
		Box box = controller.getBox();
		if(box instanceof Box){
			drawBox(box);
		}
		repaint();
	}
	
	protected void processClicked(int pieceKind){
		if(controller.getStratego().getState()<Stratego.SetupDone){
			if(Piece.isValidPieceKind(pieceKind) ){
				Box box = controller.getBox();
				box.setSelectedPieceKind(pieceKind);
			}
		}
	}
	
	private void drawBackground(){
		int width = getWidth();
		graphics.setColor(Color.black);
		for(int i=0;i<12;i++){
			graphics.drawRect(0,i*width,width,width);
		}
		graphics.setColor(Color.white);
		for(int i=0;i<12;i++){		
			graphics.fillRect(1,i*width+1,width-1,width-1);
		}
	}
	
	private void drawBox(Box box){
		int width = getWidth();
		int pieceKind = box.getSelectedPieceKind();
		drawHighlight(graphics.create(1,pieceKind*width+1,width-1,width-1));
		for(int i=0;i<12;i++){
			drawPiece(box.peekPiece(i), box.getPieceAmount(i), graphics.create(0,i*width,width,width) );
		}
	}
	
	private void drawHighlight(Graphics g){
		g.setColor(Color.yellow);
		g.fillRect(0,0,getWidth(),getWidth());
	}
	
	private void drawPiece(Piece piece, int ammount,Graphics g){
		int y = getWidth()/4+1;
		g.setColor(Color.black);
		g.setFont(new Font("verdana",Font.BOLD,12));
		if(piece.getName().length()>4){
			g.drawString(piece.getName().substring(0,5),1,y);
			g.drawString(piece.getName().substring(5,piece.getName().length()),1,y*2);
		}else {
            g.drawString(piece.getName(),1,y);
        }

        String s=piece.getNumber()+"  #"+ammount;
		s=s.replace("  #",piece.getNumber()>9?" #":"  #");
		g.drawString(s,1,y*3+2);
	}
}