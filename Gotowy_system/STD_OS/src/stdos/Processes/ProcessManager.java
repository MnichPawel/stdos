package stdos.Processes;

import stdos.CPU.CPU;
import stdos.Filesystem.Katalogi;
import stdos.VM.VirtualMemory;

import java.util.ArrayList;
import java.util.List;

public class ProcessManager {
    private static int actPid = 0;
    private static List<PCB> activeProcesses;
    private static List<PCB> readyProcesses;
    private static String idleProcessFilename = "dummy.txt";

    private static PCB zeroPriority;

    /*
    Constructor of ProcessManager()
    Create lists of all and ready processes
    Create Idle Process (Static priority = 0)
     */
    public ProcessManager() {
        activeProcesses = new ArrayList<>();
        readyProcesses = new ArrayList<>();
        try {
            zeroPriority = KM_CreateProcess(idleProcessFilename, "DUMMY", 0);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /*
    KM_CreateProcess
    _filename = filename of file which have code of process
    _processname = processname of new process
    _p = static priority of process
    return PCB of new process
     */
    public static PCB KM_CreateProcess (String _filename, String _processname, int _p) throws Exception {
        if(_filename.trim().equals("")) { //Check if filename is not ""
            throw new Exception("KM_CreateProcess:fileNameMustNotBeNull");
        }
        if(_processname.trim().equals("")) { // Check if processname is not ""
            throw new Exception("KM_CreateProcess:processNameMustNotBeNull");
        }
        if(_filename.equalsIgnoreCase(idleProcessFilename)) { // Check if filename is not idle process filename
            if(_p!=0) throw new Exception("KM_CreateProcess:idleProcessPriorityMustBeZero");
            if(zeroPriority!=null) throw new Exception("KM_CreateProcess:idleProcessAlreadyExist");
        } else {
            if(_p==0) throw new Exception("KM_CreateProcess:notIdleProcessPriorityMustBeGreaterThanZero");
        }
        if(_p<1||_p>15) {
            if(!(_p==0&&_filename.equalsIgnoreCase(idleProcessFilename))) {
                throw new Exception("KM_CreateProcess:priorityOutsideRange");
            }
        }
        Katalogi.getTargetDir().getFiles().KP_otwP(_filename);
        byte[] code = Katalogi.getTargetDir().getFiles().KP_pobP(_filename);
        Katalogi.getTargetDir().getFiles().KP_zamkP(_filename);
        if(code[0] != -1) {
            PCB pcb1 = new PCB(actPid, _filename, _processname, _p);
            actPid++;
            String codeStr = getStringFromByteArray(code);
            codeStr = removeNewLineCharacters(codeStr);
            VirtualMemory.load_to_virtualmemory(pcb1.getPid(), codeStr);
            KM_setProcessState(pcb1, ProcessState.READY);
            activeProcesses.add(pcb1);
            readyProcesses.add(pcb1);
            if(!pcb1.getFilename().equalsIgnoreCase(idleProcessFilename)) {
                CPU.MM_addReadyProcess(pcb1);
            }
            return pcb1;
        } else {
            throw new Exception("KM_CreateProcess:FileNotExist");
        }
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

    public static boolean KM_TerminateProcess (PCB pcb) throws Exception { //TODO: function
        if(pcb.getPid()==0) {
            throw new Exception("KM_TerminateProcess:IdleProcessCannotBeTerminated");
        }
        VirtualMemory.remove_from_virtualmemory(pcb.getPid());
        activeProcesses.remove(pcb);
        readyProcesses.remove(pcb);
        CPU.MM_unreadyProcess(pcb);

        return true;
    }

    public static boolean KM_TerminateProcess (String _processname) throws Exception {
        return KM_TerminateProcess(_processname, false);
    }

    public static boolean KM_TerminateProcess (String _processname, boolean firstOnly) throws Exception {
        if(firstOnly) {
            List<PCB> pcbList = KM_getPCBbyPN(_processname);
            if(pcbList.isEmpty()) {
                throw new Exception("KM_TerminateProcess:ProcessNotExist");
            } else {
                return KM_TerminateProcess(pcbList.get(0));
            }
        } else {
            List<PCB> pcbList = KM_getPCBbyPN(_processname);
            if(pcbList.isEmpty()) {
                throw new Exception("KM_TerminateProcess:ProcessNotExist");
            } else {
                for(PCB _p:pcbList) {
                    KM_TerminateProcess(_p);
                }
                return true;
            }
        }
    }

    public static boolean KM_TerminateProcess (int pid) throws Exception {
        PCB pcb = KM_getPCBbyPID(pid);
        if(pcb==null) {
            throw new Exception("KM_TerminateProcess:ProcessNotExist");
        } else {
            return KM_TerminateProcess(pcb);
        }
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

    public static boolean KM_setProcessState (PCB _pcb, ProcessState _ps) {
        if(_ps==_pcb.getPs()) { //Before = after
            return true;
        } else if(_ps==ProcessState.NEW) { //Error
            return false;
        } else if(_ps==ProcessState.READY) {
            if(_pcb.getPs()==ProcessState.WAITING||_pcb.getPs()==ProcessState.NEW) {
                _pcb.setPs(_ps);
                readyProcesses.add(_pcb);
                if(!_pcb.getFilename().equalsIgnoreCase(idleProcessFilename)) {
                    CPU.MM_addReadyProcess(_pcb);
                }
                return true;
            } else if(_pcb.getPs()==ProcessState.RUNNING) {
                _pcb.setPs(_ps);
                readyProcesses.add(_pcb);
                CPU.MM_addReadyProcess(_pcb);
                return true;
            }
        } else if(_ps==ProcessState.RUNNING) {
            if(_pcb.getPs()==ProcessState.READY) {
                readyProcesses.remove(_pcb);
                CPU.MM_unreadyProcess(_pcb);
                _pcb.setPs(_ps);
                return true;
            }
        } else if(_ps==ProcessState.WAITING) {
            if(_pcb.getPs()==ProcessState.RUNNING) {
                _pcb.setPs(_ps);
                return true;
            }
        }
        return false;
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
        }
    }

    public static void KM_getReadyProcessListPrint() {
        System.out.print("Ready Process List:\n");
        System.out.format("%-3s %-16s %-16s %-2s %-2s\n", "PID", "ProName", "FileName", "PS", "PD");
        for(PCB _p  : activeProcesses) {
            if(_p.getPs()==ProcessState.READY) {
                System.out.format("%-3d %-16s %-16s %-2d %-2d\n", _p.getPid(), _p.getPn(), _p.getFilename(), _p.getPriS(), _p.getPriD());
            }
        }
    }

    public static PCB KM_getZeroPriorityPCB() {
        return zeroPriority;
    }

    public static List<PCB> KM_getAllProcessList() {
        return activeProcesses;
    }

    public static List<PCB> KM_getReadyProcessList() {
        return readyProcesses;
    }

    public static void KM_printRunningRegistersSimple() {
        PCB pcb = CPU.MM_getRUNNING();
        System.out.printf("AX: %d, BX: %d, CX: %d, DX: %d", pcb.getAx(), pcb.getBx(), pcb.getCx(), pcb.getDx());
    }

    public static void KM_printRunningRegisters() { //For Interface command register
        PCB pcb = CPU.MM_getRUNNING();
        System.out.printf("Register state for process PID: %d, ProcessName: %s, Filename: %s\n", pcb.getPid(), pcb.getPn(), pcb.getFilename());
        System.out.printf("AX: %d, BX: %d, CX: %d, DX: %d\n", pcb.getAx(), pcb.getBx(), pcb.getCx(), pcb.getDx());
    }

    public static void KM_printRegisters(PCB process) { //For Interface command register
        PCB pcb = process;
        System.out.printf("Register state for process PID: %d, ProcessName: %s, Filename: %s\n", pcb.getPid(), pcb.getPn(), pcb.getFilename());
        System.out.printf("AX: %d, BX: %d, CX: %d, DX: %d\n", pcb.getAx(), pcb.getBx(), pcb.getCx(), pcb.getDx());
    }

    private static PCB KM_getPCBbyPID (int pid) {
        for(PCB _p : activeProcesses) {
            if(_p.getPid()==pid) {
                return _p;
            }
        }
        return null;
    }

    private static List<PCB> KM_getPCBbyPN (String _processname) {
        List<PCB> pcbList = new ArrayList<>();
        for(PCB _p : activeProcesses) {
            if(_p.getPn().equalsIgnoreCase(_processname)) {
                pcbList.add(_p);
            }
        }
        return pcbList;
    }

    public static String getStringFromByteArray(byte[] arr) {
        String temp = new String(arr);
        int end = 0;
        boolean findWrongChar = false;

        for (int i = 0; i < temp.length(); i++){
            char c = temp.charAt(i);
            if(c==65533) {
                end = i;
                findWrongChar = true;
                break;
            }
        }
        if(findWrongChar==true) {
            temp = temp.substring(0, end);
        }
        return temp;
    }

    private static String removeNewLineCharacters(String code) {
        String lineSep = System.getProperty("line.separator");
        code = code.replace("\n", "").replace("\r", "").replace(lineSep, "");
        return code;
    }
}
