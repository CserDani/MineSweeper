import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class WinWindow extends JFrame implements ActionListener {
    private final JButton ok = new JButton("OK");
    private final JTextField nameField = new JTextField(20);
    private final GameWindow gameWind;

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

    public class TopListEntry {
        String name;
        int time;
        String diff;
        public TopListEntry(String n, int t, String d) {
            name = n;
            time = t;
            diff = d;
        }
    }
    public void tryOnToplist(String name, int time, String diff) {
        ArrayList<TopListEntry> toplist = new ArrayList<>();

        Scanner scan = null;
        try {
            File topl = new File("TopList.txt");
            scan = new Scanner(topl);

            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                if(!line.isEmpty()) {
                    String[] lineArr = line.split(";");

                    toplist.add(new TopListEntry(lineArr[0], Integer.parseInt(lineArr[1]), lineArr[2]));
                }
            }

            scan.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(scan != null) {
            scan.close();
        }

        boolean added = false;
        for(TopListEntry entry : toplist) {
            if(entry.time > time) {
                toplist.add(toplist.indexOf(entry), new TopListEntry(name, time, diff));
                added = true;
                break;
            }
        }

        if((toplist.isEmpty() || toplist.size() < 10) && !added) {
            toplist.add(new TopListEntry(name, time, diff));
        }

        while(toplist.size() > 10) {
            toplist.remove(toplist.size()-1);
        }


        try (FileWriter fw = new FileWriter("TopList.txt")){
            for (TopListEntry entry : toplist) {
                fw.write(entry.name + ";" + entry.time + ";" + entry.diff + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == ok) {
            String name = nameField.getText();
            if(!name.isEmpty()) {
                tryOnToplist(name, gameWind.getTimeCounter(), gameWind.getDiff());
            }

            this.setVisible(false);
            gameWind.setVisible(false);
            new MainMenu();
        }
    }
}
