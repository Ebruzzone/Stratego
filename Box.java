import java.util.Stack;

class Box{

	protected Stack[] pieces;
	protected Controller controller;
	protected int selectedPieceKind;

	Box(Controller _controller){
		controller = _controller;
		controller.setBox(this);
		pieces = new Stack[12];
		initBox();
		selectedPieceKind = -1;
	}
	
	public int getPieceAmount(int _piece){
		return pieces[_piece].size();
	}
	
	public Piece peekPiece(int _piece){
	
		return new Piece(_piece);
	}
	
	public void setSelectedPieceKind(int _piece){
		if (selectedPieceKind == _piece){
			selectedPieceKind = -1;
		} else if(getPieceAmount(_piece)==0){
			selectedPieceKind = -1;
		} else {
			selectedPieceKind = _piece;
		}
		controller.updateBoxCanvas();
	}
	
	public int getSelectedPieceKind(){
		return selectedPieceKind;
	}
	
	public void deselectPieceKind(){
		selectedPieceKind = -1;
	}
	
	public Piece popPiece(){
		Piece piece = (Piece)pieces[selectedPieceKind].pop();
		if(getPieceAmount(selectedPieceKind)==0){
				selectedPieceKind = nextPieceSelection(selectedPieceKind);;
		}
		return piece;
	}
	
	public boolean checkAllPiecesPlaced(){
		for(int pieceKind = 0; pieceKind < 12; pieceKind++){
			if(!pieces[pieceKind].isEmpty()){
				return false;
			}
		}
		return true;
	}

	private void initBox(){
		initStacks();
		pushPieces(1,new Piece(Piece.Flag) );
		pushPieces(1,new Piece(Piece.Marshal) );
		pushPieces(1,new Piece(Piece.General) );
		pushPieces(2,new Piece(Piece.Colonel) );
		pushPieces(3,new Piece(Piece.Major) );
		pushPieces(4,new Piece(Piece.Captain) );
		pushPieces(4,new Piece(Piece.Lieutenant) );
		pushPieces(4,new Piece(Piece.Sergeant) );
		pushPieces(5,new Piece(Piece.Miner) );
		pushPieces(8,new Piece(Piece.Scout) );
		pushPieces(1,new Piece(Piece.Spy) );
		pushPieces(6,new Piece(Piece.Bomb) );
	}
	
	private void initStacks(){
		for(int i=0;i<12;i++){
			pieces[i]= new Stack();
		}
	}
	
	private void pushPieces(int ammount, Piece piece){
		Piece p;
		int what = piece.getNumber();
		for(int i=0;i<ammount;i++){
			p=(Piece) piece.copy();
			pieces[what].push(p);
		}
	}
	
	private int nextPieceSelection(int currentSelection){
		for(int i=currentSelection+1;i<pieces.length;i++){
			if(getPieceAmount(i)>0){
				return i;
			}
		}
		return -1;
	}
	
	protected void pushPiece(Piece _piece){
		Piece piece = _piece;
		int pieceKind = piece.getNumber();
		pieces[pieceKind].push(piece);
	}
	
}
