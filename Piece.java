class Piece {
	public static final int Flag=0;
	public static final int Marshal=10;
	public static final int General=9;
	public static final int Colonel=8;
	public static final int Major=7;
	public static final int Captain=6;
	public static final int Lieutenant=5;
	public static final int Sergeant=4;
	public static final int Miner=3;
	public static final int Scout=2;
	public static final int Spy=1;
	public static final int Bomb=11;
	
	private int number;
	private String name;
	
	Piece (int _number){
		number = _number;
		setName();
	}
	
	public static boolean isValidPieceKind(int _piecenr){
		return (_piecenr>=0 && _piecenr<=11);
	}

	
	public String getName(){
		return name;
	}
	
	public int getNumber(){
		return number;
	}
	
	public Piece copy(){
		return new Piece(number);
	}
	public boolean equals(Object obj){
		if(obj instanceof Piece){
			Piece _piece = (Piece) obj;
			return (name.equals(_piece.getName()) )&& (number==_piece.getNumber());
		}
		return false;
	}
	
	private void setName(){
		switch(number){
			case 0  : name = "Flag";break;
			case 10  : name = "Marshal";break;
			case 9  : name = "General";break;
			case 8  : name = "Colonel";break;
			case 7  : name = "Major";break;
			case 6  : name = "Captain";break;
			case 5  : name = "Lieutenant";break;
			case 4  : name = "Sergeant";break;
			case 3  : name = "Miner";break;
			case 2  : name = "Scout";break;
			case 1 : name = "Spy";break;
			case 11 : name = "Bomb!";
		}
	}
}
