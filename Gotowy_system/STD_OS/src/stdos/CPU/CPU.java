package stdos.CPU;

import stdos.Processes.PCB;
import stdos.Processes.ProcessManager;
import stdos.Processes.ProcessState;

import java.util.List;

import static stdos.Interpreter.Interpreter.*;
import static stdos.Processes.ProcessManager.*;

public class CPU {

    public static PCB RUNNING, prevRUNNING;
    private static PCB ZEROPRIORITY;
    private static PriorityList priorityList = new PriorityList();



    /* -----------------Public---------------------------------------------------------------------*/


    /*Konstruktor, przypisanie procesow z modulu zarzadzania procesami, uruchomienie planisty*/
    public CPU() {
        ZEROPRIORITY = KM_getZeroPriorityPCB();
        if (KM_getReadyProcessList() != null) {
            List<PCB> tmp = KM_getReadyProcessList();
            for (int i = 0; i < tmp.size(); i++) {
                if (tmp.get(i).getPriS() == 0) ZEROPRIORITY = tmp.get(i);
                else {
                    priorityList.addProcess(tmp.get(i));
                }
            }
        }
        MM_scheduler();
        prevRUNNING = RUNNING;
    }


    /*Wykonanie kroku procesora. Skutkuje wykonaniem jednego rozkazu. Po wykonanym rozkazie uruchamiany jest planista*/
    public static void MM_go() throws Exception {
        prevRUNNING = RUNNING;

        if (!KK_Interpret()) {//funkcja zwraca  0, gdy wykona ostatni rozkaz lub nie ma dalszych rozkazów
            if(ProcessManager.KM_TerminateProcess(RUNNING)) RUNNING = null;
        }
        MM_refreshPriority();

        MM_scheduler();
    }


    /*Dodaje PCB procesu do tablicy kolejek priorytetowych. Jesli wywlaszcza RUNNING to uruchamiany jest planista*/
    public static void MM_addReadyProcess(PCB ready_process) {
        if (ready_process.getPs() == ProcessState.READY) {
            priorityList.addProcess(ready_process);
        }
        if(ready_process.getPriD() > RUNNING.getPriD())  MM_scheduler();
    }


    /*Usuniecie PCB procesu z tablicy kolejek priorytetowych*/
    public static void MM_unreadyProcess(PCB pcb) {
        if(RUNNING == pcb && pcb.getPs()==ProcessState.WAITING){
            RUNNING = null;
        }
        else {
            priorityList.deleteProcess(pcb.getPid());
        }
    }




    /* -----------------STEP MODE---------------------------------------------------------------------*/


    /*Wyswietla dane procesu w stanie RUNNING*/
    public static void MM_show_running() {
        System.out.println("RUNNING: id: " + RUNNING.getPid() + " name: " + RUNNING.getPn());
    }


    /*Wyswietla priorytety statyczne oraz dynamiczne procesow w stanie Ready oraz Running*/
    public static void MM_show_actual_priority() {
        priorityList.displayQueues();
        System.out.print("RUNNING: [ " + RUNNING.getPid() + " " + RUNNING.getPn() + " " + RUNNING.getPriS() + " " + RUNNING.getPriD() + " ]");
    }



    /*--------------------GET---------------------------------------------------------------------------*/


    /*Zwraca zmienna RUNNING*/
    public static PCB MM_getRUNNING() {
        return RUNNING;
    }



    /* -----------------Private---------------------------------------------------------------------*/



    /*Regulacja priorytetow dynamicznych, postarzanie procesow gotowych, zmniejszenie priortetu RUNNING*/
    private static void MM_refreshPriority() {
        priorityList.updateDynamicPriority();
        if (RUNNING != ZEROPRIORITY && RUNNING != null) {
            if (RUNNING.getPriD() > RUNNING.getPriS()) RUNNING.setPriD(RUNNING.getPriD() - 1);
        }
    }


    /*Planista, dokonuje przydzialu procesora procesowi*/
    private static void MM_scheduler() {
        PCB tmp;

        tmp = MM_findReady();

        if(RUNNING != null) {
            if (RUNNING.getPs() == ProcessState.WAITING) {
                RUNNING = null;
            }
        }

        if (RUNNING == null) {
            RUNNING = tmp;

            ProcessManager.KM_setProcessState(RUNNING, ProcessState.RUNNING);
        } else {
            if (tmp.getPriD() > RUNNING.getPriD()) {
                KM_setProcessState(RUNNING, ProcessState.READY);
                KM_setProcessState(tmp, ProcessState.RUNNING);
                RUNNING = tmp;
            }
        }
    }


    /*Zwraca PCB procesu o najwyzszym priorytecie*/
    private static PCB MM_findReady() {
        PCB tmp;
        tmp = priorityList.getHighestPriority();
        if (tmp != null) {
            return tmp;
        } else return ZEROPRIORITY;
    }
}
