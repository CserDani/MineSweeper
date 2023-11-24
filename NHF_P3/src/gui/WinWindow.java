package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * WinWindow class.
 * It extends from JFrame and implements ActionListener.
 * The window pops up when the player won.
 */
public class WinWindow extends JFrame implements ActionListener {
    /**
     * ok, JButton, the only button on the frame.
     */
    private final JButton ok = new JButton("OK");

    /**
     * nameField, JTextField, the text field in which the player can enter its name.
     */
    private final JTextField nameField = new JTextField(20);

    /**
     * gameWind, gui.GameWindow, to store the gui.GameWindow.
     */
    private final GameWindow gameWind;

    /**
     * The initializer of gui.WinWindow.
     * It creates the gui.WinWindow and sets gameWind to gameW.
     * @param gameW gui.GameWindow
     */
    public WinWindow(GameWindow gameW) {
        gameWind = gameW;
        this.setTitle("Won");
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

        grid.add(new JLabel("Congrats, you won!! :)", SwingConstants.CENTER), gbc);
        gbc.gridy++;
        grid.add(new JLabel("Please enter a win to confirm your game!", SwingConstants.CENTER), gbc);
        gbc.gridy++;
        grid.add(new JLabel("If you leave it with nothing, you won't have a chance to get on the Toplist!", SwingConstants.CENTER), gbc);
        gbc.gridy++;
        grid.add(new JLabel("Please enter a win to confirm your game!", SwingConstants.CENTER), gbc);
        gbc.gridy++;
        grid.add(nameField, gbc);
        gbc.gridy++;
        grid.add(ok, gbc);

        this.add(grid, BorderLayout.CENTER);
        this.pack();
        this.setVisible(true);
    }

    /**
     * TopListEntry static class.
     * It is used to store the entries of the top list.
     */
    public static class TopListEntry {
        private final String name;
        private final int time;
        private final String diff;
        public TopListEntry(String n, int t, String d) {
            name = n;
            time = t;
            diff = d;
        }
    }

    /**
     * This function tries the player's data, whether it can be placed on the top list.
     * @param name Name of the player
     * @param time Time of the player's game
     * @param diff Difficulty which the player chose
     */
    public void tryOnTopList(String name, int time, String diff) {
        ArrayList<TopListEntry> topList = new ArrayList<>();

        Scanner scan = null;
        try {
            File topLi = new File("TopList.txt");
            scan = new Scanner(topLi);

            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                if(!line.isEmpty()) {
                    String[] lineArr = line.split(";");

                    topList.add(new TopListEntry(lineArr[0], Integer.parseInt(lineArr[1]), lineArr[2]));
                }
            }

            scan.close();
        } catch (FileNotFoundException e) {
            Logger logger = Logger.getLogger(WinWindow.class.getName());
            logger.log(Level.WARNING, "TopList.txt was not found!", e);
        }

        if(scan != null) {
            scan.close();
        }

        boolean added = false;
        for(TopListEntry entry : topList) {
            if(entry.time > time) {
                topList.add(topList.indexOf(entry), new TopListEntry(name, time, diff));
                added = true;
                break;
            }
        }

        if((topList.isEmpty() || topList.size() < 10) && !added) {
            topList.add(new TopListEntry(name, time, diff));
        }

        while(topList.size() > 10) {
            topList.remove(topList.size()-1);
        }


        try (FileWriter fw = new FileWriter("TopList.txt")){
            for (TopListEntry entry : topList) {
                fw.write(entry.name + ";" + entry.time + ";" + entry.diff + "\n");
            }
        } catch (IOException e) {
            Logger logger = Logger.getLogger(WinWindow.class.getName());
            logger.log(Level.WARNING, "IOException when tried to write in TopList.txt", e);
        }
    }

    /**
     * The required implementation of actionPerformed defined in ActionListener.
     * If it was clicked on ok button, then it closes both this and the gui.GameWindow, and
     * it tries the players data on the top list if it gave a name. If no name was given, it just closes.
     * Then opens the gui.MainMenu.
     * @param e ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == ok) {
            String name = nameField.getText();
            if(!name.isEmpty()) {
                tryOnTopList(name, gameWind.getTimeCounter(), gameWind.getDiff());
            }

            this.setVisible(false);
            gameWind.setVisible(false);
            new MainMenu();
        }
    }
}
