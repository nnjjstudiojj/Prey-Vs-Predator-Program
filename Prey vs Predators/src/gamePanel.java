import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.Timer;

public class gamePanel extends JPanel { // complete
    Prey prey;
    Predator predator;
    int size = 5; // switch back to 5 when finished testing
    boolean endProgram = false;

    public gamePanel() {
        int frameWidth = 600;
        int frameHeight = 600;

        this.setPreferredSize(new Dimension(frameWidth, frameHeight));
        this.setBackground(new Color(125, 255, 125));

        prey = new Prey();
        predator = new Predator();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (!predator.animalArray.isEmpty()) predator.Methods(prey.animalArray, prey.deadAnimalIndex);

                else {
                    System.out.println("Predator extinct");
                    endProgram = true;
                    cancel();
                }

                if (!prey.animalArray.isEmpty()) prey.Methods(predator.animalArray);

                else {
                    System.out.println("Prey extinct");
                    endProgram = true;
                    cancel();
                }
                repaint();
            }
        };
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(timerTask, 0, 300);

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.ORANGE);
        for (Point Prey: prey.animalArray) {
            g.fillRect(Prey.x, Prey.y, size, size);
        }

        g.setColor(Color.RED);
        for (Point predator: predator.animalArray) {
            g.fillRect(predator.x, predator.y, size, size);
        }
    }
}
