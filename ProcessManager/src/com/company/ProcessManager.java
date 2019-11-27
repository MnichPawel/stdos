package com.company;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class ProcessManager {
    private static int actPid;
    private static List<PCB> activeProcesses;
    private static List<PCB> readyProcesses;
    private static String idleProcessFilename = "PC";
    final static int maxProcesses = 128; //TODO: ??Be or not to be
    //public int actPid = 0;

    public ProcessManager() {
        activeProcesses = new ArrayList<PCB>();
        readyProcesses = new ArrayList<PCB>();
        //Idle process
        try {
            KM_CreateProcess(idleProcessFilename, 0);
            KM_CreateProcess("COS", 1);
        } catch(Exception e) {
            System.out.println(e);
        }
        return;
    }

    public static void KM_CreateProcess (String _filename , int _p) throws Exception {
        if(_filename.equals(idleProcessFilename)) {
            if(_p!=0) throw new Exception("KM_CreateProcess:idleProcessPriorityMustBeZero");
        } else {
            if(_p==0) throw new Exception("KM_CreateProcess:notIdleProcessPriorityMustBeGreaterThanZero");
        }
        if(true) {//TODO: File exist?
            int _pl = 1; //TODO: Program length
            PCB pcb1 = new PCB(actPid, _filename, 0, _p, _pl);
            actPid++; //TODO: if actPid less than maxProcesses / Windows PID Management
            activeProcesses.add(pcb1);
            //TODO: semaphore
            if(_filename.equals(idleProcessFilename)) { pcb1.setPs(ProcessState.READY); readyProcesses.add(pcb1); }
        } else {
            throw new Exception("KM_CreateProcess:FileNotExist");
        }
        return;
    }

    public static void KM_TerminateProcess (PCB pcb) { //TODO: function
        activeProcesses.remove(pcb);
        readyProcesses.remove(pcb);
        return;
    }

    public static void KM_setProcessState (PCB _pcb, ProcessState _ps) {
        _pcb.setPs(_ps);
        return;
    }

    public static void KM_setProcessStaticPriority(PCB _pcb, int p) {
        _pcb.setPriS(p);
        return;
    }

    public static void KM_setProcessDynamicPriority (PCB _pcb, int p) {
        _pcb.setPriD(p);
        return;
    }

    public static void KM_getAllProcessListPrint() {
        System.out.println("All Process List: ");
        System.out.println("PID\tFile\tSPrio\tDPrio\tPS");
        for(PCB _p  : activeProcesses) {
            System.out.print("- "+_p.getPid()+"\t"+_p.getFilename()+"\t\t"+_p.getPriS()+"\t\t"+_p.getPriD()+"\t\t"+_p.getPs()+"\n");
        }
        return;
    }

    public static void KM_getReadyProcessListPrint() {
        System.out.println("Ready Process List: ");
        System.out.println("PID\tFile\tSPrio\tDPrio");
        for(PCB _p  : readyProcesses) {
            System.out.print("- "+_p.getPid()+"\t"+_p.getFilename()+"\t\t"+_p.getPriS()+"\t\t"+_p.getPriD()+"\n");
        }
        return;
    }

    public static List<PCB> KM_getAllProcessList() {
        return activeProcesses;
    }

    public static List<PCB> KM_getReadyProcessList() {
        return readyProcesses;
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
