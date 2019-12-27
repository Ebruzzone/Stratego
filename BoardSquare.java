
public class BoardSquare {
	Piece piece;
	int xPosition=-1;
	int yPosition=-1;

	public BoardSquare(int _xPosition, int _yPosition) {
		setXYValues(_xPosition,_yPosition);
	}
	
	public void setXYValues(int _xPosition, int _yPosition){
		if (_xPosition >= 0 && _xPosition <=9 && _yPosition >= 0 && _yPosition <=9) {
			xPosition = _xPosition;
			yPosition = _yPosition;
		}
	}
	
	public Piece getPiece() {
		return piece; 
	}
	
	public void setPiece(Piece _piece) {
	  piece=_piece;
	}
	
	public int getXPosition() {
		return xPosition;
	}
	
	public int getYPosition() {
		return yPosition;
	}
	
	protected void removePiece(){
		piece = null;
	}
}
