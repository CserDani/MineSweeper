package gui;

import entity.Cell;
import mymouseadapt.MouseAdaptForGame;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * gui.GameWindow class.
 * It extends from JFrame, implements ActionListener to check for certain button pushes
 * and implements Serializable to save the actual game.
 */
public class GameWindow extends JFrame implements ActionListener, Serializable {
    /**
     * Initialization of serialVersionUID, it helps to serialize the class.
     */
    private static final long serialVersionUID = 12345678L;

    /**
     * Initialization of rand, a Random type variable, used for random cell generation.
     */
    private final Random rand = new Random();

    /**
     * The cell grid.
     */
    private Cell[][] cells;

    /**
     * The cell grid's X size.
     */
    private int sizex;

    /**
     * The cell grid's Y size.
     */
    private int sizey;

    /**
     * Revealed Cells counter, used to determine whether the player has won.
     */
    private int revealedCells = 0;

    /**
     * timeButton, a JButton in which the counter of passed seconds can be seen.
     */
    private final JButton timeButton = new JButton("");

    /**
     * flagButton, a JButton in which the counter of remaining flags can be seen.
     */
    private final JButton flagButton = new JButton("");

    /**
     * saveButton, a JButton, if the player hits it, it saves the game.
     */
    private final JButton saveButton = new JButton("Save!");

    /**
     * smileButton, not exactly used for anything, in it the game shows the iconic MineSweeper smileys.
     */
    private final JButton smileButton = new JButton();

    /**
     * timeCounter, a counter used to count the passed seconds.
     * It functions as a "point system", the lower, the better.
     */
    private int timeCounter = 0;

    /**
     * timer, a Timer type variable, it is used to count the seconds as an event.
     */
    private final Timer timer = new Timer(1000, this);

    /**
     * mineCount, the member variable that stores the number of mines.
     */
    private int mineCount;

    /**
     * flagCount, the member variable that stores the number of flags.
     */
    private int flagCount;

    /**
     * holeCount, the member variable that stores the number of holes.
     */
    private int holeCount;

    /**
     * plusHealth, the member variable that stores the number of plus healths.
     */
    private int plusHealth;

    /**
     * healthCount, the member variable that stores the number of acquired healths.
     */
    private int healthCount = 0;

    /**
     * lost, boolean, to check whether the player lost or not.
     */
    private boolean lost = false;

    /**
     * diff, String, to store the difficulty level, used when the player gets on the top list
     * and to determine how to initialize the game.
     */
    private String diff;

    /**
     * mouseAdapt, MyMouseAdapt.MouseAdaptForGame, an own MouseAdapter for the game, only uses mouseClicked event.
     * Used to determine if a click was a right-click or left-click. (Right-click flag, left-click reveal.)
     */
    private final MouseAdaptForGame mouseAdapt = new MouseAdaptForGame(this);

    /**
     * getter for healthCount.
     * @return healthCount
     */
    public int getHealthCount() { return healthCount; }

    /**
     * Incrementer for healthCount.
     */
    public void incHealthCount() { healthCount++; }

    /**
     * Decrementer for healthCount.
     */
    public void decrHealthCount() { healthCount--; }

    /**
     * getter for mineCount.
     * @return mineCount
     */
    public int getMineCount() { return mineCount; }

    /**
     * getter for flagCount.
     * @return flagCount
     */
    public int getFlagCount() { return flagCount; }

    /**
     * Incrementer for flagCount.
     */
    public void incFlagCount() { flagCount++; }

    /**
     * Decrementer for flagCount.
     */
    public void decrFlagCount() { flagCount--; }

    /**
     * getter for timeCounter.
     * @return timeCounter
     */
    public int getTimeCounter() { return timeCounter; }

    /**
     * getter for diff.
     * @return diff
     */
    public String getDiff() { return diff; }

    /**
     * getter for revealedCells.
     * @return revealedCells
     */
    public int getRevealedCells() { return revealedCells; }

    /**
     * Incrementer of revealedCells.
     */
    public void incRevealedCells() { revealedCells++; }

    /**
     * getter for sizex.
     * @return sizex
     */
    public int getSizeX() { return sizex; }

    /**
     * getter for sizey.
     * @return sizey
     */
    public int getSizeY() { return sizey; }

    /**
     * getter for cells (cell grid).
     * @return cells
     */
    public Cell[][] getCells() { return cells; }

    /**
     * Stops the timer from counting.
     */
    public void timeStop() { timer.stop(); }

    /**
     * getter for saveButton.
     * @return saveButton
     */
    public JButton getSaveButton() {
        return saveButton;
    }

    /**
     * getter for smileButton.
     * @return smileButton
     */
    public JButton getSmileButton() {
        return smileButton;
    }

    /**
     * getter for flagButton.
     * @return flagButton
     */
    public JButton getFlagButton() { return flagButton; }

    /**
     * setter for lost.
     * It sets lost to true, if the player lost.
     */
    public void setLost() { lost = true; }

    /**
     * getter for lost.
     * @return lost
     */
    public boolean getLost() {return lost; }

    /**
     * The main initialization of the gui.GameWindow.
     * It either initializes a new window with the difficulty, or loads a saved game.
     * @param difficulty - the diff variable gets the difficulty from the gui.GameStartMenu
     */
    public GameWindow(String difficulty) {
        if(difficulty.equals("Load")) {
            loadGame();
        } else {
            gameWindowIni(difficulty);
        }
    }

    /**
     * The game window initializer. It creates a window with the given difficulty.
     * @param difficulty the difficulty of the game
     */
    public void gameWindowIni(String difficulty) {
        this.setTitle("Game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600, 400);
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        diff = difficulty;
        // This switch determines the values of certain variables, according to the difficulty.
        switch (difficulty) {
            case "Beginner":
                sizex = 9;
                sizey = 9;
                mineCount = 10;
                flagCount = 10;
                holeCount = 2;
                plusHealth = 1;
                break;
            case "Normal":
                sizex = 16;
                sizey = 16;
                mineCount = 40;
                flagCount = 40;
                holeCount = 6;
                plusHealth = 2;
                break;
            case "Hard":
                sizex = 20;
                sizey = 20;
                mineCount = 99;
                flagCount = 99;
                holeCount = 10;
                plusHealth = 3;
                break;
            default:
                break;
        }

        JPanel northp = new JPanel(new BorderLayout());
        northp.setBackground(Color.GRAY);

        JPanel northNestCenter = new JPanel();
        JPanel northNestWest = new JPanel();
        JPanel northNestEast = new JPanel();
        northNestCenter.setBackground(Color.GRAY);
        northNestWest.setBackground(Color.GRAY);
        northNestEast.setBackground(Color.GRAY);

        smileButton.setEnabled(false);
        Icon aliveIcon = new ImageIcon("alive.png");
        smileButton.setIcon(aliveIcon);
        smileButton.setDisabledIcon(aliveIcon);
        smileButton.setPreferredSize(new Dimension(40, 40));
        smileButton.setBackground(Color.GRAY);
        smileButton.setBorder(new LineBorder(Color.GRAY));

        northNestCenter.add(smileButton);

        saveButton.addActionListener(this);
        northNestWest.add(saveButton);

        Font buttonFontFlTi = new Font("Times New Roman", Font.BOLD, 22);
        Insets noMargin = new Insets(0,0,0,0);

        timeButton.setPreferredSize(new Dimension(85, 30));
        timeButton.setFont(buttonFontFlTi);
        timeButton.setHorizontalAlignment(SwingConstants.RIGHT);
        timeButton.setText(String.valueOf(0));
        flagButton.setMargin(noMargin);
        timeButton.setEnabled(false);
        timer.start();

        flagButton.setPreferredSize(new Dimension(45,30));
        flagButton.setFont(buttonFontFlTi);
        flagButton.setHorizontalAlignment(SwingConstants.CENTER);
        flagButton.setText(String.valueOf(flagCount));
        flagButton.setMargin(noMargin);
        flagButton.setEnabled(false);

        northNestEast.add(timeButton);
        northNestEast.add(flagButton);

        northp.add(northNestCenter, BorderLayout.CENTER);
        northp.add(northNestWest, BorderLayout.WEST);
        northp.add(northNestEast, BorderLayout.EAST);

        JPanel centerp = new JPanel(new GridBagLayout());
        centerp.setBackground(Color.GRAY);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.gridx = 0;

        cells = new Cell[sizex][sizey];

        for (int i = 0; i < sizex; i++) {
            gbc.gridy = 0;
            for (int j = 0; j < sizey; j++) {
                cells[i][j] = new Cell();
                cells[i][j].addActionListener(this);
                cells[i][j].addMouseListener(mouseAdapt);
                cells[i][j].setBackground(Color.DARK_GRAY);
                centerp.add(cells[i][j], gbc);
                gbc.gridy++;
            }
            gbc.gridx++;
        }

        this.minesIni();

        this.holesIni();

        this.healthsIni();

        for (int i = 0; i < sizex; i++) {
            for (int j = 0; j < sizey; j++) {
                cells[i][j].setNeighbouringCells(cellsAroundOne(i, j));
            }
        }

        centerp.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        this.add(northp, BorderLayout.NORTH);
        this.add(centerp, BorderLayout.CENTER);
        this.pack();
        this.setVisible(true);
    }

    /**
     * The initializer of the mines. It randomly selects the mines on the grid.
     * It selects a number of mineCount cells. And it not selects the same cells.
     */
    private void minesIni() {
        for (int i = 0; i < mineCount; i++) {
            int x;
            int y;
            do {
                x = rand.nextInt(sizex);
                y = rand.nextInt(sizey);
            } while (cells[x][y].getMine());

            cells[x][y].setMine();
        }
    }

    /**
     * The initializer of holes. It randomly selects the holes on the grid.
     * It selects a number of holeCount cells. And it not select cells if those are either
     * holes, mines or plus healths.
     */
    private void holesIni() {
        for(int i = 0; i < holeCount; i++) {
            int x;
            int y;
            do {
                x = rand.nextInt(sizex);
                y = rand.nextInt(sizey);
            } while(cells[x][y].getMine() || cells[x][y].getIsHole() || cells[x][y].getIsHealth());

            cells[x][y].setHole();
        }
    }

    /**
     * The initializer of plus healths. It randomly selects the plus healths on the grid.
     * It selects a number of healthCount cells. And it not select cells if those are either
     * holes, mines or plus healths.
     */
    private void healthsIni() {
        for(int i = 0; i < plusHealth; i++) {
            int x;
            int y;

            do {
                x = rand.nextInt(sizex);
                y = rand.nextInt(sizey);
            } while(cells[x][y].getMine() || cells[x][y].getIsHole() || cells[x][y].getIsHealth());

            cells[x][y].setPlusHealth();
        }
    }

    /**
     * The function to reveal all not revealed or not flagged cells. It is used at the end of a game.
     */
    public void revealAll() {
        for(int i = 0; i < sizex; i++) {
            for(int j = 0; j < sizey; j++) {
                if(!cells[i][j].getIsRevealed()) {
                    if(!cells[i][j].getIsFlagged()) {
                        cells[i][j].revealCell(this);
                    } else {
                        cells[i][j].setRevealed();
                        cells[i][j].setEnabled(false);
                    }
                }
            }
        }
    }

    /**
     * This function is used to set the cells around each cell.
     * The returned array is used to set the neighbouringCells variable in Cells class.
     * @param x the x coordinate of a cell
     * @param y the y coordinate of a cell
     * @return entity.Cell[] cellsAround - an array of cells around one cell.
     */
    public Cell[] cellsAroundOne(int x, int y) {
        Cell[] cellsAround = new Cell[8];
        int cntr = 0;

        for(int iOffSet = -1; iOffSet <= 1; iOffSet++) {
            int iWithOffSet = x + iOffSet;

            for(int jOffSet = -1; jOffSet <= 1; jOffSet++) {
                if(iOffSet == 0 && jOffSet == 0) {
                    continue;
                }

                int jWithOffSet = y + jOffSet;

                if((iWithOffSet < sizex && iWithOffSet >= 0) && (jWithOffSet < sizey && jWithOffSet >= 0)) {
                        cellsAround[cntr++] = cells[iWithOffSet][jWithOffSet];
                }
            }
        }

        return cellsAround;
    }

    /**
     * This function is used to write the gui.GameWindow out into a txt (SavedGame.txt).
     * Always one saved game, if the txt contains one already, it overwrites it.
     * @param wind the gui.GameWindow which we save
     */
    public void saveGame(GameWindow wind) {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get("SavedGame.txt")))){
            oos.writeObject(wind);
        } catch (IOException e) {
            Logger logger = Logger.getLogger(GameWindow.class.getName());
            logger.log(Level.WARNING, "IOException when tried to open SavedGame.txt", e);
        }
    }

    /**
     * This function is used to load a game.
     * A saved game can only be loaded once, hence the "this.saveGame(null)"
     */
    public void loadGame() {
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get("SavedGame.txt")))) {
            GameWindow tmp = (GameWindow) ois.readObject();
            tmp.timer.start();
            tmp.setVisible(true);
            for(int i = 0; i < tmp.sizex; i++) {
                for(int j = 0; j < tmp.sizey; j++) {
                    Cell tmpCell = tmp.cells[i][j];
                    if(tmpCell.getIsRevealed()) {
                        tmpCell.setTextColor(tmpCell.getCellColor());
                    }
                }
            }

            this.saveGame(null);
        } catch (Exception e) {
            Logger logger = Logger.getLogger(GameWindow.class.getName());
            logger.log(Level.WARNING, "EXCEPTION when tried to open SavedGame.txt", e);
            new MainMenu();
        }
    }

    /**
     * The required implementation of actionPerformed defined in ActionListener.
     * @param e ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == saveButton) {
            saveGame(this);
        }

        if(e.getSource() == timer) {
            timeCounter++;
            timeButton.setText(String.valueOf(timeCounter));
        }
    }
}
