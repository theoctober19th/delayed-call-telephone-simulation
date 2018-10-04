import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;


/**
 *
 * @author bikalpa
 */
public class TelephoneCallSimulation {

    private static final int numOfLines = 8;
    public static final int NUMBER_OF_LINKS = 3;
    private static final int maxTalkTime = 20;
    
    static CallsOnProgressList callsInProgress;
    public static ArrayList<Call> delayedCallList;
    
    static Line[] line;
    static Timer timer = new Timer();

    public TelephoneCallSimulation(){
        /*
        //adding a frate to the layout.
        JFrame frame = new JFrame();
        frame.setSize(300, 400);
        //frame.setLayout(null);

        String data[][]={ {"101","Amit","670000"},    
                          {"102","Jai","780000"},    
                          {"101","Sachin","700000"}};    
        String column[]={"ID","NAME","SALARY"};         
        JTable jt=new JTable(data,column); 
        jt.setBounds(30,40,200,300);  
        jt.setEnabled(false);
        JScrollPane sp=new JScrollPane(jt); 
        
        frame.add(sp);
        frame.setVisible(true);

        //create a table with callsOnProgress
        //JTable callsProgressTable = new JTable();
        */

    }
    
    
    static class GenerateRandomCall extends TimerTask{

        @Override
        public void run() {
             //create a random call
            Call call;
            do{
                call = new Call(line, maxTalkTime);
            }while(call.getFromLine().getState().equals("busy"));
            System.out.println("\n\n\n\nCURRENT TIME: " + new Date());
            System.out.println("\n>> A call came from: " + call.getFromLine().getID() + " TO " + call.getToLine().getID() + " at " + new Date(call.getArrivalTimestamp()) + " which has duration " + call.getDuration()/1000);
            
            //connect to a call
            call.connect(callsInProgress, delayedCallList);
            printLists();
            
            int delay = (1 + new Random().nextInt(10)) * 1000;
            timer.schedule(new GenerateRandomCall(), delay);
        }  
    }
    
    static void printLists(){
        System.out.println("\nIn progress List: ");
        for(Object c: callsInProgress){
                System.out.println((Call)c);
        }
        System.out.println("\nDelayed List: ");
        for(Call c: delayedCallList){
                System.out.println(c);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        //create 8 idle lines
        line = new Line[numOfLines];
        for (int i=0; i<numOfLines; i++){
                line[i] = new Line();
        }

        //creating a list of calls as 'Call in Progress'
        callsInProgress = new CallsOnProgressList();

        //creating a list for delayed calls
         delayedCallList = new ArrayList<Call>();

        new GenerateRandomCall().run();
    }
   
    
}
