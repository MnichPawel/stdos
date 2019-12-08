package CPU;

import java.util.LinkedList;
import java.util.Queue;
import static ProcessManager.ProcessManager.*;
import ProcessManager.*;

public class main {
    public static void main(String[] args) {
        ProcessManager pm = new ProcessManager();
        ProcessManager.KM_getAllProcessListPrint();
        try {
            ProcessManager.KM_CreateProcess("SOC", "p2", 3);
            ProcessManager.KM_CreateProcess("SOCCOS", "p3");

        } catch (Exception e) {
            System.out.println(e);
        }
        ProcessManager.KM_getAllProcessListPrint();
        ProcessManager.KM_setProcessState(ProcessManager.KM_getPCBbyPID(1), ProcessState.READY);
        ProcessManager.KM_setProcessState(ProcessManager.KM_getPCBbyPID(1), ProcessState.RUNNING);
        ProcessManager.KM_setProcessState(ProcessManager.KM_getPCBbyPID(2), ProcessState.WAITING);
        ProcessManager.KM_getAllProcessListPrint();
        ProcessManager.KM_getReadyProcessListPrint();
        CPU cpu = new CPU();
    }
}
