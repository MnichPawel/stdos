package CPU;

import ProcessManager.*;

import java.util.Queue;

public class CPU {

    PCB RUNNING;
    PCB ZEROPRIORITY;
    PriorityList priorityList = new PriorityList();

    //Konstruktor
    public CPU() {

    }

    //szuka procesu o najwyzszym priorytecie
    public void MM_find_ready(){
        PCB tmp;
        tmp = priorityList.getHighestPriority();
        if(tmp != null) RUNNING = tmp;
        else RUNNING = ZEROPRIORITY;
    }

    //dodaje proces do listy gotowych procesow
    public void MM_add_ready(PCB ready_process){
        priorityList.addProcess(ready_process);
        ready_process.setPs(ProcessState.READY);
    }

    //aktualizuje priorytet chwilowy
    public void MM_refresh_priority(){
        priorityList.updateDynamicPriority();

    }

    //wyswietla dane wykonywanego procesu
    public void MM_show_running(){
        System.out.println("RUNNING: id: "+RUNNING.getPid()+" name: "+RUNNING.getPn());
    }

    //wyswietla liste gotowych procesow wraz z priorytetami
    public void MM_show_actual_priority(){

    }
}
