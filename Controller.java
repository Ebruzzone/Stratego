class Controller {

	protected Box box;
	protected BoxCanvas boxCanvas;
	protected Board board;
	protected BoardCanvas boardCanvas;
	protected Stratego stratego;

	Controller(){
	}
	
	public void setBoxCanvas(BoxCanvas _boxCanvas){
		boxCanvas = _boxCanvas;
	}
	
	public BoxCanvas getBoxCanvas(){
		return boxCanvas;
	}
	
	public void setBox(Box _box){
		box = _box;
	}
	
	public Box getBox(){
		return box;
	}
	
	public void setBoardCanvas(BoardCanvas _boardCanvas){
		boardCanvas = _boardCanvas;
	}
	
	public BoardCanvas getBoardCanvas(){
		return boardCanvas;
	}
	
	public void setBoard(Board _board){
		board = _board;
	}
	
	public Board getBoard(){
		return board;
	}

	public int getPlayState(){
		if(stratego!=null){
			return stratego.getPlayState();
		}else{
			return 0;
		}
	}

	public Stratego getStratego(){
		return stratego;
	}

	public void setStratego(Stratego _stratego){
		stratego = _stratego;
	}
	
	public void updateBoxCanvas(){
		if(boxCanvas!=null){
			boxCanvas.update();
		}
	}

	public void updateBoardCanvas(){
		if(boardCanvas!=null){
			boardCanvas.update();
		}
	}
	
	public void updateStratego(){
		if(stratego!=null){
			stratego.update();
		}
	}
}
