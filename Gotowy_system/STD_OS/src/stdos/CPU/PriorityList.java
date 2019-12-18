package stdos.CPU;

import stdos.Processes.*;

import java.util.*;

class PriorityList {

    private static final int NOP = 15; //Number of Priorities

    private ArrayList<PCB>[] priorityList = new ArrayList[NOP];
    private boolean[] boolPriorityList = new boolean[NOP];



    /*------------------------------------------------------------------------*/
    /*---------Public functions-----------------------------------------------*/
    /*------------------------------------------------------------------------*/


    /*Konstruktor, inicjalizacja listy*/
    PriorityList() {
        for(int i=0; i<NOP; i++){
            priorityList[i] = new ArrayList<PCB>();
        }
    }


    /*Iteruje od najwyzszego priorytetu przez tablice bool, w momencie gdy znajdzie niepusta kolejke,
     zwraca pierwszy element*/
    PCB getHighestPriority(){
        for(int i = NOP - 1; i > -1; i--)
            if(this.boolPriorityList[i])
                return this.priorityList[i].get(0);
        return null;
    }


    /*Dodaje proces p1 do odpowiedniego miejsca w tablicy kolejek. Sprawdza czy nie dany element nie znajduje sie
     w kolejce oraz czy nie jest to proces o zerowym priorytecie*/
    void addProcess(PCB p1){
        if(p1.getPriS() == 0) return;
        for(int i = 0; i < NOP; i++){
            if(boolPriorityList[i]){
                for(int j = 0; j < priorityList[i].size(); j++){
                    if(priorityList[i].get(j).getPid() == p1.getPid())
                        return;
                }
            }
        }
        priorityList[p1.getPriD() - 1].add(p1);
        updateBoolean();
    }


    /*Usuwa proces z tablicy kolejek*/
    void deleteProcess(int pid){
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


    /*Implementacja postarzania procesow. Zwieksza priorytet dynamiczny o 1. Priorytet nie może być większy od 15.*/
    void updateDynamicPriority(){
        int tmp;
        for (int i = 0; i < NOP; i++){
            for(PCB e: priorityList[i]){
                e.setWt(e.getWt() + 1);
                tmp = e.getPriD() + 1;
                if(tmp > 15) tmp = 15;
                else if(tmp < 1) tmp = 1;
                else if(tmp < e.getPriS()) tmp = e.getPriS();
                e.setPriD(tmp);
            }
        }
        cleanUpPriority();
        updateBoolean();
    }


    /*Wyswietla zawartosc wszystkich kolejek*/
    void displayQueues(){
        System.out.println("Priority Queues:");
        for(int i = 0; i < NOP; i++){
            System.out.print((i+1) + ": ");
            for(int j = 0; j < priorityList[i].size(); j++){
                System.out.print("[ " + priorityList[i].get(j).getPid() + " " + priorityList[i].get(j).getPn() + " " +
                        priorityList[i].get(j).getPriS() + " ] ");
            }
            System.out.print("\n");
        }
    }



    /*--------------------------------------------------------------------------*/
    /*---------Private functions-----------------------------------------------*/
    /*------------------------------------------------------------------------*/


    /*Przenosi PCB procesow do kolejek odpowiadajacych ich priorytetowi dynamicznemu*/
    private void cleanUpPriority(){
        for(int i = 0; i < NOP; i++){
            for(int j = (priorityList[i].size() - 1); j > -1; j--){
                int tmpPriD = priorityList[i].get(j).getPriD();
                if(tmpPriD != i+1){
                    priorityList[tmpPriD - 1].add(priorityList[tmpPriD - 1].size(), priorityList[i].remove(j));

                }
            }
        }
    }


    /*Aktualizuje tablice boolowska*/
    private void updateBoolean(){
        for(int i=0; i<NOP; i++){
            boolPriorityList[i] = !priorityList[i].isEmpty();
        }
    }
}
