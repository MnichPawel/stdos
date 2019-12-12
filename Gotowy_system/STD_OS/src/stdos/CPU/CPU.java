package stdos.CPU;

import stdos.Processes.PCB;
import stdos.Processes.ProcessManager;
import stdos.Processes.ProcessState;

import java.util.List;

import static stdos.Interpreter.Interpreter.*;
import static stdos.Processes.ProcessManager.*;

public class CPU {

    public static PCB RUNNING;
    public static PCB ZEROPRIORITY;
    private static PriorityList priorityList = new PriorityList();

    //Konstruktor
    public CPU() {
        ZEROPRIORITY = KM_getZeroPriorityPCB();
        if(KM_getReadyProcessList() != null) {
            List<PCB> tmp = KM_getReadyProcessList();
            for (int i = 0; i < tmp.size(); i++) {
                if (tmp.get(i).getPriS() == 0) ZEROPRIORITY = tmp.get(i);
                else {
                    priorityList.addProcess(tmp.get(i));
                }
            }
        }
        MM_scheduler();
    }

    public static void MM_go() throws Exception {

        MM_scheduler();

        if(!KK_Interpret()){//funkcja zwraca  0, gdy wykona ostatni rozkaz lub nie ma dalszych rozkazów
            ProcessManager.KM_TerminateProcess(RUNNING);
        }
        MM_refreshPriority();

    }

    //dodaje proces do listy gotowych procesow
    public static void MM_addReadyProcess(PCB ready_process){
        if(ready_process.getPs() == ProcessState.READY)
            priorityList.addProcess(ready_process);
        MM_scheduler();
    }

    public static  void MM_unreadyProcess(PCB pcb){
        priorityList.deleteProcess(pcb.getPid());
    }

    //aktualizuje priorytet chwilowy
    private static void MM_refreshPriority(){
        priorityList.updateDynamicPriority();
        if(RUNNING != ZEROPRIORITY){
            if(RUNNING.getPriD() > RUNNING.getPriS()) RUNNING.setPriD(RUNNING.getPriD()-1);
        }
    }

    private static void MM_scheduler(){
        PCB tmp;

        tmp = MM_findReady();

        if(RUNNING == null){
            RUNNING = tmp;
            ProcessManager.KM_setProcessState(RUNNING, ProcessState.RUNNING); //Zwraca T / F
        }
        else {
            if (tmp.getPriS() > RUNNING.getPriS()) {
                KM_setProcessState(RUNNING, ProcessState.READY); //Zwraca T / F
                KM_setProcessState(tmp, ProcessState.RUNNING); // Zwraca T / F
                RUNNING = tmp;
            }
        }
    }

    //szuka procesu o najwyzszym priorytecie
    private static PCB MM_findReady(){
        PCB tmp;
        tmp = priorityList.getHighestPriority();
        if(tmp != null) {
            return tmp;
        }
        else return ZEROPRIORITY;
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
    } //TODO: useless
}
