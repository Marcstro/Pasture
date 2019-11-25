
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.util.List;
import java.util.ArrayList;


public class StartHere extends JPanel implements ActionListener{
    
      Pasture          pasture;//creates an instance of the pasture-class
    JButton klar;//my button that ends this screen
    JFrame f;//This picture, adding it
    String afg;
    
    ArrayList<Integer> list = new ArrayList<Integer>();//this list is going to be sent
    //to pasture with all necessary information
        public StartHere(){
        this.add("SheepNumber", "How many sheep do you want?", "20");
        this.add("WolfNumber", "How many wolves do you want?", "10");
        this.add("PlantnumberNumber", "How many plants do you want?", "40");
        
        this.add("SheepSpeed", "How fast should the sheep be?", "9");
        this.add("WolfSpeed", "How fast should the wolves be?", "8");//these are text
        //for the parameter-questions and suggested answers
        
        this.add("SheepSight", "How far should the sheep see?", "4");
        this.add("WolfSight", "How far should the wolves see?", "5");
        
        this.add("SheepRep", "How fast should the sheep reproduce?", "101");
        this.add("WolfRep", "How fast should the wolves reproduce?", "201");
        this.add("PlantRep", "How fast should the plants reproduce?", "10");
        
        this.add("SheepDeath", "How long can the sheep last without food?", "100");
        this.add("WolfDeath", "How long can the wolves last without food?", "200");
        
        this.add("FenceNumber", "How many piece of random fence do you want placed?", "40");
        
        JButton klar = new JButton("Klar!");
        

        this.add(klar);
        klar.addActionListener(this);//normal graphical stuff
        klar.setEnabled(true);
        
        f = new JFrame();
        f.add(this);
        f.pack();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        f.setLocation(screenSize.width/2 - f.getWidth()/2,
              screenSize.height/2 - f.getHeight()/2);//set size correctly


              f.setVisible(true);
              f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
              
    }
    

    void init () {
        setLayout(new GridLayout(m.size()+1, 2));//set layout correctly
    }

    private Map<String, JTextField> m = new HashMap<String, JTextField> ();
    //in this MAP-variable all information is stored temporarily
    public void add (String param, String description, String _default) {
        JLabel label = new JLabel(description);
        JTextField jtf = new JTextField(_default);

        add(label);//this method creates teh JTEXTField for the player to enter info
        add(jtf);

        m.put(param, jtf);

        setLayout(new GridLayout(m.size()+1, 2, 10, 0));
    }

    public void actionPerformed(ActionEvent e) {
        //SOMEONE CLICKED THE BUTTON
        for (Map.Entry<String, JTextField> entry : m.entrySet()) {
            String temp =entry.getValue().getText();
            list.add(Integer.parseInt(temp));
        }//all data inserted into list and a pasture-class is created and
        pasture=new Pasture(list);//sent all the information
        f.dispose();
        
    }
    

   
    public static void main (String [] arg) {
        StartHere p = new StartHere();//here the simulation begins!
    }
}
