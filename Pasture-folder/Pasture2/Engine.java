import java.util.*;
import javax.swing.Timer;
import java.awt.event.*;


public class Engine implements ActionListener {
    
    private final int SPEED_REFERENCE = 1000; 
    private final int speed           = 10; //Number of times per second
    private final Timer timer         = new Timer(SPEED_REFERENCE/speed,this);
    private int time                  = 0;

    private Pasture pasture;//NOTICE NOTICE NOTICE
        //NO COMMENTS HERE BECAUSE THIS CLASS IS UNCHANGED

    public Engine (Pasture pasture) {
        this.pasture = pasture;
    }

    public void actionPerformed(ActionEvent event) {
     
        List<Entity> queue = pasture.getEntities();
        for (Entity e : queue) {
            e.tick();
        }
        pasture.refresh();
        time++;
    }

    public void setSpeed(int speed) {
        timer.setDelay(SPEED_REFERENCE/speed);
    }

    public void start() {
        setSpeed(speed);
        timer.start();
    }

    public void stop() {
        timer.stop();
    }

    public int getTime () {
        return time;
    }

}
