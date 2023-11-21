package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * gui.GameStartMenu class.
 * It extends from JFrame and implements ActionListener.
 * It is the menu that pops up when the player hits start in GUI.MainMenu.
 */
public class GameStartMenu extends JFrame implements ActionListener {
    /**
     * start, JButton, the button that starts the game with the selected difficulty.
     */
    private final JButton start = new JButton("START");

    /**
     * back, JButton, the button to go back to the GUI.MainMenu.
     */
    private final JButton back = new JButton("BACK");

    /**
     * difficulties, String[], to store selectable difficulties.
     */
    private final String[] difficulties = { "Beginner", "Normal", "Hard" };

    /**
     * diffs, JComboBox, the combobox from which the player can select the difficulties.
     */
    private final JComboBox<String> diffs = new JComboBox<>(difficulties);

    /**
     * The initializer of the GUI.GameStartMenu.
     * It creates the GUI.GameStartMenu window.
     */
    public GameStartMenu() {
        this.setTitle("GUI.GameStartMenu");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600,400);
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        JPanel northp = new JPanel();

        JLabel menutext = new JLabel("Start Game Menu", SwingConstants.CENTER);
        menutext.setFont(new Font("Times New Roman", Font.BOLD, 40));
        northp.add(menutext);

        JPanel centerp = new JPanel();

        JLabel selGameText = new JLabel("Please select a difficulty then press START!", SwingConstants.CENTER);
        selGameText.setFont(new Font ("Times New Roman", Font.BOLD, 20));
        diffs.setPreferredSize(new Dimension(150,30));
        start.setPreferredSize(new Dimension(80,30));
        start.addActionListener(this);

        JPanel centerGrid = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;

        centerGrid.add(selGameText, gbc);
        gbc.gridy++;

        JPanel centerGridhelp = new JPanel();

        if(hasLoad()) {
            diffs.addItem("Load");
        }

        centerGridhelp.add(diffs);
        centerGridhelp.add(start);

        centerGrid.add(centerGridhelp, gbc);

        centerp.add(centerGrid);

        JPanel southp = new JPanel();

        back.addActionListener(this);

        southp.add(back);

        this.add(northp, BorderLayout.NORTH);
        this.add(centerp, BorderLayout.CENTER);
        this.add(southp, BorderLayout.SOUTH);
        this.setVisible(true);
    }

    /**
     * The function that check whether there is a saved game in the SavedGame.txt or not.
     * If it returns true, a "Load" option is put into the combobox.
     * If it returns false, nothing happens.
     * @return true/false
     */
    public boolean hasLoad() {
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get("SavedGame.txt")))) {
            if(ois.readObject() != null) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }

        return false;
    }

    /**
     * The required implementation of actionPerformed defined in ActionListener.
     * If the player hit the start button, a GUI.GameWindow starts with the selected difficulty.
     * If the player hit the back button, it closes and opens the GUI.MainMenu.
     * @param e ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == start) {
            String diff = (String) diffs.getSelectedItem();

            this.setVisible(false);
            new GameWindow(diff);

        } else if(e.getSource() == back) {
            this.setVisible(false);
            new MainMenu();
        }
    }
}
