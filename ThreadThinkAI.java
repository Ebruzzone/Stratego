import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ThreadThinkAI implements Runnable {

    private boolean alive;
    private boolean think;
    private JLabel load;
    private JLabel load_;
    private Graphics graphics1;
    private Graphics graphics2;

    ThreadThinkAI(Icon icon) {
        alive = true;
        think = false;
        load = new JLabel(icon);
        load_ = new JLabel(icon);

        load.setBounds(491,292,99,99);
        load_.setBounds(763,292,99,99);

        BufferedImage buffer = new BufferedImage(load.getWidth(), load.getHeight(), BufferedImage.TYPE_INT_ARGB);
        graphics1 = buffer.createGraphics();
        buffer = new BufferedImage(load_.getWidth(), load_.getHeight(), BufferedImage.TYPE_INT_ARGB);
        graphics2 = buffer.createGraphics();
    }

    void setup(JLayeredPane s){

        s.add(load,1);
        s.add(load_,1);
    }

    void doThink(){
        synchronized (this){
            think=true;
            this.notify();
        }
    }

    void end(){
        alive=false;
        think=false;
        synchronized (this){
            this.notify();
        }
    }

    void endThink(){
        think=false;
    }

    @Override
    public void run() {

        while (alive) {
            load.setVisible(false);
            load_.setVisible(false);

            synchronized (this){
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            load.setVisible(true);
            load_.setVisible(true);

            while (think){
                load.paint(graphics1);
                load_.paint(graphics2);
            }
        }
    }
}
