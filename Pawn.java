import java.awt.*;

class Pawn extends Component {

    private String type;
    private int force;
    private String player;
    private Graphics2D graphics;
    private int size;

    Pawn(String type, int size, String p, Graphics2D graphics) {
        super();
        this.size = size;
        this.type = type;
        if (type.equals("Scout")) {
            force = 2;
        } else if (type.equals("Bomb!")) {
            force = 11;
        } else if (type.equals("Miner")) {
            force = 3;
        } else if (type.equals("Sergeant")) {
            force = 4;
        } else if (type.equals("Lieutenant")) {
            force = 5;
        } else if (type.equals("Captain")) {
            force = 6;
        } else if (type.equals("Major")) {
            force = 7;
        } else if (type.equals("Colonel")) {
            force = 8;
        } else if (type.equals("General")) {
            force = 9;
        } else if (type.equals("Marshal")) {
            force = 10;
        } else if (type.equals("Spy")) {
            force = 1;
        } else if (type.equals("Flag")) {
            force = 0;
        }

        player = p;
        this.graphics = graphics;

        setSize(size, size);
    }

    Pawn(int force, int size) {
        this.size = size;
        graphics = null;
        setSize(size, size);
        player = Stratego.player;
        this.force = force;
        setType();
    }

    Pawn(int force, Graphics2D g, int size, String p) {
        this.size = size;
        graphics = g;
        setSize(size, size);
        player = p;
        this.force = force;
        setType();
    }

    private void setType() {
        if (force == 0) {
            type = "Flag";
        } else if (force == 1) {
            type = "Spy";
        } else if (force == 2) {
            type = "Scout";
        } else if (force == 3) {
            type = "Miner";
        } else if (force == 4) {
            type = "Sergeant";
        } else if (force == 5) {
            type = "Lieutenant";
        } else if (force == 6) {
            type = "Captain";
        } else if (force == 7) {
            type = "Major";
        } else if (force == 8) {
            type = "Colonel";
        } else if (force == 9) {
            type = "General";
        } else if (force == 10) {
            type = "Marshal";
        } else if (force == 11) {
            type = "Bomb!";
        }
    }

    Pawn clonePawn() {
        return new Pawn(type, size, player, graphics);
    }

    int getForce() {
        return force;
    }

    String getType() {
        return type;
    }

    void setGraphics(Graphics2D graphics) {
        this.graphics = graphics;
    }

    void draw() {

        int y = size / 4 + 1, x = size;

        graphics.setColor(Color.blue);
        graphics.setFont(new Font("verdana", Font.BOLD, 12));

        if (type.length() > 8) {
            graphics.drawString(type.substring(0, 3), x / 2 - 12, y);
            graphics.drawString(type.substring(3, type.length()), (x / 17 + 4), y * 2);
        } else if (type.length() > 5) {
            graphics.drawString(type, ((8 - type.length()) * x / 17) + 4, y);
        } else if (type.length() == 5) {
            graphics.drawString(type, x / 2 - 21, y);
        } else if (type.length() == 4) {
            graphics.drawString("Flag", x / 2 - 16, y);
        } else {
            graphics.drawString("Spy", x / 2 - 14, y);
        }

        graphics.drawString(String.valueOf(force), force > 9 ? x / 2 - 9 : x / 2 - 5, y * 3 + 2);
    }

    void drawOpp() {

        graphics.setColor(Color.red);
        graphics.setFont(new Font("verdana", Font.BOLD, 18));
        graphics.drawString(player, size / 2 - 12, size / 2 - 8);
    }

    Boolean fight(int oppForce) {
        if (force == 3 && oppForce == 11) {
            return true;
        }

        if (force == 1 && oppForce == 10) {
            return true;
        }

        if (force == oppForce) {
            return null;
        } else {
            return force > oppForce;
        }
    }
}
