package entity;

import gui.GameWindow;
import gui.LoseWindow;
import gui.WinWindow;

import javax.swing.*;
import javax.swing.plaf.metal.MetalButtonUI;
import java.awt.*;
import java.io.Serializable;

/**
 * entity.Cell class.
 * It extends from JButton and implements Serializable to properly save the game.
 */
public class Cell extends JButton implements Serializable {
    /**
     * Initialization of serialVersionUID, it helps to serialize the class.
     */
    private static final long serialVersionUID = 87654321L;

    /**
     * minesAround, the counter of the mines around one cell.
     * It initializes with 0.
     */
    private int minesAround = 0;

    /**
     * neighbouringCells, an array to store the neighbours, maximum size 8.
     */
    private Cell[] neighbouringCells = new Cell[8];

    /**
     * isMine, to determine if the cell is a mine or not.
     */
    private boolean isMine = false;

    /**
     * isRevealed, to determine if the cell is revealed or not.
     */
    private boolean isRevealed = false;

    /**
     * isHole, to determine if the cell is a hole or not.
     */
    private boolean isHole = false;

    /**
     * isPlusHealth, to determine if the cell is a plus health or not.
     */
    private boolean isPlusHealth = false;

    /**
     * isFlagged, to determine if the cell is flagged or not.
     */
    private boolean isFlagged = false;

    /**
     * The constructor of entity.Cell.
     * It sets the size, margin and the used Font.
     */
    public Cell() {
        this.setPreferredSize(new Dimension(30,30));
        this.setMargin(new Insets(0,0,0,0));
        this.setFont(new Font("Arial", Font.PLAIN, 20));
    }

    /**
     * setter for isRevealed.
     */
    public void setRevealed() { isRevealed = true; }

    /**
     * setter for isMine.
     */
    public void setMine() { isMine = true; }

    /**
     * setter for isHole.
     */
    public void setHole() { isHole = true; }

    /**
     * setter for isFlagged.
     */
    public void setFlag() { isFlagged = !isFlagged; }

    /**
     * setter for isPlusHealth.
     */
    public void setPlusHealth() { isPlusHealth = true; }

    /**
     * setter for neighbouringCells.
     * And calls setMinesAround setter.
     * @param cells an array of entity.Cell
     */
    public void setNeighbouringCells(Cell[] cells) {
        neighbouringCells = cells;
        this.setMinesAround();
    }

    /**
     * setter for minesAround.
     */
    public void setMinesAround() {
        for(Cell cell : neighbouringCells) {
            if(cell != null && cell.isMine) {
                minesAround++;
            }
        }
    }

    /**
     * getter for isMine.
     * @return isMine
     */
    public boolean getMine() { return isMine; }

    /**
     * getter for isRevealed.
     * @return isRevealed
     */
    public boolean getIsRevealed() { return isRevealed; }

    /**
     * getter for isFlagged.
     * @return isFlagged
     */
    public boolean getIsFlagged() { return isFlagged; }

    /**
     * getter for isHole.
     * @return isHole
     */
    public boolean getIsHole() { return isHole; }

    /**
     * getter for isPlusHealth.
     * @return isPlusHealth
     */
    public boolean getIsHealth() { return isPlusHealth; }

    /**
     * This function is to help the Cognitive Complexity of revealCell.
     * It recursively reveals the neighbouring cells after a click if it was empty (There was no mine around the cell).
     * It only reveals those neighbours which are not (a) mine/revealed/hole/plus health/flagged.
     * If the player hasn't lost, then it sets the background color as well.
     * @param wind gui.GameWindow
     */
    public void recursCogComplex(GameWindow wind) {
        for(Cell cell : neighbouringCells) {
            if(cell != null && !cell.isMine && !cell.isRevealed && !cell.isHole && !cell.isPlusHealth && !cell.isFlagged) {
                if(!wind.getLost()) {
                    cell.setBackground(Color.GRAY);
                }
                cell.revealCell(wind);
            }
        }
    }

    /**
     * It determines which color to use in the cells text.
     * All numbers (minesAround) are different colors. As it is in the original game.
     * @return Color
     */
    public Color getCellColor() {
        switch(minesAround) {
            case 1:
                return Color.BLUE;
            case 2:
                return Color.GREEN;
            case 3:
                return Color.RED;
            case 4:
                return new Color(16, 0, 170);
            case 5:
                return new Color(108, 7, 7);
            case 6:
                return Color.CYAN;
            case 7:
                return Color.BLACK;
            case 8:
                return new Color(255, 204, 3);
            default:
                return Color.GRAY;
        }
    }

    /**
     * It reveals the cell in which it was clicked (or was recursively revealed or the gui.GameWindow called reveal all).
     * @param wind gui.GameWindow - Mainly used to increment revealedCells.
     */
    public void revealCell(GameWindow wind) {
        this.setEnabled(false);
        this.setRevealed();
        if(isMine) {
            Icon mineIcon = new ImageIcon("mine.png");
            this.setIcon(mineIcon);
            this.setDisabledIcon(mineIcon);
        } else if(isHole) {
            this.setBackground(new Color(105, 24, 24));
            wind.incRevealedCells();
        } else if(isPlusHealth) {
            Icon heartIcon = new ImageIcon("heart.png");
            this.setIcon(heartIcon);
            this.setDisabledIcon(heartIcon);
            this.setBackground(Color.YELLOW);
            wind.incHealthCount();
            wind.incRevealedCells();
        } else {
            if(minesAround == 0) {
                this.setText("");
                this.recursCogComplex(wind);
            } else {
                this.setTextColor(getCellColor());
                this.setText(String.valueOf(minesAround));
            }

            wind.incRevealedCells();
        }
    }

    /**
     * The manager of a cell on which it was clicked. It sets the game lost if there are no healths left
     * and checks for a win at the end.
     * @param wind gui.GameWindow
     */
    public void manageCell(GameWindow wind) {
        this.setBackground(Color.GRAY);

        if(isMine) {
            Icon mineIcon = new ImageIcon("mine.png");
            this.setIcon(mineIcon);
            this.setDisabledIcon(mineIcon);
            this.setBackground(Color.RED);
            if(wind.getHealthCount() == 0) {
                wind.timeStop();
                wind.setLost();
                wind.revealAll();
                Icon deadIcon = new ImageIcon("dead.png");
                wind.getSmileButton().setDisabledIcon(deadIcon);
                wind.getSaveButton().setEnabled(false);
                new LoseWindow(wind);
            } else {
                wind.decrHealthCount();
            }
        }

        this.revealCell(wind);

        if(wind.getRevealedCells() == (wind.getSizeX() * wind.getSizeY()) - wind.getMineCount() && !wind.getLost()) {
            wind.timeStop();
            wind.revealAll();
            new WinWindow(wind);
        }
    }

    /**
     * The implementation of setTextColor to set the color of a button's text.
     * @param color Color
     */
    public void setTextColor(Color color) {
        this.setUI(new MetalButtonUI() {
            @Override
            protected Color getDisabledTextColor() {
                return color;
            }
        });
    }
}
