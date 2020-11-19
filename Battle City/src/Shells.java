import java.util.concurrent.TimeUnit;

public class Shells implements Runnable {
    private int x;
    private int y;
    private final String direction;
    private final int tankCPUNumber;
    Thread t;

    @Override
    public void run() {
        try {
            shoot(); //shoot a shell
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        if (t == null) {
            t = new Thread(this); //create a new thread
            t.start(); //start the new thread, call the run method
        }
    }

    public Shells() {
        this.x = GameGUI.tank.getX(); //set the x to tank x position
        this.y = GameGUI.tank.getY(); //set the y to tank y position
        this.direction = GameGUI.tank.getDirection();  //set the direction to tank direction
        this.tankCPUNumber = -1;
        this.start();
    }

    public Shells(int i) {
        this.x = GameGUI.tankCPU[i].getX(); //set the x to tank x position
        this.y = GameGUI.tankCPU[i].getY(); //set the y to tank y position
        this.direction = GameGUI.tankCPU[i].getDirection(); //set the direction to enemy tank direction
        this.tankCPUNumber = i;
        this.start();
    }

    public void shoot() throws InterruptedException {
        switch (direction) {
            case "UP":
                for (int i = 0; i < 10; i++) {
                    GameGUI.drawMap();
                    checkTankPosition(); //check not hit the shooting tank
                    y--;
                    if (y == -1 || isHitWall() || isHitTarget()) {
                        if (y == -1 || isHitWall()) {
                            y--;
                        } else {
                            hitTarget();
                        }
                        t.interrupt();
                        break;
                    }
                    GameGUI.getMap()[y][x] = 8;
                    TimeUnit.MILLISECONDS.sleep(100);
                }
                break;
            case "DOWN":
                for (int i = 0; i < 10; i++) {
                    GameGUI.drawMap();
                    checkTankPosition(); //check not hit the shooting tank
                    y++;
                    if (y == 10 || isHitWall() || isHitTarget()) {
                        if (y == 10 || isHitWall()) {
                            y++; //let shell go out the map
                        } else {
                            hitTarget();
                        }
                        t.interrupt();
                        break;
                    }
                    GameGUI.getMap()[y][x] = 8;
                    TimeUnit.MILLISECONDS.sleep(100);
                }
                break;
            case "LEFT":
                for (int i = 0; i < 20; i++) {
                    GameGUI.drawMap();
                    checkTankPosition(); //check not hit the shooting tank
                    x--;
                    if (x == -1 || isHitWall() || isHitTarget()) {
                        if (x == -1 || isHitWall()) {
                            x--; //let shell go out the map
                        } else {
                            hitTarget();
                        }
                        t.interrupt();
                        break;
                    }
                    GameGUI.getMap()[y][x] = 8;
                    TimeUnit.MILLISECONDS.sleep(100);
                }
                break;
            case "RIGHT":
                for (int i = 0; i < 20; i++) {
                    GameGUI.drawMap();
                    checkTankPosition(); //check not hit the shooting tank
                    x++;
                    if (x == 20 || isHitWall() || isHitTarget()) {
                        if (x == 20 || isHitWall()) {
                            x++; //let shell go out the map
                        } else {
                            hitTarget();
                        }
                        t.interrupt();
                        break;
                    }
                    GameGUI.getMap()[y][x] = 8;
                    TimeUnit.MILLISECONDS.sleep(100);
                }
                break;
        }
        t.interrupt();
    }

    public boolean isHitWall() {
        try {
            return GameGUI.getMap()[y][x] == 1; //check if shell hit the wall
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("out of bound");
            return false;
        }
    }

    public boolean isHitTarget() {
        return GameGUI.getMap()[y][x] == 2 || GameGUI.getMap()[y][x] == 3 || GameGUI.getMap()[y][x] == 4 || GameGUI.getMap()[y][x] == 5
                || GameGUI.getMap()[y][x] == 6 || GameGUI.getMap()[y][x] == 7; //check if tank hit the target
    }

    public void checkTankPosition() {
        if (!isHitTarget()) {
            GameGUI.getMap()[y][x] = 0; //if not hit target, then set to 0 to make shell move
        }
    }

    public void hitTarget() {
        if (tankCPUNumber == -1) {
            for (int i = 0; i < 4; i++) {
                if (x == GameGUI.tankCPU[i].getX() && y == GameGUI.tankCPU[i].getY()) {
                    GameGUI.getMap()[GameGUI.tankCPU[i].getY()][GameGUI.tankCPU[i].getX()] = 0; //if it hit the enemy
                    GameGUI.tankCPU[i].setLife(); //kill enemy tank
                }
            }
        } else {
            if (x == GameGUI.tank.getX() && y == GameGUI.tank.getY() || GameGUI.getMap()[y][x] == 2) {
                GameGUI.getMap()[GameGUI.tank.getY()][GameGUI.tank.getX()] = 0; //if it hit the tank
                GameGUI.tank.setLife(); //set life -1
            }
        }
    }

}

