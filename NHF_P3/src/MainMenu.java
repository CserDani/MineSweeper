import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JFrame implements ActionListener {
    private final JButton gameButton = new JButton("START");
    private final JButton toplistButton = new JButton("TOPLIST");
    public MainMenu() {
        this.setTitle("MainMenu");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600, 400);
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        JPanel northp = new JPanel();

        JLabel menutext = new JLabel("MineSweeper Main Menu", SwingConstants.CENTER);
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
