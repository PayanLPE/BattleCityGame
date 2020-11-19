import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

public class GameGUI extends JFrame implements KeyListener, Runnable {

    private static int[][] map; //initialize the map data
    private String historyScore;
    static ImageIcon tankUpIcon = new ImageIcon(GameGUI.class.getResource("/image/tankUP.png"));
    static ImageIcon tankDownIcon = new ImageIcon(GameGUI.class.getResource("/image/tankDOWN.png"));
    static ImageIcon tankLeftIcon = new ImageIcon(GameGUI.class.getResource("/image/tankLEFT.png"));
    static ImageIcon tankRightIcon = new ImageIcon(GameGUI.class.getResource("/image/tankRIGHT.png"));
    static ImageIcon tankCPUUpIcon = new ImageIcon(GameGUI.class.getResource("/image/tankCPUUP.png"));
    static ImageIcon tankCPUDownIcon = new ImageIcon(GameGUI.class.getResource("/image/tankCPUDOWN.png"));
    static ImageIcon tankCPULeftIcon = new ImageIcon(GameGUI.class.getResource("/image/tankCPULEFT.png"));
    static ImageIcon tankCPURightIcon = new ImageIcon(GameGUI.class.getResource("/image/tankCPURIGHT.png"));
    static ImageIcon wallIcon = new ImageIcon(GameGUI.class.getResource("/image/wall.png"));
    static ImageIcon eagleIcon = new ImageIcon(GameGUI.class.getResource("/image/eagle.png"));
    static ImageIcon roadIcon = new ImageIcon(GameGUI.class.getResource("/image/road.png"));
    static ImageIcon shellIcon = new ImageIcon(GameGUI.class.getResource("/image/shell.png"));
    static Font font = new Font("Cooper Black", Font.PLAIN, 18);
    JPanel game, score, instruction;
    static JLabel[][] mapGUI = new JLabel[10][20];
    JLabel instructionText, scoreText;
    public static Tanks tank;
    public static Tanks[] tankCPU;
    private Thread t;


    public GameGUI() {
        map = new int[][]{
                {4, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 6},
                {0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0},
                {0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 0, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 0, 1},
                {1, 0, 1, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 1, 0, 1},
                {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
                {0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0},
                {0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0},
                {5, 0, 0, 1, 0, 0, 0, 0, 0, 3, 2, 0, 0, 0, 0, 0, 1, 0, 0, 7}
        };
        tank = new Tanks(); //create a new tank object(player)
        tankCPU = new Tanks[4]; //create a new tank object array(CPU)
        for (int i = 0; i < 4; i++) {
            tankCPU[i] = new Tanks("cpu", i); //Each tank(CPU) is different instance
        }

        game = new JPanel(); //create a new game panel
        game.setLayout(new GridLayout(10, 20)); //set the layout to grid layout
        game.setBackground(Color.GRAY); //set the background colour
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 20; j++) {
                mapGUI[i][j] = new JLabel(); //using loop to create each label
                game.add(mapGUI[i][j]); //add each label to the game panel
            }
        }

        instructionText = new JLabel("<html><font color=white>Welcome to the Battle City" +
                "<br>Press <b>ENTER</b> to start" +
                "<br>Have Fun!</font></html>"); //initialize instruction text
        instructionText.setFont(font); //set the font of instruction text
        instruction = new JPanel(); //create a new instruction panel
        instruction.setBackground(Color.GRAY); //set the background colour
        instruction.add(instructionText); //add the instruction text to the instruction panel

        scoreText = new JLabel("<html><br><b>Score:</b>" +
                "<br>Current enemy <br>tank alive: <br><font color=blue>" + 4 +
                "</font><br>Current tank <br>life remain: <br><font color=red>" + tank.getLife() +
                "</font></html>"); //initialize the score text
        scoreText.setFont(font); //set the font of score text
        score = new JPanel(); //create a new score panel
        score.setLayout(new BorderLayout());
        score.setBackground(Color.GRAY); //set the background colour
        score.add(scoreText, BorderLayout.NORTH); //add the scoreText to score panel

        drawMap();
        JOptionPane.showMessageDialog(this, "<html>Kill all the enemy to win" +
                "<br>If your tank was kill or turret is destroyed, you lose<html>" +
                "<br>Using <b>UP DOWN LEFT RIGHT</b> to control the tank, <b>SPACE</b> to shoot shells");
        this.setLayout(new BorderLayout()); //set layout to border layout
        this.add(game, BorderLayout.CENTER); //add game panel to this frame
        this.add(instruction, BorderLayout.NORTH); //add instruction panel to this frame
        this.add(score, BorderLayout.EAST); //add score panel to this frame
        this.addKeyListener(this); //add action listener to this frame
        this.setTitle("Battle City");
        this.setSize(1280, 720); //set this frame size to 1080 * 720
        this.setVisible(true); //make the frame invisible
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //set default close option
    }

    public void tankCPUMove() {
        for (int i = 0; i < 4; i++) {
            if (tankCPU[i].isNotDied()) { //if the tank not died, do the move
                if (!tankCPU[i].isTankHitWall()) { //if it does not hit the wall, go straight
                    switch (tankCPU[i].getDirection()) {
                        case "UP":
                            tankCPU[i].moveUP(i + 4); //if the tank direction is up, tank continue move up
                            break;
                        case "DOWN":
                            tankCPU[i].moveDOWN(i + 4); //if the tank direction is down, tank continue move down
                            break;
                        case "LEFT":
                            tankCPU[i].moveLEFT(i + 4); //if the tank direction is left, tank continue move left
                            break;
                        case "RIGHT":
                            tankCPU[i].moveRIGHT(i + 4); //if the tank direction is right, tank continue move right
                            break;
                    }
                } else { //if the tank has no way to go, random a direction
                    int random = (int) (Math.random() * 4 + 1); //random a direction
                    switch (random) {
                        case 1:
                            tankCPU[i].moveUP(i + 4); //if the number is 1, tank continue move up
                            break;
                        case 2:
                            tankCPU[i].moveDOWN(i + 4); //if the number is 2, tank continue move down
                            break;
                        case 3:
                            tankCPU[i].moveLEFT(i + 4); //if the number is 3, tank continue move left
                            break;
                        case 4:
                            tankCPU[i].moveRIGHT(i + 4); //if the number is 4, tank continue move right
                            break;
                    }
                }
                new Shells(i); //shoot a shell
            }
        }
        try {
            TimeUnit.MILLISECONDS.sleep(500); //try to sleep for a while (250ms)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int numberAlive = 0;
        for (int i = 0; i < 4; i++) {
            if (tankCPU[i].getLife() != 0) {
                numberAlive++; //check if enemy alive
            }
        }
        scoreText.setText("<html><b>Score:</b>" +
                "<br>Current enemy <br>tank alive: <br><font color=blue>" + numberAlive +
                "</font><br>Current tank <br>life remain: <br><font color=red>" + tank.getLife() +
                "</font></html>");
    }

    public static void drawMap() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 20; j++) {
                //draw the map depends on the value of each element
                switch (map[i][j]) {
                    case 0:
                        mapGUI[i][j].setIcon(roadIcon); //set the icon to road icon
                        break;
                    case 1:
                        mapGUI[i][j].setIcon(wallIcon); //set the icon to wall icon
                        break;
                    case 2:
                        mapGUI[i][j].setIcon(eagleIcon); //set the icon to eagle icon
                        break;
                    case 3:
                        switch (tank.getDirection()) {
                            case "UP":
                                mapGUI[i][j].setIcon(tankUpIcon); //if the tank is up, set the up icon
                                break;
                            case "DOWN":
                                mapGUI[i][j].setIcon(tankDownIcon); //if the tank is down, set the down icon
                                break;
                            case "LEFT":
                                mapGUI[i][j].setIcon(tankLeftIcon);  //if the tank is left, set the left icon
                                break;
                            case "RIGHT":
                                mapGUI[i][j].setIcon(tankRightIcon); //if the tank is right, set the right icon
                                break;
                        }
                        break;
                    case 4:
                        setTankCPUIcon(0, i, j); //call set tank icon method, and give its number and coordinates
                        break;
                    case 5:
                        setTankCPUIcon(1, i, j); //call set tank icon method, and give its number and coordinates
                        break;
                    case 6:
                        setTankCPUIcon(2, i, j); //call set tank icon method, and give its number and coordinates
                        break;
                    case 7:
                        setTankCPUIcon(3, i, j); //call set tank icon method, and give its number and coordinates
                        break;
                    case 8:
                        mapGUI[i][j].setIcon(shellIcon); //set the icon to eagle icon
                        break;
                }
            }
        }
    }

    public static int[][] getMap() {
        return map;
    }

    public void highScore() throws IOException {
        int tankCount, tankCPUCount = 0;

        FileWriter fw = new FileWriter("HighScore.txt", true); //new txt file record the history score
        PrintWriter pw = new PrintWriter(fw); //makes a new PrintWriter to write text to the file
        tankCount = 3 - tank.getLife();
        for (int i = 0; i < 4; i++) {
            if (!tankCPU[i].isNotDied()) {
                tankCPUCount++;
            }
        }
        for (int i = 0; i < tankCount; i++) {
            pw.println("tankDeath"); //print the winner to the file
        }
        for (int i = 0; i < tankCPUCount; i++) {
            pw.println("tankCPUDeath"); //print the winner to the file
        }
        pw.close(); //close the file in order to read it

        tankCount = 0;
        tankCPUCount = 0;
        FileReader fr = new FileReader("HighScore.txt"); //new txt file reader to read the history score
        BufferedReader br = new BufferedReader(fr); //new buffer reader to read the file reader
        String temp = br.readLine(); //set the temp value to first line value
        while (temp != null) {
            //count the number of occurrences of each element
            switch (temp) {
                case "tankDeath":
                    tankCount++;
                    break;
                case "tankCPUDeath":
                    tankCPUCount++;
                    break;
            }
            temp = br.readLine();
        }
        historyScore(tankCount, tankCPUCount); //call the setter to set the history score
    }

    public void historyScore(int tank, int tankCPU) {
        historyScore = "<html>The total death of tanks<br>since you play this game is:" +
                "<br>Your tank death: " + tank +
                "<br>Enemy(CPU) tank death: " + tankCPU + "</html>";
    }

    public static void setTankCPUIcon(int n, int i, int j) {
        switch (tankCPU[n].getDirection()) {
            case "UP":
                mapGUI[i][j].setIcon(tankCPUUpIcon); //if the cpu tank is up, set the up icon
                break;
            case "DOWN":
                mapGUI[i][j].setIcon(tankCPUDownIcon); //if the cpu tank is down, set the down icon
                break;
            case "LEFT":
                mapGUI[i][j].setIcon(tankCPULeftIcon); //if the cpu tank is left, set the left icon
                break;
            case "RIGHT":
                mapGUI[i][j].setIcon(tankCPURightIcon); //if the cpu tank is right, set the right icon
                break;
        }
    }

    public boolean isNotEnd() {
        return tank.isNotDied() && (tankCPU[0].isNotDied() || tankCPU[1].isNotDied()
                || tankCPU[2].isNotDied() || tankCPU[3].isNotDied()); //return true if game not end
    }

    public void start() {
        System.out.println("Starting"); //print start to indicate the game is start
        if (t == null) {
            t = new Thread(this); //create a new thread
            t.start(); //start the new thread, call the run method
        }
    }

    @Override
    public void run() {
        while (isNotEnd()) {
            tankCPUMove(); //make cpu tanks move
            try {
                TimeUnit.MILLISECONDS.sleep(100); //delay for 0.1s
            } catch (InterruptedException e) {
                e.printStackTrace(); //catch if delay encounter an error
            }
        }  //if the game end then stop the loop
        try {
            highScore(); ///write high score
        } catch (IOException e) {
            e.printStackTrace(); //catch if write high score encounter an error
        }
        if (tank.getLife() == 0) {
            JOptionPane.showMessageDialog(this,
                    "<html>Game Over! <Br>Your Tank is died</html>"); //output the dank died message
        } else {
            JOptionPane.showMessageDialog(this,
                    "<html>Good Game! <Br>Your Tank killed all the enemy</html>"); //output cpu tanks died message
        }
        JOptionPane.showMessageDialog(this, historyScore); //pop up the history score message
        this.dispose();//game gui disposed
        System.out.println("finished"); //print finished to indicate the game is end
        t.interrupt(); //thread interrupt
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_UP:
                tank.moveUP(3); //if the tank direction is up, tank continue move up
                break;
            case KeyEvent.VK_DOWN:
                tank.moveDOWN(3); //if the tank direction is down, tank continue move down
                break;
            case KeyEvent.VK_LEFT:
                tank.moveLEFT(3); //if the tank direction is left, tank continue move left
                break;
            case KeyEvent.VK_RIGHT:
                tank.moveRIGHT(3); //if the tank direction is right, tank continue move right
                break;
            case KeyEvent.VK_SPACE:
                new Shells(); //shoot a shell
                break;
            case KeyEvent.VK_ENTER:
                this.start(); //start the thread to draw the map
                break;
        }
        drawMap(); //draw the map
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

//    public void debugMap() {
//        for (int i = 0; i < 10; i++) {
//            for (int j = 0; j < 20; j++) {
//                System.out.print(map[i][j]);
//            }
//            System.out.println();
//        }
//        System.out.println();
//    }
}

 class Welcome extends JFrame implements ActionListener {
    JButton play;
    ImageIcon background = new ImageIcon(getClass().getResource("/image/background.png"));

    public Welcome() {
        play = new JButton(); //create a new button play
        play.addActionListener(this); //add an action listener to play button
        play.setIcon(background); //set the icon to the background

        this.add(play); //add play button to this frame
        this.setTitle("BattleCity"); //set the title to Battle City
        this.setSize(800, 600); //set the size to 800 * 600
        this.setVisible(true); //make it visible
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.dispose(); //make welcome panel dispose
        new GameGUI(); //call main game gui
    }
}