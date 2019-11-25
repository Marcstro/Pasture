import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.util.List;



public class Wolf implements Entity {
    private final ImageIcon image = new ImageIcon("wolf.gif"); 
    private final Pasture pasture;
    

    private boolean alive;
    private int hunger;
    int birthCooldown, reproduction;//OBS OBS 
    int sight;//NOTE NOTE NOTE
    int direction;//NO COMMENTS HERE, CHECK SHEEP CLASS INSTEAD
    int speed, moveDelay;

    public Wolf(Pasture pasture) {
        this(pasture, true);
    }


    public Wolf(Pasture pasture, boolean alive) {
        this.pasture   = pasture;
        this.alive     = alive;
        int[] temp = pasture.GetWolfData();
        
        speed          = temp[0];
        moveDelay      = speed;
        hunger         = temp[3];
        reproduction   = temp[2];
        birthCooldown  = reproduction;
        sight          = temp[1];
        direction      = 0;
    }


    public void tick() {
        
        if(alive) {
            moveDelay--;
            hunger--;
            birthCooldown--;
        
            Point neighbour;
        
            if(birthCooldown==0){
                neighbour = pasture.getRandomMember(pasture.getFreeNeighbours(this));
                    if(neighbour != null) {
                    
                        Wolf w = new Wolf(pasture);
                        pasture.addEntity(w,neighbour);
                    }
                        birthCooldown=reproduction;
                } 
                    
        
        
        if(moveDelay == 0) {
            
            pasture.moveEntity(this,
                    pasture.AI(direction, pasture.getEntityPosition(this),
                     getSpecies(), this, sight));
            
            pasture.eatOrEaten(pasture.getEntityPosition(this));
            moveDelay = speed;
            
        }
        
        if(hunger==0){
            kill();
        }
    }
}
    
    public ImageIcon getImage() {
        return image;
    }


    public boolean isCompatible(Entity otherEntity) {
        if(otherEntity.getSpecies()==1) return true;
        else
        return false;
    }


    
    
    public int getSpecies(){
     return 3;   
    }
    
    
    public void kill(){
        alive=false;
        pasture.removeEntity(this);
    }
    
    public boolean checkLife(){
     return alive;   
    }
    
    public void fed(){
        hunger=200;
    }
    
    public float dist(Point a, Point b){
        int dx=Math.abs(a.x - b.x);
        int dy=Math.abs(a.y-b.y);
        float distance = Math.max(dx,dy);
        distance=(float)(distance*distance);
     return distance;  
    }
    
    public int getSpeed(){
     return speed;   
    }
    
    public void direct(int d){
        direction=d;
    }

}
