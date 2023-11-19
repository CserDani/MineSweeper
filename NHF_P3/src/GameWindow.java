import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Random;

public class GameWindow extends JFrame implements ActionListener, Serializable {
    private static final long serialVersionUID = 12345678L;
    private final Random rand = new Random();
    private Cell[][] cells;
    private int sizex;
    private int sizey;
    private int revealedCells = 0;
    private final JButton timeButton = new JButton("");
    private final JButton saveButton = new JButton("Save!");
    private final JButton smileButton = new JButton();
    private int timeCounter = 0;
    private final Timer timer = new Timer(1000, this);
    private int mineCount;
    private int holeCount;
    private int plusHealth;
    private int healthCount;
    private boolean lost = false;
    private String diff;

    public int getHealthCount() {
        return healthCount;
    }
    public void incHealthCount() {
        healthCount++;
    }
    public void decrHealthCount() {
        healthCount--;
    }
    public int getMineCount() {
        return mineCount;
    }
    public int getTimeCounter() {
        return timeCounter;
    }
    public String getDiff() {
        return diff;
    }
    public int getRevealedCells() {
        return revealedCells;
    }
    public void incRevealedCells() {
        revealedCells++;
    }
    public int getSizeX() {
        return sizex;
    }
    public int getSizeY() {
        return sizey;
    }
    public void timeStop() {
        timer.stop();
    }
    public JButton getSaveButton() {
        return saveButton;
    }
    public JButton getSmileButton() {
        return smileButton;
    }

    public GameWindow(String difficulty) {
        if(difficulty.equals("Load")) {
            loadGame();
        } else {
            gameWindowIni(difficulty);
        }
    }

    public void gameWindowIni(String difficulty) {
        this.setTitle("Game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600, 400);
        this.setLocationRelativeTo(null);
        this.setResizable(true);

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

        northNestCenter.add(smileButton);

        saveButton.addActionListener(this);
        northNestWest.add(saveButton);

        timeButton.setPreferredSize(new Dimension(85, 30));
        timeButton.setFont(new Font("Times New Roman", Font.BOLD, 22));
        timeButton.setHorizontalAlignment(SwingConstants.RIGHT);
        timeButton.setText(String.valueOf(0));
        timeButton.setEnabled(false);
        timer.start();

        northNestEast.add(timeButton);

        northp.add(northNestCenter, BorderLayout.CENTER);
        northp.add(northNestWest, BorderLayout.WEST);
        northp.add(northNestEast, BorderLayout.EAST);

        healthCount = 0;
        diff = difficulty;
        switch (difficulty) {
            case "Beginner":
                sizex = 9;
                sizey = 9;
                mineCount = 10;
                holeCount = 2;
                plusHealth = 1;
                break;
            case "Normal":
                sizex = 16;
                sizey = 16;
                mineCount = 40;
                holeCount = 6;
                plusHealth = 2;
                break;
            case "Hard":
                sizex = 20;
                sizey = 20;
                mineCount = 99;
                holeCount = 10;
                plusHealth = 3;
                break;
            default:
                break;
        }

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
                cells[i][j].setBackground(Color.DARK_GRAY);
                centerp.add(cells[i][j], gbc);
                gbc.gridy++;
            }
            gbc.gridx++;
        }

        for (int i = 0; i < mineCount; i++) {
            int x;
            int y;
            do {
                x = rand.nextInt(sizex);
                y = rand.nextInt(sizey);
            } while (cells[x][y].getMine());

            cells[x][y].setMine();
        }

        for(int i = 0; i < holeCount; i++) {
            int x;
            int y;
            do {
                x = rand.nextInt(sizex);
                y = rand.nextInt(sizey);
            } while(cells[x][y].getMine());

            cells[x][y].setHole();
        }

        for(int i = 0; i < plusHealth; i++) {
            int x;
            int y;

            do {
                x = rand.nextInt(sizex);
                y = rand.nextInt(sizey);
            } while(cells[x][y].getMine());

            cells[x][y].setPlusHealth();
        }

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

    public void setLost() {
        lost = true;
    }
    public boolean getLost() {
        return lost;
    }
    public void revealAll() {
        for(int i = 0; i < sizex; i++) {
            for(int j = 0; j < sizey; j++) {
                if(!cells[i][j].getIsRevealed()) {
                    cells[i][j].revealCell(this);
                }
            }
        }
    }

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

    public void saveGame() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("SavedGame.txt"))){
            oos.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadGame() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("SavedGame.txt"))) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == saveButton) {
            saveGame();
        }

        for(int i = 0; i < sizex; i++) {
            for(int j = 0; j < sizey; j++) {
                if(e.getSource() == cells[i][j]) {
                    Cell tmpCell = (Cell) e.getSource();
                    tmpCell.manageCell(this);
                }
            }
        }

        if(e.getSource() == timer) {
            timeCounter++;
            timeButton.setText(String.valueOf(timeCounter));
        }
    }
}
