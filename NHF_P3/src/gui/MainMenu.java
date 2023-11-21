package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * gui.MainMenu class.
 * It extends from JFrame and implements ActionListener.
 * It is the Main.Main menu that pops up when the program is started.
 */
public class MainMenu extends JFrame implements ActionListener {
    /**
     * gameButton, JButton, when it was clicked on it opens the GUI.GameStartMenu.
     */
    private final JButton gameButton = new JButton("START");

    /**
     * toplistButton, JButton, when it was clicked on it opens the GUI.TopList.
     */
    private final JButton toplistButton = new JButton("TOPLIST");

    /**
     * The initializer of GUI.MainMenu.
     * It creates the GUI.MainMenu window.
     */
    public MainMenu() {
        this.setTitle("GUI.MainMenu");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600, 400);
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        JPanel northp = new JPanel();

        JLabel menutext = new JLabel("MineSweeper Main.Main Menu", SwingConstants.CENTER);
        menutext.setFont(new Font("Times New Roman", Font.BOLD, 40));
        northp.add(menutext);

        JPanel centerp = new JPanel();

        gameButton.setPreferredSize(new Dimension(120, 30));
        toplistButton.setPreferredSize(new Dimension(120, 30));
        gameButton.addActionListener(this);
        toplistButton.addActionListener(this);

        JPanel centerGrid = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.gridy = 0;

        centerGrid.add(gameButton, gbc);
        gbc.gridy++;
        centerGrid.add(toplistButton, gbc);

        centerp.add(centerGrid);

        JPanel southp = new JPanel();

        JLabel toExitText = new JLabel("To Exit, press the close button in the top right corner!", SwingConstants.CENTER);
        toExitText.setFont(new Font("Times New Roman", Font.BOLD, 24));
        southp.add(toExitText);

        this.add(northp, BorderLayout.NORTH);
        this.add(centerp, BorderLayout.CENTER);
        this.add(southp, BorderLayout.SOUTH);
        this.setVisible(true);
    }

    /**
     * The required implementation of actionPerformed defined in ActionListener.
     * If the player hit the gameButton, it closes and opens the GUI.GameStartMenu.
     * If the player hit the toplistButton, it closes and opens the top list.
     * @param e ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == gameButton) {
            this.setVisible(false);
            new GameStartMenu();
        } else if(e.getSource() == toplistButton) {
            this.setVisible(false);
            new TopList();
        }
    }
}
