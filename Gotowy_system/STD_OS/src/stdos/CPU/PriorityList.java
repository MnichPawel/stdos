package stdos.CPU;

import stdos.Processes.*;
import static stdos.Processes.ProcessManager.*;

import java.util.*;

public class PriorityList {

    private static final int NOP = 15; //Number of Priorities

    private ArrayList<PCB>[] priorityList = new ArrayList[NOP];
    private Boolean[] boolPriorityList = new Boolean[NOP];

  /*------------------------------------------------------------------------*/
 /*---------Public functions-----------------------------------------------*/
/*------------------------------------------------------------------------*/
    public PriorityList() {
        for(int i=0; i<NOP; i++){
            priorityList[i] = new ArrayList<PCB>();
        }
    }



    public PCB getHighestPriority(){
        for(int i = 0; i < NOP; i++)
            if(!this.boolPriorityList[i])
                return this.priorityList[i].remove(0);
        return null;
    }

    public void addProcess(PCB p1){
        priorityList[p1.getPriS()].add(p1);
        updateBoolean();
    }

    public void deleteProcess(int pid){
        for(int i = 0; i < NOP; i++){
            for(int j = 0; j < priorityList[i].size(); j++){
                if(priorityList[i].get(j).getPid() == pid){
                    priorityList[i].remove(j);
                    updateBoolean();
                    return;
                }
            }
        }
    }

    public void updateDynamicPriority(){
        int tmp;
        for (int i = 0; i < NOP; i++){
            for(PCB e: priorityList[i]){
                e.setWt(e.getWt() + 1);
                tmp = e.getPriS() + e.getWt() - e.getPC();
                if(tmp > 15) tmp = 15;
                else if(tmp < 1) tmp = 1;
                else if(tmp < e.getPriS()) tmp = e.getPriS();
                e.setPriD(tmp);
            }
        }
        cleanUpPriority();
        updateBoolean();
    }

    public void displayQueues(){
        System.out.println("Priority Queues:");
        for(int i = 0; i < NOP; i++){
            System.out.print((i+1) + ": ");
            for(int j = 0; j < priorityList[i].size(); j++){
                System.out.print("[ " + priorityList[i].get(j).getPid() + " " + priorityList[i].get(j).getPn() + " ] ");
            }
            System.out.print("\n");
        }
    }

  /*--------------------------------------------------------------------------*/
 /*---------Private functions-----------------------------------------------*/
/*------------------------------------------------------------------------*/

    private void cleanUpPriority(){
        priorityList[14].addAll(priorityList[13]);
        for(int i = 13; i > 0; i--){
            priorityList[i] = priorityList[i-1];
        }
        priorityList[0].clear();
    }

    public void printObjectID(){
        System.out.println("Priority Queues:");
        for(int i = 0; i < NOP; i++){
            System.out.print((i+1) + ": ");
            for(int j = 0; j < priorityList[i].size(); j++){
                System.out.print(priorityList[i].get(j).toString());
            }
            System.out.print("\n");
        }
    }

    private void updateBoolean(){
        for(int i=0; i<NOP; i++){
            if(priorityList[i].isEmpty()) boolPriorityList[i] = false;
            else boolPriorityList[i] = true;
        }
    }
}
