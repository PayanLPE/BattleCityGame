public class Tanks {
    private int x;
    private int y;
    private String direction;
    private int life;

    public Tanks() {
        this.x = 9; //initialize the x value
        this.y = 9; //initialize the y value
        this.direction = "UP"; //initialize the direction
        this.life = 3; //initialize the life value
    }

    public Tanks(String cpu, int n) {
        this.life = 1; //initialize the life value
        switch (n) {
            case 0:
                x = 0;
                y = 0;
                direction = "DOWN";
                break;
            case 1:
                x = 19;
                y = 0;
                direction = "UP";
                break;
            case 2:
                x = 0;
                y = 9;
                direction = "DOWN";
                break;
            case 3:
                x = 19;
                y = 9;
                direction = "UP";
                break;
        }

    }

    public int getX() {
        return x; //return x
    }

    public int getY() {
        return y; //return x
    }

    public String getDirection() {
        return direction; //return direction
    }

    public int getLife() {
        return life; //return life
    }

    public void setLife() {
        this.life = life -1; //get hit, life -1
    }

    public boolean isNotDied() {
        return life != 0; //check tank is died or not
    }

    public boolean isTankHitWall() {
        switch (direction) {
            case "UP":
                if (y != 0 && GameGUI.getMap()[y - 1][x] == 0) {
                    return false; //tank has not way to go
                }
                break;
            case "DOWN":
                if (y != 9 && GameGUI.getMap()[y + 1][x] == 0) {
                    return false; //tank has not way to go
                }
                break;
            case "LEFT":
                if (x != 0 && GameGUI.getMap()[y][x - 1] == 0) {
                    return false; //tank has not way to go
                }
                break;
            case "RIGHT":
                if (x != 19 && GameGUI.getMap()[y][x + 1] == 0) {
                    return false; //tank has not way to go
                }
                break;
        }
        return true;
    }

    public void moveUP(int n) {
        if (isNotDied()) {
            direction = "UP";
            GameGUI.getMap()[y][x] = 0;
            if (y != 0 && GameGUI.getMap()[y - 1][x] == 0) {
                this.y -= 1; //if not died, move up
            }
            GameGUI.getMap()[y][x] = n;
        }
    }

    public void moveDOWN(int n) {
        if (isNotDied()) {
            direction = "DOWN";
            GameGUI.getMap()[y][x] = 0;
            if (y != 9 && GameGUI.getMap()[y + 1][x] == 0) {
                this.y += 1; //if not died, move down
            }
            GameGUI.getMap()[y][x] = n;
        }
    }

    public void moveLEFT(int n) {
        if (isNotDied()) {
            direction = "LEFT";
            GameGUI.getMap()[y][x] = 0;
            if (x != 0 && GameGUI.getMap()[y][x - 1] == 0) {
                this.x -= 1; //if not died, move left
            }
            GameGUI.getMap()[y][x] = n;
        }
    }

    public void moveRIGHT(int n) {
        if (isNotDied()) {
            direction = "RIGHT";
            GameGUI.getMap()[y][x] = 0;
            if (x != 19 && GameGUI.getMap()[y][x + 1] == 0) {
                this.x += 1; //if not died, move right
            }
            GameGUI.getMap()[y][x] = n;
        }
    }

}
