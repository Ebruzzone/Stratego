import java.awt.*;

class NameCanvas extends Canvas {

    private String name;
    private Color color;

    NameCanvas(String string,int x,int y,Color color){
        name=string;
        this.color=color;
        setSize(x,y);
    }

    public void paint(Graphics g){
        g.setColor(color);
        g.setFont(new Font("verdana", Font.BOLD, 12));
        g.drawString(name,5,14);
        g.setColor(new Color(0,0,0,1));
        g.fillRect(0,0,getWidth(),getHeight());
    }
}
