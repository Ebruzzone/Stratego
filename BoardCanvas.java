import java.awt.*;
import java.lang.Math;
import java.awt.event.*;

class BoardCanvas extends BufferedCanvas{
	
	BoardCanvas(Controller _controller){
		super(_controller);
		controller.setBoardCanvas(this);
		setSize(700,700);
	}
	
	public void setSize(int w, int h){
		int size = (h/10)*10+1;
		super.setSize(size, size);
		createBuffer();
	}
	
	public void update(){
		drawBackgroundSquares(graphics);
		Board board = controller.getBoard();
		if(board!=null){
			drawBoard(graphics);
		}
		repaint();
	}
	
	public void mouseClicked(MouseEvent e){
		int squareSize = getHeight()/10;
		int x = e.getX()/squareSize;
		int y = e.getY()/squareSize;
		processClicked(x,y);		
	}
	
	protected void processClicked(int x, int y){
		if(Board.isValidBoardSquare(x,y) ){
			Board board = controller.getBoard();
			board.processClicked(x, y);
		}
	}
	
	private void drawBackgroundSquares(Graphics g){
		int squareSize = getHeight()/10;
		for(int i=0;i<10;i++){
			for(int j=0;j<10;j++){
				g.setColor(Color.black);
				g.drawRect(i*squareSize,j*squareSize,squareSize,squareSize);
				g.setColor(Color.green);
				g.fillRect(i*squareSize+1,j*squareSize+1,squareSize-1,squareSize-1);
				
			}
		}
		g.setColor(Color.blue);
		g.fillRect(squareSize*2+1,squareSize*4+1,squareSize*2-1,squareSize*2-1);
		g.fillRect(squareSize*6+1,squareSize*4+1,squareSize*2-1,squareSize*2-1);	
	}
	
	private void drawBoard(Graphics g){
		int squareSize = getHeight()/10;
		Piece piece;
		for(int i=0;i<10;i++){
			for(int j=0;j<10;j++){
				piece = controller.getBoard().getBoardSquare(i,j).getPiece();
				if(piece!=null){
					drawPiece(piece,graphics.create(i*squareSize,j*squareSize,squareSize,squareSize) );
				}
			}
		}
	}
	
	private void drawPiece(Piece piece, Graphics g){
		int y = getHeight()/40+1;
		g.setColor(Color.black);
		g.setFont(new Font("verdana",Font.BOLD,12));
		String name=piece.getName();
		if(name.length()>8){

            g.drawString(name.substring(0,3),21,y);
            g.drawString(name.substring(3,name.length()), (int) ((12-name.length())*3.5),y*2);
        }else if(name.length()>5) {
            g.drawString(name, (8 - name.length())*4+2,y);
        }else if(name.length()==5){
            g.drawString(name, 12,y);
        } else if(name.length()==4) {
            g.drawString("Flag", 17,y);
        }else {
            g.drawString("Spy", 19,y);
        }
		g.drawString(String.valueOf(piece.getNumber()),piece.getNumber()>9?getHeight()/20-8:getHeight()/20-4,y*3+2);
	}
}
