import java.awt.*;
import java.util.*;

class AI {

    private int size;
    private Random r, r1;
    Cell[][] board;
    private HashMap<Integer, LinkedList<Point>> zone;
    private int[] amount;
    private int[] firstLine;
    private Graphics graphics;
    private Point[] bombs;
    private Point Flag;
    private LinkedList<Point> limits;

    AI(int sizeCell) {
        size = sizeCell;
        r = new Random(new Date().getTime());
        amount = new int[]{1, 1, 8, 5, 4, 4, 4, 3, 2, 1, 1, 6};
        zone = new HashMap<Integer, LinkedList<Point>>();
        bombs = new Point[6];
        Flag = new Point();
        limits = new LinkedList<Point>();

        for (int i = 0; i < 4; i++) {
            limits.add(new Point(-1, i));
            limits.add(new Point(10, i));
        }

        for (int i = 0; i < 10; i++) {
            limits.add(new Point(i, -1));
        }

        limits.add(new Point(2, 4));
        limits.add(new Point(3, 4));
        limits.add(new Point(6, 4));
        limits.add(new Point(7, 4));

        for (int f = 0; f < 15; f++) {

            LinkedList<Point> points = new LinkedList<Point>();

            switch (f) {
                case 0:
                    for (int i = 0; i < 2; i++) {
                        for (int j = 0; j < 10; j++) {
                            points.add(new Point(j, i));
                        }
                    }
                    break;
                case 1:
                    for (int i = 1; i < 3; i++) {
                        for (int j = 1; j < 9; j++) {
                            points.add(new Point(j, i));
                        }
                    }
                    break;
                case 2:
                    break;
                case 3:
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 10; j++) {
                            points.add(new Point(j, i));
                        }
                    }
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    break;
                case 7:
                    break;
                case 8:
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 10; j++) {
                            points.add(new Point(j, i));
                        }
                    }
                    break;
                case 9:
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 10; j++) {
                            points.add(new Point(j, i));
                        }
                    }
                    break;
                case 10:
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 10; j++) {
                            points.add(new Point(j, i));
                        }
                    }
                    break;
                case 11:
                    break;
                case 12:
                    for (int i = 1; i < 3; i++) {
                        for (int j = 0; j < 5; j++) {
                            points.add(new Point(j, i));
                        }
                    }
                    break;
                case 13:
                    for (int i = 1; i < 3; i++) {
                        for (int j = 5; j < 10; j++) {
                            points.add(new Point(j, i));
                        }
                    }
                    break;
                case 14:
                    for (int i = 2; i < 4; i++) {
                        for (int j = 0; j < 10; j++) {
                            points.add(new Point(j, i));
                        }
                    }
                    break;
            }

            zone.put(f, points);
        }
    }

    void setup(Graphics graphics) {

        r1 = new Random(new Date().getTime());
        this.graphics = graphics;
        String p = "AI";
        int force = r.nextInt(14) - 2;
        force = force < 0 ? 0 : force;
        boolean flag, endFront = false;
        firstLine = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        int[][] b = new int[4][10],b1=new int[4][10];
        int count = 0,bombFront=0;

        for (int k = 0; k < 40;) {

            if (count++ > 200) {
                reset();
                count = 0;
                endFront = false;
                continue;
            }
/*
            if (k > 0) {
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 10; j++) {
                        b[i][j] = board[i][j].force;
                        b1[i][j] = board[i][j].empty?0:1;
                    }
                }

                System.out.println(Arrays.toString(amount));
                System.out.println(Arrays.deepToString(b1));
                System.out.println(Arrays.deepToString(b));
            }*/

            if (endFront) {
                flag = true;
            } else {
                flag = r.nextBoolean();
            }

            if (flag) {
                while (getAmount(force) < 1) {
                    force = r.nextInt(1000 + k * k) % 14 - 2;
                    force = force < 0 ? 0 : force;
                }

                Point point = findPos(force);

                if (point == null) {

                    reset();
                    endFront = false;
                    continue;
                } else if (point.y == -1) {

                    r = new Random(new Date().getTime());
                    force = r.nextInt(12);
                    continue;
                } else if (point.y == 3) {

                    if (controlFirstLine(force)) {

                        firstLine[point.x] = force;
                    } else {

                        reset();
                        endFront = false;
                        continue;
                    }
                }

                if (force == 10 || force == 9 || force == 1) {
                    modAmount(1);
                    modAmount(9);
                    modAmount(10);
                    force = 1;
                } else {
                    modAmount(force);
                }

                board[point.y][point.x].pawn = new Pawn(force, (Graphics2D) graphics.create(point.x * size, point.y * size, size, size), size, p);
                board[point.y][point.x].player = p;
                board[point.y][point.x].empty = false;
                board[point.y][point.x].force = force;

                if (force == 11) {
                    bombs[5 - getAmount(11)] = point;
                }

                if (force == 0) {
                    Flag = point;
                }
            } else {
                int i = r.nextInt(2) + 2, j = r.nextInt(10);

                while (!board[i][j].empty) {
                    i = r.nextInt(2) + 2;
                    j = r.nextInt(10);
                }

                int f = posPawn(i);

                if (f == 0) {
                    reset();
                    endFront = false;
                    continue;
                } else {

                    if (f == 11 && getAmount(11) < 3) {
                        reset();
                        endFront = false;
                        continue;
                    }

                    if (getAmount(f) < 1) {
                        reset();
                        endFront = false;
                        continue;
                    }

                    modAmount(f);

                    if (i == 3) {
                        if (controlFirstLine(f)) {
                            firstLine[j] = f;
                        } else {
                            reset();
                            endFront = false;
                            continue;
                        }
                    }

                    if (f==11){
                        if(++bombFront>2){
                            bombFront=0;
                            reset();
                            endFront=false;
                            continue;
                        }
                    }

                    board[i][j].pawn = new Pawn(f, (Graphics2D) graphics.create(j * size, i * size, size, size), size, p);
                    board[i][j].player = p;
                    board[i][j].empty = false;
                    board[i][j].force = f;

                    if (f == 11) {
                        bombs[5 - getAmount(11)] = new Point(j, i);
                    }
                }
            }

            if (!endFront) {
                endFront = controlZone(zone.get(14));
            }

            k = (40 - sumAmount());

            if (k==40) {
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 10; j++) {
                        b[i][j] = board[i][j].force;
                        b1[i][j] = board[i][j].empty?0:1;
                    }
                }

                System.out.println(Arrays.toString(amount));
                System.out.println(Arrays.deepToString(b1));
                System.out.println(Arrays.deepToString(b));

                if (controlBombs() || controlField()) {
                    endFront=false;
                    reset();
                    k=0;
                }
            }
        }

        amount = new int[]{1, 1, 8, 5, 4, 4, 4, 3, 2, 1, 1, 6};

        allEmpty();

        Stratego.setTurn();
    }

    private void allEmpty(){
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 10; j++) {
                board[i][j].empty=true;
            }
        }
    }

    private boolean controlField() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 10; j++) {
                if (board[i][j].empty) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean controlBombs() {
        LinkedList<Integer> p = new LinkedList<Integer>();
        LinkedList<Point> pp = new LinkedList<Point>();

        for (int i = 0; i < bombs.length; i++) {
            for (int j = i + 1; j < bombs.length; j++) {
                if (near(bombs[i], bombs[j]) && !(near(bombs[i], Flag) && near(bombs[j], Flag))) {
                    p.add(i);
                    p.add(j);
                }
            }
        }

        for (int i = 0; i < bombs.length; i++) {
            if (p.contains(i)) {
                pp.addLast(bombs[i]);
            }
        }

        for (int i = 0; i < pp.size(); i++) {

            if (nearBomb(pp.get(i), pp)) {
                return true;
            }
        }

        return false;
    }

    private boolean nearBomb(Point p, LinkedList<Point> pp) {
        Point a = new Point(p.x - 1, p.y), b = new Point(p.x + 1, p.y), c = new Point(p.x, p.y - 1), d = new Point(p.x, p.y + 1);

        return !((pp.contains(a) || limits.contains(a) || a == Flag || movable(a, pp)) && (pp.contains(b) || limits.contains(b) || b == Flag || movable(b, pp)) && (pp.contains(c) || limits.contains(c) || c == Flag || movable(c, pp)) && (pp.contains(d) || limits.contains(d) || d == Flag || movable(d, pp)));
    }

    private boolean movable(Point p, LinkedList<Point> pp) {
        Point a = new Point(p.x - 1, p.y), b = new Point(p.x + 1, p.y), c = new Point(p.x, p.y - 1), d = new Point(p.x, p.y + 1);

        return (pp.contains(a) || limits.contains(a)) && (pp.contains(b) || limits.contains(b)) && (pp.contains(c) || limits.contains(c)) && (pp.contains(d) || limits.contains(d));
    }

    private boolean near(Point a, Point b) {
        return a.x <= b.x + 1 && a.x >= b.x - 1 && a.y <= b.y + 1 && a.y >= b.y - 1;
    }

    private int sumAmount() {
        int sum = 0;

        for (int anAmount : amount) {
            sum += anAmount;
        }

        return sum;
    }

    //2 da 6, 2 da 7, 3 da 4, 3 da 5, 1 B, 3 da 2, 1 da 3 e almeno 3 di 6 e 7
    private boolean controlFirstLine(int force) {

        if (force == 1) {
            return false;
        }

        int count = 0;

        for (int aFirstLine : firstLine) {
            if (aFirstLine != 0 && aFirstLine != 7 && aFirstLine != 6) {
                count++;
            }
        }

        if (count > 5 && force < 6) {
            return false;
        }

        count = 0;

        for (int aFirstLine : firstLine) {
            if (aFirstLine == force) {
                count++;
            }
        }

        if (count == 0) {
            return true;
        } else if (count < 2) {
            return force != 3 && force != 11;
        } else if (count < 3) {
            return force < 6;
        } else {
            return false;
        }
    }

    private int posPawn(int y) {

        int force, count = 0;

        if (y == 3) {

            force = r.nextInt(7) + 2;
            force = force == 8 ? 11 : force;

            while (getAmount(force) < 1 && !controlFirstLine(force)) {
                force = r.nextInt(7) + 2;
                force = (force == 3 || force == 8) && r.nextBoolean() ? r.nextInt(7) + 2 : force;
                force = force == 8 ? 11 : force;

                if (count++ > 100) {
                    return 0;
                }
            }

            return force;
        } else {

            force = r.nextInt(7) + 2;
            force = force == 8 ? 11 : force;

            while (getAmount(force) < 1) {
                force = r.nextInt(7) + 2;
                force = (force == 3 || force == 8) && (r.nextBoolean() || r.nextBoolean()) ? r.nextInt(7) + 2 : force;
                force = force == 8 ? 11 : force;

                if (count++ > 100) {
                    return 0;
                }
            }

            return force;
        }
    }

    private void reset() {
        amount = new int[]{1, 1, 8, 5, 4, 4, 4, 3, 2, 1, 1, 6};
        firstLine = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        r = new Random(new Date().getTime() + r.nextLong() + r1.nextLong());

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 10; j++) {
                board[i][j].empty = true;
                board[i][j].force = 0;
            }
        }
    }

    private Point addSpy() {

        if (controlZone(zone.get(13), 2) || controlZone(zone.get(12), 2)) {
            return null;
        }

        int i, j, c = 0;
        Boolean flag;

        do {
            i = r.nextInt(100) / 50 + 1;
            j = r.nextBoolean() ? (r.nextInt(3) + 1) : (r.nextInt(3) + 6);
            flag = addGeneral(j, i);

            if (flag == null)
                return null;

            if (c++ > 100) {
                return null;
            }
        } while (!board[i][j].empty && !flag);

        return new Point(j, i);
    }

    private Boolean addGeneral(int x, int y) {

        Boolean flag;

        if (board[y][x - 1].empty || board[y][x + 1].empty || board[y - 1][x].empty || board[y - 1][x + 1].empty || board[y - 1][x - 1].empty) {
            flag = addMarshal(x < 5);

            if (flag) {

                int i, j;
                do {
                    i = r.nextInt(100) / 50 + 1;
                    j = x < 5 ? (r.nextInt(5)) : (r.nextInt(5) + 5);
                } while (!board[i][j].empty);

                board[i][j].pawn = new Pawn(9, (Graphics2D) graphics.create(j * size, i * size, size, size), size, "AI");
                board[i][j].player = "AI";
                board[i][j].empty = false;
                board[i][j].force = 9;

                return true;

            } else {
                return null;
            }
        } else {
            return false;
        }
    }

    private boolean addMarshal(boolean flag) {

        int i, j;
        do {
            i = r.nextInt(100) / 50 + 1;
            j = flag ? (r.nextInt(5) + 5) : (r.nextInt(5));
        } while (!board[i][j].empty);

        board[i][j].pawn = new Pawn(10, (Graphics2D) graphics.create(j * size, i * size, size, size), size, "AI");
        board[i][j].player = "AI";
        board[i][j].empty = false;
        board[i][j].force = 10;

        return true;
    }

    private Point findPos(int force) {

        int i = -1, j = -1, c = 0;

        if (controlZone(zone.get(force))) {
            return null;
        }

        if (getAmount(force) < 1) {
            return null;
        }

        switch (force) {
            case 0:
                if (!emptyNearFlagAndBomb()) {
                    return null;
                }

                do {
                    i = r.nextInt(100) / 94;
                    j = r.nextInt(10);

                    if (c++ > 100) {
                        return null;
                    }

                    if (board[i][j].empty) {
                        if (emptyNearFlag(j, i)) {
                            break;
                        }
                    }
                } while (true);
                break;
            case 1:
                return addSpy();
            case 2:
                do {
                    i = r.nextInt(4);
                    j = r.nextInt(10);

                    if (c++ > 100) {
                        return null;
                    }
                } while (!board[i][j].empty || (i == 3 && !controlFirstLine(2)));
                break;
            case 3:
                do {
                    i = (r.nextInt(100) / 45) + ( r.nextInt(100) / 50);
                    j = r.nextInt(10);

                    if (c++ > 100) {
                        return null;
                    }
                } while (!board[i][j].empty || (i == 3 && !controlFirstLine(3)));
                break;
            case 4:
                if (getAmount(4) < 3) {
                    do {
                        i = r.nextInt(4);
                        j = r.nextInt(10);

                        if (c++ > 100) {
                            return null;
                        }
                    } while (!board[i][j].empty || (i == 3 && !controlFirstLine(4)));
                }
                break;
            case 5:
                if (getAmount(5) < 3) {
                    do {
                        i = r.nextInt(4);
                        j = r.nextInt(10);

                        if (c++ > 100) {
                            return null;
                        }
                    } while (!board[i][j].empty || (i == 3 && !controlFirstLine(5)));
                }
                break;
            case 6:
                if (getAmount(6) < 3) {
                    do {
                        i = r.nextInt(4);
                        j = r.nextInt(10);

                        if (c++ > 100) {
                            return null;
                        }
                    } while (!board[i][j].empty || (i == 3 && !controlFirstLine(6)));
                }
                break;
            case 7:
                if (getAmount(7) < 2) {
                    do {
                        i = r.nextInt(2) + 1;
                        j = r.nextInt(10);

                        if (c++ > 100) {
                            return null;
                        }
                    } while (!board[i][j].empty);
                }
                break;
            case 8:
                do {
                    i = r.nextInt(3);
                    j = r.nextInt(10);

                    if (c++ > 100) {
                        return null;
                    }
                } while (!board[i][j].empty);
                break;
            case 9:
                return addSpy();
            case 10:
                return addSpy();
            case 11:
                if (getAmount(11) < 2) {
                    do {
                        i = r.nextInt(100) / 40 + r.nextInt(100) / 70;
                        j = r.nextInt(10);

                        if (c++ > 100) {
                            return null;
                        }
                    } while (!board[i][j].empty || (i == 3 && !controlFirstLine(11)));
                } else if (getAmount(11) < 4) {
                    do {
                        i = r.nextInt(100) / 70;
                        j = r.nextInt(10);

                        if (c++ > 100) {
                            return null;
                        }
                    } while (!board[i][j].empty);
                }
                break;
        }

        return new Point(j, i);
    }

    private boolean controlZone(LinkedList<Point> points) {

        return controlZone(points, 1);
    }

    private boolean controlZone(LinkedList<Point> points, int n) {
        if (points.size() == 0) {
            return false;
        }

        for (Point point : points) {
            if (board[point.y][point.x].empty) {
                n--;
            }
        }

        return n > 0;
    }

    private boolean emptyNearFlag(int x, int y) {

        if (getAmount(11) == 2) {
            if (x == 0) {
                if (board[y][x + 1].empty && board[y + 1][x].empty) {

                    board[y][x + 1].pawn = new Pawn(11, (Graphics2D) graphics.create((x + 1) * size, y * size, size, size), size, "AI");
                    board[y][x + 1].player = "AI";
                    board[y][x + 1].empty = false;
                    board[y][x + 1].force = 11;
                    board[y + 1][x].pawn = new Pawn(11, (Graphics2D) graphics.create(x * size, (y + 1) * size, size, size), size, "AI");
                    board[y + 1][x].player = "AI";
                    board[y + 1][x].empty = false;
                    board[y + 1][x].force = 11;

                    modAmount(11);
                    bombs[5 - getAmount(11)] = new Point(x + 1, y);
                    modAmount(11);
                    bombs[5 - getAmount(11)] = new Point(x, y + 1);

                    return true;
                }
            } else if (x == 9) {

                if (board[y][x - 1].empty && board[y + 1][x].empty) {

                    board[y][x - 1].pawn = new Pawn(11, (Graphics2D) graphics.create((x - 1) * size, y * size, size, size), size, "AI");
                    board[y][x - 1].player = "AI";
                    board[y][x - 1].empty = false;
                    board[y][x - 1].force = 11;
                    board[y + 1][x].pawn = new Pawn(11, (Graphics2D) graphics.create(x * size, (y + 1) * size, size, size), size, "AI");
                    board[y + 1][x].player = "AI";
                    board[y + 1][x].empty = false;
                    board[y + 1][x].force = 11;

                    modAmount(11);
                    bombs[5 - getAmount(11)] = new Point(x - 1, y);
                    modAmount(11);
                    bombs[5 - getAmount(11)] = new Point(x, y + 1);

                    return true;
                }
            }

            return false;
        } else {

            if (x == 0) {
                if (board[y][x + 1].empty && board[y + 1][x].empty) {

                    board[y][x + 1].pawn = new Pawn(11, (Graphics2D) graphics.create((x + 1) * size, y * size, size, size), size, "AI");
                    board[y][x + 1].player = "AI";
                    board[y][x + 1].empty = false;
                    board[y][x + 1].force = 11;
                    board[y + 1][x].pawn = new Pawn(11, (Graphics2D) graphics.create(x * size, (y + 1) * size, size, size), size, "AI");
                    board[y + 1][x].player = "AI";
                    board[y + 1][x].empty = false;
                    board[y + 1][x].force = 11;

                    modAmount(11);
                    bombs[5 - getAmount(11)] = new Point(x + 1, y);
                    modAmount(11);
                    bombs[5 - getAmount(11)] = new Point(x, y + 1);

                    if (y == 1 && board[y - 1][x].empty && getAmount(11) > 0) {

                        board[y - 1][x].pawn = new Pawn(11, (Graphics2D) graphics.create(x * size, (y - 1) * size, size, size), size, "AI");
                        board[y - 1][x].player = "AI";
                        board[y - 1][x].empty = false;
                        board[y - 1][x].force = 11;

                        modAmount(11);
                        bombs[5 - getAmount(11)] = new Point(x, y - 1);
                    }

                    return true;
                }
            } else if (x == 9) {

                if (board[y][x - 1].empty && board[y + 1][x].empty) {

                    board[y][x - 1].pawn = new Pawn(11, (Graphics2D) graphics.create((x - 1) * size, y * size, size, size), size, "AI");
                    board[y][x - 1].player = "AI";
                    board[y][x - 1].empty = false;
                    board[y][x - 1].force = 11;
                    board[y + 1][x].pawn = new Pawn(11, (Graphics2D) graphics.create(x * size, (y + 1) * size, size, size), size, "AI");
                    board[y + 1][x].player = "AI";
                    board[y + 1][x].empty = false;
                    board[y + 1][x].force = 11;

                    modAmount(11);
                    bombs[5 - getAmount(11)] = new Point(x - 1, y);
                    modAmount(11);
                    bombs[5 - getAmount(11)] = new Point(x, y - 1);

                    if (y == 1 && board[y - 1][x].empty && getAmount(11) > 0) {

                        board[y - 1][x].pawn = new Pawn(11, (Graphics2D) graphics.create(x * size, (y - 1) * size, size, size), size, "AI");
                        board[y - 1][x].player = "AI";
                        board[y - 1][x].empty = false;
                        board[y - 1][x].force = 11;

                        modAmount(11);
                        bombs[5 - getAmount(11)] = new Point(x, y - 1);
                    }

                    return true;
                }
            } else {

                if (board[y][x - 1].empty && board[y + 1][x].empty && board[y][x + 1].empty) {

                    board[y][x - 1].pawn = new Pawn(11, (Graphics2D) graphics.create((x - 1) * size, y * size, size, size), size, "AI");
                    board[y][x - 1].player = "AI";
                    board[y][x - 1].empty = false;
                    board[y][x - 1].force = 11;
                    board[y + 1][x].pawn = new Pawn(11, (Graphics2D) graphics.create(x * size, (y + 1) * size, size, size), size, "AI");
                    board[y + 1][x].player = "AI";
                    board[y + 1][x].empty = false;
                    board[y + 1][x].force = 11;
                    board[y][x + 1].pawn = new Pawn(11, (Graphics2D) graphics.create((x + 1) * size, y * size, size, size), size, "AI");
                    board[y][x + 1].player = "AI";
                    board[y][x + 1].empty = false;
                    board[y][x + 1].force = 11;

                    modAmount(11);
                    bombs[5 - getAmount(11)] = new Point(x - 1, y);
                    modAmount(11);
                    bombs[5 - getAmount(11)] = new Point(x + 1, y);
                    modAmount(11);
                    bombs[5 - getAmount(11)] = new Point(x, y + 1);

                    if (y == 1 && board[y - 1][x].empty && getAmount(11) > 0) {

                        board[y - 1][x].pawn = new Pawn(11, (Graphics2D) graphics.create(x * size, (y - 1) * size, size, size), size, "AI");
                        board[y - 1][x].player = "AI";
                        board[y - 1][x].empty = false;
                        board[y - 1][x].force = 11;

                        modAmount(11);
                        bombs[5 - getAmount(11)] = new Point(x, y - 1);
                    }

                    return true;
                }
            }

            return false;
        }
    }

    private boolean emptyNearFlagAndBomb() {

        if (getAmount(11) < 2) {
            return false;
        } else if (getAmount(11) == 2) {

            for (Point point : zone.get(0)) {
                if (board[point.y][point.x].empty && (point.x == 0 || point.x == 9)) {
                    if (board[point.y + 1][point.x].empty) {
                        if (point.x == 0 && board[point.y][point.x + 1].empty) {

                            return true;
                        } else if (point.x == 9 && board[point.y][point.x - 1].empty) {

                            return true;
                        }
                    }
                }
            }

            return false;
        } else {

            for (Point point : zone.get(0)) {
                if (board[point.y][point.x].empty) {

                    if (point.x == 0) {
                        if (board[point.y + 1][point.x].empty && board[point.y][point.x + 1].empty) {
                            return true;
                        }
                    } else if (point.x == 9) {

                        if (board[point.y + 1][point.x].empty && board[point.y][point.x - 1].empty) {
                            return true;
                        }
                    } else {

                        if (board[point.y + 1][point.x].empty && board[point.y][point.x + 1].empty && board[point.y][point.x - 1].empty) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    private void modAmount(int force) {
        amount[force]--;
    }

    private int getAmount(int force) {
        return amount[force];
    }
}
