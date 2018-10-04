/*
 * TThis class extends the typical ArrayList, but it has one extra feature : It will have a task running in background that tells the list to either remove a value or add a value once the list is full or empty.
 */

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class CallsOnProgressList extends ArrayList{

    class CheckEmptyListTask extends TimerTask{

        @Override
        public void run() {
            try{

                //Remove a call from delayed list and admit it to progress list when its time reaches, or when the list is empty.
                if(!TelephoneCallSimulation.delayedCallList.isEmpty()){ //if there is at least one call in delayed list
                        if(CallsOnProgressList.this.size() < TelephoneCallSimulation.NUMBER_OF_LINKS){
                            for(Call call: TelephoneCallSimulation.delayedCallList){
                                if(call.getFromLine().getState().equals("idle") && call.getFromLine().getState().equals("idle")){
                                    TelephoneCallSimulation.delayedCallList.remove(call);
                                    call.setAdmittedTimestamp(System.currentTimeMillis());
                                    CallsOnProgressList.this.add(call);
                                    call.getFromLine().setState("busy");
                                    call.getToLine().setState("busy");
                                    System.out.println("\n>>" + new Date() + "transfer from delay to progress: " + call);
                                }
                            }
                        }
                    }

                
                //if time finished for the call on progress, remove it and then free the lines
                if(!CallsOnProgressList.this.isEmpty()){
                        for(Object c: CallsOnProgressList.this){
                        Call call = (Call)c;
                        if(call.getAdmittedTimestamp() != 0 
                            && call.getAdmittedTimestamp() + call.getDuration() <= System.currentTimeMillis()){
                            CallsOnProgressList.this.remove(call);
                            call.getFromLine().setState("idle");
                            call.getToLine().setState("idle");
                            System.out.println("\n>>" + new Date() + "Call terminated from progress " + call);
                        }
                    }
                }
            }catch(Exception e){}
        }        
    }
    
    public CallsOnProgressList() {
        super();
        Timer timer = new Timer();
        timer.schedule(new CheckEmptyListTask(), 0, 100);
    }
    
}
