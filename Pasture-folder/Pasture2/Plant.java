import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.util.List;


public class Plant implements Entity {
    
    private final ImageIcon image = new ImageIcon("plant.gif"); 
  
    private final Pasture pasture;
   
    

    private  boolean alive;//necessary to be able to give birth to others
    
    private int birthCooldown, reproduction;//ones ticking down, the other to reset
    private int deathCooldown;//this can allow plants to wither and die
    
    
  
    public Plant(Pasture pasture) {
        this(pasture, true);
    }

    
    public Plant(Pasture pasture, boolean alive) {
        this.pasture   = pasture;
        this.alive     = alive;
        int[] temp     = pasture.GetPlantData();//all data about plants come from here
        reproduction   = temp[0];//its not much though
        birthCooldown  = reproduction;
        deathCooldown  = 71;
    }

    
    public void tick() {
        if(alive) {
           
            birthCooldown--;
            deathCooldown--;//while alive it gets closer to giving "birth"
        if (birthCooldown==0){
            
                    Point neighbour = 
                pasture.getRandomMember(pasture.getFreeNeighbours(this));
            
                if(neighbour != null){//if there's space it will create a new flower
                    Plant p = new Plant(pasture);
                    pasture.addEntity(p,neighbour);
                    
                }
                birthCooldown=reproduction;
        }
        if(deathCooldown==0){//removed, enable this and plants begin to die
         //pasture.removeEntity(this);   
        }
       }
    }
    
    public ImageIcon getImage() {
        return image;
    }
    public void kill(){
        alive=false;
        pasture.removeEntity(this);
    }

   
    public boolean isCompatible(Entity otherEntity) {
        
        if (otherEntity.getSpecies()==1){//2 (sheep) and 3 (wolves) can walk on plants
            return false;//1 (other plants) cant grow on other plants
        }
        else if (otherEntity.getSpecies()==2 || otherEntity.getSpecies()==3){
            return true;
        }
        else
        return false;
    }
    
    public int getSpecies(){
     return 1;   //method to say im a plant
    }
    
    public void fed(){
    }
    
    public boolean checkLife(){
     return alive;   
    }
    public void direct(int d){
    }
    
    
}
