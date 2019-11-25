import javax.swing.*;
import java.awt.*;
import java.util.*;


public interface Entity {

    public void tick();

    public ImageIcon getImage();
    
    public int getSpecies();//a method to identify what kind of creature
    
   
    public boolean checkLife();
    
    public void kill();//kill and feed methods
    
    public void fed();
    
    public boolean isCompatible(Entity otherEntity);
    
    public void direct(int direct);

}
