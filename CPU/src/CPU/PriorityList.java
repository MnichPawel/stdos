package CPU;

import Processes.*;

import java.util.*;

public class PriorityList {

    private List<Queue<PCB>> priorityList = new ArrayList<>(15);
    private List<Boolean> boolPriorityList = new ArrayList<>(15);

  /*------------------------------------------------------------------------*/
 /*---------Public functions-----------------------------------------------*/
/*------------------------------------------------------------------------*/
    public PriorityList() {
        for(int i=0; i<15; i++){
            priorityList.add(new LinkedList<>());
        }
    }

    public void updateBoolean(){
        for(int i=0; i<15; i++){
            if(priorityList.get(i).isEmpty()) boolPriorityList.set(i,false);
            else boolPriorityList.set(i,true);
        }
    }

    public PCB getHighestPriority(){
        for(int i = 0; i < 15; i++)
            if(!this.boolPriorityList.get(i))
                return this.priorityList.get(i).poll();
        return null;
    }

    public void addProcess(PCB p1){
        priorityList.get(p1.getPriS()).add(p1);
    }

    public void deleteProcess(int pid){}

    public void updateDynamicPriority(){
        int tmp;
        for (int i = 0; i < 15; i++){
            for(PCB e: priorityList.get(i)){
                e.setWt(e.getWt()+1);
                tmp = e.getPriS() + e.getWt() - e.getPC();
                if(tmp > 15) tmp = 15;
                if(tmp < 1) tmp = 1;
                e.setPriD(tmp);
            }
        }
        cleanUpPriority();
        updateBoolean();
    }

    public void displayQueues(){
        PCB tmp;
        for(int i = 0; i < 15; i++){
            System.out.print((i+1) + ": ");
            for(int j = 0; j < priorityList.get(i).size(); j++){
                tmp = priorityList.get(i).poll();
                System.out.print("[ " + tmp.getPid() + " " + tmp.getPn() + " ] ");
                priorityList.get(i).add(tmp);
            }
            System.out.print("\n");
        }
    }

  /*--------------------------------------------------------------------------*/
 /*---------Private functions-----------------------------------------------*/
/*------------------------------------------------------------------------*/

    private void cleanUpPriority(){
        priorityList.get(14).addAll(priorityList.get(13));
        for(int i = 13; i > 0; i--){
            priorityList.set(i, priorityList.get(i-1));
        }
        priorityList.set(0, null);
    }
}
