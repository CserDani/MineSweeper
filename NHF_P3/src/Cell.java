import javax.swing.*;
import javax.swing.plaf.metal.MetalButtonUI;
import java.awt.*;
import java.io.Serializable;

public class Cell extends JButton implements Serializable {
    private static final long serialVersionUID = 87654321L;
    private int minesAround = 0;
    private Cell[] neighbouringCells = new Cell[8];
    private boolean isMine = false;
    private boolean isRevealed = false;
    private boolean isHole = false;
    private boolean isPlusHealth = false;
    public Cell() {
        this.setPreferredSize(new Dimension(30,30));
        this.setMargin(new Insets(0,0,0,0));
        this.setFont(new Font("Arial", Font.PLAIN, 20));
    }

    public void setRevealed() {
        isRevealed = true;
    }
    public void setMine() {
        isMine = true;
    }
    public void setHole() {
        isHole = true;
    }
    public void setPlusHealth() {
        isPlusHealth = true;
    }
    public void setNeighbouringCells(Cell[] cells) {
        neighbouringCells = cells;
        this.setMinesAround();
    }
    public void setMinesAround() {
        for(Cell cell : neighbouringCells) {
            if(cell != null && cell.isMine) {
                minesAround++;
            }
        }
    }

    public boolean getMine() { return isMine; }
    public boolean getIsRevealed() { return isRevealed; }

    public void recursCogCompl(GameWindow wind) {
        for(Cell cell : neighbouringCells) {
            if(cell != null && !cell.isMine && !cell.isRevealed && !cell.isHole && !cell.isPlusHealth) {
                if(!wind.getLost()) {
                    cell.setBackground(Color.GRAY);
                }
                cell.revealCell(wind);
            }
        }
    }

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
                this.recursCogCompl(wind);
            } else {
                this.setTextColor(getCellColor());
                this.setText(String.valueOf(minesAround));
            }

            wind.incRevealedCells();
        }
    }

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

    public void setTextColor(Color color) {
        this.setUI(new MetalButtonUI() {
            @Override
            protected Color getDisabledTextColor() {
                return color;
            }
        });
    }
}
