import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.util.List;



public class Fence implements Entity {
    private final ImageIcon image = new ImageIcon("fence.gif"); 
    private final Pasture pasture;
    private int moveDelay;

    private final boolean alive;
    private int hunger=30;//really isnt much to say about this method
        //it's alive. It's immobile, that's basically it.
   
    public Fence(Pasture pasture) {
        this(pasture, true);
    }

  
    public Fence(Pasture pasture, boolean alive) {
        this.pasture   = pasture;
        this.alive     = alive;
        moveDelay      = 10;
    }

   
    public void tick() {
        
        
    }
 
    public ImageIcon getImage() {
        return image;
    }

    public boolean isCompatible(Entity otherEntity) {
        return false;//nobody can go into a fence
    }
    
    public int getSpecies(){
     return 0;   
    }
    
    public void eatOrEaten(){
     pasture.removeEntity(this); 
    }
    
    public void kill(){
    }
    public void fed(){
    }
    public boolean checkLife(){
     return alive;   
    }
    public void direct(int d){
    }
    
  

}
