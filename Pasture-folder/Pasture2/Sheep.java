import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.util.List;


public class Sheep implements Entity {
    private final ImageIcon image = new ImageIcon("sheep.gif"); 
    private final Pasture pasture;
    
    
    private boolean alive;//required to do stuff
    int hunger;//if this reaches 0 the creature dies
    int birthCooldown, reproduction;//one's to tick down, the other to reset
    int sight;//how far it can see
    int direction;//the direction it has walked in the past
    int speed, moveDelay;//to move and a reset for movement

   
    public Sheep(Pasture pasture) {
        this(pasture, true);
    }

    public Sheep(Pasture pasture, boolean alive) {
        this.pasture   = pasture;
        this.alive     = alive;
        int[] temp = pasture.GetSheepData();//retrieve all data from the pasture class
        speed          = temp[0];//setting the variables
        moveDelay      = speed;
        hunger         = temp[3];
        reproduction   = temp[2];
        birthCooldown  = reproduction;
        sight          = temp[1];
        direction      = 0;//hasn't moved, 0=standing still
    }

 
    public void tick() {
        
        if(alive) {
            moveDelay--;//life's costing each day
            hunger--;
            birthCooldown--;
            
            Point neighbour;
                if(birthCooldown==0){//if alive for long enough it gives birth
                    neighbour = pasture.getRandomMember(pasture.getFreeNeighbours(this));
                    if(neighbour != null) {
                    
                        Sheep s = new Sheep(pasture);//if null means there's space
                        pasture.addEntity(s,neighbour);
                    }
                    birthCooldown=reproduction;//resets teh birth cooldown
                }
                
                if(moveDelay == 0) {
                    
                    
                    pasture.moveEntity(this,
                    pasture.AI(direction, pasture.getEntityPosition(this),
                     getSpecies(), this, sight));
                    //sheep checks this method
                    pasture.eatOrEaten(pasture.getEntityPosition(this));//for where to go
                    moveDelay = speed;//and what to avoid/eat
            
               }
               if(hunger==0){//hasn't eaten in long enough and it dies
                   kill();
                }
    }
        
    }

    public ImageIcon getImage() {
        return image;
    }


    public boolean isCompatible(Entity otherEntity) {
        if(otherEntity.getSpecies()==3) return true;//checks the other creatures ID
        else//wolves (3) can enter into sheeps (and devour them)
        return false;
    }

 
    
    public int getSpecies(){//a way to check for animals species
     return 2;   
    }
    
    public boolean checkLife(){
     return alive;   
    }
    
    public void kill(){//sheep died
        alive=false;
        pasture.removeEntity(this);
    }
    public void fed(){//sheep fed
        hunger=100;
    }
    public void direct(int d){
        direction=d;
    }
    
    public float dist(Point a, Point b){//checks distance between two points
        int dx=Math.abs(a.x - b.x);//later, the motivation to walk a particular direction
        int dy=Math.abs(a.y-b.y);//is divided by the distance^2
        float distance = Math.max(dx,dy);
        distance=(float)(distance*distance);
     return distance;  
    }
    
}
