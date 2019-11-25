import java.util.*;
import java.awt.Point;
import java.util.ArrayList;



public class Pasture {

    private final int   width = 35;
    private final int   height = 24;//size of pasture

    private  int   sheep;
    private  int   wolves;//our animals!
    private  int   plants;
    

    private final Set<Entity> world = //array of all creatures
        new HashSet<Entity>();
    private final Map<Point, List<Entity>> grid = //array of entities points
        new HashMap<Point, List<Entity>>();
    private final Map<Entity, Point> point //array of points entities 
        = new HashMap<Entity, Point>();
        

    private final PastureGUI gui;
    
    int SheepNumber, WolfNumber, PlantNumber, SheepSpeed,
    WolfSpeed, SheepSight, WolfSight, SheepRep, WolfRep,
    PlantRep, SheepDeath, WolfDeath, FenceNumber;//data from parameters
    
        public Pasture(ArrayList<Integer> listan){//here comes the list with data
            
        SheepNumber=listan.get(10);
        WolfNumber=listan.get(1);
        PlantNumber=listan.get(5);
        SheepSpeed=listan.get(0);
        WolfSpeed=listan.get(8);
        SheepSight=listan.get(6);
        WolfSight=listan.get(12);
        SheepRep=listan.get(4);
        WolfRep=listan.get(2);
        PlantRep=listan.get(11);
        SheepDeath=listan.get(9);
        WolfDeath=listan.get(3);//setting the data
        FenceNumber=listan.get(7);
        
        
            
        Engine engine = new Engine(this);//start the engines
        
        gui = new PastureGUI(width, height, engine);//and gui
        
        for (int i = 0; i < width; i++) {//creating fence around the pasture
            addEntity(new Fence(this, false), new Point(i,0));
            addEntity(new Fence(this, false), new Point(i, height - 1));
        }
        for (int i = 1; i < height-1; i++) {
            addEntity(new Fence(this, false), new Point(0,i));
            addEntity(new Fence(this, false), new Point(width - 1,i));
        }

        
        
        for (int i = 0; i < SheepNumber; i++) {//adding all creatures
            Sheep sheep = new Sheep(this, true);
            addEntity(sheep, getFreePosition(width, height, sheep));
        }
        for (int i = 0; i < WolfNumber; i++) {
            Wolf wolves = new Wolf(this, true);
            addEntity(wolves, getFreePosition(width, height, wolves));
        }
        for (int i = 0; i < PlantNumber; i++) {
            Plant plants = new Plant(this, true);
            addEntity(plants, getFreePosition(width, height, plants));
        }
        for (int i = 0; i < FenceNumber; i++) {
            Fence fences = new Fence(this, true);
            addEntity(fences, getFreePosition(width, height, fences));
        }
        
        
        gui.sizeWithoutFence();
        gui.update();
    }
    

    public void refresh() {
        gui.update();
    }
    
    public int[] GetWolfData(){//when a creature is created
        //here it gets its necessary information
        int[] wolfdata = new int[]{WolfSpeed, WolfSight, WolfRep, WolfDeath};
        return wolfdata;
    }
    public int[] GetSheepData(){
        
        int[] sheepdata = new int[]{SheepSpeed, SheepSight, SheepRep, SheepDeath};
        return sheepdata;
    }
    public int[] GetPlantData(){
        
        int[] plantdata = new int[]{PlantRep};
        return plantdata;
    }


 
    public void addEntity(Entity entity, Point pos) {
        
        
        world.add(entity);//add entity, 
            //add it to all the lists
        List<Entity> l = grid.get(pos);
        if (l == null) {
            l = new  ArrayList<Entity>();
            grid.put(pos, l);
        }
        l.add(entity);

        point.put(entity,pos);

        gui.addEntity(entity, pos);
        
    }
    
    public void moveEntity(Entity e, Point newPos) {
        
        Point oldPos = point.get(e);//move it in all the lists and so on
        List<Entity> l = grid.get(oldPos);
        if (!l.remove(e)) 
            throw new IllegalStateException("Inconsistent stat in Pasture");
        
                
        l = grid.get(newPos);
        if (l == null) {
            l = new ArrayList<Entity>();
            grid.put(newPos, l);
        }
        l.add(e);

        point.put(e, newPos);

        gui.moveEntity(e, oldPos, newPos);
        
    }

    
    public void removeEntity(Entity entity) { 

        Point p = point.get(entity);//bye bye
        world.remove(entity); 
        grid.get(p).remove(entity);
        point.remove(entity);
        gui.removeEntity(entity, p);
        

    }
    
    public void eatOrEaten(Point p){
       
        List<Entity> l = grid.get(p);//heres a list of all creatures on a point
        
             
             if(l.size()>1){//if more than 1 creature on a spot, do something
             if((l.get(0).getSpecies()-l.get(1).getSpecies()==1)){
                 l.get(0).fed();
                 l.get(1).kill();//if one can eat the other
                }
                
                else if(l.get(0).getSpecies()-l.get(1).getSpecies()==-1){
                    l.get(1).fed();//or the other can eat the first
                    l.get(0).kill();
                }
         }
        
            
    }
    
    public <X> X getRandomMember(List<X> c) {
        if (c.size() == 0)
            return null;
        
        int n = (int)(Math.random() * c.size());
        
        return c.get(n);
    }
    
    

   
    public List<Entity> getEntities() {//kinda obvious from here on down
        return new ArrayList<Entity>(world);//does what the methods name says
        
    }
        
    public Collection<Entity> getEntitiesAt(Point lookAt) {

        Collection<Entity> l = grid.get(lookAt);
        
        if (l==null) {
            return null;
        }
        else {
            return new ArrayList<Entity>(l);
        }
    }
    
    public List<Point> getFreeNeighbours(Entity entity) {
        List<Point> free = new ArrayList<Point>();
        
        int entityX = getEntityPosition(entity).x;
        int entityY = getEntityPosition(entity).y;
        
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                
                Point p = new Point(entityX + x,
                          entityY + y);
                if (freeSpace(p, entity))
                    free.add(p);
            }
        }
        return free;
    }

    public boolean freeSpace(Point p, Entity e) {
                              
        List <Entity> l = grid.get(p);
        if (l == null  ){ 
        return true;}
        for (Entity old : l ) {
            if (! old.isCompatible(e)) return false;
        }
        return true;
    }

    public Point getEntityPosition(Entity entity) {
        return point.get(entity);
    }

    public Point getFreePosition(int width, int height, Entity e) 
        throws MissingResourceException {
        Point position = new Point((int)(Math.random() * width),
                                   (int)(Math.random() * height)); 

        int p = position.x+position.y*width;
        int m = height * width;
        int q = 97; //any large prime will do

        for (int i = 0; i<m; i++) {
            int j = (p+i*q) % m;
            int x = j % width;
            int y = j / width;

            position = new Point(x,y);
            boolean free = true;

            Collection <Entity> c = getEntitiesAt(position);
            if (c != null) {
                for (Entity thisThing : c) {
                    if(!e.isCompatible(thisThing)) { 
                        free = false; break; 
                    }
                }
            }
            if (free) {
                return position;
            }
        }
        throw new MissingResourceException(
                  "There is no free space"+" left in the pasture",
                  "Pasture", "");
    }
    
    public float dist(Point a, Point b){
        int dx=Math.abs(a.x - b.x);
        int dy=Math.abs(a.y-b.y);
        float distance = Math.max(dx,dy);
        distance=(float)(distance*distance);
     return distance;  
    }
    
    public boolean isEntityAt(Point lookAt, int sought){
        
        Collection<Entity> l = grid.get(lookAt);
        //this method check if a particular animal is at a point
        //if it is, return true, otherwise false
        if (l==null) {
            return false;
        }
        
       for (Entity old : l ){
           if (old.getSpecies()==sought){
              return true;}
            }
            return false;
        }
    
    public Point AI(int dire, Point po, int Species, Entity ent, int sightlength){
        
        float s=0, n=0, w=0, e=0;//all directions have values
        float se=0,sw=0,nw=0,ne=0;//indicating motivation to walk that way
        
        Point pos = po;
        int x = pos.x;
        int y = pos.y;
        int tempx, tempy;
        Point temp;
        int direction = dire;
        Entity enten = ent;//This creature
        int food = enten.getSpecies()-1;//the kind of animal is food
        int avoid = food+2;//the kind of animal that eats this animal
        int sight = sightlength;
        
        /** okey so sight = how far it can see.
         * At first s1 = 1, it increases until it has reached max sight distance
         * at first the creature looks 1 step away, then 2, then 3 and so on
         * the for-loop with a is for changing the direction the creature is looking
         * first it's north, northwest and northeast 
         * (that includes all spaces in between those directions)
         * 
         * Then its east, northeast and southeast
         * then south
         * then west
         * after a complete lap, it increases sight range
         * until sight range is at max
         * Onto Stage 2!
         */
        
        //method that gives value to directions
        //return float array with (sw,w,nw,s,n,se,e,ne)
        float[] tempPosDir = Stage1AI(pos, food, avoid, sight);
        
        
        //method that makes sure animal doesn't go into fence/other animal it can't eat
        //return float array with (sw,w,nw,s,n,se,e,ne)
        tempPosDir=Stage2AI(x, y, enten, tempPosDir);
        
        //method that calculates highest and lowest values
        //also checks for special cases where max value direction isn't enough
        //returns a direction to walk to
        int direct = Stage3AI(tempPosDir, direction);
        if (direct==-1){//this means stand still
            enten.direct(0);
         return pos;  
        }
        
        //case-switch to determine direction
        //returns a position, this method returns that position to the animal class
        Point p1 = Stage4AI(direct, pos);
        
        
        enten.direct(direct);//sends the animal its direction
        //so it will remember it for the future
        return p1;//point the animal should go to
        
    }
        
    public float[] Stage1AI(Point p, int f, int aa, int ss){
         Point pos = p;
         int food = f;
         int avoid = aa;
         int sight = ss;
         int x = p.x;
         int y = p.y;
         float s=0, sw=0, se=0, n=0, w=0, e=0, nw=0, ne=0;
         for (int s1=1; s1<=sight; s1++){//AI STAGE 1
            
            Point temp = new Point((x),(y+s1));//north
                 if(isEntityAt(temp, avoid)){
                     s=s+((float)10/(dist(pos, temp)));
                     sw=sw+((float)6/(dist(pos, temp)));
                     se=se+((float)6/(dist(pos, temp)));
                     w=w+((float)2/(dist(pos, temp)));
                     e=e+((float)2/(dist(pos, temp)));
                    }
                    else if(isEntityAt(temp, food)){
                     n=n+((float)0.1/(dist(pos, temp)));
                     nw=nw+((float)0.06/(dist(pos, temp)));
                     ne=ne+((float)0.06/(dist(pos, temp)));
                     w=w+((float)0.02/(dist(pos, temp)));
                     e=e+((float)0.02/(dist(pos, temp)));
                    }
            for (int a =1; a<=sight; a++){
                 temp = new Point((x+a),(y+s1));//northeast1
                 if(isEntityAt(temp, avoid)){
                     sw=sw+((float)10/(dist(pos, temp)));
                     w=w+((float)6/(dist(pos, temp)));
                     s=s+((float)6/(dist(pos, temp)));
                     nw=nw+((float)2/(dist(pos, temp)));
                     se=se+((float)2/(dist(pos, temp)));
                    }
                    else if(isEntityAt(temp, food)){
                     ne=ne+((float)0.1/(dist(pos, temp)));
                     n=n+((float)0.06/(dist(pos, temp)));
                     e=e+((float)0.06/(dist(pos, temp)));
                     nw=nw+((float)0.02/(dist(pos, temp)));
                     se=se+((float)0.02/(dist(pos, temp)));
                    }
                 temp = new Point((x-a),(y+s1));//northwest1
                 if(isEntityAt(temp, avoid)){
                     se=se+((float)10/(dist(pos, temp)));
                     s=s+((float)6/(dist(pos, temp)));
                     e=e+((float)6/(dist(pos, temp)));
                     sw=sw+((float)2/(dist(pos, temp)));
                     ne=ne+((float)2/(dist(pos, temp)));
                    } 
                    else if(isEntityAt(temp, food)){
                     nw=nw+((float)0.1/(dist(pos, temp)));
                     w=w+((float)0.06/(dist(pos, temp)));
                     n=n+((float)0.06/(dist(pos, temp)));
                     sw=sw+((float)0.02/(dist(pos, temp)));
                     ne=ne+((float)0.02/(dist(pos, temp)));
                    }
            }
            
            temp = new Point((x+s1),(y));//east
                 if(isEntityAt(temp, avoid)){
                     w=w+((float)10/(dist(pos, temp)));
                     sw=sw+((float)6/(dist(pos, temp)));
                     nw=nw+((float)6/(dist(pos, temp)));
                     n=n+((float)2/(dist(pos, temp)));
                     s=s+((float)2/(dist(pos, temp)));
                    }
                    else if(isEntityAt(temp, food)){
                     e=e+((float)0.1/(dist(pos, temp)));
                     ne=ne+((float)0.06/(dist(pos, temp)));
                     se=se+((float)0.06/(dist(pos, temp)));
                     n=n+((float)0.02/(dist(pos, temp)));
                     s=s+((float)0.02/(dist(pos, temp)));
                    }
            for (int a =1; a<=sight; a++){
                 temp = new Point((x+s1),(y+a));//northeast2
                 if(isEntityAt(temp, avoid) && !(s1==a)){
                     sw=sw+((float)10/(dist(pos, temp)));
                     w=w+((float)6/(dist(pos, temp)));
                     s=s+((float)6/(dist(pos, temp)));
                     nw=nw+((float)2/(dist(pos, temp)));
                     se=se+((float)2/(dist(pos, temp)));
                    }
                    else if(isEntityAt(temp, food) && !(s1==a)){
                     ne=ne+((float)0.1/(dist(pos, temp)));
                     n=n+((float)0.06/(dist(pos, temp)));
                     e=e+((float)0.06/(dist(pos, temp)));
                     nw=nw+((float)0.02/(dist(pos, temp)));
                     se=se+((float)0.02/(dist(pos, temp)));
                    }
                 temp = new Point((x+s1),(y-a));//southeast1
                 if(isEntityAt(temp, avoid)){
                     nw=nw+((float)10/(dist(pos, temp)));
                     w=w+((float)6/(dist(pos, temp)));
                     n=n+((float)6/(dist(pos, temp)));
                     sw=sw+((float)2/(dist(pos, temp)));
                     ne=ne+((float)2/(dist(pos, temp)));
                    } 
                    else if(isEntityAt(temp, food)){
                     se=se+((float)0.1/(dist(pos, temp)));
                     s=s+((float)0.06/(dist(pos, temp)));
                     e=e+((float)0.06/(dist(pos, temp)));
                     sw=sw+((float)0.02/(dist(pos, temp)));
                     ne=ne+((float)0.02/(dist(pos, temp)));
                    }
            }
            
            temp = new Point((x),(y-s1));//south
                 if(isEntityAt(temp, avoid)){
                     n=n+((float)10/(dist(pos, temp)));
                     nw=nw+((float)6/(dist(pos, temp)));
                     ne=ne+((float)6/(dist(pos, temp)));
                     w=w+((float)2/(dist(pos, temp)));
                     e=e+((float)2/(dist(pos, temp)));
                    }
                    else if(isEntityAt(temp, food)){
                     s=s+((float)0.1/(dist(pos, temp)));
                     sw=sw+((float)0.06/(dist(pos, temp)));
                     se=se+((float)0.06/(dist(pos, temp)));
                     w=w+((float)0.02/(dist(pos, temp)));
                     e=e+((float)0.02/(dist(pos, temp)));
                    }
            for (int a =1; a<=sight; a++){
                 temp = new Point((x+a),(y-s1));//southeast2
                 if(isEntityAt(temp, avoid) && !(s1==a)){
                     nw=nw+((float)10/(dist(pos, temp)));
                     n=n+((float)6/(dist(pos, temp)));
                     w=w+((float)2/(dist(pos, temp)));
                     ne=ne+((float)2/(dist(pos, temp)));
                     sw=sw+((float)2/(dist(pos, temp)));
                    }
                    else if(isEntityAt(temp, food) && !(s1==a)){
                     se=se+((float)0.1/(dist(pos, temp)));
                     s=s+((float)0.06/(dist(pos, temp)));
                     e=e+((float)0.06/(dist(pos, temp)));
                     sw=sw+((float)0.02/(dist(pos, temp)));
                     ne=ne+((float)0.02/(dist(pos, temp)));
                    }
                 temp = new Point((x-a),(y-s1));//southwest1
                 if(isEntityAt(temp, avoid)){
                     ne=ne+((float)10/(dist(pos, temp)));
                     n=n+((float)6/(dist(pos, temp)));
                     e=e+((float)6/(dist(pos, temp)));
                     nw=nw+((float)2/(dist(pos, temp)));
                     se=se+((float)2/(dist(pos, temp)));
                    } 
                    else if(isEntityAt(temp, food)){
                     sw=sw+((float)0.1/(dist(pos, temp)));
                     w=w+((float)0.06/(dist(pos, temp)));
                     s=s+((float)0.06/(dist(pos, temp)));
                     nw=nw+((float)0.02/(dist(pos, temp)));
                     se=se+((float)0.02/(dist(pos, temp)));
                    }
            }
            
            temp = new Point((x-s1),(y));//west
                 if(isEntityAt(temp, avoid)){
                     e=e+((float)10/(dist(pos, temp)));
                     ne=ne+((float)6/(dist(pos, temp)));
                     se=se+((float)6/(dist(pos, temp)));
                     n=n+((float)2/(dist(pos, temp)));
                     s=s+((float)2/(dist(pos, temp)));
                    }
                    else if(isEntityAt(temp, food)){
                     w=w+((float)0.1/(dist(pos, temp)));
                     sw=sw+((float)0.06/(dist(pos, temp)));
                     nw=nw+((float)0.06/(dist(pos, temp)));
                     n=n+((float)0.02/(dist(pos, temp)));
                     s=s+((float)0.02/(dist(pos, temp)));
                    }
            for (int a =1; a<=sight; a++){
                 temp = new Point((x-s1),(y+a));//northwest2
                 if(isEntityAt(temp, avoid) && !(s1==a)){
                     se=se+((float)10/(dist(pos, temp)));
                     e=e+((float)6/(dist(pos, temp)));
                     s=s+((float)6/(dist(pos, temp)));
                     ne=ne+((float)2/(dist(pos, temp)));
                     sw=sw+((float)2/(dist(pos, temp)));
                    }
                    else if(isEntityAt(temp, food) && !(s1==a)){
                     nw=nw+((float)0.1/(dist(pos, temp)));
                     n=n+((float)0.06/(dist(pos, temp)));
                     w=w+((float)0.02/(dist(pos, temp)));
                     ne=ne+((float)0.02/(dist(pos, temp)));
                     sw=sw+((float)0.02/(dist(pos, temp)));
                    }
                 temp = new Point((x-s1),(y-a));//southwest2
                 if(isEntityAt(temp, avoid) && !(s1==a)){
                     ne=ne+((float)10/(dist(pos, temp)));
                     n=n+((float)6/(dist(pos, temp)));
                     e=e+((float)6/(dist(pos, temp)));
                     nw=nw+((float)2/(dist(pos, temp)));
                     se=se+((float)2/(dist(pos, temp)));
                    } 
                    else if(isEntityAt(temp, food) && !(s1==a)){
                     sw=sw+((float)0.1/(dist(pos, temp)));
                     w=w+((float)0.06/(dist(pos, temp)));
                     s=s+((float)0.06/(dist(pos, temp)));
                     nw=nw+((float)0.02/(dist(pos, temp)));
                     se=se+((float)0.02/(dist(pos, temp)));
                    }
            }
            
            
            
        }
      
        float[] tempPosDir = new float[]{sw,w,nw,s,n,se,e,ne}; //sw,s,se,w,e,nw,n,ne};
         return tempPosDir;
    }
        
    public float[] Stage2AI(int x1, int y1, Entity ee, float[] f){
        int x=x1;
        int y = y1;
        Entity enten = ee;
        float[] tempPosDir=f;
        
        int counter=0;
        for (int a = -1; a <= 1; a++) {//AI STAGE 2
            for (int b = -1; b <= 1; b++) {
                if(!(a==0 && b==0)){//here we check all spots right around
                Point p = new Point(x + a,y + b);//the creature
                //and if they're occupied, we set their value
                if (!freeSpace(p, enten))//really low so that the animal
                    tempPosDir[counter]=(float)tempPosDir[counter]-100;
                counter++;//doesn't walk into other things
                }
                
            }
        }
        return tempPosDir;
    }
    
    public int Stage3AI(float[] tempPosDir, int dir){
        float[] posDir = new float[]{tempPosDir[2],tempPosDir[4],tempPosDir[7],
        tempPosDir[6],tempPosDir[5],tempPosDir[3],tempPosDir[0],tempPosDir[1]};
        
        float max=(float)posDir[0];
        float min = (float)max; //STEG 3
        int direct = 0;
        int direction = dir;
        for(int i=0; i<posDir.length; i++){
            if(max<=posDir[i]){
             max=posDir[i]; //in this for loop we calculate the best
             direct=(i+1);//and worst direction to go to
            }
            if(min>posDir[i]){
                min=posDir[i];
            }
        }
        
        if((float)max==0 && (float)min==0 && direction==0){//STEG 4
        direct = (int)(Math.random()*8+1);
        }//this is if no route is interesting and it hasnt walked before
        else if(((float)max==0 && (float)min==0)){
            direct=direction;//this is if it has walked before
        }
        else if(max<0){
            return -1;
        }
        else if (max==0 && min == -100){
            if(direction==0 || posDir[(direction-1)]<-20){
            direct = (int)(Math.random()*8);
                while(posDir[direct]<0){//don't run into walls, silly sheep!
                    direct = (int)(Math.random()*8);
                }
                direct++;
            }
            else{
                direct=direction;
            }
        }
        
        return direct;
    }
    
    public Point Stage4AI(int dir, Point p){
        Point point = p;
        int direction = dir;
        int x = point.x;
        int y = point.y;
        switch (direction) {
            case 0:  x=x; y=y;
                     break;//turn directions into real positions
            case 1:  x=x-1; y=y+1;
                     break;
            case 2:  x=x; y=y+1;
                     break;
            case 3:  x=x+1; y=y+1;
                     break;
            case 4:  x=x+1; y=y;//1 step right and 0 up, that means east
                     break;
            case 5:  x=x+1; y=y-1;
                     break;
            case 6:  x=x; y=y-1;
                     break;
            case 7:  x=x-1; y=y-1;
                     break;
            case 8:  x=x-1; y=y;
                     break;
                    }
                    
            p=new Point(x,y);//return the points
            
        return p;
    }
    
    
    

}


