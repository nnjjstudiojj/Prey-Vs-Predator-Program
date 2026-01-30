import javax.swing.*;

public class Main {
    public static void main(String[] args) { // just need to add a way to tally the total number of creatures

        JFrame frame = new JFrame("PvP");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gamePanel gamePanel = new gamePanel();

        frame.setResizable(false);
        frame.add(gamePanel);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        if (gamePanel.endProgram) frame.dispose();
    }
}