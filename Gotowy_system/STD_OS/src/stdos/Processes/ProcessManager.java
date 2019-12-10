package ProcessManager;

import java.util.ArrayList;
import java.util.List;
import CPU.*;

/*
FROM CPU
//dodaje proces do listy gotowych procesow
    public void MM_add_ready(PCB ready_process){
        priorityList.addProcess(ready_process);
        ready_process.setPs(ProcessState.READY);
    }

    public void MM_unreadyProcess(int pid){
        priorityList.deleteProcess(pid);
    }
 */

public class ProcessManager {
    private static int actPid;
    private static List<PCB> activeProcesses;
    private static List<PCB> readyProcesses;
    private static String idleProcessFilename = "PC";
    final static int maxProcesses = 128; //TODO: ??Be or not to be

    static CPU cpu = new CPU();

    public ProcessManager() {
        activeProcesses = new ArrayList<PCB>();
        readyProcesses = new ArrayList<PCB>();
        try {
            KM_CreateProcess(idleProcessFilename, "dummy", 0);
        } catch(Exception e) {
            System.out.println(e);
        }
        return;
    }

    @Deprecated
    public static void KM_CreateProcess (String _filename, int _p) throws Exception {
        if(_filename.equals(idleProcessFilename)) {
            if(_p!=0) throw new Exception("KM_CreateProcess:idleProcessPriorityMustBeZero");
        } else {
            if(_p==0) throw new Exception("KM_CreateProcess:notIdleProcessPriorityMustBeGreaterThanZero");
        }
        if(true) {//TODO: File exist?
            //TODO: add somewhere loading to virtual memory
            int _pl = 1; //TODO: Program length
            PCB pcb1 = new PCB(actPid, _filename, _p);
            actPid++; //TODO: if actPid less than maxProcesses / Windows PID Management
            //activeProcesses.add(pcb1); //TODO: activeProcesses remove, just use CPU priorityList

            if(_filename.equals(idleProcessFilename)) { pcb1.setPs(ProcessState.READY); readyProcesses.add(pcb1); }
        } else {
            throw new Exception("KM_CreateProcess:FileNotExist");
        }
        return;
    }

    public static void KM_CreateProcess (String _filename, String _processname, int _p) throws Exception {
        if(_filename.trim().equals("")) { //Check if filename is not ""
            throw new Exception("KM_CreateProcess:fileNameMustNotBeNull");
        }
        if(_processname.trim().equals("")) { // Check if processname is not ""
            throw new Exception("KM_CreateProcess:processNameMustNotBeNull");
        }
        if(_filename.equals(idleProcessFilename)) { // Check if filename is not idle process filename
            if(_p!=0) throw new Exception("KM_CreateProcess:idleProcessPriorityMustBeZero");
        } else {
            if(_p==0) throw new Exception("KM_CreateProcess:notIdleProcessPriorityMustBeGreaterThanZero");
        }
        if(true) {//TODO: File exist?
            //TODO: add somewhere loading to virtual memory
            //int _pl = 1; //TODO: Program length - useless
            PCB pcb1 = new PCB(actPid, _filename, _processname, _p);
            actPid++; //TODO: if actPid less than maxProcesses / Windows PID Management
            //activeProcesses.add(pcb1); //TODO: activeProcesses remove, just use CPU priorityList
            cpu.MM_add_ready(pcb1);
            if(_filename.equals(idleProcessFilename)) { pcb1.setPs(ProcessState.READY); readyProcesses.add(pcb1); }
        } else {
            throw new Exception("KM_CreateProcess:FileNotExist");
        }
        return;
    }

    public static void KM_CreateProcess (String _filename, String _processname) throws Exception {
        ProcessManager.KM_CreateProcess(_filename, _processname, 1);
    }

    public static void KM_CreateProcess (String _filename) throws Exception {
        ProcessManager.KM_CreateProcess(_filename, _filename, 1);
    }

    public static void KM_TerminateProcess (PCB pcb) { //TODO: function
        if(pcb.getPid()==0) {
            return;
        }
        activeProcesses.remove(pcb);
        //readyProcesses.remove(pcb); //TODO: activeProcesses remove, just use CPU priorityList
        cpu.MM_unreadyProcess(pcb.getPid());
        //TODO: remove from VM
        return;
    }

    /*
    Allowed ProcessState changes
    NEW -> READY

    READY -> RUNNING
    READY -> WAITING - maybe not allowed

    WAITING -> READY

    RUNNING -> READY
    RUNNING -> WAITING

     */

    public static void KM_setProcessState (PCB _pcb, ProcessState _ps) {
        if(_ps==_pcb.getPs()) { //Before = after
            //NULL
        } else if(_ps==ProcessState.NEW) { //Error
            //NULL
        } else if(_ps==ProcessState.READY) {
            if(_pcb.getPs()==ProcessState.WAITING||_pcb.getPs()==ProcessState.NEW) {
                _pcb.setPs(_ps);
                readyProcesses.add(_pcb);
            } else if(_pcb.getPs()==ProcessState.RUNNING) {
                _pcb.setPs(_ps);
                //TODO: add to ready after RUNNING->READY?
            }
        } else if(_ps==ProcessState.RUNNING) {
            if(_pcb.getPs()==ProcessState.READY) {
                //TODO: remove from ready after READY->RUNNING?
                _pcb.setPs(_ps);
            }
        } else if(_ps==ProcessState.WAITING) {
            if(_pcb.getPs()==ProcessState.RUNNING||_pcb.getPs()==ProcessState.READY) { //TODO: READY->WAITING?
                //TODO: remove from ready RUNNING->WAITING?
                readyProcesses.remove(_pcb);
                _pcb.setPs(_ps);
            }
        }
        return;
    }

    //useless, CPU don't change it, CPU change only dynamic priority
    /*
    @Deprecated
    public static void KM_setProcessStaticPriority(PCB _pcb, int p) { //TODO: useless, CPU don't change it, CPU change only dynamic priority
        _pcb.setPriS(p);
        return;
    }
     */

    public static void KM_setProcessDynamicPriority (PCB _pcb, int p) {
        _pcb.setPriD(p);
        return;
    }

    public static void KM_getAllProcessListPrint() {
        System.out.print("All Process List:\n");
        System.out.format("%-3s %-16s %-16s %-2s %-2s %-7s \n", "PID", "ProName", "FileName", "PS", "PD", "PState");
        for(PCB _p  : activeProcesses) {
            System.out.format("%-3d %-16s %-16s %-2d %-2d %-7s\n", _p.getPid(), _p.getPn(), _p.getFilename(), _p.getPriS(), _p.getPriD(), _p.getPs());
            //System.out.print("- "+_p.getPid()+"\t"+_p.getPn()+"\t"+_p.getFilename()+"\t\t"+_p.getPriS()+"\t\t"+_p.getPriD()+"\t\t"+_p.getPs()+"\n");
        }
        return;
    }

    public static void KM_getReadyProcessListPrint() {
        System.out.print("Ready Process List:\n");
        System.out.format("%-3s %-16s %-16s %-2s %-2s\n", "PID", "ProName", "FileName", "PS", "PD");
        for(PCB _p  : activeProcesses) {
            if(_p.getPs()==ProcessState.READY) {
                System.out.format("%-3d %-16s %-16s %-2d %-2d\n", _p.getPid(), _p.getPn(), _p.getFilename(), _p.getPriS(), _p.getPriD());
            }
        }
        return;
    }

    public static List<PCB> KM_getAllProcessList() {
        return activeProcesses;
    }

    public static List<PCB> KM_getReadyProcessList() {
        //return readyProcesses; //too simple
        List<PCB> readyProc = new ArrayList<PCB>();
        for (PCB _p : activeProcesses) {
            if(_p.getPs()==ProcessState.READY) {
                readyProc.add(_p);
            }
        }
        return readyProc;
    }

    //TODO: Useless
    public static PCB KM_getPCBbyPCB (PCB pcb) { //TODO: be or not to be
        return pcb;
    }

    public static PCB KM_getPCBbyPID (int pid) { //TODO: be or not to be
        for(PCB _p : activeProcesses) {
            if(_p.getPid()==pid) {
                return _p;
            }
        }
        return null;
    }
}
