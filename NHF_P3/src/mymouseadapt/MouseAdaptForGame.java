package mymouseadapt;

import entity.Cell;
import gui.GameWindow;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;

/**
 * mymouseadapt.MouseAdaptForGame class.
 * The own MouseAdapter.
 * Only uses mouseClicked event.
 */
public class MouseAdaptForGame extends MouseAdapter implements Serializable {
    /**
     * Initialization of serialVersionUID, it helps to serialize the class.
     */
    private static final long serialVersionUID = 13579L;

    /**
     * window, gui.GameWindow type, it is used to store the gui.GameWindow.
     */
    private final GameWindow window;

    /**
     * We use super(), to use the constructor of the ancestor class.
     * And we set the window member variable to the param gui.GameWindow.
     * @param wind gui.GameWindow
     */
    public MouseAdaptForGame(GameWindow wind) {
        super();
        window = wind;
    }

    /**
     * The override of mouseClicked event.
     * It sets the flag of a cell true/false if there was a right-click.
     * And it reveals the cell if it was a left-click and is not flagged.
     * @param e MouseEvent
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        Cell tmpCell = this.setCellToSource(e);

        if(tmpCell != null) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                if(!tmpCell.getIsFlagged() && !tmpCell.getIsRevealed()) {
                    tmpCell.manageCell(window);
                }
            } else if (SwingUtilities.isRightMouseButton(e) && !tmpCell.getIsRevealed()) {
                this.setFlagOnCell(tmpCell);
            }
        }
    }

    /**
     * The function to help the Cognitive Complexity of mouseClicked function.
     * It sets the flag and updates the counter.
     * @param cell entity.Cell, on which it was clicked
     */
    private void setFlagOnCell(Cell cell) {
        if (cell.getIsFlagged()) {
            cell.setFlag();
            cell.setIcon(null);
            cell.setDisabledIcon(null);
            window.incFlagCount();
            window.getFlagButton().setText(String.valueOf(window.getFlagCount()));
        } else {
            if (window.getFlagCount() > 0) {
                cell.setFlag();
                Icon flagIcon = new ImageIcon("flag.png");
                cell.setIcon(flagIcon);
                cell.setDisabledIcon(flagIcon);
                window.decrFlagCount();
                window.getFlagButton().setText(String.valueOf(window.getFlagCount()));
            }
        }
    }

    /**
     * Same function as setFlagOnCell.
     * It locates the source from the grid.
     * @param e MouseEvent
     * @return (entity.Cell) e.getSource() / null
     */
    private Cell setCellToSource(MouseEvent e) {
        for(int i = 0; i < window.getSizeX(); i++) {
            for(int j = 0; j < window.getSizeY(); j++) {
                if(e.getSource() == window.getCells()[i][j]) {
                    return (Cell) e.getSource();
                }
            }
        }

        return null;
    }
}
