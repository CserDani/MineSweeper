package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * gui.TopList class.
 * It extends from JFrame and implements ActionListener.
 */
public class TopList extends JFrame implements ActionListener {
    /**
     * back, JButton, the only button of the frame.
     * Used to go back to the gui.MainMenu.
     */
    private final JButton back = new JButton("BACK");

    /**
     * The initialization of the gui.TopList frame.
     */
    public TopList() {
        this.setTitle("TopList");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600, 400);
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        JPanel northp = new JPanel();

        JLabel menutext = new JLabel("Top List", SwingConstants.CENTER);
        menutext.setFont(new Font("Times New Roman", Font.BOLD, 40));
        northp.add(menutext);

        JPanel centerp = new JPanel();

        Font listFont = new Font("Times New Roman", Font.BOLD, 14);

        Box centerBox1 = Box.createVerticalBox();
        centerBox1.add(new JLabel("NAME"), listFont);
        Box centerBox2 = Box.createVerticalBox();
        centerBox2.add(new JLabel("TIME"), listFont);
        Box centerBox3 = Box.createVerticalBox();
        centerBox3.add(new JLabel("DIFFICULTY"), listFont);
        centerBox1.add(Box.createRigidArea(new Dimension(0,15)));
        centerBox2.add(Box.createRigidArea(new Dimension(0,15)));
        centerBox3.add(Box.createRigidArea(new Dimension(0,15)));

        // Scanner used, to read all the lines of opList.txt
        Scanner scan = null;
        try {
            File topl = new File("TopList.txt");
            scan = new Scanner(topl);

            while(scan.hasNextLine()) {
                String line = scan.nextLine();
                String[] lineArr = line.split(";");

                JLabel tmplab1 = new JLabel(lineArr[0]);
                tmplab1.setFont(listFont);
                JLabel tmplab2 = new JLabel(lineArr[1]);
                tmplab2.setFont(listFont);
                JLabel tmplab3 = new JLabel(lineArr[2]);
                tmplab3.setFont(listFont);
                centerBox1.add(tmplab1);
                centerBox2.add(tmplab2);
                centerBox3.add(tmplab3);
                centerBox1.add(Box.createRigidArea(new Dimension(0,5)));
                centerBox2.add(Box.createRigidArea(new Dimension(0,5)));
                centerBox3.add(Box.createRigidArea(new Dimension(0,5)));
            }
        } catch (FileNotFoundException e) {
            Logger logger = Logger.getLogger(TopList.class.getName());
            logger.log(Level.WARNING, "TopList.txt was not found!", e);
        }

        if(scan != null) {
            scan.close();
        }

        centerp.add(centerBox1);
        centerp.add(Box.createHorizontalStrut(100));
        centerp.add(centerBox2);
        centerp.add(Box.createHorizontalStrut(100));
        centerp.add(centerBox3);

        JPanel southp = new JPanel();

        back.setPreferredSize(new Dimension(100, 30));
        back.addActionListener(this);

        southp.add(back);

        this.add(northp, BorderLayout.NORTH);
        this.add(centerp, BorderLayout.CENTER);
        this.add(southp, BorderLayout.SOUTH);
        this.setVisible(true);
    }

    /**
     * The required implementation of actionPerformed defined in ActionListener.
     * If it was clicked on back, it closes this window and opens the gui.MainMenu.
     * @param e ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == back) {
            this.setVisible(false);
            new MainMenu();
        }
    }
}
