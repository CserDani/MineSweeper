import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class GameStartMenu extends JFrame implements ActionListener {
    private final JButton start = new JButton("START");
    private final JButton back = new JButton("BACK");
    private final String[] difficulties = { "Beginner", "Normal", "Hard" };
    private final JComboBox<String> diffs = new JComboBox<>(difficulties);
    public GameStartMenu() {
        this.setTitle("GameStartMenu");
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

    public boolean hasLoad() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("SavedGame.txt"))) {
            if((GameWindow) ois.readObject() != null) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }

        return false;
    }

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
