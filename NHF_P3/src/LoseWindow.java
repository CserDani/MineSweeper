import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoseWindow extends JFrame implements ActionListener {
    private final JButton ok = new JButton("OK");
    private final GameWindow gameWind;
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
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == ok) {
            this.setVisible(false);
            gameWind.setVisible(false);
            new MainMenu();
        }
    }
}
