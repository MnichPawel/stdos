package CPU;

import ProcessManager.PCB;
import ProcessManager.ProcessState;

import java.util.List;

import static ProcessManager.ProcessManager.KM_getReadyProcessList;
import static ProcessManager.ProcessManager.KM_setProcessState;

public class CPU {

    private static final int TIME_QUANTUM = 1;

    public static PCB RUNNING;
    public static PCB ZEROPRIORITY;
    private static PriorityList priorityList = new PriorityList();

    //Konstruktor
    public CPU() {
        List<PCB> tmp = KM_getReadyProcessList();
        for(int i = 0; i < tmp.size(); i++){
            if(tmp.get(i).getPriS() == 0) ZEROPRIORITY = tmp.get(i);
            else{
                priorityList.addProcess(tmp.get(i));
            }
        }
    }

    //TODO: metoda go()?, kontrola  assemblera, żeby nie wykonał za dużo rozkazów
    public void MM_go(){
        int executedOrders = 0;

        MM_scheduler();

        while(executedOrders < TIME_QUANTUM){
            if(!KK_Interpret()) break;  //funkcja zwraca  0, gdy wykona ostatni rozkaz lub nie ma dalszych rozkazów
            else executedOrders++;
        }
    }

    public void MM_scheduler(){
        PCB tmp;
        MM_refreshPriority();

        tmp = MM_findReady();

        if(tmp.getPriS() > RUNNING.getPriS()){
            KM_setProcessState(RUNNING, ProcessState.READY);
            //TODO: add previous RUNNING to priority list????
            KM_setProcessState(tmp, ProcessState.RUNNING);
            RUNNING = tmp;
        }
    }

    //szuka procesu o najwyzszym priorytecie
    public PCB MM_findReady(){
        PCB tmp;
        tmp = priorityList.getHighestPriority();
        if(tmp != null) {
            return tmp;
        }
        else return ZEROPRIORITY;
    }

    //dodaje proces do listy gotowych procesow
    public void MM_addReadyProcess(PCB ready_process){    //for semafor usage
        priorityList.addProcess(ready_process);
        KM_setProcessState(ready_process, ProcessState.READY); //TODO: useless, semafor do same thing
    }

    public void MM_unreadyProcess(PCB pcb){         //for semafor usage
        KM_setProcessState(pcb, ProcessState.WAITING); //TODO: useless, semafor do same thing
        priorityList.deleteProcess(pcb.getPid());
    }

    //aktualizuje priorytet chwilowy
    public void MM_refreshPriority(){
        priorityList.updateDynamicPriority();
    }

/* -----------------STEP MODE---------------------------------------------------------------------*/

    //wyswietla dane wykonywanego procesu
    public static void MM_show_running(){
        System.out.println("RUNNING: id: "+RUNNING.getPid()+" name: "+RUNNING.getPn());
    }

    //wyswietla liste gotowych procesow wraz z priorytetami
    public static void MM_show_actual_priority(){
        priorityList.displayQueues();
        System.out.print("RUNNING: [ "+ RUNNING.getPid() + " " + RUNNING.getPn() + " " + RUNNING.getPriS() + " " + RUNNING.getPriD());
    }

/*--------------------GET---------------------------------------------------------------------------*/

    public static PCB MM_getRUNNING(){
        return RUNNING;
    }
    public static PCB MM_getZEROPRIORITY(){            //may be useless
        return ZEROPRIORITY;
    }
}
