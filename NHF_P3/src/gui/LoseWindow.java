package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * gui.LoseWindow class.
 * It extends from JFrame and implements ActionListener.
 * The Window that pops up when the player lost.
 */
public class LoseWindow extends JFrame implements ActionListener {
    /**
     * ok, JButton, the only button on the frame.
     */
    private final JButton ok = new JButton("OK");

    /**
     * gameWind, gui.GameWindow, to store the gui.GameWindow as it will close when the gui.LoseWindow is closed.
     */
    private final GameWindow gameWind;

    /**
     * The initializer of the gui.LoseWindow.
     * It creates the window and sets gameWind to gameW.
     * @param gameW gui.GameWindow
     */
    public LoseWindow(GameWindow gameW) {
        gameWind = gameW;
        this.setTitle("Lost");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(0,0);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setAlwaysOnTop(true);

        ok.addActionListener(this);

        JPanel grid = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.gridy = 0;

        grid.add(new JLabel("I'm sorry, You Lost! :(", SwingConstants.CENTER), gbc);
        gbc.gridy++;
        grid.add(new JLabel("Press OK to go back to the Menu", SwingConstants.CENTER), gbc);
        gbc.gridy++;
        grid.add(ok, gbc);

        this.add(grid, BorderLayout.CENTER);
        this.pack();
        this.setVisible(true);
    }

    /**
     * The required implementation of actionPerformed defined in ActionListener.
     * If it was clicked on ok button, then it closes both this and the gui.GameWindow.
     * Then open the gui.MainMenu.
     * @param e ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == ok) {
            this.setVisible(false);
            gameWind.setVisible(false);
            new MainMenu();
        }
    }
}
