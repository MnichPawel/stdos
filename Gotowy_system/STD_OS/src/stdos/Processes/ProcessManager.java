package stdos.Processes;

import java.util.ArrayList;
import java.util.List;
import stdos.CPU.*;
import stdos.VM.VirtualMemory;

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
        if(_p<1||_p>15) {
            if(!(_p==0&&_filename.equals(idleProcessFilename))) {
                throw new Exception("KM_CreateProcess:priorityOutsideRange");
            }
        }
        if(true) {//TODO: File exist?
            //TODO: add somewhere loading to virtual memory
            VirtualMemory.load_to_virtualmemory(actPid, ""); //TODO: program data
            //int _pl = 1; //TODO: Program length - useless
            PCB pcb1 = new PCB(actPid, _filename, _processname, _p);
            actPid++; //TODO: if actPid less than maxProcesses / Windows PID Management
            activeProcesses.add(pcb1); //TODO: readyProcesses remove, just use CPU priorityList
            readyProcesses.add(pcb1);
            CPU.MM_addReadyProcess(pcb1);
            if(_filename.equals(idleProcessFilename)) { pcb1.setPs(ProcessState.READY); readyProcesses.add(pcb1); }
        } else {
            throw new Exception("KM_CreateProcess:FileNotExist");
        }
        return;
    }

    public static void KM_CreateProcess (String _filename, int _p) throws Exception {
        ProcessManager.KM_CreateProcess(_filename, _filename, _p);
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
        readyProcesses.remove(pcb);
        CPU.MM_unreadyProcess(pcb);
        VirtualMemory.remove_from_virtualmemory(pcb.getPid());
        //TODO: remove from VM
        return;
    }

    /*
    Allowed ProcessState changes
    NEW -> READY

    READY -> RUNNING

    WAITING -> READY

    RUNNING -> READY
    RUNNING -> WAITING

    Removed: READY -> WAITING

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
                CPU.MM_addReadyProcess(_pcb);
            } else if(_pcb.getPs()==ProcessState.RUNNING) {
                _pcb.setPs(_ps);
            }
        } else if(_ps==ProcessState.RUNNING) {
            if(_pcb.getPs()==ProcessState.READY) {
                _pcb.setPs(_ps);
            }
        } else if(_ps==ProcessState.WAITING) {
            if(_pcb.getPs()==ProcessState.RUNNING) {
                readyProcesses.remove(_pcb);
                CPU.MM_unreadyProcess(_pcb);
                _pcb.setPs(_ps);
            }
        }
        return;
    }

    /*
    KM_setProcessDynamicPriority - set dynamic priority of process, by given PCB (_pcb) and priority (p)
     */
    public static boolean KM_setProcessDynamicPriority (PCB _pcb, int p) {
        if(p>=1&&p<=15) {
            _pcb.setPriD(p);
            return true;
        } else {
            return false;
        }
    }

    /*
    KM_setProcessDynamicPriorityDefault - set dynamic priority of process, by given PCB (_pcb) and priority (p)
    */
    public static boolean KM_setProcessDynamicPriorityDefault (PCB _pcb) {
        _pcb.setPriD(_pcb.getPriS());
        return true;
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

    public static void KM_printRunningRegisters() { //For Interface
        PCB pcb = CPU.RUNNING;
        System.out.printf("Register state for process PID: %d, ProcessName: %s, Filename: %s\n", pcb.getPid(), pcb.getPn(), pcb.getFilename());
        System.out.printf("AX: %d, BX: %d, CX: %d, DX: %d\n", pcb.getAx(), pcb.getBx(), pcb.getCx(), pcb.getDx());
    }

    public static PCB KM_getPCBbyPID (int pid) {
        for(PCB _p : activeProcesses) {
            if(_p.getPid()==pid) {
                return _p;
            }
        }
        return null;
    }
}
