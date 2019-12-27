class PawnInfoAI {

    int posx;
    int posy;
    boolean ai;
    int force;
    boolean justMove;
    Boolean explor;

    PawnInfoAI(boolean ai,int x,int y,int force,boolean justMove,boolean explor){
        this.ai=ai;
        posx=x;
        posy=y;
        this.force=force;
        this.justMove=justMove;
        this.explor=explor;
    }

    PawnInfoAI(boolean ai,int x,int y,int force){
        this.ai=ai;
        posx=x;
        posy=y;
        this.force=force;
        this.justMove=false;
        this.explor=false;
    }
}
