public class Board {

	protected BoardSquare boardSquare[][];
	protected Controller controller;
	
	public Board(Controller _controller) {
		controller = _controller;
		controller.setBoard(this);
		boardSquare = new BoardSquare[10][10];
		initBoard();
	}
	
	public BoardSquare getBoardSquare(int i, int j){
		return boardSquare[i][j];
	}
	
	public void processClicked(int x, int y){
		if (controller.getPlayState()<=Stratego.SetupDone){
			Box box = controller.getBox();
			if(box.getSelectedPieceKind() == -1 && boardSquareContainsPiece(x, y)){
				box.pushPiece(getBoardSquare(x, y).getPiece() );
				getBoardSquare(x, y).removePiece();
				controller.updateStratego();
				controller.updateBoxCanvas();
				controller.updateBoardCanvas();
				return;
			}
			if(box.getSelectedPieceKind() == -1) {
				return;
			}
			if(boardSquareIsWater(x, y) || boardSquareContainsPiece(x, y)
			||boardSquareNotInZone(x, y)) {
				return;
			}
			Piece piece = box.popPiece();
			boardSquare[x][y].setPiece( piece );
		}else{
		}	
		controller.updateStratego();
		controller.updateBoxCanvas();
		controller.updateBoardCanvas();
	}

	public static boolean isValidBoardSquare(int x, int y){
		if(x<0 || x>9){
			return false;
		} else if(y<0 || y>9){
			return false;
		} else {
			return true;
		}
	}
	
	protected boolean boardSquareIsWater(int x, int y){
		if((x==2 || x==3 || x==6 || x==7) && (y==4 || y==5)){
			return true;
		} else {
			return false;
		}
	}

	protected boolean boardSquareContainsPiece(int x, int y){
		if(getBoardSquare(x,y).getPiece() != null){
			return true;
		} else {
			return false;
		}
	}

	protected boolean boardSquareNotInZone(int x, int y){
		if(y>=0 && y<6){
			return true;
		} else {
			return false;
		}
	}
	
	private void initBoard(){
		for(int i=0;i<10;i++){
			for(int j=0;j<10;j++){
				boardSquare[i][j] = new BoardSquare(i,j);
			}
		}
	}
}
	
